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
package org.onesocialweb.gwt.client.ui.widget.compose;

import java.util.Set;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.Roster;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.util.Observer;
import org.onesocialweb.model.activity.ActivityObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class PrivacyAttachmentPanel extends AbstractAttachmentPanel {

	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private ListBox visibility;

	public PrivacyAttachmentPanel() {
		setHeader(uiText.Privacy());
		setVisible(false);
		StyledFlowPanel layout = new StyledFlowPanel("privacyAttachment");
		Label label = new Label(uiText.WhoCanSeeUpdate());
		StyledFlowPanel fix = new StyledFlowPanel("fix");

		// basic settings
		setIcon(OswClient.getInstance().getPreference("theme_folder")
				+ "assets/i-private-r.png");
		setCloseTooltip(uiText.BackToDefault());

		// construct
		visibility = new ListBox();
		StyledLabel show = new StyledLabel("label", uiText.With());
		final Roster roster = OswServiceFactory.getService().getRoster();
		roster.registerEventHandler(new Observer<RosterEvent>() {

			@Override
			public void handleEvent(RosterEvent event) {
				updateVisibility(visibility, roster.getGroups());
			}

		});
		updateVisibility(visibility, roster.getGroups());
		visibility.setVisibleItemCount(1);
		layout.add(label);
		layout.add(visibility);
		add(layout);
		add(fix);
	}

	@Override
	public void reset() {
		visibility.setSelectedIndex(0);
	}

	public String getPrivacyValue() {
		return visibility.getValue(visibility.getSelectedIndex());
	}

	private void updateVisibility(ListBox visibility, Set<String> groups) {
		visibility.clear();
		visibility.addItem(uiText.Everyone());
		for (String group : groups) {
			visibility.addItem(group);
		}
	}

	@Override
	protected void render(ActivityObject object) {
		// TODO Auto-generated method stub

	}

}
