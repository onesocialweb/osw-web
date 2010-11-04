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

import org.onesocialweb.gwt.client.OswClient;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class ConfirmLabel extends FlowPanel {

	Label label = new Label();

	public ConfirmLabel(String text, StyledTooltipImage icon) {

		addStyleName("confirmLabel");
		TooltipPushButton confirm = new TooltipPushButton(new Image(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-ok.png"), "Confirm");
		TooltipPushButton deny = new TooltipPushButton(new Image(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-deny.png"), "Deny");

		confirm.addStyleName("confirm");
		deny.addStyleName("deny");

		label.setText(text);

		add(icon);
		add(label);
		add(confirm);
		add(deny);

		confirm.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//
			}
		});

		deny.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//
			}
		});

	}

}
