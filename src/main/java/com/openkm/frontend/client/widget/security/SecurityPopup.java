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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.openkm.frontend.client.Main;
import com.openkm.frontend.client.util.Util;

/**
 * Security popup
 * 
 * @author jllort
 *
 */
public class SecurityPopup extends DialogBox implements ClickHandler {	
	public Status status;
	private VerticalPanel vPanel;
	public CheckBox recursive;
	private Button button;
	private SimplePanel sp;
	public SecurityPanel securityPanel;
	private int width = 612;
	
	/**
	 * Security popup
	 */
	public SecurityPopup() {
		// Establishes auto-close when click outside
		super(false,true);
		
		status = new Status();
		vPanel = new VerticalPanel();
		sp = new SimplePanel();
		securityPanel = new SecurityPanel();
		recursive = new CheckBox(Main.i18n("security.recursive"));
		button = new Button(Main.i18n("button.close"), this);
		
		sp.setHeight("4");
				
		vPanel.add(sp);
		vPanel.add(securityPanel);
		vPanel.add(recursive);
		vPanel.add(button);
		
		vPanel.setCellHeight(sp, "4");
		vPanel.setCellHeight(button, "25");
		vPanel.setCellHorizontalAlignment(securityPanel, VerticalPanel.ALIGN_CENTER);
		vPanel.setCellHorizontalAlignment(button, VerticalPanel.ALIGN_CENTER);
		vPanel.setCellVerticalAlignment(button, VerticalPanel.ALIGN_MIDDLE);
		
		vPanel.setWidth(String.valueOf(width));
		
		button.setStyleName("okm-Button");
		status.setStyleName("okm-StatusPopup");
		
		vPanel.setWidth(String.valueOf(width));

		super.hide();
		setWidget(vPanel);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	public void onClick(ClickEvent event) {
		Main.get().mainPanel.desktop.browser.tabMultiple.securityRefresh();
		super.hide();
	}
	
	/**
	 * Language refresh
	 */
	public void langRefresh() {
		setText(Main.i18n("security.label"));
		recursive.setText(Main.i18n("security.recursive"));
		button.setText(Main.i18n("button.close"));
		securityPanel.langRefresh();
	}
	
	/**
	 * Show the security popup
	 */
	public void show(String path) {
		int left = (Window.getClientWidth()-width) / 2;
		int top = (Window.getClientHeight()-400) / 2;
		setPopupPosition(left, top);
		setText(Main.i18n("security.label"));
		securityPanel.reset(path);
		super.show();
		
		// TODO:Solves minor bug with IE
		if (Util.getUserAgent().startsWith("ie")) {
			securityPanel.tabPanel.setWidth(String.valueOf(width));
			securityPanel.tabPanel.setWidth(String.valueOf((width+1)));
		}
		
		// Fill width must be done on visible widgets
		securityPanel.fillWidth();
	}
	
	
	
	/**
	 * enableAdvancedFilter
	 */
	public void enableAdvancedFilter() {
		securityPanel.enableAdvancedFilter();
	}
}