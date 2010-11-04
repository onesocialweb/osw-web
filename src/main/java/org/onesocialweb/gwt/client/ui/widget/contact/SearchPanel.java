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
package org.onesocialweb.gwt.client.ui.widget.contact;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.ui.application.AbstractApplication;
import org.onesocialweb.gwt.client.ui.dialog.AlertDialog;
import org.onesocialweb.gwt.client.ui.widget.StyledButton;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledHorizontalPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledTextBox;
import org.onesocialweb.gwt.client.ui.window.ProfileWindow;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.model.vcard4.Profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;

public class SearchPanel extends Composite {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	final StyledTextBox input = new StyledTextBox("bigtextbox", "", "");

	public SearchPanel() {

		StyledFlowPanel searchPanel = new StyledFlowPanel("addPanel");
		StyledHorizontalPanel hpanel = new StyledHorizontalPanel("middle");
		StyledButton show = new StyledButton("bigbutton", uiText.Search());

		hpanel.add(input);
		hpanel.add(show);

		searchPanel.add(hpanel);

		initWidget(searchPanel);

		searchPanel.setStyleName("topPanel");

		// handlers
		show.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				search();
			}
		});

		input.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent arg0) {

				char keyCode = arg0.getCharCode();

				if (keyCode == KeyCodes.KEY_ENTER) {
					search();
				}

			}

		});

	}

	public void onShow() {
		// empty the text area
		input.setText("");
		input.setFocus(true);
	}

	public void refreshLayout() {

	}

	public void search() {

		// basic validation if this represent a user identifier
		if (!input.getText().matches(
				"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			AlertDialog
					.getInstance()
					.showDialog(
							uiText.PleaseUseUserAtDomain(),
							uiText.OopsCannotSearch());
		} else {

			// Check if JID exists
			final DefaultTaskInfo task = new DefaultTaskInfo(
					uiText.FetchingProfile(), false);
			TaskMonitor.getInstance().addTask(task);
			OswServiceFactory.getService().getProfile(input.getText(),
					new RequestCallback<Profile>() {

						@Override
						public void onFailure() {
							// if it does not exist show error message
							AlertDialog
									.getInstance()
									.showDialog(
											uiText.AccountUnavailable(),
											uiText.FailedToGetProfile());
						}

						@Override
						public void onSuccess(Profile result) {
							// get the app instance from the session manager
							AbstractApplication app = OswClient.getInstance()
									.getCurrentApplication();
							ProfileWindow profileWindow = (ProfileWindow) app
									.addWindow(ProfileWindow.class.toString(),
											1);
							profileWindow.setJID(input.getText());
							profileWindow.show();
						}

					});

		}
	}

}