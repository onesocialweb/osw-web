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

import org.onesocialweb.gwt.client.i18n.UserInterfaceText;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class NameEditField extends Composite {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	StyledTextBox firstField = new StyledTextBox("subtextbox", "", "150px");
	//StyledTextBox middleField = new StyledTextBox("subtextbox", "", "75px");
	StyledTextBox lastField = new StyledTextBox("subtextbox", "", "150px");
	
	public NameEditField() {

		StyledLabel firstLabel = new StyledLabel("sublabel", uiText.FirstName());
		//StyledLabel middleLabel = new StyledLabel("sublabel", "Middle");
		StyledLabel lastLabel = new StyledLabel("sublabel", uiText.Surname());		

		HorizontalPanel container = new HorizontalPanel();
		StyledFlowPanel firstpanel = new StyledFlowPanel("subpanel");
		//StyledFlowPanel middlepanel = new StyledFlowPanel("subpanel");
		StyledFlowPanel lastpanel = new StyledFlowPanel("subpanel");

		firstpanel.add(firstField);
		firstpanel.add(firstLabel);
		container.add(firstpanel);

		//middlepanel.add(middleField);
		//middlepanel.add(middleLabel);
		//container.add(middlepanel);

		lastpanel.add(lastField);
		lastpanel.add(lastLabel);
		container.add(lastpanel);
		
		initWidget(container);

	}
	
	public void setName(String firstname, String lastname) {
		if (firstname != null && firstname.length() > 0) firstField.setText(firstname);
		//if (middlename != null && middlename.length() > 0) middleField.setText(middlename);
		if (lastname != null && lastname.length() > 0) lastField.setText(lastname);
	}
	
	public String getFirstName() {
		return firstField.getText();
	}
	
	public String getLastName() {
		return lastField.getValue();
	}
	
}
