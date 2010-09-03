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
 *  2010-08-09 Modified by Luca Faggioli Copyright 2010 Openliven S.r.l
 *  added code to handle comments (replies)
 *
 */
package org.onesocialweb.gwt.client.ui.widget.activity;

import static org.onesocialweb.gwt.client.util.FormatHelper.getFormattedDate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.handler.ActivityButtonHandler;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.application.AbstractApplication;
import org.onesocialweb.gwt.client.ui.dialog.PicturePreviewDialog;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.client.ui.widget.StyledTooltipImage;
import org.onesocialweb.gwt.client.ui.widget.compose.CommentPanel;
import org.onesocialweb.gwt.client.ui.window.ProfileWindow;
import org.onesocialweb.gwt.client.util.FormatHelper;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.service.RosterItem.Presence;
import org.onesocialweb.gwt.util.Observer;
import org.onesocialweb.model.acl.AclAction;
import org.onesocialweb.model.acl.AclRule;
import org.onesocialweb.model.acl.AclSubject;
import org.onesocialweb.model.activity.ActivityEntry;
import org.onesocialweb.model.activity.ActivityObject;
import org.onesocialweb.model.atom.AtomReplyTo;
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ActivityItemView extends FlowPanel implements MouseOverHandler,
		HasMouseOverHandlers, MouseOutHandler, HasMouseOutHandlers {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private HTML statusLabel = new HTML();
	private HTML infoLabel = new HTML();
	private final RepliesPanel repliesPanel = new RepliesPanel();
	private final CommentPanel commentPanel = new CommentPanel();

	private StyledTooltipImage avatarImage = new StyledTooltipImage("", "link",
			uiText.ViewProfile());
	private StyledFlowPanel avatarwrapper = new StyledFlowPanel("avatarwrapper");
	private StyledTooltipImage infoIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-info.png", "icon", "");
	private StyledTooltipImage emptyIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-empty.png", "icon", "");
	private HorizontalPanel hpanel = new HorizontalPanel();
	private FlowPanel attachmentswrapper = new StyledFlowPanel("attachments");
	private StyledTooltipImage statusIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-notavailable.png", "available",
			"Visual indicator of user online presence");

	private ActivityButtonHandler handler;
	private final ActivityEntry activity;
	private boolean isUpdating = false;
	
	private StyledFlowPanel statusActivity = new StyledFlowPanel("statusActivity");
	
	private StyledLabel author = new StyledLabel("link", "");

	public ActivityItemView(final ActivityEntry activity) {
				
		
		this.activity = activity;
		
		// add the mouseOver handlers
		this.addMouseOverHandler(this);
		this.addMouseOutHandler(this);
		
		// Place the check above the text box using a vertical panel.
		StyledFlowPanel flow = new StyledFlowPanel("contents");
		StyledFlowPanel statuswrapper = new StyledFlowPanel("wrapper");
		StyledFlowPanel statuswrapper2 = new StyledFlowPanel("wrapper2");
		StyledFlowPanel infowrapper = new StyledFlowPanel("wrapper");
		StyledFlowPanel authorWrapper = new StyledFlowPanel("author-wrapper");
		StyledLabel author = new StyledLabel("link", activity.getActor()
				.getName());
		authorWrapper.add(author);
		final OswService service = OswServiceFactory.getService();
		
		boolean isComment=false;
        List<AtomReplyTo> recs=activity.getRecipients();
        Iterator<AtomReplyTo> itRecipients=recs.iterator();

        while (itRecipients.hasNext()){
                AtomReplyTo recipient=itRecipients.next();
                if (recipient.getHref().contains("?;node"))
                        isComment=true;
        }


        if ((!isComment) && (activity.hasRecipients())) {
			authorWrapper.add(new StyledLabel("separator", " " + uiText.To() + " "));
			Iterator<AtomReplyTo> recipients = activity.getRecipients()
					.iterator();
			while (recipients.hasNext()) {
				final AtomReplyTo recipient = recipients.next();

				recipientActivityID = extractRecipientActivityID(recipient.getHref());
				if(recipientActivityID != null) {
					commentNotification = true;
				}

				final String recipientJID = extractRecipientJID(recipient.getHref());
				final StyledLabel label = new StyledLabel("link", recipientJID);
				label.setTitle(uiText.ViewProfileOf() + recipientJID);

				service.getProfile(recipientJID,
						new RequestCallback<Profile>() {

							@Override
							public void onFailure() {
								// do nothing
							}

							@Override
							public void onSuccess(Profile result) {
								// show display name
								final String fullName = result.getFullName();
								if (fullName != null && fullName.length() > 0) {
									label.setText(fullName);
								}

							}

						});

				label.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// get the app instance from the session manager
						AbstractApplication app = OswClient.getInstance()
								.getCurrentApplication();
						ProfileWindow profileWindow = (ProfileWindow) app
								.addWindow(ProfileWindow.class.toString(), 1);
						profileWindow.setJID(recipientJID);
						profileWindow.show();
					}
				});
				authorWrapper.add(label);

				if (recipients.hasNext()) {
					authorWrapper.add(new StyledLabel("separator", ", "));
				}
			}
		}

		statuswrapper.add(statusIcon);
		statuswrapper2.add(authorWrapper);
		statuswrapper2.add(statusLabel);
		statuswrapper.add(statuswrapper2);
		infowrapper.add(infoIcon);
		infowrapper.add(infoLabel);
		flow.add(statuswrapper);
		flow.add(attachmentswrapper);
		flow.add(infowrapper);
		flow.add(replieswrapper);
		replieswrapper.add(emptyIcon);


		hpanel.add(avatarwrapper);
		avatarwrapper.getElement().setAttribute(
				"style",
				"background-image: url('"
						+ OswClient.getInstance().getPreference("theme_folder")
						+ "assets/avatar-loader.gif');");
		hpanel.add(flow);
		hpanel.setCellWidth(avatarwrapper, "40px");

		// display who can see your own items
		List<String> visibility = new ArrayList<String>();

		if (activity.getActor().getUri().equals(
				OswServiceFactory.getService().getUserBareJID())) {
			
			// show the acl rule
			for (AclRule rule : activity.getAclRules()) {
				for (AclAction action : rule.getActions(AclAction.ACTION_VIEW,
						AclAction.PERMISSION_GRANT)) {
					for (AclSubject subject : rule.getSubjects()) {
						if (subject.getType().equals(AclSubject.EVERYONE)) {
							visibility.add(uiText.Everyone());
						} else if (subject.getType().equals(AclSubject.GROUP)) {
							visibility.add(subject.getName());
						} else if (subject.getType().equals(AclSubject.PERSON)) {
							visibility.add(subject.getName());
						}
					}
				}
			}
			
			// and add a special style to show this is your own item
			addStyleName("isOwner");
		}

		statusLabel.setText(" - " + activity.getTitle());

		if(!commentNotification) {
			if(activity.hasReplies()) {
				final StyledLabel repliesLabel = new StyledLabel("replies-link", "Comments: " +
						activity.getRepliesLink().getCount());

				repliesLabel.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						commentPanel.compose(activity);
						replieswrapper.add(commentPanel);
						repliesLabel.setVisible(false);
					}
				});

				replieswrapper.add(repliesLabel);
			} else {
				final StyledLabel repliesLabel = new StyledLabel("replies-link", "Add a comment");

				repliesLabel.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						commentPanel.compose(activity);
						replieswrapper.add(commentPanel);
						repliesLabel.setVisible(false);
					}
				});

				replieswrapper.add(repliesLabel);
			}
		}

		String info = "";
		info += getFormattedDate(activity.getPublished());
		if (!visibility.isEmpty())
			info += " - " + uiText.VisibleTo() + " " + FormatHelper.implode(visibility, ", ");
		// if (location != "") info += " - From: " + location;
		// if (tags != "") info += " - Tagged: " + tags;
		if(commentNotification)
			info += " - This is a comment to a previous post";

		infoLabel.setText(info);
		author.setTitle(uiText.ViewProfileOf() + " " + activity.getActor().getUri());

		// check for any attachments
		for (int i = 0; i < activity.getObjects().size(); i++) {
			if (activity.getObjects().get(i).getType().equals(
					ActivityObject.PICTURE)) {
				addPictureAttachment(activity.getObjects().get(i));
			} else if (activity.getObjects().get(i).getType().equals(
					ActivityObject.LINK)) {
				addLinkAttachment(activity.getObjects().get(i));
			}
		}
		
		statusActivity.add(hpanel);
		add(statusActivity);

		// add styles
		avatarImage.setStyleName("avatar");
		infoLabel.setStyleName("info");
		addStyleName("statusActivityWrapper");

		// handlers
		author.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// get the app instance from the session manager
				AbstractApplication app = OswClient.getInstance()
						.getCurrentApplication();
				ProfileWindow profileWindow = (ProfileWindow) app.addWindow(
						ProfileWindow.class.toString(), 1);
				profileWindow.setJID(activity.getActor().getUri());
				profileWindow.show();
			}
		});

		this.addMouseOverHandler(this);
		this.addMouseOutHandler(this);

		// Fetch the avatar image
		service.getProfile(activity.getActor().getUri(),
				new RequestCallback<Profile>() {

					@Override
					public void onFailure() {
						// Do nothing
						avatarImage.setUrl(OswClient.getInstance()
								.getPreference("theme_folder")
								+ "assets/default-avatar.png");
						avatarwrapper.getElement().setAttribute("style",
								"background-image: none;");
						avatarwrapper.add(avatarImage);
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
							avatarImage.setUrl(OswClient.getInstance()
									.getPreference("theme_folder")
									+ "assets/default-avatar-big.png");
							avatarwrapper.getElement().setAttribute("style",
									"background-image: none;");
							avatarwrapper.add(avatarImage);
						}
					}

				});

		// Display the presence
		if (activity.getActor().getUri().equals(service.getUserBareJID())) {
			setPresence(service.getPresence());
		} else {
			final RosterItem rosterItem = service.getRoster().getItem(
					activity.getActor().getUri());
			if (rosterItem != null) {
				setPresence(rosterItem.getPresence());
				rosterItem.registerEventHandler(new Observer<RosterEvent>() {

					@Override
					public void handleEvent(RosterEvent event) {
						setPresence(rosterItem.getPresence());
					}

				});
			}
		}
	}


	public ActivityEntry getActivity() {
		return activity;
	}



	public void onMouseOver(MouseOverEvent event) {
		select();
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

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

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public void setButtonHandler(ActivityButtonHandler handler) {
		this.handler = handler;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		if(commentNotification) {
			Widget parent = getParent();
			if(parent instanceof InboxPanel) {
				InboxPanel inbox = (InboxPanel) parent;
				inbox.updateActivityReplies(recipientActivityID);
			}
		}
	}

	private void select() {
		statusActivity.addStyleName("selected");
		handler.handleShow(this.getAbsoluteTop(), this);
	}

	private void deselect() {
		statusActivity.removeStyleName("selected");
		handler.handleHide();
	}
	
	private void showComments() {
		// addComment();
	}

	private void addComment(String status, String authorName, String avatarUri,
			String timestamp, String tags, String location) {

	}

	private void showAttachments() {
		// addPhotoAttachment("http://www.iwatchstuff.com/2008/05/30/emily-the-strange-movie.jpg");
	}

	private void addPictureAttachment(ActivityObject object) {

		StyledFlowPanel attachment = new StyledFlowPanel("wrapper");
		StyledTooltipImage attachmentIcon = new StyledTooltipImage(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-attachment.png", "icon", "");
		StyledFlowPanel attachmentFlow = new StyledFlowPanel("image");
		StyledFlowPanel wrapper = new StyledFlowPanel("metadata");
		StyledLabel title = new StyledLabel("title", "");
		StyledLabel description = new StyledLabel("description", "");
		attachment.add(attachmentIcon);
		attachment.add(attachmentFlow);
		attachment.add(wrapper);
		wrapper.add(title);
		attachmentswrapper.add(attachment);

		// show the link elements
		for (int i = 0; i < object.getLinks().size(); i++) {
			// get the url to the picture
			if (object.hasLinks() && object.getLinks().get(0).hasRel()
					&& object.getLinks().get(i).getRel().equals("alternate")) {

				// get the link to the image
				final StyledTooltipImage image = new StyledTooltipImage(object
						.getLinks().get(i).getHref(), "attachment",
						uiText.ShowPreview());

				image.addStyleName("link");

				// if the activity has a title
				if (activity.hasTitle() && activity.getTitle().length() > 0) {

					// and the link has a title as well
					if (object.getLinks().get(i).hasTitle()
							&& object.getLinks().get(i).getTitle().length() > 0) {

						// check if they are the same and skip it on the link if
						// so
						// TODO this needs to be refactored to the
						// object.getTitle()
						if (!activity.getTitle().trim().equals(
								object.getLinks().get(i).getTitle().trim())) {
							title.setText(object.getLinks().get(i).getTitle());
						}

					}

					// otherwise we can show it
					// TODO this needs to be refactored to the object.getTitle()
				} else {
					title.setText(object.getLinks().get(i).getTitle());
				}

				attachmentFlow.add(image);

				image.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						PicturePreviewDialog.getInstance().showDialog(
								image.getUrl(), uiText.ImagePreview());
					}
				});
			}
		}

		// show description
		if (object.hasContents()
				&& object.getContents().get(0).getValue() != null
				&& object.getContents().get(0).getValue().length() > 0) {
			description.setText(object.getContents().get(0).getValue());
			wrapper.add(description);
		}

	}

	private void addVideoAttachment(String url) {

		StyledFlowPanel attachment = new StyledFlowPanel("wrapper");
		StyledTooltipImage attachmentIcon = new StyledTooltipImage(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-attachment.png", "icon", "");
		FlowPanel attachmentFlow = new FlowPanel();
		HTML movie = new HTML();
		movie
				.setHTML("<embed src='http://vimeo.com/moogaloop.swf?clip_id=7659259&amp;server=vimeo.com&amp;show_title=0&amp;show_byline=0&amp;show_portrait=0&amp;color=00ADEF&amp;fullscreen=1' type='application/x-shockwave-flash' allowfullscreen='true' allowscriptaccess='always' width='200' height='150'></embed>");

		attachment.add(attachmentIcon);
		attachment.add(attachmentFlow);
		attachmentFlow.add(movie);
		attachmentswrapper.add(attachment);

	}

	private void addLinkAttachment(ActivityObject object) {

		// check the links
		for (int i = 0; i < object.getLinks().size(); i++) {
			if (object.getLinks().get(i).getRel().equals("alternate")) {
				StyledFlowPanel attachment = new StyledFlowPanel("wrapper");
				StyledTooltipImage attachmentIcon = new StyledTooltipImage(
						OswClient.getInstance().getPreference("theme_folder")
								+ "assets/i-attachment.png", "icon", "");
				FlowPanel attachmentFlow = new FlowPanel();

				attachment.add(attachmentIcon);
				attachment.add(attachmentFlow);

				Anchor link = new Anchor();
				link.setHref(object.getLinks().get(i).getHref());
				link.setText(object.getLinks().get(i).getHref());
				link.setTarget("_blank");
				link.addStyleName("smalllink");

				attachmentFlow.add(link);
				attachmentswrapper.add(attachment);
			}
		}

	}

	private void setPresence(Presence presence) {
		if (presence.equals(Presence.chat)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-chatty.png");
			statusIcon.setTitle("In a chatty mood!");
		} else if (presence.equals(Presence.away)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-away.png");
			statusIcon.setTitle("Away");
		} else if (presence.equals(Presence.dnd)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-dnd.png");
			statusIcon.setTitle("Do not disturb");
		} else if (presence.equals(Presence.xa)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-xaway.png");
			statusIcon.setTitle("Away for a while");
		} else if (presence.equals(Presence.available)) {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-available.png");
			statusIcon.setTitle("Online");
		} else {
			statusIcon.setUrl(OswClient.getInstance().getPreference(
					"theme_folder")
					+ "assets/i-notavailable.png");
			statusIcon.setTitle("Not available");
		}
	}

	private String extractRecipientJID(String recipientHref) {
		if(recipientHref.startsWith("xmpp:")) {
			int i = recipientHref.indexOf("?");
			if(i == -1) {
				return "";
			}
			else {
				return recipientHref.substring(5, i);
			}
		}
		else {
			return recipientHref;
		}
	}

	private String extractRecipientActivityID(String recipientHref) {

		if(recipientHref.startsWith("xmpp:")) {
			int i = recipientHref.indexOf("item=");
			if(i == -1) {
				return null;
			}
			else {
				return recipientHref.substring(i+5);
			}
		}
		else {
			return null;
		}

	}

}
