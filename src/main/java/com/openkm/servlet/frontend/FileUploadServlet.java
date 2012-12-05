package com.openkm.servlet.frontend;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.openkm.api.OKMAuth;
import com.openkm.api.OKMDocument;
import com.openkm.api.OKMFolder;
import com.openkm.api.OKMNotification;
import com.openkm.automation.AutomationException;
import com.openkm.bean.Document;
import com.openkm.bean.FileUploadResponse;
import com.openkm.bean.Folder;
import com.openkm.core.AccessDeniedException;
import com.openkm.core.Config;
import com.openkm.core.ConversionException;
import com.openkm.core.DatabaseException;
import com.openkm.core.FileSizeExceededException;
import com.openkm.core.ItemExistsException;
import com.openkm.core.MimeTypeConfig;
import com.openkm.core.PathNotFoundException;
import com.openkm.core.Ref;
import com.openkm.core.RepositoryException;
import com.openkm.core.UnsupportedMimeTypeException;
import com.openkm.core.VersionException;
import com.openkm.core.VirusDetectedException;
import com.openkm.extension.core.ExtensionException;
import com.openkm.frontend.client.constants.service.ErrorCode;
import com.openkm.frontend.client.constants.ui.UIFileUploadConstants;
import com.openkm.module.db.DbDocumentModule;
import com.openkm.module.jcr.JcrDocumentModule;
import com.openkm.util.DocConverter;
import com.openkm.util.FileUtils;
import com.openkm.util.FormatUtil;
import com.openkm.util.PathUtils;
import com.openkm.util.SecureStore;
import com.openkm.util.impexp.ImpExpStats;
import com.openkm.util.impexp.RepositoryImporter;
import com.openkm.util.impexp.TextInfoDecorator;

import de.schlichtherle.io.File;
import de.schlichtherle.io.FileOutputStream;

/**
 * FileUploadServlet
 * 
 * @author pavila
 */
public class FileUploadServlet extends OKMHttpServlet {
	private static Logger log = LoggerFactory.getLogger(FileUploadServlet.class);
	private static final long serialVersionUID = 1L;
	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final String FILE_UPLOAD_STATUS = "file_upload_status";
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("doPost({}, {})", request, response);
		String fileName = null;
		InputStream is = null;
		String path = null;
		int action = 0;
		long size = 0;
		boolean notify = false;
		boolean importZip = false;
		String users = null;
		String roles = null;
		String message = null;
		String comment = null;
		String folder = null;
		String rename = null;
		PrintWriter out = null;
		String uploadedUuid = null;
		java.io.File tmp = null;
		boolean redirect = false;
		boolean convertToPdf = false;
		String redirectURL = "";
		updateSessionManager(request);
		
		// JSON Stuff
		Ref<FileUploadResponse> fuResponse = new Ref<FileUploadResponse>(new FileUploadResponse());
		
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			response.setContentType(MimeTypeConfig.MIME_TEXT);
			out = response.getWriter();
			log.debug("isMultipart: {}", isMultipart);
			
			// Create a factory for disk-based file items
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				String contentLength = request.getHeader("Content-Length");
				FileUploadListener listener = new FileUploadListener(Long.parseLong(contentLength));
				
				// Saving listener to session
				request.getSession().setAttribute(FILE_UPLOAD_STATUS, listener);
				upload.setHeaderEncoding("UTF-8");
				
				// upload servlet allows to set upload listener
				upload.setProgressListener(listener);
				List<FileItem> items = upload.parseRequest(request);
				
				// Parse the request and get all parameters and the uploaded file
				for (Iterator<FileItem> it = items.iterator(); it.hasNext();) {
					FileItem item = it.next();
					
					if (item.isFormField()) {
						if (item.getFieldName().equals("path")) {
							path = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("action")) {
							action = Integer.parseInt(item.getString("UTF-8"));
						}
						
						if (item.getFieldName().equals("users")) {
							users = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("roles")) {
							roles = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("notify")) {
							notify = true;
						}
						
						if (item.getFieldName().equals("importZip")) {
							importZip = true;
						}
						
						if (item.getFieldName().equals("message")) {
							message = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("comment")) {
							comment = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("folder")) {
							folder = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("rename")) {
							rename = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("redirect")) {
							redirect = true;
							redirectURL = item.getString("UTF-8");
						}
						
						if (item.getFieldName().equals("convertToPdf")) {
							convertToPdf = true;
						}
					} else {
						fileName = item.getName();
						is = item.getInputStream();
						size = item.getSize();
					}
				}
				
				// Save document with different name than uploaded
				log.info("Filename: '{}'", fileName);
				if (rename != null && !rename.equals("")) {
					log.info("Rename: '{}'", rename);
					
					if (FilenameUtils.indexOfExtension(rename) > -1) {
						// The rename contains filename + extension
						fileName = rename;
					} else {
						// The rename only contains filename, so get extension from uploaded file
						String ext = FilenameUtils.getExtension(fileName);
						
						if (ext.equals("")) {
							fileName = rename;
						} else {
							fileName = rename + "." + ext;
						}
					}
					
					log.info("Filename: '{}'", fileName);
				}
				
				// Now, we have read all parameters and the uploaded file
				if (action == UIFileUploadConstants.ACTION_INSERT) {
					if (fileName != null && !fileName.equals("")) {
						if (importZip && FilenameUtils.getExtension(fileName).equalsIgnoreCase("zip")) {
							log.info("Import zip file '{}' into '{}'", fileName, path);
							String erroMsg = importZip(path, is);
							
							if (erroMsg == null) {
								sendResponse(out, action, fuResponse.get());
							} else {
								log.warn("erroMsg: {}", erroMsg);
								fuResponse.get().setError(erroMsg);
								sendResponse(out, action, fuResponse.get());
							}
						} else if (importZip && FilenameUtils.getExtension(fileName).equalsIgnoreCase("jar")) {
							log.info("Import jar file '{}' into '{}'", fileName, path);
							String erroMsg = importJar(path, is);
							
							if (erroMsg == null) {
								sendResponse(out, action, fuResponse.get());
							} else {
								fuResponse.get().setError(erroMsg);
								sendResponse(out, action, fuResponse.get());
							}
						} else {
							fileName = FilenameUtils.getName(fileName);
							log.info("Upload file '{}' into '{} ({})'", new Object[] { fileName, path, FormatUtil.formatSize(size) });
							String mimeType = MimeTypeConfig.mimeTypes.getContentType(fileName.toLowerCase());
							Document doc = new Document();
							doc.setPath(path + "/" + fileName);
							
							if (convertToPdf && !mimeType.equals(MimeTypeConfig.MIME_PDF)) {
								DocConverter converter = DocConverter.getInstance();
								
								if (converter.convertibleToPdf(mimeType)) {
									// Changing path name
									if (fileName.contains(".")) {
										fileName = fileName.substring(0, fileName.lastIndexOf(".") + 1) + "pdf";
									} else {
										fileName += ".pdf";
									}
									
									doc.setPath(path + "/" + fileName);
									tmp = File.createTempFile("okm", ".tmp");
									java.io.File tmpPdf = File.createTempFile("okm", ".pdf");
									FileOutputStream fos = new FileOutputStream(tmp);
									IOUtils.copy(is, fos);
									converter.doc2pdf(tmp, mimeType, tmpPdf);
									is = new FileInputStream(tmpPdf);
									doc = OKMDocument.getInstance().create(null, doc, is);
									fuResponse.get().setPath(doc.getPath());
									uploadedUuid = doc.getUuid();
									tmp.delete();
									tmpPdf.delete();
									tmp = null;
								} else {
									throw new ConversionException("Not convertible to pdf");
								}
							} else {
								log.info("Wizard: {}", fuResponse);
								
								if (Config.REPOSITORY_NATIVE) {
									doc = new DbDocumentModule().create(null, doc, is, size, null, fuResponse);
									fuResponse.get().setPath(doc.getPath());
									uploadedUuid = doc.getUuid();
								} else {
									doc = new JcrDocumentModule().create(null, doc, is);
									fuResponse.get().setPath(doc.getPath());
									uploadedUuid = doc.getUuid();
								}
								
								log.info("Wizard: {}", fuResponse);
							}
							
							// Return the path of the inserted document in response
							sendResponse(out, action, fuResponse.get());
						}
					}
				} else if (action == UIFileUploadConstants.ACTION_UPDATE) {
					log.info("File updated: {}", path);
					
					// http://en.wikipedia.org/wiki/Truth_table#Applications => ¬p ∨ q
					if (!Config.SYSTEM_DOCUMENT_NAME_MISMATCH_CHECK || PathUtils.getName(path).equals(fileName)) {
						Document doc = OKMDocument.getInstance().getProperties(null, path);
						
						if (Config.REPOSITORY_NATIVE) {
							new DbDocumentModule().checkin(null, path, is, size, comment, null);
							fuResponse.get().setPath(path);
							uploadedUuid = doc.getUuid();
						} else {
							new JcrDocumentModule().checkin(null, path, is, comment);
							fuResponse.get().setPath(path);
							uploadedUuid = doc.getUuid();
						}
						
						// Return the path of the inserted document in response
						sendResponse(out, action, fuResponse.get());
					} else {
						fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_DocumentNameMismatch));
						sendResponse(out, action, fuResponse.get());
					}
				} else if (action == UIFileUploadConstants.ACTION_FOLDER) {
					log.info("Folder create: {}", path);
					Folder fld = new Folder();
					fld.setPath(path + "/" + folder);
					fld = OKMFolder.getInstance().create(null, fld);
					fuResponse.get().setPath(fld.getPath());
					sendResponse(out, action, fuResponse.get());
				}
				
				listener.setUploadFinish(true); // Mark uploading operation has finished
				
				// If the document have been added to the repository, perform user notification
				if ((action == UIFileUploadConstants.ACTION_INSERT || action == UIFileUploadConstants.ACTION_UPDATE) & notify) {
					List<String> userNames = new ArrayList<String>(Arrays.asList(users.split(",")));
					List<String> roleNames = Arrays.asList(roles.split(","));
					
					for (String role : roleNames) {
						List<String> usersInRole = OKMAuth.getInstance().getUsersByRole(null, role);
						
						for (String user : usersInRole) {
							if (!userNames.contains(user)) {
								userNames.add(user);
							}
						}
					}
					
					String notifyPath = URLDecoder.decode(fuResponse.get().getPath(), "UTF-8");
					OKMNotification.getInstance().notify(null, notifyPath, userNames, message, false);
				}
				
				// After uploading redirects to some URL
				if (redirect) {
					ServletContext sc = getServletContext();
					request.setAttribute("docPath", fuResponse.get().getPath());
					request.setAttribute("uuid", uploadedUuid);
					sc.setAttribute("docPath", fuResponse.get().getPath());
					sc.setAttribute("uuid", uploadedUuid);
					sc.getRequestDispatcher(redirectURL).forward(request, response);
				}
			} else {
				// Used only when document is digital signed ( form in that case is not multipart it's a normal post )
				action = (request.getParameter("action") != null ? Integer.parseInt(request.getParameter("action")) : -1);
				
				if (action == UIFileUploadConstants.ACTION_DIGITAL_SIGNATURE_INSERT
						|| action == UIFileUploadConstants.ACTION_DIGITAL_SIGNATURE_UPDATE) {
					path = request.getParameter("path");
					String data = request.getParameter("data");
					tmp = java.io.File.createTempFile("okm", ".tmp");
					FileOutputStream fos = new FileOutputStream(tmp);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bos.write(SecureStore.b64Decode(data));
					bos.flush();
					bos.close();
					fos.flush();
					fos.close();
					FileInputStream fis = new FileInputStream(tmp);
					
					switch (action) {
						case UIFileUploadConstants.ACTION_DIGITAL_SIGNATURE_INSERT:
							Document newDoc = new Document();
							path = path.substring(0, path.lastIndexOf(".") + 1) + "pdf";
							newDoc.setPath(path);
							newDoc = OKMDocument.getInstance().create(null, newDoc, fis);
							fuResponse.get().setPath(newDoc.getPath());
							break;
						
						case UIFileUploadConstants.ACTION_DIGITAL_SIGNATURE_UPDATE:
							OKMDocument.getInstance().checkout(null, path);
							OKMDocument.getInstance().checkin(null, path, fis, "Signed");
							fuResponse.get().setPath(path);
							break;
					}
				}
			}
		} catch (AccessDeniedException e) {
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_AccessDenied));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (PathNotFoundException e) {
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_PathNotFound));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (ItemExistsException e) {
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_ItemExists));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (UnsupportedMimeTypeException e) {
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_UnsupportedMimeType));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (FileSizeExceededException e) {
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_FileSizeExceeded));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (VirusDetectedException e) {
			fuResponse.get().setError(VirusDetectedException.class.getSimpleName() + " : " + e.getMessage());
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (VersionException e) {
			log.error(e.getMessage(), e);
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_Version));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (RepositoryException e) {
			log.error(e.getMessage(), e);
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_Repository));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (DatabaseException e) {
			log.error(e.getMessage(), e);
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_Database));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (ExtensionException e) {
			log.error(e.getMessage(), e);
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_Extension));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_IO));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (ConversionException e) {
			fuResponse.get().setError(ErrorCode.get(ErrorCode.ORIGIN_OKMUploadService, ErrorCode.CAUSE_Conversion));
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fuResponse.get().setError(e.toString());
			sendErrorResponse(out, action, fuResponse.get(), request, response, redirect, redirectURL);
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
			
			IOUtils.closeQuietly(is);
			out.flush();
			IOUtils.closeQuietly(out);
			System.gc();
		}
	}
	
	/**
	 * sendErrorResponse
	 */
	private void sendErrorResponse(PrintWriter out, int action, FileUploadResponse fur, HttpServletRequest request,
			HttpServletResponse response, boolean redirect, String redirectURL) {
		if (redirect) {
			ServletContext sc = getServletContext();
			
			try {
				sc.getRequestDispatcher(redirectURL).forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			sendResponse(out, action, fur);
		}
	}
	
	/**
	 * Send response back to browser.
	 */
	private void sendResponse(PrintWriter out, int action, FileUploadResponse fur) {
		Gson gson = new Gson();
		String json = gson.toJson(fur);
		out.print(json);
		log.info("Action: {}, JSON Response: {}", action, json);
	}
	
	/**
	 * Import zipped documents
	 * 
	 * @param path Where import into the repository.
	 * @param is The zip file to import.
	 */
	private synchronized String importZip(String path, InputStream is) throws PathNotFoundException, ItemExistsException,
			AccessDeniedException, RepositoryException, IOException, DatabaseException, ExtensionException, AutomationException {
		log.debug("importZip({}, {})", path, is);
		java.io.File tmpIn = null;
		java.io.File tmpOut = null;
		String errorMsg = null;
		
		try {
			// Create temporal
			tmpIn = File.createTempFile("okm", ".zip");
			tmpOut = FileUtils.createTempDir();
			FileOutputStream fos = new FileOutputStream(tmpIn);
			IOUtils.copy(is, fos);
			fos.close();
			
			// Unzip files
			File fileTmpIn = new File(tmpIn);
			fileTmpIn.archiveCopyAllTo(tmpOut);
			File.umount();
			
			// Import files
			StringWriter out = new StringWriter();
			ImpExpStats stats = RepositoryImporter.importDocuments(null, tmpOut, path, false, false, false, out,
					new TextInfoDecorator(tmpOut));
			
			if (!stats.isOk()) {
				errorMsg = out.toString();
			}
			
			out.close();
		} catch (IOException e) {
			log.error("Error importing zip", e);
			throw e;
		} finally {
			if (tmpIn != null) {
				org.apache.commons.io.FileUtils.deleteQuietly(tmpIn);
			}
			
			if (tmpOut != null) {
				org.apache.commons.io.FileUtils.deleteQuietly(tmpOut);
			}
			
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("Error closing zip input stream", e);
					throw e;
				}
			}
		}
		
		log.debug("importZip: {}", errorMsg);
		return errorMsg;
	}
	
	/**
	 * Import jarred documents
	 * 
	 * @param path Where import into the repository.
	 * @param is The jar file to import.
	 */
	private String importJar(String path, InputStream is) throws PathNotFoundException, ItemExistsException,
			AccessDeniedException, RepositoryException, IOException, DatabaseException, ExtensionException, AutomationException {
		log.debug("importJar({}, {})", path, is);
		java.io.File tmpIn = null;
		java.io.File tmpOut = null;
		String errorMsg = null;
		
		try {
			// Create temporal
			tmpIn = File.createTempFile("okm", ".jar");
			tmpOut = FileUtils.createTempDir();
			FileOutputStream fos = new FileOutputStream(tmpIn);
			IOUtils.copy(is, fos);
			fos.close();
			
			// Unzip files
			File fileTmpIn = new File(tmpIn);
			fileTmpIn.archiveCopyAllTo(tmpOut);
			
			// Import files
			StringWriter out = new StringWriter();
			ImpExpStats stats = RepositoryImporter.importDocuments(null, tmpOut, path, false, false, false, out,
					new TextInfoDecorator(tmpOut));
			if (!stats.isOk()) {
				errorMsg = out.toString();
			}
			out.close();
		} catch (IOException e) {
			log.error("Error importing jar", e);
			throw e;
		} finally {
			if (tmpIn != null) {
				File.umount();
				org.apache.commons.io.FileUtils.deleteQuietly(tmpIn);
			}
			
			if (tmpOut != null) {
				org.apache.commons.io.FileUtils.deleteQuietly(tmpOut);
			}
			
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("Error closing zip input stream", e);
					throw e;
				}
			}
		}
		
		log.debug("importJar: {}", errorMsg);
		return errorMsg;
	}
}
