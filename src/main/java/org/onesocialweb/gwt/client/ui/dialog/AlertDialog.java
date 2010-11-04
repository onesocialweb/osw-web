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
package org.onesocialweb.gwt.client.ui.dialog;

import org.onesocialweb.gwt.client.i18n.UserInterfaceText;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AlertDialog extends AbstractDialog {

	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT
			.create(UserInterfaceText.class);

	// private final LoginHandler loginHandler;
	private static AlertDialog instance;
	private Button buttonOK = new Button(uiText.OK());
	private Label message = new Label();

	public static AlertDialog getInstance() {
		if (instance == null) {
			instance = new AlertDialog();
		}
		return instance;
	}

	public void showDialog(String message, String title) {
		setText(title);
		this.message.setText(message);
		center();
		buttonOK.setFocus(true);
	}

	private AlertDialog() {

		// Create dialog
		VerticalPanel vpanel1 = new VerticalPanel();
		HorizontalPanel buttoncontainer = new HorizontalPanel();

		vpanel1.add(message);
		vpanel1.add(buttoncontainer);

		buttoncontainer.add(buttonOK);
		buttoncontainer.setStyleName("dialogButtons");

		setWidth("300px");
		setWidget(vpanel1);

		buttonOK.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				AlertDialog.getInstance().hide();
			}
		});

	}

	@Override
	protected void onHide() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onShow() {
		// TODO Auto-generated method stub

	}

}
