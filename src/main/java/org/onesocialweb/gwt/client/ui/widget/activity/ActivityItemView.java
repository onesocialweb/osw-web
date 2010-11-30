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
import org.onesocialweb.gwt.client.ui.widget.TooltipPushButton;
import org.onesocialweb.gwt.client.ui.widget.compose.CommentPanel;
import org.onesocialweb.gwt.client.ui.widget.compose.TextareaEdit;
import org.onesocialweb.gwt.client.ui.window.ProfileWindow;
import org.onesocialweb.gwt.client.util.FormatHelper;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ActivityItemView extends FlowPanel implements MouseOverHandler,
		HasMouseOverHandlers, MouseOutHandler, HasMouseOutHandlers {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	private HTML statusLabel = new HTML();
	private HTML infoLabel = new HTML();
	private /*final*/ CommentPanel commentPanel = new CommentPanel();
	private boolean editing=false;
	

	private StyledTooltipImage avatarImage = new StyledTooltipImage("", "link",
			uiText.ViewProfile());
	private StyledFlowPanel avatarwrapper = new StyledFlowPanel("avatarwrapper");
	private StyledTooltipImage infoIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-info.png", "icon", "");
	
	TooltipPushButton buttonEdit = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-edit.png"), "Edit Post");
	

	
	private StyledTooltipImage emptyIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-empty.png", "icon", "");
	private HorizontalPanel hpanel = new HorizontalPanel();
	private FlowPanel attachmentswrapper = new StyledFlowPanel("attachments");
	private StyledTooltipImage statusIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-notavailable.png", "available",
			"Visual indicator of user online presence");
	private StyledTooltipImage commentIcon = new StyledTooltipImage(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-comment.png", "icon", "");

	private ActivityButtonHandler handler;
	private final ActivityEntry activity;
	
	private StyledFlowPanel statusActivity = new StyledFlowPanel("statusActivity");
	
//	private StyledLabel author = new StyledLabel("link", "");
	
	protected final VerticalPanel vpanel = new VerticalPanel();
	protected final HorizontalPanel replieswrapper = new HorizontalPanel();	
	protected final StyledFlowPanel commentswrapper =  new StyledFlowPanel("author-wrapper");
	protected final StyledFlowPanel unreadwrapper =  new StyledFlowPanel("author-wrapper");
	/*final*/ StyledFlowPanel statuswrapper2 = new StyledFlowPanel("wrapper2");
	StyledFlowPanel statuswrapper = new StyledFlowPanel("wrapper");		
	StyledFlowPanel authorWrapper = new StyledFlowPanel("author-wrapper");
	private String recipientActivityID = null;
	private boolean commentNotification = false;
	private StyledLabel repliesLabel=null;
	
	TextareaEdit edit;
	
	private boolean expanded;

	public CommentPanel getCommentPanel() {
		return commentPanel;
	}
	
	
	public StyledLabel getRepliesLabel(){
		return repliesLabel;
	}
	
	public StyledFlowPanel getStatusWrapper(){
		return statuswrapper2;
	}
	
	public void setStatusWrapper(StyledFlowPanel statusw){
		statuswrapper2=statusw;
	}

	public ActivityItemView(final ActivityEntry activity, boolean expand, int unread) {
						
	
		
		this.expanded=expand;
		this.activity = activity;			
		
		// add the mouseOver handlers
		this.addMouseOverHandler(this);
		this.addMouseOutHandler(this);
		
		// Place the check above the text box using a vertical panel.
		StyledFlowPanel flow = new StyledFlowPanel("contents");
	
	//	final StyledFlowPanel statuswrapper1 = new StyledFlowPanel("wrapper2");
		
		
		StyledFlowPanel infowrapper = new StyledFlowPanel("wrapper");
		StyledFlowPanel editwrapper = new StyledFlowPanel("wrapper");
		
		
		StyledLabel author = new StyledLabel("link", activity.getActor().getName());
		authorWrapper.add(author);
		final OswService service = OswServiceFactory.getService();
		
		edit =new TextareaEdit(this);
		
		boolean isComment=activity.getParentId()!=null && activity.getParentJID()!=null;
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
        
        if (!isComment){       
        buttonEdit.addClickHandler(new ClickHandler(){
        	public void onClick(ClickEvent event){  
        		if (!editing){
        			statuswrapper.remove(statuswrapper2);
        			statuswrapper2= new StyledFlowPanel("wrapper2");
        			statuswrapper2.add(authorWrapper);
        			statuswrapper.add(statuswrapper2);
        			edit.setText(activity.getTitle());
        			statuswrapper.add(edit);
        			editing=true;
        		}
        		else {
        			statuswrapper.remove(statuswrapper2);
        			statuswrapper2= new StyledFlowPanel("wrapper2");
        			statuswrapper2.add(authorWrapper);        			
        			formatContent(statuswrapper2, activity.getTitle());   
        			statuswrapper.add(statuswrapper2);
        			statuswrapper.remove(edit);
        			editing=false;
        		}
        		
        	}
        	});
        }
        statuswrapper.add(statusIcon);
		statuswrapper2.add(authorWrapper);
		statuswrapper.add(statuswrapper2);
	
		
		editwrapper.add(buttonEdit);
		infowrapper.add(infoIcon);		
		infowrapper.add(infoLabel);

		flow.add(statuswrapper);		
		flow.add(attachmentswrapper);
		flow.add(infowrapper);			
		vpanel.add(replieswrapper);				
		flow.add(vpanel);		


		hpanel.add(avatarwrapper);
		avatarwrapper.getElement().setAttribute(
				"style",
				"background-image: url('"
						+ OswClient.getInstance().getPreference("theme_folder")
						+ "assets/avatar-loader.gif');");
		hpanel.add(flow);
		if ((activity.getActor().getUri().equals(OswServiceFactory.getService().getUserBareJID())) && !(isComment)){
			hpanel.add(editwrapper);
			hpanel.setCellWidth(editwrapper, "25px");
		}
		
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

				
		formatContent(statuswrapper2,  activity.getTitle());
		statuswrapper2.setVisible(true);
		statuswrapper.add(statuswrapper2);
		
				
		
		commentswrapper.add(emptyIcon);

		if(!commentNotification) {
			if(activity.hasReplies()) {
				repliesLabel = new StyledLabel("replies-link", uiText.Comments() + ": " +
						activity.getRepliesLink().getCount());
				final StyledLabel notificationsLabel = new StyledLabel("replies-link", uiText.UnreadComments() + ": "+ unread); 
				
				if (expand){
					commentPanel.compose(activity);
					vpanel.add(commentPanel);
					commentswrapper.setVisible(false);
				}

				repliesLabel.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						commentPanel.compose(activity);
						vpanel.add(commentPanel);					
						commentswrapper.setVisible(false);
						unreadwrapper.setVisible(false);
						AbstractActivityPanel<ActivityEntry> parent =(AbstractActivityPanel<ActivityEntry>) getParent();
						parent.addExpanded(activity.getId());
						parent.resetNotifications(activity.getId());	
						notificationsLabel.setVisible(false);
					}
				});
				commentswrapper.add(repliesLabel);	
				replieswrapper.add(commentswrapper);
				replieswrapper.setCellWidth(commentswrapper, "20px");					
				
				if (unread>0 && !expanded){
					//add the comment image and a label with the number of new comments						
					unreadwrapper.add(notificationsLabel);					
					unreadwrapper.add(commentIcon);
					replieswrapper.add(unreadwrapper);
					replieswrapper.setCellWidth(unreadwrapper, "180px");					
					notificationsLabel.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							commentPanel.compose(activity);
							vpanel.add(commentPanel);	
							unreadwrapper.setVisible(false);
							commentswrapper.setVisible(false);
							AbstractActivityPanel<ActivityEntry> parent =(AbstractActivityPanel<ActivityEntry>) getParent();
							parent.addExpanded(activity.getId());							
							parent.resetNotifications(activity.getId());							
						}
					});
				}
								
				
			} else {
				repliesLabel = new StyledLabel("replies-link", "Add a comment");
				commentswrapper.add(repliesLabel);	
				replieswrapper.add(commentswrapper);
				
				repliesLabel.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						commentPanel.compose(activity);						
						commentswrapper.add(commentPanel);						
						repliesLabel.setVisible(false);							
						AbstractActivityPanel parent =(AbstractActivityPanel) getParent();						
						parent.addExpanded(activity.getId());							
						parent.resetNotifications(activity.getId());
					}
				});

				
					
		
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

	}
	
	public void removeSelect() {
		statusActivity.removeStyleName("selected");
	}
	
	public void addSelect() {
		statusActivity.addStyleName("selected");
	}

	private void select() {
		statusActivity.addStyleName("selected");
		handler.handleShow(this.getAbsoluteTop(), this);
	}

	private void deselect() {
		statusActivity.removeStyleName("selected");
		handler.handleHide();
	}
	

	private void showAttachments() {
		// addPhotoAttachment("http://www.iwatchstuff.com/2008/05/30/emily-the-strange-movie.jpg");
	}
	
	public boolean isExpanded() {
		return expanded;
	}


	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
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
	
	public void formatContent(StyledFlowPanel statuswrapper, String activityContent){
		
		
		final OswService service = OswServiceFactory.getService();
		
		statuswrapper.add(new HTML(" - "));
		//add the activity context, with formatted links (clickable) and mentions	
		if(activityContent.indexOf("http://")>=0 || activityContent.indexOf("https://")>=0 || activityContent.indexOf("@")>=0) {
		String[] tokens = activityContent.split("\\s+");
			for(int i=0; i<tokens.length; i++) {
				String token = tokens[i];
				if(token.startsWith("http://") || token.startsWith("https://")) {
					statuswrapper.add(formatLink(token, i));
				} else if(token.startsWith("@")) {
					statuswrapper.add(formatMention(service, token.substring(1), i));
				} else {
					statuswrapper.add(formatText(token, i));
				}
					
			}
		} else {
			statuswrapper.add(formatText(activityContent, 0));
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
	
	
	private HTML formatLink(String link, int index) {
		
		String formattedLink = "<a href='" + link + "' target='_blank'>" + link + "</a>";
		
		HTML htmlFragment = new HTML();
		if(index == 0) {
			htmlFragment.setHTML(formattedLink);
		} else {
			htmlFragment.setHTML(" " + formattedLink);
		}
		
		return htmlFragment;
	
	}
	
	private StyledLabel formatMention(OswService service, String mentionJID, int index) {
		
		final StyledLabel label = new StyledLabel("link", index==0?"@"+mentionJID:" @"+mentionJID);
		final String JID = mentionJID;
		final int fIndex = index;
		label.setTitle(uiText.ViewProfileOf() + mentionJID);

		service.getProfile(mentionJID,
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
							label.setText(fIndex==0?"@"+fullName:" @"+fullName);
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
				profileWindow.setJID(JID);
				profileWindow.show();
			}
		});
		
		return label;
	
	}
	
	private HTML formatText(String text, int index) {
		
		HTML htmlFragment = new HTML();
		if(index == 0) {
			htmlFragment.setText(text);
		} else {
			htmlFragment.setText(" " + text);
		}
		
		return htmlFragment;
	}

}
