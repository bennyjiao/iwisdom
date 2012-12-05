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

package com.openkm.frontend.client.widget.security;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.openkm.frontend.client.Main;
import com.openkm.frontend.client.bean.GWTGrantedUser;
import com.openkm.frontend.client.bean.GWTPermission;
import com.openkm.frontend.client.bean.GWTUser;
import com.openkm.frontend.client.service.OKMAuthService;
import com.openkm.frontend.client.service.OKMAuthServiceAsync;
import com.openkm.frontend.client.util.GWTGrantedUserComparator;
import com.openkm.frontend.client.util.OKMBundleResources;

/**
 * Security User
 * 
 * @author jllort
 * 
 */
public class SecurityUser extends Composite implements HasWidgets {
	private final OKMAuthServiceAsync authService = (OKMAuthServiceAsync) GWT.create(OKMAuthService.class);
	
	public UserScrollTable assignedUser;
	public UserScrollTable unassignedUser;
	private HorizontalPanel panel;
	private VerticalPanel buttonPanel;
	private SimplePanel spRight;
	private SimplePanel spHeight;
	private Image addButton;
	private Image removeButton;
	private String path = "";
	private GWTUser tmpUser;
	private int width = 612;
	
	/**
	 * Security user
	 */
	public SecurityUser() {
		panel = new HorizontalPanel();
		buttonPanel = new VerticalPanel();
		assignedUser = new UserScrollTable(true);
		unassignedUser = new UserScrollTable(false);
		spRight = new SimplePanel();
		spHeight = new SimplePanel();
		spRight.setWidth("1");
		spHeight.setHeight("30");
		
		addButton = new Image(OKMBundleResources.INSTANCE.add());
		removeButton = new Image(OKMBundleResources.INSTANCE.remove());
		
		buttonPanel.add(addButton);
		buttonPanel.add(spHeight); // separator
		buttonPanel.add(removeButton);
		
		addButton.addClickHandler(addButtonHandler);
		removeButton.addClickHandler(removeButtonHandler);
		
		panel.add(unassignedUser);
		panel.add(buttonPanel);
		panel.add(assignedUser);
		
		panel.setCellWidth(buttonPanel, "20");
		panel.setCellVerticalAlignment(buttonPanel, HasAlignment.ALIGN_MIDDLE);
		panel.setCellHorizontalAlignment(buttonPanel, HasAlignment.ALIGN_CENTER);
		
		assignedUser.addStyleName("okm-Border-Left");
		assignedUser.addStyleName("okm-Border-Right");
		
		unassignedUser.addStyleName("okm-Border-Left");
		unassignedUser.addStyleName("okm-Border-Right");
		
		panel.setSize(String.valueOf(width), "365");
		
		initWidget(panel);
	}
	
	/**
	 * Add button listener
	 */
	ClickHandler addButtonHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			if (unassignedUser.getUser() != null) {
				tmpUser = unassignedUser.getUser();
				addUser(tmpUser, GWTPermission.READ, Main.get().securityPopup.recursive.getValue());
			}
		}
	};
	
	/**
	 * Remove button listener
	 */
	ClickHandler removeButtonHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			if (assignedUser.getUser() != null) {
				tmpUser = assignedUser.getUser();
				removeUser(tmpUser, Main.get().securityPopup.recursive.getValue());
			}
		}
	};
	
	/**
	 * Language refresh
	 */
	public void langRefresh() {
		assignedUser.langRefresh();
		unassignedUser.langRefresh();
	}
	
	/**
	 * Resets the values
	 */
	public void reset() {
		assignedUser.reset();
		unassignedUser.reset();
		assignedUser.getDataTable().resize(0, assignedUser.getNumberOfColumns());
		unassignedUser.getDataTable().resize(0, unassignedUser.getNumberOfColumns());
	}
	
	/**
	 * resetUnassigned
	 */
	public void resetUnassigned() {
		unassignedUser.reset();
		unassignedUser.getDataTable().resize(0, unassignedUser.getNumberOfColumns());
	}
	
	/**
	 * Call back add new granted user
	 */
	final AsyncCallback<Object> callbackAddUser = new AsyncCallback<Object>() {
		public void onSuccess(Object result) {
			assignedUser.addRow(tmpUser, new Integer(GWTPermission.READ));
			unassignedUser.removeSelectedRow();
			tmpUser = new GWTUser();
			Main.get().securityPopup.status.unsetFlag_update();
		}
		
		public void onFailure(Throwable caught) {
			Main.get().securityPopup.status.unsetFlag_update();
			Main.get().showError("AddUser", caught);
		}
	};
	
	/**
	 * Call back revoke granted user
	 */
	final AsyncCallback<Object> callbackRevokeUser = new AsyncCallback<Object>() {
		public void onSuccess(Object result) {
			unassignedUser.addRow(tmpUser);
			unassignedUser.selectLastRow();
			assignedUser.removeSelectedRow();
			tmpUser = new GWTUser();
			Main.get().securityPopup.status.unsetFlag_update();
		}
		
		public void onFailure(Throwable caught) {
			Main.get().securityPopup.status.unsetFlag_update();
			Main.get().showError("RevokeUser", caught);
		}
	};
	
	/**
	 * Gets the granted users
	 */
	public void getGrantedUsers() {
		if (path != null) {
			authService.getGrantedUsers(path, new AsyncCallback<List<GWTGrantedUser>>() {
				@Override
				public void onSuccess(List<GWTGrantedUser> result) {
					Collections.sort(result, GWTGrantedUserComparator.getInstance());
					for (GWTGrantedUser gu : result) {
						assignedUser.addRow(gu.getUser(), gu.getPermissions());
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Main.get().showError("GetGrantedUsers", caught);
				}
			});
		}
	}
	
	/**
	 * Gets the ungranted users
	 */
	public void getUngrantedUsers() {
		if (path != null) {
			authService.getUngrantedUsers(path, new AsyncCallback<List<GWTGrantedUser>>() {
				@Override
				public void onSuccess(List<GWTGrantedUser> result) {
					for (GWTGrantedUser gu : result) {
						unassignedUser.addRow(gu.getUser());
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Main.get().showError("GetUngrantedUsers", caught);
				}
			});
		}
	}
	
	/**
	 * Gets the ungranted users by filter
	 */
	public void getFilteredUngrantedUsers(String filter) {
		if (path != null) {
			resetUnassigned();
			authService.getFilteredUngrantedUsers(path, filter, new AsyncCallback<List<GWTGrantedUser>>() {
				@Override
				public void onSuccess(List<GWTGrantedUser> result) {
					for (GWTGrantedUser gu : result) {
						unassignedUser.addRow(gu.getUser());
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Main.get().showError("GetUngrantedUsers", caught);
				}
			});
		}
	}
	
	/**
	 * Grant the user
	 * 
	 * @param user The granted user
	 * @param permissions The permissions value
	 */
	public void addUser(GWTUser user, int permissions, boolean recursive) {
		if (path != null) {
			Main.get().securityPopup.status.setFlag_update();
			authService.grantUser(path, user.getId(), permissions, recursive, callbackAddUser);
		}
	}
	
	/**
	 * Revokes all user permissions
	 * 
	 * @param user The user
	 */
	public void removeUser(GWTUser user, boolean recursive) {
		if (path != null) {
			Main.get().securityPopup.status.setFlag_update();
			authService.revokeUser(path, user.getId(), recursive, callbackRevokeUser);
		}
	}
	
	/**
	 * Sets the path
	 * 
	 * @param path The path
	 */
	public void setPath(String path) {
		assignedUser.setPath(path);
		this.path = path;
	}
	
	/**
	 * fillWidth
	 */
	public void fillWidth() {
		assignedUser.fillWidth();
		unassignedUser.fillWidth();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
	 */
	public void add(Widget w) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#clear()
	 */
	public void clear() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
	 */
	public Iterator<Widget> iterator() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
	 */
	public boolean remove(Widget w) {
		return true;
	}
}