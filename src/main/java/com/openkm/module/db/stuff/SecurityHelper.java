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

package com.openkm.module.db.stuff;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.bean.Permission;
import com.openkm.core.AccessDeniedException;
import com.openkm.core.Config;
import com.openkm.core.DatabaseException;
import com.openkm.core.PathNotFoundException;
import com.openkm.dao.NodeBaseDAO;
import com.openkm.dao.bean.NodeBase;
import com.openkm.dao.bean.NodeDocument;
import com.openkm.dao.bean.NodeFolder;
import com.openkm.dao.bean.NodeMail;

public class SecurityHelper {
	private static Logger log = LoggerFactory.getLogger(SecurityHelper.class);
	private static DbAccessManager accessManager = null;
	
	static {
		if (DbSimpleAccessManager.NAME.equals(Config.SECURITY_ACCESS_MANAGER)) {
			log.info("Configuring AccessManager with {}", DbSimpleAccessManager.class.getCanonicalName());
			accessManager = new DbSimpleAccessManager();
		}
	}
	
	/**
	 * Return current access manager
	 */
	public static DbAccessManager getAccessManager() {
		return accessManager;
	}
	
	/**
	 * Prune not accessible nodes
	 */
	public static void pruneFolderList(List<NodeFolder> nodeList) throws DatabaseException {
		for (Iterator<NodeFolder> it = nodeList.iterator(); it.hasNext();) {
			NodeFolder node = it.next();
			
			if (!accessManager.isGranted(node, Permission.READ)) {
				it.remove();
			}
		}
	}
	
	/**
	 * Prune not accessible nodes
	 */
	public static void pruneDocumenList(List<NodeDocument> nodeList) throws DatabaseException {
		for (Iterator<NodeDocument> it = nodeList.iterator(); it.hasNext();) {
			NodeDocument node = it.next();
			
			if (!accessManager.isGranted(node, Permission.READ)) {
				it.remove();
			}
		}
	}
	
	/**
	 * Prune not accessible nodes
	 */
	public static void pruneMailList(List<NodeMail> nodeList) throws DatabaseException {
		for (Iterator<NodeMail> it = nodeList.iterator(); it.hasNext();) {
			NodeMail node = it.next();
			
			if (!accessManager.isGranted(node, Permission.READ)) {
				it.remove();
			}
		}
	}
	
	/**
	 * Check for node read access
	 */
	public static void checkRead(NodeBase node) throws PathNotFoundException, DatabaseException {
		log.debug("checkRead({})", node);
		
		if (!accessManager.isGranted(node, Permission.READ)) {
			String path = NodeBaseDAO.getInstance().getPathFromUuid(node.getUuid());
			throw new PathNotFoundException(node.getUuid() + " : " + path);
		}
	}
	
	/**
	 * Check for node write
	 */
	public static void checkWrite(NodeBase node) throws AccessDeniedException, PathNotFoundException,
			DatabaseException {
		log.debug("checkWrite({})", node);
		
		if (!accessManager.isGranted(node, Permission.WRITE)) {
			String path = NodeBaseDAO.getInstance().getPathFromUuid(node.getUuid());
			throw new AccessDeniedException(node.getUuid() + " : " + path);
		}
	}
	
	/**
	 * Check for node delete
	 */
	public static void checkDelete(NodeBase node) throws AccessDeniedException, PathNotFoundException,
			DatabaseException {
		log.debug("checkDelete({})", node);
		
		if (!accessManager.isGranted(node, Permission.DELETE)) {
			String path = NodeBaseDAO.getInstance().getPathFromUuid(node.getUuid());
			throw new AccessDeniedException(node.getUuid() + " : " + path);
		}
	}
	
	/**
	 * Check for node security
	 */
	public static void checkSecurity(NodeBase node) throws AccessDeniedException, DatabaseException {
		log.debug("checkSecurity({})", node);
		
		if (!accessManager.isGranted(node, Permission.SECURITY)) {
			String path = NodeBaseDAO.getInstance().getParentUuid(node.getUuid());
			throw new AccessDeniedException(node.getUuid() + " : " + path);
		}
	}
}
