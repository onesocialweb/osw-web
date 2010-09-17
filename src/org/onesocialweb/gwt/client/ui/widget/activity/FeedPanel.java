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
 *  2010-08-17 Modified by Luca Faggioli Copyright 2010 Openliven S.r.l
 *  replaced ActivityItemView with FeedItemView in order to prevent users
 *  to post comments to a comment
 *  
 */
package org.onesocialweb.gwt.client.ui.widget.activity;

import org.onesocialweb.gwt.client.handler.ActivityButtonHandler;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.model.activity.ActivityEntry;

import com.google.gwt.user.client.ui.Widget;

public class FeedPanel extends AbstractActivityPanel<ActivityEntry> {

	private final ActivityButtons buttons = new ActivityButtons(this);

	private ActivityItemView lastSelected;

	public FeedPanel() {
		// UI stuff
		addStyleName("activityPanel");

		// hide the buttons (used as overlay on focus)
		buttons.setVisible(false);
	}

	@Override
	protected Widget render(final ActivityEntry activityEntry, boolean expand) {
		ActivityItemView sa = new ActivityItemView(activityEntry, expand);
		sa.setButtonHandler(new ActivityButtonHandler() {
			public void handleShow(int top, ActivityItemView sa) {
				
				// pass the Id for editing, deleting etc.
				buttons.setActivityId(activityEntry.getId());
				
				// only show options like edit and delete for own items
				if (activityEntry.getActor().getUri().equals(OswServiceFactory.getService().getUserBareJID())) {
					buttons.showLoggedInOptions();
				} else {
					buttons.hideLoggedInOptions();
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
				// force selecting the activity
				if (!sa.getStyleName().equals("selected"))
					sa.addStyleName("selected");
				lastSelected = sa;
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
