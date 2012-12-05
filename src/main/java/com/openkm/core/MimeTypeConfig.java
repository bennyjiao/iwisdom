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

package com.openkm.core;

import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.dao.MimeTypeDAO;
import com.openkm.dao.bean.MimeType;

public class MimeTypeConfig {
	private static Logger log = LoggerFactory.getLogger(MimeTypeConfig.class);
	
	// Mime types
	public static String MIME_RTF = "application/rtf";
	public static String MIME_PDF = "application/pdf";
	public static String MIME_ZIP = "application/zip";
	public static String MIME_POSTSCRIPT = "application/postscript";
	public static String MIME_MS_WORD = "application/msword";
	public static String MIME_MS_EXCEL = "application/vnd.ms-excel";
	public static String MIME_MS_POWERPOINT = "application/vnd.ms-powerpoint";
	public static String MIME_OO_TEXT = "application/vnd.oasis.opendocument.text";
	public static String MIME_OO_SPREADSHEET = "application/vnd.oasis.opendocument.spreadsheet";
	public static String MIME_OO_PRESENTATION = "application/vnd.oasis.opendocument.presentation";
	public static String MIME_SWF = "application/x-shockwave-flash";
	public static String MIME_TIFF = "image/tiff";
	public static String MIME_JPEG = "image/jpeg";
	public static String MIME_GIF = "image/gif";
	public static String MIME_HTML = "text/html";
	public static String MIME_TEXT = "text/plain";
	
	// Registered MIME types
	public static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();
	
	/**
	 * load mime types
	 */
	public static void loadMimeTypes() {
		try {
			List<MimeType> mimeTypeList = MimeTypeDAO.findAll("mt.id");
			MimeTypeConfig.mimeTypes = new MimetypesFileTypeMap();
			
			for (MimeType mt : mimeTypeList) {
				String entry = mt.getName();
				
				for (String ext : mt.getExtensions()) {
					entry += " " + ext;
				}
				
				log.debug("loadMimeTypes => Add Entry: {}", entry);
				MimeTypeConfig.mimeTypes.addMimeTypes(entry);
			}
		} catch (DatabaseException e) {
			log.error(e.getMessage(), e);
		}
	}
}
