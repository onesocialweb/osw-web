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
package org.onesocialweb.gwt.client.ui.widget.activity;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.dialog.AlertDialog;
import org.onesocialweb.gwt.client.ui.widget.TooltipPushButton;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.model.activity.ActivityEntry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class ActivityButtons extends FlowPanel {
	private TooltipPushButton buttonLike = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-like.png"), "Like");
	private TooltipPushButton buttonComment = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-comment.png"), "Comment");
	private TooltipPushButton buttonShare = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-share.png"), "Share");
	private TooltipPushButton buttonDelete = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
			+ "assets/i-delete.png"), "Delete");

	private String activityId = "";
	private AbstractActivityPanel<ActivityEntry> panel;
	private ActivityItemView activityItemView;
	
	public ActivityButtons(final AbstractActivityPanel<ActivityEntry> panel) {
		this.panel = panel;
		addStyleName("activityButtons");
		add(buttonComment);
		add(buttonLike);
		add(buttonShare);
		add(buttonDelete);

		buttonComment.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AlertDialog.getInstance().showDialog(
						"Sorry this feature is not yet implemented.",
						"We're working on this");
			}

		});

		buttonLike.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AlertDialog.getInstance().showDialog(
						"Sorry this feature is not yet implemented.",
						"We're working on this");
			}

		});

		buttonShare.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AlertDialog.getInstance().showDialog(
						"Sorry this feature is not yet implemented.",
						"We're working on this");
			}

		});

		buttonDelete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				// Add message in task bar
				final DefaultTaskInfo task = new DefaultTaskInfo(
						"Deleting activity", false);
				TaskMonitor.getInstance().addTask(task);
				
				// set the item to updating and disable the buttons
				disable();
				
				OswService service = OswServiceFactory.getService();
				
				if (activityId.length() > 0) {
					service.delete(activityId, new RequestCallback<Object>() {

						@Override
						public void onFailure() {
							task.complete("", Status.failure);
							enable();
							AlertDialog.getInstance().showDialog("Could not delete the item.", "Oops");
						}

						@Override
						public void onSuccess(Object result) {
							task.complete("", Status.succes);
							enable();
							panel.repaint();
							setVisible(false);
						}
					});
				}
			}

		});
	}
	
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	public void setActivityItemView(ActivityItemView activityItemView) {
		this.activityItemView = activityItemView;
	}
	
	public void showLoggedInOptions() {
		buttonDelete.setVisible(true);
	}
	
	public void hideLoggedInOptions() {
		buttonDelete.setVisible(false);
	}
	
	private void disable() {
		// temporarily disable the UI while processing request
		buttonComment.setEnabled(false);
		buttonLike.setEnabled(false);
		buttonShare.setEnabled(false);
		buttonDelete.setEnabled(false);
		panel.setUpdating(false);
	}
	
	private void enable() {
		// enable the UI after processing request
		buttonComment.setEnabled(true);
		buttonLike.setEnabled(true);
		buttonShare.setEnabled(true);
		buttonDelete.setEnabled(true);
		panel.setUpdating(true);
	}

}
