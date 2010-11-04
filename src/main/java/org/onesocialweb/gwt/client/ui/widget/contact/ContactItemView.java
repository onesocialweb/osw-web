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

import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.handler.ContactButtonHandler;
import org.onesocialweb.gwt.client.i18n.UserInterfaceMessages;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.application.AbstractApplication;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.client.ui.widget.StyledTooltipImage;
import org.onesocialweb.gwt.client.ui.window.ProfileWindow;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.service.RosterItem.Presence;
import org.onesocialweb.gwt.util.Observer;
import org.onesocialweb.model.vcard4.Profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ContactItemView extends FlowPanel implements MouseOverHandler,
		HasMouseOverHandlers, MouseOutHandler, HasMouseOutHandlers {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	private UserInterfaceMessages uiMessages = (UserInterfaceMessages) GWT.create(UserInterfaceMessages.class);
	
	private HTML statusLabel = new HTML();
	private Label infoLabel = new Label();
	private final Label followingLabel = new Label(uiText.Following());

	private StyledTooltipImage avatarImage = new StyledTooltipImage("", "link",
			uiText.ViewProfile());
	private StyledFlowPanel avatarwrapper = new StyledFlowPanel("avatarwrapper");
	private HorizontalPanel hpanel = new HorizontalPanel();
	private StyledTooltipImage statusIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-notavailable.png", "available",
			"Visual indicator of user online presence");
	private StyledTooltipImage infoIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-info.png", "icon", "");
	private StyledTooltipImage followingIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-ok.png", "icon", "");

	final RosterItem rosterItem;
	final StyledLabel author = new StyledLabel("link", "");
	private boolean isFollowing = false;

	private ContactButtonHandler handler;

	public ContactItemView(RosterItem item) {
		rosterItem = item;

		// Place the check above the text box using a vertical panel.
		StyledFlowPanel flow = new StyledFlowPanel("contents");
		StyledFlowPanel statuswrapper = new StyledFlowPanel("wrapper");
		StyledFlowPanel statuswrapper2 = new StyledFlowPanel("wrapper2");
		final StyledFlowPanel infowrapper = new StyledFlowPanel("wrapper");
		final StyledFlowPanel followingwrapper = new StyledFlowPanel("wrapper");

		statuswrapper.add(statusIcon);
		statuswrapper2.add(author);
		statuswrapper2.add(statusLabel);
		statuswrapper.add(statuswrapper2);
		flow.add(statuswrapper);
		flow.add(followingwrapper);
		flow.add(infowrapper);

		hpanel.add(avatarwrapper);
		avatarwrapper.getElement().setAttribute(
				"style",
				"background-image: url('"
						+ OswClient.getInstance().getPreference("theme_folder")
						+ "assets/avatar-loader.gif');");
		hpanel.add(flow);
		hpanel.setCellWidth(avatarwrapper, "40px");

		add(hpanel);

		// add styles
		avatarImage.setStyleName("avatar");
		addStyleName("contact");

		// show jid on tooltip
		author.setTitle("View profile of " + rosterItem.getJid());

		// handlers
		author.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// get the app instance from the session manager
				AbstractApplication app = OswClient.getInstance()
						.getCurrentApplication();
				ProfileWindow profileWindow = (ProfileWindow) app.addWindow(
						ProfileWindow.class.toString(), 1);
				profileWindow.setJID(rosterItem.getJid());
				profileWindow.show();
			}
		});

		this.addMouseOverHandler(this);
		this.addMouseOutHandler(this);

		// Fetch the avatar image
		OswService service = OswServiceFactory.getService();
		service.getProfile(rosterItem.getJid(), new RequestCallback<Profile>() {

			@Override
			public void onFailure() {
				// Do nothing
				avatarImage.setUrl(OswClient.getInstance().getPreference(
						"theme_folder")
						+ "assets/default-avatar.png");
				avatarwrapper.getElement().setAttribute("style",
						"background-image: none;");
				avatarwrapper.add(avatarImage);
				author.setText(rosterItem.getJid());
			}

			@Override
			public void onSuccess(Profile result) {
				String url = result.getPhotoUri();
				if (url != null && url.length() > 0) {
					avatarImage.setUrl(url);
					avatarwrapper.getElement().setAttribute("style",
							"background-image: none;");
					avatarwrapper.add(avatarImage);
				} else {
					avatarImage.setUrl(OswClient.getInstance().getPreference(
							"theme_folder")
							+ "assets/default-avatar-big.png");
					avatarwrapper.getElement().setAttribute("style",
							"background-image: none;");
					avatarwrapper.add(avatarImage);
				}

				String displayName = result.getFullName();
				if (displayName != null) {
					author.setText(displayName);
				} else {
					author.setText(rosterItem.getJid());
				}

			}

		});

		// Display following status
		service.getSubscriptions(service.getUserBareJID(),
				new RequestCallback<List<String>>() {

					@Override
					public void onFailure() {
						// Do nothing on fail
					}

					@Override
					public void onSuccess(List<String> result) {
						// Show following green arow and label
						if (result.contains(rosterItem.getJid())) {
							isFollowing = true;
							followingwrapper.add(followingIcon);
							followingwrapper.add(followingLabel);
							followingLabel.setStyleName("info");
						} else {
							isFollowing = false;
						}
					}

				});

		// Display the presence
		if (rosterItem.getJid().equals(service.getUserBareJID())) {
			setPresence(service.getPresence());
		} else {
			setPresence(rosterItem.getPresence());
			rosterItem.registerEventHandler(new Observer<RosterEvent>() {

				@Override
				public void handleEvent(RosterEvent event) {
					setPresence(rosterItem.getPresence());
				}

			});
		}

		// Display the tags, if available
		String tags = "";
		String info = "";
		for (int i = 0; i < rosterItem.getGroups().size(); i++) {
			String tag = rosterItem.getGroups().get(i);

			if (i < (rosterItem.getGroups().size() - 1)) {
				tags += tag + ", ";
			} else {
				tags += tag;
			}
		}

		info += uiMessages.ListedAsTags(tags);

		if (tags != null && tags.length() > 0) {
			infowrapper.add(infoIcon);
			infowrapper.add(infoLabel);
		}

		infoLabel.setText(info);
		infoLabel.setStyleName("info");

	}

	private void setPresence(Presence presence) {
		if (presence.equals(Presence.chat)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-chatty.png");
			statusIcon.setTitle(uiText.Chatty());
		} else if (presence.equals(Presence.away)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-away.png");
			statusIcon.setTitle(uiText.Away());
		} else if (presence.equals(Presence.dnd)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-dnd.png");
			statusIcon.setTitle(uiText.DoNotDisturb());
		} else if (presence.equals(Presence.xa)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-xaway.png");
			statusIcon.setTitle(uiText.ExtendedAway());
		} else if (presence.equals(Presence.available)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-available.png");
			statusIcon.setTitle(uiText.Online());
		} else {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-notavailable.png");
			statusIcon.setTitle(uiText.NotAvailable());
		}
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		select();
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		// the buttons are implemented in a separate object to avoid having too
		// many handlers
		// to keep the item selected when hovering over the buttons (triggers a
		// mouseout)
		if (event.getRelativeX(this.getElement()) < 0
				|| event.getRelativeY(this.getElement()) < 0) {
			deselect();

		} else if (event.getRelativeX(this.getElement()) > this.getElement()
				.getOffsetWidth()
				|| event.getRelativeY(this.getElement()) > this.getElement()
						.getOffsetHeight()) {
			deselect();
		}
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public void setButtonHandler(ContactButtonHandler handler) {
		this.handler = handler;
	}

	private void select() {
		addStyleName("selected");
		handler.handleShow(this.getAbsoluteTop(), this);
	}

	private void deselect() {
		removeStyleName("selected");
		handler.handleHide();
	}

}
