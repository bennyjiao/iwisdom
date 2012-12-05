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

package com.openkm.frontend.client.widget.searchin;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.openkm.frontend.client.util.Util;

/**
 * SearchSimple
 * 
 * @author jllort
 *
 */
public class SearchSimple extends Composite {
	private ScrollPanel scrollPanel;
	private VerticalPanel vPanel;
	public TextBox fullText;
	
	/**
	 * SearchSimple
	 */
	public SearchSimple() {
		fullText = new TextBox();
		fullText.setWidth("365");
		vPanel = new VerticalPanel();
		HTML spacer = Util.vSpace("70");
		vPanel.add(spacer);
		vPanel.add(fullText);
		vPanel.setCellHeight(spacer, "70");
		vPanel.setCellVerticalAlignment(fullText, HasAlignment.ALIGN_TOP);
		vPanel.setCellHorizontalAlignment(fullText, HasAlignment.ALIGN_CENTER);
		scrollPanel = new ScrollPanel(vPanel);
		fullText.setStyleName("okm-Input");
		
		initWidget(scrollPanel);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setPixelSize(int, int)
	 */
	public void setPixelSize(int width, int height) {
		scrollPanel.setPixelSize(width, height);
		vPanel.setPixelSize(width, height);
	}
}