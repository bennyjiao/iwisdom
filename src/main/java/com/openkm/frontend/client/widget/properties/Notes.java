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

package com.openkm.frontend.client.widget.properties;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.openkm.frontend.client.Main;
import com.openkm.frontend.client.bean.GWTDocument;
import com.openkm.frontend.client.bean.GWTFolder;
import com.openkm.frontend.client.bean.GWTMail;
import com.openkm.frontend.client.bean.GWTNote;
import com.openkm.frontend.client.extension.event.HasDocumentEvent;
import com.openkm.frontend.client.service.OKMNoteService;
import com.openkm.frontend.client.service.OKMNoteServiceAsync;
import com.openkm.frontend.client.util.OKMBundleResources;
import com.openkm.frontend.client.widget.ConfirmPopup;
import com.openkm.frontend.client.widget.richtext.RichTextToolbar;

/**
 * Notes
 * 
 * @author jllort
 *
 */
public class Notes extends Composite {
	private final OKMNoteServiceAsync noteService = (OKMNoteServiceAsync) GWT.create(OKMNoteService.class);
	public static final int DOCUMENT_NOTE 	= 1;
	public static final int FOLDER_NOTE 	= 2;
	public static final int MAIL_NOTE		= 3;
	
	private FlexTable tableNotes;
	private Object object;
	private Button add;
	private Button update;
	private Button cancel;
	private ScrollPanel scrollPanel;
	public RichTextArea richTextArea;
	private RichTextToolbar richTextToolbar;
	private VerticalPanel newNotePanel;
	private HTML addNote;
	private Grid gridRichText;
	boolean visibleButtons = true;
	boolean addNoteOption = false;
	boolean isEditingNote = false;
	private String editedNotePath = "";
	private int editedNoteRow = 0;
	private boolean removeNoteEnabled = false;
	int type = 0;
	
	/**
	 * Notes
	 */
	public Notes(int type) {
		this.type = type;
		tableNotes = new FlexTable();
		scrollPanel = new ScrollPanel(tableNotes);
		newNotePanel = new VerticalPanel(); 
		addNote = new HTML("<b>" + Main.i18n("general.menu.edit.add.note") + "</b>");
		richTextArea = new RichTextArea();
		richTextArea.setSize("100%", "14em");
		richTextToolbar = new RichTextToolbar(richTextArea);
		// richTextToolbar.setWidth("100%");
	    
	    gridRichText = new Grid(2, 1);
	    gridRichText.setStyleName("RichTextToolbar");
	    gridRichText.addStyleName("okm-Input");
	    gridRichText.setWidget(0, 0, richTextToolbar);
	    gridRichText.setWidget(1, 0, richTextArea);
	    
		add = new Button(Main.i18n("button.add"), new ClickHandler() { 
			@Override
			public void onClick(ClickEvent event) {
				addNote();
			}
		});
		
		update = new Button(Main.i18n("button.update"), new ClickHandler() { 
			@Override
			public void onClick(ClickEvent event) {
				setNote(editedNotePath, getTextNote(), editedNoteRow);
			}
		});
		
		cancel = new Button(Main.i18n("button.cancel"), new ClickHandler() { 
			@Override
			public void onClick(ClickEvent event) {
				reset();
			}
		});
		update.setVisible(false);
		cancel.setVisible(false);
		
		HTML space = new HTML("");
		newNotePanel.add(space);
		newNotePanel.add(addNote);
		newNotePanel.add(gridRichText);
		HTML space2 = new HTML("");
		newNotePanel.add(space2);
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(add);
		hPanel.add(new HTML("&nbsp;"));
		hPanel.add(update);
		hPanel.add(new HTML("&nbsp;"));
		hPanel.add(cancel);
		newNotePanel.add(hPanel);
		
		newNotePanel.setCellHeight(space, "40px");
		newNotePanel.setCellHeight(space2, "10px");
		newNotePanel.setCellHorizontalAlignment(addNote, HasAlignment.ALIGN_CENTER);
		newNotePanel.setCellHorizontalAlignment(gridRichText, HasAlignment.ALIGN_CENTER);
		newNotePanel.setCellHorizontalAlignment(hPanel, HasAlignment.ALIGN_CENTER);
		
		add.setStyleName("okm-Button");
		update.setStyleName("okm-Button");
		cancel.setStyleName("okm-Button");

		tableNotes.setWidth("100%");
		
		newNotePanel.setVisible(false);
		
		initWidget(scrollPanel);
	}
	
	/**
	 * Sets the document values
	 * 
	 * @param doc The document object
	 */
	public void set(GWTDocument doc) {
		reset();
		object = doc;
		richTextArea.setText("");
		
		while (tableNotes.getRowCount() > 0) {
			tableNotes.removeRow(0);
		}
		
		for (Iterator<GWTNote> it = doc.getNotes().iterator(); it.hasNext();) {
			writeNote(it.next());
		}
		
		writeAddNote();
	}
	
	/**
	 * Sets the folder values
	 * 
	 * @param doc The folder object
	 */
	public void set(GWTFolder folder) {
		reset();
		object = folder;
		richTextArea.setText("");
		
		while (tableNotes.getRowCount() > 0) {
			tableNotes.removeRow(0);
		}
		
		for (Iterator<GWTNote> it = folder.getNotes().iterator(); it.hasNext();) {
			writeNote(it.next());
		}
		
		writeAddNote();
	}
	
	/**
	 * Sets the folder values
	 * 
	 * @param doc The folder object
	 */
	public void set(GWTMail mail) {
		reset();
		object = mail;
		richTextArea.setText("");
		
		while (tableNotes.getRowCount() > 0) {
			tableNotes.removeRow(0);
		}
		
		for (Iterator<GWTNote> it = mail.getNotes().iterator(); it.hasNext();) {
			writeNote(it.next());
		}
		
		writeAddNote();
	}
	
	/**
	 * writeAddNote
	 */
	private void writeAddNote() {
		int row = tableNotes.getRowCount();
		tableNotes.setWidget(row, 0, newNotePanel);
		tableNotes.getFlexCellFormatter().setColSpan(row, 0, 2);
		tableNotes.getCellFormatter().setHorizontalAlignment(row, 0, HasAlignment.ALIGN_CENTER);
	}
	
	/**
	 * Writes the note 
	 * 
	 * @param note
	 */
	private void writeNote(final GWTNote note) {
		int row = tableNotes.getRowCount();
		tableNotes.setHTML(row, 0, "<b>" + note.getUser().getUsername() + "</b>");
		Image editNote = new Image(OKMBundleResources.INSTANCE.noteEdit());
		Image deleteNote = new Image(OKMBundleResources.INSTANCE.noteDelete());
		editNote.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reset();
				isEditingNote = true;
				add.setVisible(false);
				update.setVisible(true);
				cancel.setVisible(true);
				editedNoteRow = tableNotes.getCellForEvent(event).getRowIndex() + 2; // The text row is + 2
				editedNotePath = note.getPath();
				setTextNoteToEditor(tableNotes.getHTML(editedNoteRow, 0));
			}
		});
		
		deleteNote.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				switch (type) {
					case DOCUMENT_NOTE:
						Main.get().confirmPopup.setConfirm(ConfirmPopup.CONFIRM_DELETE_NOTE_DOCUMENT);
						break;
					case FOLDER_NOTE:
						Main.get().confirmPopup.setConfirm(ConfirmPopup.CONFIRM_DELETE_NOTE_FOLDER);
						break;
					case MAIL_NOTE:
						Main.get().confirmPopup.setConfirm(ConfirmPopup.CONFIRM_DELETE_NOTE_MAIL);
						break;
				}
				
				NoteToDelete noteToDelete = new NoteToDelete(note.getPath(), tableNotes.getCellForEvent(event).getRowIndex());
				Main.get().confirmPopup.setValue(noteToDelete);
				Main.get().confirmPopup.show();
			}
		});
		
		editNote.setStyleName("okm-Hyperlink");
		deleteNote.setStyleName("okm-Hyperlink");
		
		DateTimeFormat dtf = DateTimeFormat.getFormat(Main.i18n("general.date.pattern"));
		HTML space = new HTML("");
		HTML space2 = new HTML("");
		HTML date = new HTML(dtf.format(note.getDate()));
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(editNote);
		hPanel.add(space);
		
		if (removeNoteEnabled) {
			hPanel.add(deleteNote);
		}
		
		hPanel.add(space2);
		hPanel.add(date);
		hPanel.setCellWidth(space, "5");
		hPanel.setCellWidth(space2, "5");
		
		if (note.getAuthor().equals(Main.get().workspaceUserProperties.getUser().getId())) {
			if (visibleButtons || addNoteOption) {
				editNote.setVisible(true);
			} else {
				editNote.setVisible(false);
			}
		} else {
			editNote.setVisible(false);
		}
		
		if (note.getAuthor().equals(Main.get().workspaceUserProperties.getUser().getId()) || 
				(Main.get().workspaceUserProperties.getWorkspace() != null &&
						Main.get().workspaceUserProperties.getWorkspace().isAdminRole())) {
			if (visibleButtons || addNoteOption) {
				deleteNote.setVisible(true);
			} else {
				deleteNote.setVisible(false);
			}
		} else {
			deleteNote.setVisible(false);
		}
		
		tableNotes.setWidget(row, 1, hPanel);
		tableNotes.getCellFormatter().setHorizontalAlignment(row, 1, HasAlignment.ALIGN_RIGHT);
		tableNotes.getRowFormatter().setStyleName(row, "okm-Notes-Title");
		tableNotes.getCellFormatter().setHeight(row, 1, "30");
		tableNotes.getCellFormatter().setVerticalAlignment(row, 0, HasAlignment.ALIGN_BOTTOM);
		tableNotes.getCellFormatter().setVerticalAlignment(row, 1, HasAlignment.ALIGN_BOTTOM);
		row++;
		tableNotes.setHTML(row, 0, "");
		tableNotes.getCellFormatter().setHeight(row, 0, "6");
		tableNotes.getRowFormatter().setStyleName(row, "okm-Notes-Line");
		tableNotes.getFlexCellFormatter().setColSpan(row, 0, 2);
		row++;
		tableNotes.setHTML(row, 0, note.getText());
		tableNotes.getFlexCellFormatter().setColSpan(row, 0, 2);
	}
	
	/**
	 * Lang refresh
	 */
	public void langRefresh() {		
		add.setHTML(Main.i18n("button.add"));
		update.setHTML(Main.i18n("button.update"));
		cancel.setHTML(Main.i18n("button.cancel"));
		addNote.setHTML("<b>" + Main.i18n("general.menu.edit.add.note") + "</b>");
		richTextToolbar.langRefresh();
	}	
	
	/**
	 * Sets visibility to buttons ( true / false )
	 * 
	 * @param visible The visible value
	 */
	public void setVisibleButtons(boolean visible) {
		visibleButtons = visible;
		add.setVisible(visible);
		addNote.setVisible(visible);
		gridRichText.setVisible(visible);
	}
	
	/**
	 * Sets the visible add note or not
	 * 
	 * @param visible The visible value
	 */
	public void setVisibleAddNote(boolean visible) {
		addNoteOption = visible && visibleButtons;
		add.setVisible(addNoteOption);
		addNote.setVisible(addNoteOption);
		gridRichText.setVisible(addNoteOption);
	}
	
	/**
	 * Callback addNote 
	 */
	final AsyncCallback<GWTNote> callbackAddNote = new AsyncCallback<GWTNote>() {
		public void onSuccess(GWTNote result) {	
			tableNotes.removeRow(tableNotes.getRowCount() - 1); // Deletes last row = addComment
			writeNote(result);
			writeAddNote();
			reset();
			
			if (object instanceof GWTDocument) {
				GWTDocument document = (GWTDocument) object;
				document.getNotes().add(result);
				
				if (!document.isHasNotes()) {
					Main.get().mainPanel.desktop.browser.fileBrowser.addNoteIconToSelectedRow();
					document.setHasNotes(true);
				}
			} else if (object instanceof GWTFolder) {
				GWTFolder folder = (GWTFolder) object;
				folder.getNotes().add(result);
				
				// If is added first note must adding some icon on filebrowser
				if (!folder.isHasNotes() && !Main.get().activeFolderTree.isPanelSelected()) {
					Main.get().mainPanel.desktop.browser.fileBrowser.addNoteIconToSelectedRow();
					folder.setHasNotes(true);
				}
			} else if (object instanceof GWTMail) {
				GWTMail mail = (GWTMail) object;
				mail.getNotes().add(result);
				
				if (!mail.isHasNotes()) {
					Main.get().mainPanel.desktop.browser.fileBrowser.addNoteIconToSelectedRow();
					mail.setHasNotes(true);
				}
			}
			
			Main.get().mainPanel.desktop.browser.tabMultiple.tabDocument.fireEvent(HasDocumentEvent.NOTE_ADDED);
		}

		public void onFailure(Throwable caught) {
			Main.get().showError("addNote", caught);
		}
	};
	
	/**
	 * addNote
	 */
	private void addNote() {
		noteService.add(getPath(), getTextNote(), callbackAddNote);
	}
	
	/**
	 * addNote
	 */
	public void addNote(String text) {
		noteService.add(getPath(), text, callbackAddNote);
	}
	
	/**
	 * getPath
	 */
	private String getPath() {
		String path = "";
		
		if (object instanceof GWTDocument) {
			path = ((GWTDocument)object).getPath();
		} else if (object instanceof GWTFolder) {
			path = ((GWTFolder)object).getPath();
		} else if (object instanceof GWTMail) {
			path = ((GWTMail)object).getPath();
		}
		
		return path;
	}
	
	/**
	 * getTextNote
	 */
	private String getTextNote() {
		return richTextArea.getHTML();
	}
	
	/**
	 * setTextNoteToEditor
	 */
	private void setTextNoteToEditor(String text) {
		richTextArea.setHTML(text);
	}
	
	/**
	 * reset
	 */
	private void reset() {
		isEditingNote = false;
		editedNotePath = "";
		editedNoteRow = 0;
		richTextArea.setText("");
		add.setHTML(Main.i18n("button.add"));
		
		if (visibleButtons) {
			add.setVisible(true);
		}
		
		update.setVisible(false);
		cancel.setVisible(false);
	}
	
	/**
	 * deleteNote
	 */
	public void deleteNote(final String notePath, final int row) {
		noteService.delete(notePath, new AsyncCallback<Object>() {
			@Override
			public void onSuccess(Object result) {
				tableNotes.removeRow(row); // row + 0
				tableNotes.removeRow(row); // row + 1;
				tableNotes.removeRow(row); // row + 2
				
				for (GWTNote note : getNotes()) {
					if (note.getPath().equals(notePath)) {
						getNotes().remove(note);
						break;
					}
				}
				
				if (getNotes().isEmpty()) {
					if (object instanceof GWTDocument) {
						((GWTDocument)object).setHasNotes(false);
					} else if (object instanceof GWTFolder) {
						((GWTFolder)object).setHasNotes(false);
					} else if (object instanceof GWTMail) {
						((GWTMail)object).setHasNotes(false);
					}
					
					Main.get().mainPanel.desktop.browser.fileBrowser.deleteNoteIconToSelectedRow();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Main.get().showError("removeNote", caught);
			}
		});
	}
	
	/**
	 * setNote
	 */
	private void setNote(String notePath, final String text, final int row) {
		noteService.set(notePath, text, new AsyncCallback<Object>() {
			@Override
			public void onSuccess(Object result) {
				tableNotes.setHTML(row, 0, text);
				
				for (GWTNote note : getNotes()) {
					if (note.getPath().equals(editedNotePath)) {
						note.setText(text);
						break;
					}
				}
				
				reset();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Main.get().showError("setNote", caught);
			}
		});
	}
	
	/**
	 * getNotes
	 */
	public Collection<GWTNote> getNotes() {
		if (object instanceof GWTDocument) {
			return ((GWTDocument)object).getNotes();
		} else if (object instanceof GWTFolder) {
			return ((GWTFolder)object).getNotes();
		} else if (object instanceof GWTMail) {
			return ((GWTMail)object).getNotes();
		} else {
			return null;
		}
	}
	
	/**
	 * setAvailableOption
	 */
	public void showAddNote() {
		newNotePanel.setVisible(true);
	}
	
	/**
	 * showRemoveNote
	 */
	public void showRemoveNote() {
		removeNoteEnabled = true;
	}
	
	/**
	 * NoteToDelete
	 * 
	 * @author jllort
	 *
	 */
	public class NoteToDelete {
		private String notePath = "";
		private int row = 0;
		
		/**
		 * NoteToDelete
		 */
		public NoteToDelete(String notePath, int row) {
			this.notePath = notePath;
			this.row = row;
		}

		/**
		 * getNotePath
		 */
		public String getNotePath() {
			return notePath;
		}

		/**
		 * getRow
		 */
		public int getRow() {
			return row;
		}
	}
}
