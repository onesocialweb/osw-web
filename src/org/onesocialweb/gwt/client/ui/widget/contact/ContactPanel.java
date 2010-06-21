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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.onesocialweb.gwt.client.handler.ContactButtonHandler;
import org.onesocialweb.gwt.client.i18n.UserInterfaceMessages;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.Roster;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.util.Observer;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class ContactPanel extends AbstractContactPanel<ContactItemView> {
	
	// internationalization
	private UserInterfaceMessages uiMessages = (UserInterfaceMessages) GWT.create(UserInterfaceMessages.class);
	
	private final ContactButtons buttons = new ContactButtons();
	private final StyledFlowPanel toolbar = new StyledFlowPanel("toolbar");
	private final FlowPanel contacts = new FlowPanel();
	private final Label connectionsCount = new Label();
	private final Roster roster;

	private ContactItemView lastSelected;

	private RosterEventHandler rosterhandler;

	public ContactPanel() {
		// Compose the UI
		addStyleName("contactPanel");
		toolbar.add(connectionsCount);
		add(toolbar);
		add(contacts);
		add(buttons);
		buttons.setVisible(false);

		// Fetch the model
		OswService service = OswServiceFactory.getService();
		roster = service.getRoster();
		rosterhandler = new RosterEventHandler();
		roster.registerEventHandler(rosterhandler);

		// Initial repaint
		repaint();
	}

	public void onDestroy() {
		// make sure to kill the roster event handler
		if (rosterhandler != null) {
			roster.unregisterEventHandler(rosterhandler);
		}
	}

	private void repaint() {
		// Clear the current contacts
		contacts.clear();

		// Get the roster items
		List<RosterItem> items = roster.getItems();

		// Show nr of connections
		connectionsCount.setText(uiMessages.YouHaveNConnections(items.size()));

		// sort alphabetically
		Collections.sort(items, new Comparator<RosterItem>() {

			@Override
			public int compare(RosterItem o1, RosterItem o2) {
				return o1.getJid().compareToIgnoreCase(o2.getJid());
			}

		});

		// add all the contacts
		for (final RosterItem rosterItem : items) {
			ContactItemView contact = new ContactItemView(rosterItem);
			contacts.add(contact);
			contact.setButtonHandler(new ContactButtonHandler() {
				public void handleShow(int top, ContactItemView contact) {

					// make sure to remove the selected state. Mouseout is not
					// always captured
					if (lastSelected != null) {
						lastSelected.removeStyleName("selected");
						showButtons(top);
						buttons.setJID(rosterItem.getJid());
					}
					// force selecting the contact
					if (!contact.getStyleName().equals("selected")) {
						contact.addStyleName("selected");
						lastSelected = contact;
					}
				}

				public void handleHide() {
					hideButtons();
				}
			});
		}
	}

	private void showButtons(int top) {
		int newtop = top - this.getAbsoluteTop();
		buttons.getElement().setAttribute("style",
				"top: " + Integer.toString(newtop) + ";");
		buttons.setVisible(true);
	}

	private void hideButtons() {
		buttons.setVisible(false);
	}

	private class RosterEventHandler implements Observer<RosterEvent> {

		@Override
		public void handleEvent(RosterEvent event) {
			repaint();
		}

	}

}
