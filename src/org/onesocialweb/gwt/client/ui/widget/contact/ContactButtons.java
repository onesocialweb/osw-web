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
import org.onesocialweb.gwt.client.ui.dialog.AlertDialog;
import org.onesocialweb.gwt.client.ui.widget.TooltipPushButton;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class ContactButtons extends FlowPanel {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private TooltipPushButton deleteButton = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-delete.png"), uiText.DeleteFromContacts());
	private TooltipPushButton shoutButton = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-shout.png"),
			uiText.ShoutToContact());
	private String jid = "";

	public ContactButtons() {
		addStyleName("contactButtons");
		add(shoutButton);
		add(deleteButton);

		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				OswService service = OswServiceFactory.getService();

				if (jid.length() > 0) {
					service.getRoster().removeItem(jid,
							new RequestCallback<RosterItem>() {

								@Override
								public void onFailure() {
								}

								@Override
								public void onSuccess(RosterItem result) {
								}
							});
				}
			}
		});

		shoutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AlertDialog.getInstance().showDialog(
						uiText.FeatureNotImplemented(),
						uiText.WorkingOnThis());
			}

		});
	}

	public void setJID(String jid) {
		this.jid = jid;
	}

}
