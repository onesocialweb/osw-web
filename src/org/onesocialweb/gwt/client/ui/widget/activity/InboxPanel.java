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
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.client.ui.widget.compose.CommentPanel;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.model.activity.ActivityEntry;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

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

	public void updateActivityReplies(String activityId) {
		
		WidgetCollection activityWidgets = getChildren();
		
		for(Widget widget: activityWidgets) {
			if(widget instanceof ActivityItemView) {
				ActivityItemView aiv = (ActivityItemView) widget;
				
				if(activityId.equals(aiv.getActivity().getId())) {
					StyledFlowPanel replieswrapper = aiv.replieswrapper;
					int repliesWidgetCount = replieswrapper.getWidgetCount();
					
					for(int i=0; i< repliesWidgetCount; i++) {
						Widget w = replieswrapper.getWidget(i);
						if(w instanceof CommentPanel) {
							CommentPanel commentPanel = (CommentPanel) w;
							RepliesPanel repliesPanel = commentPanel.getReplies();
							repliesPanel.setModel(OswServiceFactory.getService().getReplies(aiv.getActivity()));
							repliesPanel.repaint();
						} else if(w instanceof StyledLabel) {
							
							StyledLabel label = (StyledLabel) w;
							if(label.isVisible())  {
								String content = label.getHTML();
								if("Add a comment".equals(content)) {
									label.setHTML("Comments: 1");
								} else {
									content = content.substring(content.indexOf(":")+2);
									try {
										int numReplies = Integer.parseInt(content) + 1;
										label.setHTML("Comments: " + numReplies);
									} catch(NumberFormatException e) {
										//never mind...simply do not change the number of comments in the GUI
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		
	}
	
	@Override
	protected Widget render(final ActivityEntry activityEntry) {
		
		if (activityEntry.getId()==null)
			return null;

		if (activityEntry.getActor()==null)
			return null;

		ActivityItemView sa = new ActivityItemView(activityEntry);
		sa.setButtonHandler(new ActivityButtonHandler() {
			public void handleShow(int top, ActivityItemView sa) {
				
				// pass info for editing, deleting etc.
				buttons.setActivityId(activityEntry.getId());
				buttons.setActivityItemView(sa);
				
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
