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
import org.onesocialweb.model.vcard4.GenderField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class GenderEditField extends Composite {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT
			.create(UserInterfaceText.class);
	
	private ListBox genderList = new ListBox();
	
	public GenderEditField() {
		
		initValues();
		
		initWidget(genderList);
	}
	
	public void setGender(GenderField.Type gender) {
		
		for (int i=0;i<genderList.getItemCount();i++) {
			if (gender.toString().equals(genderList.getValue(i))) {
				genderList.setSelectedIndex(i);
				return;
			}
		}
		
	}
	
	public String getGenderValue() {
		
		return genderList.getValue(genderList.getSelectedIndex());
		
	}
	
	public String getGenderText() {
		
		return genderList.getItemText(genderList.getSelectedIndex());
		
	}
	
	private void initValues() {
		
		genderList.addItem("");
		genderList.addItem(uiText.NotKnown(), GenderField.Type.NOTKNOWN.toString());
		genderList.addItem(uiText.Male(), GenderField.Type.MALE.toString());
		genderList.addItem(uiText.Female(), GenderField.Type.FEMALE.toString());
		genderList.addItem(uiText.NotApplicable(), GenderField.Type.NOTAPPLICABLE.toString());
		
	}
}
