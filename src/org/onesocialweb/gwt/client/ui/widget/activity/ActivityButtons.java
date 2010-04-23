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
import org.onesocialweb.gwt.client.ui.dialog.AlertDialog;
import org.onesocialweb.gwt.client.ui.widget.TooltipPushButton;

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
	private TooltipPushButton buttonMore = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-more.png"), "More options");

	public ActivityButtons() {
		addStyleName("activityButtons");
		add(buttonComment);
		add(buttonLike);
		add(buttonShare);
		add(buttonMore);

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

		buttonMore.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AlertDialog.getInstance().showDialog(
						"Sorry this feature is not yet implemented.",
						"We're working on this");
			}

		});
	}

}
