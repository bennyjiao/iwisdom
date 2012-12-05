/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (c) 2006-2012  Paco Avila & Josep Llort
 *
 *  No bytes were intentionally harmed during the development of this application.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.servlet;

import java.io.File;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.SessionImpl;
import org.jbpm.JbpmContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.openkm.cache.UserItemsManager;
import com.openkm.cache.UserNodeKeywordsManager;
import com.openkm.core.Config;
import com.openkm.core.Cron;
import com.openkm.core.DatabaseException;
import com.openkm.core.MimeTypeConfig;
import com.openkm.core.RepositoryInfo;
import com.openkm.core.UINotification;
import com.openkm.core.UpdateInfo;
import com.openkm.core.UserMailImporter;
import com.openkm.core.Watchdog;
import com.openkm.dao.HibernateUtil;
import com.openkm.extension.core.ExtensionManager;
import com.openkm.extractor.TextExtractorWorker;
import com.openkm.kea.RDFREpository;
import com.openkm.module.db.DbRepositoryModule;
import com.openkm.module.db.stuff.FsDataStore;
import com.openkm.module.jcr.JcrRepositoryModule;
import com.openkm.module.jcr.stuff.DataStoreGarbageCollector;
import com.openkm.spring.SystemAuthentication;
import com.openkm.util.DocConverter;
import com.openkm.util.ExecutionUtils;
import com.openkm.util.FormUtils;
import com.openkm.util.JBPMUtils;
import com.openkm.util.UserActivity;
import com.openkm.util.WarUtils;

/**
 * Servlet Startup Class
 */
public class RepositoryStartupServlet extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(RepositoryStartupServlet.class);
	private static final long serialVersionUID = 1L;
	private static Timer dsgcTimer;  // Datastore Garbage Collector
	private static Timer wdTimer;  // Session Watchdog (Inactive Session Expiration)
	private static Timer riTimer;  // Repository Info (Administration Repository Graphs)
	private static Timer uiTimer;  // Update Info (OpenKM Update Information)
	private static Timer umiTimer;  // User Mail Importer
	private static Timer cronTimer;  // CRON Manager
	private static Timer uinTimer;  // User Interface Notification (Create From Administration)
	private static Timer tewTimer;  // Text Extractor Worker
	private static Watchdog wd;
	private static Cron cron;
	private static UINotification uin;
	private static UpdateInfo ui;
	private static RepositoryInfo ri;
	private static UserMailImporter umi;
	private static DataStoreGarbageCollector dsgc;
	private static TextExtractorWorker tew;
	private static boolean hasConfiguredDataStore = false;
	private static boolean running = false;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext sc = getServletContext();
		
		// Read configuration file
		Properties config = Config.load(sc);
		
		// Call only once during initialization time of your application
		// @see http://issues.openkm.com/view.php?id=1577
		SLF4JBridgeHandler.install();
		
		// Get OpenKM version
		WarUtils.readAppVersion(sc);
		log.info("*** Application version: {} ***", WarUtils.getAppVersion());
		
		// Database initialize
		log.info("*** Hibernate initialize ***");
		HibernateUtil.getSessionFactory();
		
		// Create missing directories
		// NOTE: Should be executed AFTER Hibernate initialization because if in created mode
		// initialization will drop these directories
		createMissingDirs();
		
		try {
			// Initialize property groups
			log.info("*** Initialize property groups... ***");
			FormUtils.parsePropertyGroupsForms(Config.PROPERTY_GROUPS_XML);
		} catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
		
		// Initialize language detection engine
		try {
			log.info("*** Initialize language detection engine... ***");
			DetectorFactory.loadProfile(Config.LANG_PROFILES_BASE);
		} catch (LangDetectException e) {
			log.error(e.getMessage(), e);
		}
		
		// Load database configuration
		Config.reload(sc, config);
		
		// Invoke start
		start();
		
		// Activity log
		UserActivity.log(Config.SYSTEM_USER, "MISC_OPENKM_START", null, null, null);
	}

	@Override
	public void destroy() {
		super.destroy();
		
		// Activity log
		UserActivity.log(Config.SYSTEM_USER, "MISC_OPENKM_STOP", null, null, null);
		
		// Invoke stop
		stop(this);
        
        try {
        	// Database shutdown
    		log.info("*** Hibernate shutdown ***");
    		HibernateUtil.closeSessionFactory();	
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
        
        try {
			// Call only once during destroy time of your application
			// @see http://issues.openkm.com/view.php?id=1577
			SLF4JBridgeHandler.uninstall();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Start OpenKM and possible repository and database initialization
	 */
	public static synchronized void start() throws ServletException {
		SystemAuthentication systemAuth = new SystemAuthentication();
		
		if (running) {
			throw new IllegalStateException("OpenKM already started");
		}
		
		try {
			log.info("*** Repository initializing... ***");
			
			if (Config.REPOSITORY_NATIVE) {
				systemAuth.enable();
				DbRepositoryModule.initialize();
				systemAuth.disable();
			} else {
				JcrRepositoryModule.initialize();
			}
			
			log.info("*** Repository initialized ***");
		} catch (Exception e) {
			throw new ServletException(e.getMessage(), e);
		}
		
		if (Config.USER_ITEM_CACHE) {
			// Deserialize
			try {
				log.info("*** Cache deserialization ***");
				UserItemsManager.deserialize();
				UserNodeKeywordsManager.deserialize();
			} catch (DatabaseException e) {
				log.warn(e.getMessage(), e);
			}
		}
		
		log.info("*** User database initialized ***");
		
		if (!Config.REPOSITORY_NATIVE) {
			// Test for datastore
			SessionImpl si = (SessionImpl) JcrRepositoryModule.getSystemSession();
			
			if (((RepositoryImpl) si.getRepository()).getDataStore() == null) {
				hasConfiguredDataStore = false;
			} else {
				hasConfiguredDataStore = true;
			}
		}
		
		// Create timers
		uiTimer = new Timer("Update Info");
		wdTimer = new Timer("Session Watchdog");
		cronTimer = new Timer("Crontab Manager");
		uinTimer = new Timer("User Interface Notification");
		riTimer = new Timer("Repository Info", true);
		umiTimer = new Timer("User Mail Importer");
		dsgcTimer = new Timer("Datastore Garbage Collector");
		tewTimer = new Timer("Text Extractor Worker", true);
		
		// Workflow
		log.info("*** Initializing workflow engine... ***");
		JbpmContext jbpmContext = JBPMUtils.getConfig().createJbpmContext();
		jbpmContext.setSessionFactory(HibernateUtil.getSessionFactory());
		jbpmContext.getGraphSession();
		jbpmContext.getJbpmConfiguration().getJobExecutor().start(); // startJobExecutor();
		jbpmContext.close();
		
		// Mime types
		log.info("*** Initializing MIME types... ***");
		MimeTypeConfig.loadMimeTypes();
		
		if (Config.UPDATE_INFO) {
			log.info("*** Activating update info ***");
			ui = new UpdateInfo();
			uiTimer.schedule(ui, 1000, 24 * 60 * 60 * 1000); // First in 1 seg, next each 24 hours
		}
		
		log.info("*** Activating watchdog ***");
		wd = new Watchdog();
		wdTimer.schedule(wd, 60 * 1000, 5 * 60 * 1000); // First in 1 min, next each 5 mins
		
		log.info("*** Activating cron ***");
		cron = new Cron();
		Calendar calCron = Calendar.getInstance();
		calCron.add(Calendar.MINUTE, 1);
		calCron.set(Calendar.SECOND, 0);
		calCron.set(Calendar.MILLISECOND, 0);
		
		// Round begin to next minute, 0 seconds, 0 miliseconds
		cronTimer.scheduleAtFixedRate(cron, calCron.getTime(), 60 * 1000); // First in 1 min, next each 1 min
		
		log.info("*** Activating UI Notification ***");
		uin = new UINotification();
		
		// First in 1 second next in x minutes
		uinTimer.scheduleAtFixedRate(uin, 1000, TimeUnit.MINUTES.toMillis(Config.SCHEDULE_UI_NOTIFICATION));
		
		log.info("*** Activating repository info ***");
		ri = new RepositoryInfo();
		
		// First in 1 min, next each X minutes
		riTimer.schedule(ri, 60 * 1000, TimeUnit.MINUTES.toMillis(Config.SCHEDULE_REPOSITORY_INFO));
		
		if (Config.MANAGED_TEXT_EXTRACTION_SCHEDULE > 0) {
			log.info("*** Activating text extractor worker ***");
			tew = new TextExtractorWorker();
			
			// First in 1 min, next each x minutes
			tewTimer.schedule(tew, 60 * 1000, TimeUnit.MINUTES.toMillis(Config.MANAGED_TEXT_EXTRACTION_SCHEDULE));
		}
		
		if (Config.SCHEDULE_MAIL_IMPORTER > 0) {
			log.info("*** Activating user mail importer ***");
			umi = new UserMailImporter();
			
			// First in 5 mins, next each x minutes
			umiTimer.schedule(umi, 5 * 60 * 1000, TimeUnit.MINUTES.toMillis(Config.SCHEDULE_MAIL_IMPORTER));
		} else {
			log.info("*** User mail importer disabled ***");
		}
		
		// Datastore garbage collection
		if (!Config.REPOSITORY_NATIVE && hasConfiguredDataStore) {
			log.info("*** Activating datastore garbage collection ***");
			dsgc = new DataStoreGarbageCollector();
			Calendar calGc = Calendar.getInstance();
			calGc.add(Calendar.DAY_OF_YEAR, 1);
			calGc.set(Calendar.HOUR_OF_DAY, 0);
			calGc.set(Calendar.MINUTE, 0);
			calGc.set(Calendar.SECOND, 0);
			calGc.set(Calendar.MILLISECOND, 0);
			dsgcTimer.scheduleAtFixedRate(dsgc, calGc.getTime(), 24 * 60 * 60 * 1000); // First tomorrow at 00:00, next
																						// each 24 hours
		}
		
		try {
			log.info("*** Activating thesaurus repository ***");
			RDFREpository.getInstance();
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		
		try {
			if (!Config.SYSTEM_OPENOFFICE_PATH.equals("")) {
				log.info("*** Start OpenOffice manager ***");
				DocConverter.getInstance().start();
			} else {
				log.warn("*** No OpenOffice manager configured ***");
			}
		} catch (Throwable e) {
			log.warn(e.getMessage(), e);
		}
		
		// Initialize plugin framework
		ExtensionManager.getInstance();
		
		try {
			log.info("*** Ejecute start script ***");
			File script = new File(Config.HOME_DIR + File.separatorChar + Config.START_SCRIPT);
			ExecutionUtils.runScript(script);
			File jar = new File(Config.HOME_DIR + File.separatorChar + Config.START_JAR);
			ExecutionUtils.getInstance().runJar(jar);
		} catch (Throwable e) {
			log.warn(e.getMessage(), e);
		}
		
		// OpenKM is started
		running = true;
	}
	
	/**
	 * Close OpenKM and free resources
	 */
	public static synchronized void stop(GenericServlet gs) {
		if (!running) {
			throw new IllegalStateException("OpenKM not started");
		}
		
		// Shutdown plugin framework
		ExtensionManager.getInstance().shutdown();
		
		try {
			if (!Config.SYSTEM_OPENOFFICE_PATH.equals("")) {
				if (log == null && gs != null) {
					gs.log("*** Shutting down OpenOffice manager ***");
				} else {
					log.info("*** Shutting down OpenOffice manager ***");
				}
				
				DocConverter.getInstance().stop();
			}
		} catch (Throwable e) {
			log.warn(e.getMessage(), e);
		}
		
		if (hasConfiguredDataStore) {
			if (log == null && gs != null)
				gs.log("*** Shutting down datastore garbage collection... ***");
			else
				log.info("*** Shutting down datastore garbage collection... ***");
			dsgc.cancel();
		}
		
		if (Config.SCHEDULE_MAIL_IMPORTER > 0) {
			if (log == null && gs != null)
				gs.log("*** Shutting down user mail importer ***");
			else
				log.info("*** Shutting down user mail importer ***");
			umi.cancel();
		}
		
		if (Config.MANAGED_TEXT_EXTRACTION_SCHEDULE > 0) {
			if (log == null && gs != null)
				gs.log("*** Shutting down text extractor worker ***");
			else
				log.info("*** Shutting down text extractor worker ***");
			tew.cancel();
		}
		
		if (log == null && gs != null)
			gs.log("*** Shutting down repository info... ***");
		else
			log.info("*** Shutting down repository info... ***");
		ri.cancel();
		
		if (log == null && gs != null)
			gs.log("*** Shutting down UI Notification... ***");
		else
			log.info("*** Shutting down UI Notification... ***");
		uin.cancel();
		
		if (log == null && gs != null)
			gs.log("*** Shutting down cron... ***");
		else
			log.info("*** Shutting down cron... ***");
		cron.cancel();
		
		if (log == null && gs != null)
			gs.log("*** Shutting down watchdog... ***");
		else
			log.info("*** Shutting down watchdog... ***");
		wd.cancel();
		
		if (Config.UPDATE_INFO) {
			if (log == null && gs != null)
				gs.log("*** Shutting down update info... ***");
			else
				log.info("*** Shutting down update info... ***");
			ui.cancel();
		}
		
		// Cancel timers
		dsgcTimer.cancel();
		umiTimer.cancel();
		riTimer.cancel();
		cronTimer.cancel();
		uinTimer.cancel();
		wdTimer.cancel();
		uiTimer.cancel();
		tewTimer.cancel();
		
		if (log == null && gs != null)
			gs.log("*** Shutting down repository... ***");
		else
			log.info("*** Shutting down repository... ***");
		
		if (Config.USER_ITEM_CACHE) {
			// Serialize
			try {
				log.info("*** Cache serialization ***");
				UserItemsManager.serialize();
				UserNodeKeywordsManager.serialize();
			} catch (DatabaseException e) {
				log.warn(e.getMessage(), e);
			}
		}
		
		try {
			// Preserve system user config
			if (!Config.REPOSITORY_NATIVE) {
				JcrRepositoryModule.shutdown();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		if (log == null && gs != null)
			gs.log("*** Repository shutted down ***");
		else
			log.info("*** Repository shutted down ***");
		
		try {
			if (log == null && gs != null)
				gs.log("*** Ejecute stop script ***");
			else
				log.info("*** Ejecute stop script ***");
			File script = new File(Config.HOME_DIR + File.separatorChar + Config.STOP_SCRIPT);
			ExecutionUtils.runScript(script);
			File jar = new File(Config.HOME_DIR + File.separatorChar + Config.STOP_JAR);
			ExecutionUtils.getInstance().runJar(jar);
		} catch (Throwable e) {
			log.warn(e.getMessage(), e);
		}
		
		if (log == null && gs != null)
			gs.log("*** Shutting down workflow engine... ***");
		else
			log.info("*** Shutting down workflow engine... ***");
		JbpmContext jbpmContext = JBPMUtils.getConfig().createJbpmContext();
		jbpmContext.getJbpmConfiguration().getJobExecutor().stop();
		jbpmContext.getJbpmConfiguration().close();
		jbpmContext.close();
		
		// OpenKM is stopped
		running = false;
	}
	
	/**
	 * Create missing needed directories.
	 */
	private static void createMissingDirs() {
		// Initialize DXF cache folder
		File dxfCacheFolder = new File(Config.REPOSITORY_CACHE_DXF);
		if (!dxfCacheFolder.exists()) {
			log.info("Create missing directory {}", dxfCacheFolder.getPath());
			dxfCacheFolder.mkdirs();
		}
		
		// Initialize PDF cache folder
		File pdfCacheFolder = new File(Config.REPOSITORY_CACHE_PDF);
		if (!pdfCacheFolder.exists()) {
			log.info("Create missing directory {}", pdfCacheFolder.getPath());
			pdfCacheFolder.mkdirs();
		}
		
		// Initialize SWF cache folder
		File swfCacheFolder = new File(Config.REPOSITORY_CACHE_SWF);
		if (!swfCacheFolder.exists()) {
			log.info("Create missing directory {}", swfCacheFolder.getPath());
			swfCacheFolder.mkdirs();
		}
		
		if (FsDataStore.DATASTORE_BACKEND_FS.equals(Config.REPOSITORY_DATASTORE_BACKEND)) {
			// Initialize datastore
			File repoDatastoreFolder = new File(Config.REPOSITORY_DATASTORE_HOME);
			if (!repoDatastoreFolder.exists()) {
				log.info("Create missing directory {}", repoDatastoreFolder.getPath());
				repoDatastoreFolder.mkdirs();
			}
		}
		
		// Initialize Hibernate Search indexes
		// NOTE: This is already created on Hibernate initialization
		File hSearchIndexesFolder = new File(Config.HIBERNATE_SEARCH_INDEX_HOME);
		if (!hSearchIndexesFolder.exists()) {
			log.info("Create missing directory {}", hSearchIndexesFolder.getPath());
			hSearchIndexesFolder.mkdirs();
		}
	}
}
