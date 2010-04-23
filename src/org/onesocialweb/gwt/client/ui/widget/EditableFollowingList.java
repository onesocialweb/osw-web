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
package org.onesocialweb.gwt.client.ui.widget;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

public class EditableFollowingList extends FlowPanel {

	private FlowPanel labels = new FlowPanel();

	public EditableFollowingList() {

		addStyleName("editableList");

		StyledFlowPanel addPanel = new StyledFlowPanel("addPanel");
		String sArray[] = new String[] { "My Default", "Everything",
				"All status updates", "Profile updates", "Picture updates",
				"Video updates", "Album updates", "Bookmark updates",
				"New relations", "Comments", "Likes" };

		// convert array to list
		List lList = Arrays.asList(sArray);

		DropdownList dropdown = new DropdownList(lList);
		Button add = new Button("Add subscription");

		add(labels);
		addPanel.add(dropdown);
		addPanel.add(add);
		add(addPanel);

		getSelectedValues();
	}

	private void getSelectedValues() {

		// get the values and display them
		DeletableLabel label1 = new DeletableLabel("Profile updates", null);
		labels.add(label1);
		DeletableLabel label2 = new DeletableLabel("Bookmark updates", null);
		labels.add(label2);
		DeletableLabel label3 = new DeletableLabel("Likes", null);
		labels.add(label3);
	}

}
