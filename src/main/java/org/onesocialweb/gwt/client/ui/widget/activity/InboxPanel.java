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
 *  2010-08-19 Modified by Luca Faggioli Copyright 2010 Openliven S.r.l
 *  added updateActivityReplies()
 *  
 */
package org.onesocialweb.gwt.client.ui.widget.activity;

import org.onesocialweb.gwt.client.handler.ActivityButtonHandler;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.model.activity.ActivityEntry;

import com.google.gwt.user.client.ui.Widget;

public class InboxPanel extends AbstractActivityPanel<ActivityEntry> {
	
	// we're passing the panel for manipulations
	private final ActivityButtons buttons = new ActivityButtons(this);

	private ActivityItemView lastSelected;

	public InboxPanel() {
		// UI stuff
		addStyleName("activityPanel");

		// hide the buttons (used as overlay on focus)
		buttons.setVisible(false);
		
		
		
	}

	
	@Override
	protected Widget render(final ActivityEntry activityEntry, boolean expand, int unread) {
		
		if (activityEntry.getId()==null)
			return null;

		if ((!activityEntry.hasAuthors()) && (activityEntry.getActor()==null))
			return null;
		
		//check that none of the recipients uri is malformed. This can be introduce in the command line console or android app and
		// will then break the web application
/*		List<AtomReplyTo> recipients= activityEntry.getRecipients();
		for (AtomReplyTo recipient: recipients){
			if ((recipient.getHref()!=null)) {
				try {
				
					XmppURI ur =  XmppURI.jid(recipient.getHref());
				
				}catch (RuntimeException e){
					recipients.remove(recipient);
					activityEntry.setRecipients(recipients);
				}
			}
		}
	*/	

		
		ActivityItemView sa = new ActivityItemView(activityEntry, expand, unread);
		sa.setButtonHandler(new ActivityButtonHandler() {
			public void handleShow(int top, ActivityItemView sa) {
				
				String author= activityEntry.hasAuthors() ? activityEntry.getAuthors().get(0).getUri() : activityEntry.getActor().getUri();
				// pass info for editing, deleting etc.
				buttons.setActivityId(activityEntry.getId());
				buttons.setActivityItemView(sa);
				
				// only show options like edit and delete for own items
				if (author.equals(OswServiceFactory.getService().getUserBareJID())) {
					buttons.showLoggedInOptions();
				} else {
					buttons.hideLoggedInOptions();
				}
				//this is a horrible patch ...it's momentary
				if (author.contains("identi.ca")) {
					buttons.hideOStatusOptions();
				}
				
				// make sure to remove the selected state. Mouseout is not
				// always captured
				if (lastSelected != null) {
					lastSelected.removeSelect();
				}
				// force selecting the activity
				if (!sa.getStyleName().equals("selected")) {
					sa.addSelect();
					lastSelected = sa;
				}
				
				showButtons(top);
			}

			public void handleHide() {
				hideButtons();
			}
		});
		
		return sa;
	}

	@Override
	protected void repaint() {
		super.repaint();
		add(buttons);
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

}
