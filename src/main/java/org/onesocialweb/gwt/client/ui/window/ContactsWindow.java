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
package org.onesocialweb.gwt.client.ui.window;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.widget.contact.ContactPanel;
import org.onesocialweb.gwt.client.ui.widget.contact.SearchPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

public class ContactsWindow extends AbstractWindow {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private final ContactPanel contactPanel = new ContactPanel();
	private final ToggleButton toggleSearchPanel = new ToggleButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/search.png"));
	private final SearchPanel searchPanel = new SearchPanel();

	@Override
	protected void onInit() {
		setStyle("contactsWindow");
		setWindowTitle(uiText.Contacts());
		composeWindow();
	}

	@Override
	protected void onShow() {
		// correct the size of the contents based on visibility of statusPanel
		if (searchPanel.isVisible()) {
			getContents().getElement()
					.setAttribute(
							"style",
							"top:"
									+ (getTitlebar().getElement()
											.getClientHeight() + searchPanel
											.getElement().getClientHeight())
									+ ";");
		} else {
			getContents().getElement().setAttribute(
					"style",
					"top:" + (getTitlebar().getElement().getClientHeight())
							+ ";");
		}
	}

	@Override
	protected void onDestroy() {
		contactPanel.onDestroy();
	}

	@Override
	protected void onRepaint() {
		updateLayout();
	}

	private void composeWindow() {

		// add components
		getTopbar().add(searchPanel);
		searchPanel.setVisible(true);
		toggleSearchPanel.setDown(true);

		getActions().add(toggleSearchPanel);
		toggleSearchPanel.setTitle(uiText.ShowSearchPanel());
		toggleSearchPanel.addStyleDependentName("toggleShowUpdate");

		getContents().add(contactPanel);

		// add handlers
		toggleSearchPanel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggleSearchPanel();
			}
		});

	}

	private void toggleSearchPanel() {
		if (searchPanel.isVisible()) {
			searchPanel.setVisible(false);
			toggleSearchPanel.setTitle("Show search panel");
		} else {
			searchPanel.setVisible(true);
			toggleSearchPanel.setTitle("Hide search panel");
			searchPanel.onShow();
		}

		// force layout update on the Feed window itself
		updateLayout();
	}

	private void updateLayout() {
		// correct the size of the contents based on visibility of statusPanel
		if (searchPanel.isVisible()) {
			getContents().getElement()
					.setAttribute(
							"style",
							"top:"
									+ (getTitlebar().getElement()
											.getClientHeight() + searchPanel
											.getElement().getClientHeight())
									+ ";");
		} else {
			getContents().getElement().setAttribute(
					"style",
					"top:" + (getTitlebar().getElement().getClientHeight())
							+ ";");
		}
	}

}
