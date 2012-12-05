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

package com.openkm.frontend.client.widget.security;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollPolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollTableImages;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.openkm.frontend.client.Main;
import com.openkm.frontend.client.bean.GWTPermission;
import com.openkm.frontend.client.service.OKMAuthService;
import com.openkm.frontend.client.service.OKMAuthServiceAsync;

/**
 * RoleScrollTable
 * 
 * @author jllort
 */
public class RoleScrollTable extends Composite {
	private final OKMAuthServiceAsync authService = (OKMAuthServiceAsync) GWT.create(OKMAuthService.class);
	
	private final int PROPERTY_READ = 0;
	private final int PROPERTY_WRITE = 1;
	private final int PROPERTY_DELETE = 2;
	private final int PROPERTY_SECURITY = 3;
	
	private ScrollTable table;
	private FixedWidthFlexTable headerTable;
	private FixedWidthGrid dataTable;
	private boolean isAssigned = false; // Determines if is assigned users table
										// or not
	private int rowIndex = -1;
	private String path;
	private int flag_property;
	private int numberOfColumns = 0;
	private int width = 405;
	
	/**
	 * RoleScrollTable
	 * 
	 * @param isAssigned
	 */
	public RoleScrollTable(boolean isAssigned) {
		this.isAssigned = isAssigned;
		
		ScrollTableImages scrollTableImages = new ScrollTableImages() {
			public AbstractImagePrototype scrollTableAscending() {
				return new AbstractImagePrototype() {
					public void applyTo(Image image) {
						image.setUrl("img/sort_asc.gif");
					}
					
					public Image createImage() {
						return new Image("img/sort_asc.gif");
					}
					
					public String getHTML() {
						return "<img border=\"0\" src=\"img/sort_asc.gif\"/>";
					}
				};
			}
			
			public AbstractImagePrototype scrollTableDescending() {
				return new AbstractImagePrototype() {
					public void applyTo(Image image) {
						image.setUrl("img/sort_desc.gif");
					}
					
					public Image createImage() {
						return new Image("img/sort_desc.gif");
					}
					
					public String getHTML() {
						return "<img border=\"0\" src=\"img/sort_desc.gif\"/>";
					}
				};
			}
			
			public AbstractImagePrototype scrollTableFillWidth() {
				return new AbstractImagePrototype() {
					public void applyTo(Image image) {
						image.setUrl("img/fill_width.gif");
					}
					
					public Image createImage() {
						return new Image("img/fill_width.gif");
					}
					
					public String getHTML() {
						return "<img border=\"0\" src=\"img/fill_width.gif\"/>";
					}
				};
			}
		};
		
		headerTable = new FixedWidthFlexTable();
		dataTable = new FixedWidthGrid();
		
		table = new ScrollTable(dataTable, headerTable, scrollTableImages);
		table.setCellSpacing(0);
		table.setCellPadding(2);
		
		// Table data
		dataTable.setSelectionPolicy(SelectionGrid.SelectionPolicy.ONE_ROW);
		table.setResizePolicy(ResizePolicy.UNCONSTRAINED);
		table.setScrollPolicy(ScrollPolicy.BOTH);
		
		initSecurity();
		
		initWidget(table);
	}
	
	/**
	 * initSecurity
	 * 
	 * @param extendedSecurity
	 */
	public void initSecurity() {
		// Level 1 headers
		int col = 0;
		if (isAssigned) {
			headerTable.setHTML(0, col, Main.i18n("security.role.name"));
			table.setColumnWidth(col++, 175);
			headerTable.setHTML(0, col, Main.i18n("security.role.permission.read"));
			table.setColumnWidth(col++, 55);
			headerTable.setHTML(0, col, Main.i18n("security.role.permission.write"));
			table.setColumnWidth(col++, 55);
			headerTable.setHTML(0, col, Main.i18n("security.role.permission.delete"));
			table.setColumnWidth(col++, 55);
			headerTable.setHTML(0, col, Main.i18n("security.role.permission.security"));
			table.setColumnWidth(col++, 55);
			
			headerTable.setHTML(0, col, ""); // Hidden user id
			table.setColumnWidth(col++, 0);
			numberOfColumns = col; // Number of columns
			table.setSize(String.valueOf(width), "365"); // Setting table size
		} else {
			table.setSize("185", "365");
			headerTable.setHTML(0, col, Main.i18n("security.role.name"));
			table.setColumnWidth(col++, 167);
			headerTable.setHTML(0, col, ""); // Hidden user id
			table.setColumnWidth(col++, 0);
			numberOfColumns = col;
		}
	}
	
	/**
	 * Lang refresh
	 */
	public void langRefresh() {
		int col = 0;
		if (isAssigned) {
			headerTable.setHTML(0, col++, Main.i18n("security.role.name"));
			headerTable.setHTML(0, col++, Main.i18n("security.role.permission.read"));
			headerTable.setHTML(0, col++, Main.i18n("security.role.permission.write"));
			headerTable.setHTML(0, col++, Main.i18n("security.role.permission.delete"));
			headerTable.setHTML(0, col++, Main.i18n("security.role.permission.security"));
		} else {
			headerTable.setHTML(0, col++, Main.i18n("security.role.name"));
		}
	}
	
	/**
	 * Adds new username permission row
	 * 
	 * @param userName The role name value
	 * @param permission The permission value
	 */
	public void addRow(String roleName, Integer permission) {
		final int rows = dataTable.getRowCount();
		dataTable.insertRow(rows);
		dataTable.setHTML(rows, 0, roleName);
		
		CheckBox checkReadPermission = new CheckBox();
		CheckBox checkWritePermission = new CheckBox();
		CheckBox checkDeletePermission = new CheckBox();
		CheckBox checkSecurityPermission = new CheckBox();
		
		ClickHandler checkBoxReadListener = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				flag_property = PROPERTY_READ;
				Widget sender = (Widget) event.getSource();
				
				// Actions are inverse to check value because before user
				// perform check on checkbox
				// it has inverse value
				if (((CheckBox) sender).getValue()) {
					grant(dataTable.getText(rows, 0), GWTPermission.READ, Main.get().securityPopup.recursive.getValue());
				} else {
					revoke(dataTable.getText(rows, 0), GWTPermission.READ,
							Main.get().securityPopup.recursive.getValue());
				}
			}
		};
		
		ClickHandler checkBoxWriteListener = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				flag_property = PROPERTY_WRITE;
				Widget sender = (Widget) event.getSource();
				
				// Actions are inverse to check value because before user
				// perform check on checkbox
				// it has inverse value
				if (((CheckBox) sender).getValue()) {
					grant(dataTable.getText(rows, 0), GWTPermission.WRITE,
							Main.get().securityPopup.recursive.getValue());
				} else {
					revoke(dataTable.getText(rows, 0), GWTPermission.WRITE,
							Main.get().securityPopup.recursive.getValue());
				}
			}
		};
		
		ClickHandler checkBoxDeleteListener = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				flag_property = PROPERTY_DELETE;
				Widget sender = (Widget) event.getSource();
				
				// Actions are inverse to check value because before user
				// perform check on checkbox
				// it has inverse value
				if (((CheckBox) sender).getValue()) {
					grant(dataTable.getText(rows, 0), GWTPermission.DELETE,
							Main.get().securityPopup.recursive.getValue());
				} else {
					revoke(dataTable.getText(rows, 0), GWTPermission.DELETE,
							Main.get().securityPopup.recursive.getValue());
				}
			}
		};
		
		ClickHandler checkBoxSecurityListener = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				flag_property = PROPERTY_SECURITY;
				Widget sender = (Widget) event.getSource();
				
				// Actions are inverse to check value because before user
				// perform check on checkbox
				// it has inverse value
				if (((CheckBox) sender).getValue()) {
					grant(dataTable.getText(rows, 0), GWTPermission.SECURITY,
							Main.get().securityPopup.recursive.getValue());
				} else {
					revoke(dataTable.getText(rows, 0), GWTPermission.SECURITY,
							Main.get().securityPopup.recursive.getValue());
				}
			}
		};
		
		checkReadPermission.addClickHandler(checkBoxReadListener);
		
		int col = 0;
		col++; // Name
		
		if ((permission & GWTPermission.READ) == GWTPermission.READ) {
			checkReadPermission.setValue(true);
			dataTable.setWidget(rows, col, checkReadPermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		} else {
			checkReadPermission.setValue(false);
			dataTable.setWidget(rows, col, checkReadPermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		}
		
		checkWritePermission.addClickHandler(checkBoxWriteListener);
		
		if ((permission & GWTPermission.WRITE) == GWTPermission.WRITE) {
			checkWritePermission.setValue(true);
			dataTable.setWidget(rows, col, checkWritePermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		} else {
			checkWritePermission.setValue(false);
			dataTable.setWidget(rows, col, checkWritePermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		}
		
		checkDeletePermission.addClickHandler(checkBoxDeleteListener);
		
		if ((permission & GWTPermission.DELETE) == GWTPermission.DELETE) {
			checkDeletePermission.setValue(true);
			dataTable.setWidget(rows, col, checkDeletePermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		} else {
			checkDeletePermission.setValue(false);
			dataTable.setWidget(rows, col, checkDeletePermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		}
		
		checkSecurityPermission.addClickHandler(checkBoxSecurityListener);
		
		if ((permission & GWTPermission.SECURITY) == GWTPermission.SECURITY) {
			checkSecurityPermission.setValue(true);
			dataTable.setWidget(rows, col, checkSecurityPermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		} else {
			checkSecurityPermission.setValue(false);
			dataTable.setWidget(rows, col, checkSecurityPermission);
			dataTable.getCellFormatter().setHorizontalAlignment(rows, col++, HasAlignment.ALIGN_CENTER);
		}
	}
	
	/**
	 * Adds new roleName name row
	 * 
	 * @param roleName The user name value
	 */
	public void addRow(String roleName) {
		int rows = dataTable.getRowCount();
		dataTable.insertRow(rows);
		dataTable.setHTML(rows, 0, roleName);
	}
	
	/**
	 * Selects the last row
	 */
	public void selectLastRow() {
		if (dataTable.getRowCount() > 0) {
			dataTable.selectRow(dataTable.getRowCount() - 1, true);
		}
	}
	
	/**
	 * Removes all rows except the first
	 */
	public void removeAllRows() {
		// Purge all rows
		while (dataTable.getRowCount() > 0) {
			dataTable.removeRow(0);
		}
		dataTable.resize(0, numberOfColumns);
	}
	
	/**
	 * Reset table values
	 */
	public void reset() {
		removeAllRows();
	}
	
	/**
	 * Gets the role
	 * 
	 * @return The role
	 */
	public String getRole() {
		String role = null;
		
		if (!dataTable.getSelectedRows().isEmpty()) {
			int selectedRow = ((Integer) dataTable.getSelectedRows().iterator().next()).intValue();
			
			if (dataTable.isRowSelected(selectedRow)) {
				role = dataTable.getHTML(((Integer) dataTable.getSelectedRows().iterator().next()).intValue(), 0);
			}
		}
		
		return role;
	}
	
	/**
	 * Removes the selected row
	 */
	public void removeSelectedRow() {
		if (!dataTable.getSelectedRows().isEmpty()) {
			int selectedRow = ((Integer) dataTable.getSelectedRows().iterator().next()).intValue();
			dataTable.removeRow(selectedRow);
			
			if (dataTable.getRowCount() > 0) {
				if (dataTable.getRowCount() > selectedRow) {
					dataTable.selectRow(selectedRow, true);
				} else {
					dataTable.selectRow(selectedRow - 1, true);
				}
			}
		}
	}
	
	/**
	 * Call back add new role grant
	 */
	final AsyncCallback<Object> callbackGrantRole = new AsyncCallback<Object>() {
		public void onSuccess(Object result) {
			Log.debug("RoleScrollTable.callbackGrantRole.onSuccess(" + result + ")");
			Main.get().securityPopup.status.unsetFlag_update();
		}
		
		public void onFailure(Throwable caught) {
			Log.debug("RoleScrollTable.callbackGrantRole.onFailure(" + caught + ")");
			
			int col = 0;
			col++; // Name
			if (flag_property < PROPERTY_READ) {
				col++;
			}
			if (flag_property < PROPERTY_WRITE) {
				col++;
			}
			if (flag_property < PROPERTY_DELETE) {
				col++;
			}
			if (flag_property < PROPERTY_SECURITY) {
				col++;
			}
			
			((CheckBox) dataTable.getWidget(rowIndex, col)).setValue(false);
			
			Main.get().securityPopup.status.unsetFlag_update();
			Main.get().showError("GrantRole", caught);
		}
	};
	
	/**
	 * Call back revoke role grant
	 */
	final AsyncCallback<Object> callbackRevokeRole = new AsyncCallback<Object>() {
		public void onSuccess(Object result) {
			Log.debug("RoleScrollTable.callbackRevokeRole.onSuccess(" + result + ")");
			
			// If user has no grants must be deleted
			if (!dataTable.getSelectedRows().isEmpty()) {
				int selectedRow = ((Integer) dataTable.getSelectedRows().iterator().next()).intValue();
				
				// If user has no grants must be deleted
				int col = 0;
				col++; // Name
				boolean isChecked = ((CheckBox) dataTable.getWidget(selectedRow, col++)).getValue()
						|| ((CheckBox) dataTable.getWidget(selectedRow, col++)).getValue()
						|| ((CheckBox) dataTable.getWidget(selectedRow, col++)).getValue()
						|| ((CheckBox) dataTable.getWidget(selectedRow, col++)).getValue();
				
				if (!isChecked) {
					Main.get().securityPopup.securityPanel.securityRole.unassignedRole.addRow(dataTable.getText(
							selectedRow, 0));
					removeSelectedRow();
				}
			}
			
			Main.get().securityPopup.status.unsetFlag_update();
		}
		
		public void onFailure(Throwable caught) {
			Log.debug("RoleScrollTable.callbackRevokeRole.onFailure(" + caught + ")");
			
			int col = 0;
			col++; // Name
			if (flag_property < PROPERTY_READ) {
				col++;
			}
			if (flag_property < PROPERTY_WRITE) {
				col++;
			}
			if (flag_property < PROPERTY_DELETE) {
				col++;
			}
			if (flag_property < PROPERTY_SECURITY) {
				col++;
			}
			
			((CheckBox) dataTable.getWidget(rowIndex, col)).setValue(true);
			
			Main.get().securityPopup.status.unsetFlag_update();
			Main.get().showError("RevokeRole", caught);
		}
	};
	
	/**
	 * Grant the role
	 * 
	 * @param user The granted role
	 * @param permissions The permissions value
	 */
	public void grant(String role, int permissions, boolean recursive) {
		if (path != null) {
			Log.debug("RoleScrollTable.grant(" + role + ", " + permissions + ", " + recursive + ")");
			Main.get().securityPopup.status.setFlag_update();
			authService.grantRole(path, role, permissions, recursive, callbackGrantRole);
		}
	}
	
	/**
	 * Revoke the role grant
	 * 
	 * @param user The role
	 * @param permissions The permissions value
	 */
	public void revoke(String role, int permissions, boolean recursive) {
		if (path != null) {
			Log.debug("RoleScrollTable.revoke(" + role + ", " + permissions + ", " + recursive + ")");
			Main.get().securityPopup.status.setFlag_update();
			authService.revokeRole(path, role, permissions, recursive, callbackRevokeRole);
		}
	}
	
	/**
	 * Sets the path
	 * 
	 * @param path The path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * fillWidth
	 */
	public void fillWidth() {
		table.fillWidth();
	}
	
	/**
	 * getDataTable
	 * 
	 * @return FixedWidthGrid
	 */
	public FixedWidthGrid getDataTable() {
		return table.getDataTable();
	}
	
	/**
	 * getNumberOfColumns
	 * 
	 * @return
	 */
	public int getNumberOfColumns() {
		return numberOfColumns;
	}
}