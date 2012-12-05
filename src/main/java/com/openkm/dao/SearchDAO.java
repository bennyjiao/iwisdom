/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2012 Paco Avila & Josep Llort
 * 
 * No bytes were intentionally harmed during the development of this application.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.dao;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.bean.Permission;
import com.openkm.bean.nr.NodeQueryResult;
import com.openkm.bean.nr.NodeResultSet;
import com.openkm.core.Config;
import com.openkm.core.DatabaseException;
import com.openkm.core.ParseException;
import com.openkm.core.PathNotFoundException;
import com.openkm.dao.bean.NodeBase;
import com.openkm.dao.bean.NodeDocument;
import com.openkm.dao.bean.NodeFolder;
import com.openkm.dao.bean.NodeMail;
import com.openkm.module.db.stuff.DbAccessManager;
import com.openkm.module.db.stuff.SecurityHelper;

/**
 * Search results are filtered by com.openkm.module.db.stuff.ReadAccessFilterFactory, which limit the results only for
 * those which have read access.
 * 
 * @author pavila
 */
public class SearchDAO {
	private static Logger log = LoggerFactory.getLogger(SearchDAO.class);
	private static SearchDAO single = new SearchDAO();
	private static final int MAX_FRAGMENT_LEN = 256;
	public static final String SEARCH_LUCENE = "lucene";
	public static Analyzer analyzer = null;
	
	static {
		try {
			Class<?> Analyzer = Class.forName(Config.HIBERNATE_SEARCH_ANALYZER);
			
			if (Analyzer.getCanonicalName().startsWith("org.apache.lucene.analysis")) {
				Constructor<?> constructor = Analyzer.getConstructor(Config.LUCENE_VERSION.getClass());
				analyzer = (Analyzer) constructor.newInstance(Config.LUCENE_VERSION);
			} else {
				analyzer = (Analyzer) Analyzer.newInstance();
			}
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			analyzer = new StandardAnalyzer(Config.LUCENE_VERSION);
		}
		
		log.debug("Analyzer: {}", analyzer.getClass());
	}
	
	private SearchDAO() {
	}
	
	public static SearchDAO getInstance() {
		return single;
	}
	
	/**
	 * Search by query
	 */
	public NodeResultSet findByQuery(Query query, int offset, int limit) throws ParseException, DatabaseException {
		log.info("findByQuery({}, {}, {})", new Object[] { query, offset, limit });
		FullTextSession ftSession = null;
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			ftSession = Search.getFullTextSession(session);
			tx = ftSession.beginTransaction();
			NodeResultSet result = runQueryLucene(ftSession, query, offset, limit);
			
			HibernateUtil.commit(tx);
			log.debug("findByQuery: {}", result);
			return result;
		} catch (IOException e) {
			HibernateUtil.rollback(tx);
			throw new DatabaseException(e.getMessage(), e);
		} catch (InvalidTokenOffsetsException e) {
			HibernateUtil.rollback(tx);
			throw new DatabaseException(e.getMessage(), e);
		} catch (HibernateException e) {
			HibernateUtil.rollback(tx);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			HibernateUtil.close(ftSession);
			HibernateUtil.close(session);
		}
	}
	
	/**
	 * Search by simple query
	 */
	public NodeResultSet findBySimpleQuery(String expression, int offset, int limit) throws ParseException,
			DatabaseException {
		log.info("findBySimpleQuery({}, {}, {})", new Object[] { expression, offset, limit });
		FullTextSession ftSession = null;
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			ftSession = Search.getFullTextSession(session);
			tx = ftSession.beginTransaction();
			
			QueryParser parser = new QueryParser(Config.LUCENE_VERSION, NodeDocument.TEXT_FIELD, analyzer);
			Query query = parser.parse(expression);
			NodeResultSet result = runQueryLucene(ftSession, query, offset, limit);
			
			HibernateUtil.commit(tx);
			log.debug("findBySimpleQuery: {}", result);
			return result;
		} catch (org.apache.lucene.queryParser.ParseException e) {
			HibernateUtil.rollback(tx);
			throw new ParseException(e.getMessage(), e);
		} catch (IOException e) {
			HibernateUtil.rollback(tx);
			throw new DatabaseException(e.getMessage(), e);
		} catch (InvalidTokenOffsetsException e) {
			HibernateUtil.rollback(tx);
			throw new DatabaseException(e.getMessage(), e);
		} catch (HibernateException e) {
			HibernateUtil.rollback(tx);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			HibernateUtil.close(ftSession);
			HibernateUtil.close(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	private NodeResultSet runQueryLucene(FullTextSession ftSession, Query query, int offset, int limit)
			throws IOException, InvalidTokenOffsetsException, HibernateException {
		log.debug("runQueryLucene({}, {}, {}, {})", new Object[] { ftSession, query, offset, limit });
		List<NodeQueryResult> results = new ArrayList<NodeQueryResult>();
		NodeResultSet result = new NodeResultSet();
		FullTextQuery ftq = ftSession.createFullTextQuery(query, NodeDocument.class, NodeFolder.class, NodeMail.class);
		ftq.setProjection(FullTextQuery.SCORE, FullTextQuery.THIS);
		ftq.enableFullTextFilter("readAccess");
		QueryScorer scorer = new QueryScorer(query, NodeDocument.TEXT_FIELD);
		
		// Set limits
		ftq.setFirstResult(offset);
		ftq.setMaxResults(limit);
		
		// Highlight using a CSS style
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class='highlight'>", "</span>");
		Highlighter highlighter = new Highlighter(formatter, scorer);
		highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, MAX_FRAGMENT_LEN));
		
		for (Iterator<Object[]> it = ftq.iterate(); it.hasNext();) {
			Object[] qRes = it.next();
			Float score = (Float) qRes[0];
			NodeBase nBase = (NodeBase) qRes[1];
			
			// Add result
			addResult(results, highlighter, score, nBase);
		}
		
		result.setTotal(ftq.getResultSize());
		result.setResults(results);
		log.debug("runQueryLucene: {}", result);
		return result;
	}
		
	/**
	 * Add result
	 */
	private void addResult(List<NodeQueryResult> results, Highlighter highlighter, Float score, NodeBase nBase)
			throws IOException, InvalidTokenOffsetsException {
		NodeQueryResult qr = new NodeQueryResult();
		NodeDocument nDocument = null;
		String excerpt = null;
		
		if (nBase instanceof NodeDocument) {
			log.debug("NODE DOCUMENT");
			nDocument = (NodeDocument) nBase;
			qr.setDocument(nDocument);
		} else if (nBase instanceof NodeFolder) {
			log.debug("NODE FOLDER");
			NodeFolder nFld = (NodeFolder) nBase;
			qr.setFolder(nFld);
		} else {
			log.warn("NODE UNKNOWN");
		}
		
		if (nDocument != null && nDocument.getText() != null) {
			excerpt = highlighter.getBestFragment(analyzer, NodeDocument.TEXT_FIELD, nDocument.getText());
		}
		
		log.debug("Result: SCORE({}), EXCERPT({}), DOCUMENT({})", new Object[] { score, excerpt, nBase });
		qr.setScore(score);
		qr.setExcerpt(excerpt);
		
		if (qr.getDocument() != null) {
			NodeDocumentDAO.getInstance().initialize(qr.getDocument());
			results.add(qr);
		} else if (qr.getFolder() != null) {
			NodeFolderDAO.getInstance().initialize(qr.getFolder());
			results.add(qr);
		} else if (qr.getMail() != null) {
			NodeMailDAO.getInstance().initialize(qr.getMail());
			results.add(qr);
		}
	}
	
	/**
	 * Find by parent in depth
	 * 
	 * TODO This cache should be for every user (no pass through access manager) and cleaned
	 * after a create, move or copy folder operation.
	 */
	public List<String> findFoldersInDepth(String parentUuid) throws PathNotFoundException, DatabaseException {
		log.debug("findFoldersInDepth({})", parentUuid);
		List<String> ret = null;
		
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			
			// Security Check
			NodeBase parentNode = (NodeBase) session.load(NodeBase.class, parentUuid);
			SecurityHelper.checkRead(parentNode);
			
			ret = findFoldersInDepthHelper(session, parentUuid);
			HibernateUtil.commit(tx);
		} catch (PathNotFoundException e) {
			HibernateUtil.rollback(tx);
			throw e;
		} catch (DatabaseException e) {
			HibernateUtil.rollback(tx);
			throw e;
		} catch (HibernateException e) {
			HibernateUtil.rollback(tx);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			HibernateUtil.close(session);
		}
		
		log.debug("findFoldersInDepth: {}", ret);
		return ret;
	}
	
	/**
	 * Find by parent in depth helper
	 */
	@SuppressWarnings("unchecked")
	private List<String> findFoldersInDepthHelper(Session session, String parentUuid) throws HibernateException,
			DatabaseException {
		log.debug("findFoldersInDepthHelper({}, {})", "session", parentUuid);
		List<String> ret = new ArrayList<String>();
		String qs = "from NodeFolder nf where nf.parent=:parent";
		org.hibernate.Query q = session.createQuery(qs);
		q.setString("parent", parentUuid);
		List<NodeFolder> results = q.list();
		
		// Security Check
		DbAccessManager am = SecurityHelper.getAccessManager();
		
		for (Iterator<NodeFolder> it = results.iterator(); it.hasNext();) {
			NodeFolder node = it.next();
			
			if (am.isGranted(node, Permission.READ)) {
				ret.add(node.getUuid());
				ret.addAll(findFoldersInDepthHelper(session, node.getUuid()));
			}
		}
		
		log.debug("findFoldersInDepthHelper: {}", ret);
		return ret;
	}
}
