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

package com.openkm.automation;

import java.util.HashMap;

import com.openkm.dao.bean.NodeBase;
import com.openkm.dao.bean.NodeDocument;
import com.openkm.dao.bean.NodeFolder;
import com.openkm.dao.bean.NodeMail;

/**
 * AutomationUtils
 * 
 * @author jllort
 *
 */
public class AutomationUtils {
	public static final String PARENT_UUID = "parentUuid";
	public static final String PARENT_PATH = "parentPath";
	public static final String PARENT_NODE = "parentNode";
	public static final String TEXT_EXTRACTED = "textExtracted";
	public static final String DOCUMENT_NODE = "documentNode";
	public static final String FOLDER_NODE = "folderNode";
	public static final String MAIL_NODE = "mailNode";
	
	
	/**
	 * getUuid
	 */
	public static String getUuid(HashMap<String, Object> env) {
		NodeDocument docNode = (NodeDocument) env.get(DOCUMENT_NODE);
		NodeFolder fldNode = (NodeFolder) env.get(FOLDER_NODE);
		NodeMail mailNode = (NodeMail) env.get(MAIL_NODE);
		String uuid = null;
		
		if (docNode != null) {
			uuid = docNode.getUuid();
		} else if (fldNode != null) {
			uuid = fldNode.getUuid();
		} else if (mailNode != null) {
			uuid = mailNode.getUuid();
		}
		
		return uuid;
	}
	
	/**
	 * getNode
	 */
	public static NodeBase getNode(HashMap<String, Object> env) {
		NodeDocument docNode = (NodeDocument) env.get(DOCUMENT_NODE);
		NodeFolder fldNode = (NodeFolder) env.get(FOLDER_NODE);
		NodeMail mailNode = (NodeMail) env.get(MAIL_NODE);
		
		if (docNode != null) {
			return docNode;
		} else if (fldNode != null) {
			return fldNode;
		} else if (mailNode != null) {
			return mailNode;
		}
		
		return null;
	}
	
	/**
	 * getParentUuid
	 */
	public static String getParentUuid(HashMap<String, Object> env) {
		return (String) env.get(PARENT_UUID);
	}
	
	/**
	 * getParentPath
	 */
	public static String getParentPath(HashMap<String, Object> env) {
		return (String) env.get(PARENT_PATH);
	}
	
	/**
	 * getTextExtracted
	 */
	public static String getTextExtracted(HashMap<String, Object> env) {
		return (String) env.get(TEXT_EXTRACTED);
	}
	
	/**
	 * getString
	 */
	public static String getString(int index, Object... params) {
		return (String) params[index];
	}
	
	/**
	 * getInterger
	 */
	public static Integer getInterger(int index, Object... params) {
		return (Integer) params[index];
	}
	
	/**
	 * getBoolean
	 */
	public static Boolean getBoolean(int index, Object... params) {
		return (Boolean) params[index];
	}
}