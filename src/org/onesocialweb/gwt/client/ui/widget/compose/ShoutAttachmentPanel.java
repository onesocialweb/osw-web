/*
 *  Copyright 2010 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */
package org.onesocialweb.gwt.client.ui.widget.compose;

import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.event.ComponentEvent;
import org.onesocialweb.gwt.client.ui.event.ComponentListener;
import org.onesocialweb.model.activity.ActivityObject;

import com.google.gwt.core.client.GWT;

public class ShoutAttachmentPanel extends AbstractAttachmentPanel {

	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private RecipientList list = new RecipientList();
	
	public ShoutAttachmentPanel() {
		
		setHeader(uiText.ShoutTo());
		setVisible(false);
		setIcon(OswClient.getInstance().getPreference("theme_folder")
				+ "assets/i-shout-r.png");
		setCloseTooltip(uiText.ClearPeople());

		// Compose the UI
		addList();
		
	}

	@Override
	public void reset() {
		remove(list);
		addList();
		
	}

	public List<String> getRecipients() {
		return list.getRecipients();
	}

	@Override
	protected void render(ActivityObject object) {
		// TODO Auto-generated method stub

	}
	
	private void addList() {
		list = new RecipientList();
		add(list);
		
		list.setFocus();
		
		list.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				fireComponentResized();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
	}

}
