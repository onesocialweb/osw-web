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

import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.model.activity.ActivityObject;

public class ShoutAttachmentPanel extends AbstractAttachmentPanel {

	private RecipientList list = new RecipientList();

	public ShoutAttachmentPanel() {
		setHeader("Shout to");
		setVisible(false);
		setTitle("Shout to these people");
		setIcon(OswClient.getInstance().getPreference("theme_folder")
				+ "assets/i-shout-r.png");
		setCloseTooltip("Remove all people");

		// Compose the UI
		add(list);
		list.setFocus();
	}

	@Override
	public void reset() {
		remove(list);
		list = new RecipientList();
		add(list);
	}

	public List<String> getRecipients() {
		return list.getRecipients();
	}

	@Override
	protected void render(ActivityObject object) {
		// TODO Auto-generated method stub

	}

}
