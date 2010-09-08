/*
 *  Copyright 2010 Openliven Srl.
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
 *  Author: luca faggioli 	luca(dot)faggioli(at)openliven(dot)com
 *    
 */
package org.onesocialweb.gwt.client.ui.widget.compose;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.handler.PictureHandler;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.ui.dialog.PictureChooserDialog;
import org.onesocialweb.gwt.client.ui.event.ComponentEvent;
import org.onesocialweb.gwt.client.ui.event.ComponentHelper;
import org.onesocialweb.gwt.client.ui.event.ComponentListener;
import org.onesocialweb.gwt.client.ui.widget.activity.RepliesPanel;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterItem.Presence;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.util.ListModel;
import org.onesocialweb.model.acl.AclAction;
import org.onesocialweb.model.acl.AclRule;
import org.onesocialweb.model.acl.AclSubject;
import org.onesocialweb.model.activity.ActivityEntry;
import org.onesocialweb.model.activity.ActivityObject;
import org.onesocialweb.model.activity.ActivityVerb;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

public class CommentPanel extends Composite {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);

	public static final String EVERYONE = "Everyone";

	private final ListModel<ActivityObject> pictureAttachments = new ListModel<ActivityObject>();

	private final ComponentHelper componentHelper = new ComponentHelper();

	private final InternalComponentListener componentListener = new InternalComponentListener();

	public CommentPanel() {
	}

	
	public void compose(ActivityEntry parentActivity) {
		this.parentActivity = parentActivity;
		composePanel();
	}
	
	public RepliesPanel getReplies() {
		return replies;
	}
	

	public void reset() {
		// Empty the text area
		textareaUpdate.setText("");

		// Tell the listener to ignore events, we´ll fire a single event when we
		// are done
		componentListener.setIgnoreEvent(true);

		// Remove all attachments
		pictureAttachmentPanel.reset();
		//privacyAttachmentPanel.reset();

		// And hide the panels
		pictureAttachmentPanel.hide();
		//privacyAttachmentPanel.hide();

		// Fire a single resize event and reactivate the listener
		componentHelper.fireComponentResized(this);
		componentListener.setIgnoreEvent(false);
	}

	public void addComponentListener(ComponentListener listener) {
		componentHelper.addComponentListener(listener);
	}

	public void removeComponentListener(ComponentListener listener) {
		componentHelper.removeComponentListener(listener);
	}
	
	public void hide() {
		if (isVisible()) {
			setVisible(false);
			fireComponentHidden();
		}
	}
	
	
	protected void fireComponentHidden() {
		componentHelper.fireComponentHidden(this);
	}


	// UI stuff
	private void composePanel() {

		// Init attachment dialogs
		pictureChooserDialog = new PictureChooserDialog(new PictureHandler() {
			public void handlePicture(String pictureUrl) {
				if (pictureUrl != null && pictureUrl.length() > 0) {
					OswService service = OswClient.getInstance().getService();
					ActivityObject object = service.getActivityFactory()
							.object(ActivityObject.PICTURE);
					object.addLink(service.getAtomFactory().link(pictureUrl,
							"alternate", null, null,0));
					pictureAttachments.add(object);
				}
			}
		});

		Stream<ActivityEntry> repliesModel = OswServiceFactory.getService().getReplies(
				parentActivity);
		replies.setModel(repliesModel);
		
		// Add components to page
		flow.add(addPhoto);
		//flow.add(addPrivacy);
		flow.add(buttonUpdate);

		// Create panel
		statusPanel.add(replies);
		statusPanel.add(textareaUpdate);
		statusPanel.add(attachmentsPanel);
		statusPanel.add(flow);

		// AttachmentsPanel
		pictureAttachmentPanel = new SinglePictureAttachmentPanel();
		pictureAttachmentPanel.setModel(pictureAttachments);
		pictureAttachmentPanel.addComponentListener(componentListener);
		attachmentsPanel.add(pictureAttachmentPanel);

		//privacyAttachmentPanel = new PrivacyAttachmentPanel();
		//privacyAttachmentPanel.addComponentListener(componentListener);
		//attachmentsPanel.add(privacyAttachmentPanel);

		// Add CSS classes
		buttonUpdate.addStyleName("buttonUpdate");
		attachment.addStyleName("updateLabel");
		flow.addStyleName("options");
		attachmentsPanel.addStyleName("attachmentWrapper");
		statusPanel.setStyleName("topPanel");

		// Set tooltips
		addPhoto.setTitle("Add picture");
		//addPrivacy.setTitle("Change privacy (default: Everyone)");
		
		initWidget(statusPanel);

		buttonUpdate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				postComment();
			}
		});


		/*addPrivacy.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				privacyAttachmentPanel.show();
			}
		});*/

		addPhoto.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				pictureChooserDialog.show();
			}
		});
	}
	
	private void postComment() {
		
		buttonUpdate.setEnabled(false);
		final OswService service = OswServiceFactory.getService();

		Date now = new Date();
		String status = textareaUpdate.getText();
		
		if(status==null || "".equals(status))
			return;

		StringBuffer limitedLengthStatus = new StringBuffer(status);
		if(limitedLengthStatus.length() > 256)
			limitedLengthStatus.setLength(256);
		
		ActivityObject object = service.getActivityFactory().object(
				ActivityObject.COMMENT);
		object.addContent(service.getAtomFactory().content(limitedLengthStatus.toString(),
				"text/plain", null));
		object.setPublished(now);

		// the basics
		ActivityEntry entry = service.getActivityFactory().entry();
		entry.setTitle(limitedLengthStatus.toString());
		entry.addVerb(service.getActivityFactory().verb(ActivityVerb.POST));
		entry.addObject(object);
		entry.setPublished(now);
		entry.setParentId(parentActivity.getId());
		entry.setParentJID(parentActivity.getActor().getUri());
		
		/*
		String href = "xmpp:"+parentActivity.getActor().getUri()+
			"?;node=urn:xmpp:microblog:0;item="+parentActivity.getId();
		entry.addLink(service.getAtomFactory().link(href, "alternate", null, 
				"text/html", 0));
		entry.addRecipient(service.getAtomFactory().reply(parentActivity.getId(), href, 
				null, null));
		*/
		
		// add attachments if there are any
		for (ActivityObject current : pictureAttachments) {
			entry.addObject(current);
		}
		
		
		/*
		// setup access control
		AclRule rule = service.getAclFactory().aclRule();
		rule.addAction(service.getAclFactory().aclAction(AclAction.ACTION_VIEW,
				AclAction.PERMISSION_GRANT));

		// check privacy settings
		String visibilityValue = privacyAttachmentPanel.getPrivacyValue();

		if (visibilityValue.equals(EVERYONE)) {
			rule.addSubject(service.getAclFactory().aclSubject(null,
					AclSubject.EVERYONE));
		} else {
			rule.addSubject(service.getAclFactory().aclSubject(visibilityValue,
					AclSubject.GROUP));
		}
		entry.addAclRule(rule);
		*/
		
		//TODO: we build the default ACL for now, but we need to fix this since 
		//comments cannot always be visible to Everyone
		AclRule rule = service.getAclFactory().aclRule();
		rule.addSubject(service.getAclFactory().aclSubject(null, AclSubject.EVERYONE));
		rule.addAction(service.getAclFactory().aclAction(AclAction.ACTION_VIEW, AclAction.PERMISSION_GRANT));
		List<AclRule> defaultRules = new ArrayList<AclRule>();
		defaultRules.add(rule);
		entry.setAclRules(defaultRules);

		// we got everything we need -> clean up UI
		reset();

		// Prepare a task to monitor status
		final DefaultTaskInfo task = new DefaultTaskInfo(
				uiText.PostingComment(), false);
		TaskMonitor.getInstance().addTask(task);
				
		
		service.post(entry, new RequestCallback<ActivityEntry>() {

			@Override
			public void onFailure() {
				task.complete(uiText.UpdateFailure(), Status.failure);
			}

			@Override
			public void onSuccess(ActivityEntry result) {
				task.complete(uiText.UpdateSuccess(), Status.succes);
			}

		});
		
		
		replies.setModel(service.getReplies(parentActivity));
		replies.repaint();
		buttonUpdate.setEnabled(true);

	}


	private SinglePictureAttachmentPanel pictureAttachmentPanel;
	//private PrivacyAttachmentPanel privacyAttachmentPanel;
	private PictureChooserDialog pictureChooserDialog;

	private final FlowPanel statusPanel = new FlowPanel();
	private final FlowPanel attachmentsPanel = new FlowPanel();
	private final FlowPanel flow = new FlowPanel();
	private final RepliesPanel replies = new RepliesPanel();
	
	private final Label attachment = new Label("Add:");
	private final Button buttonUpdate = new Button("Post your comment");
	private final TextareaUpdate textareaUpdate = new TextareaUpdate();

	/*
	private final PushButton addPrivacy = new PushButton(new Image(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-key.png"));
	*/
	private final PushButton addPhoto = new PushButton(new Image(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-camera2.png"));
	
	private ActivityEntry parentActivity;

	private class InternalComponentListener implements ComponentListener {

		private boolean ignoreEvent = false;

		public void setIgnoreEvent(boolean ignoreEvent) {
			this.ignoreEvent = ignoreEvent;
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			if (!ignoreEvent) {
				componentHelper.fireComponentHidden(e);
			}
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			if (!ignoreEvent) {
				componentHelper.fireComponentMoved(e);
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			if (!ignoreEvent) {
				componentHelper.fireComponentResized(e);
			}
		}

		@Override
		public void componentShown(ComponentEvent e) {
			if (!ignoreEvent) {
				componentHelper.fireComponentShown(e);
			}
		}

	}

}