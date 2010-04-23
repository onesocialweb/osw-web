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

import com.google.gwt.user.client.ui.FlowPanel;

public class NameField extends FlowPanel {

	public NameField(String firstname, String middlename, String lastname) {

		StyledLabel FirstLabel = new StyledLabel("sublabel", "First");
		StyledTextBox FirstField = new StyledTextBox("subtextbox", firstname,
				"125px");

		StyledLabel MiddleLabel = new StyledLabel("sublabel", "Middle");
		StyledTextBox MiddleField = new StyledTextBox("subtextbox", middlename,
				"75px");

		StyledLabel LastLabel = new StyledLabel("sublabel", "Last");
		StyledTextBox LastField = new StyledTextBox("subtextbox", lastname,
				"125px");

		StyledFlowPanel firstpanel = new StyledFlowPanel("subpanel");
		StyledFlowPanel middlepanel = new StyledFlowPanel("subpanel");
		StyledFlowPanel lastpanel = new StyledFlowPanel("subpanel");

		firstpanel.add(FirstField);
		firstpanel.add(FirstLabel);
		add(firstpanel);

		middlepanel.add(MiddleField);
		middlepanel.add(MiddleLabel);
		add(middlepanel);

		lastpanel.add(LastField);
		lastpanel.add(LastLabel);
		add(lastpanel);

	}
}
