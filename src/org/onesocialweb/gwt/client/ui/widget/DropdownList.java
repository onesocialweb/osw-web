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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.onesocialweb.gwt.client.OswClient;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

public class DropdownList extends FlowPanel {

	private StyledFlowPanel list = new StyledFlowPanel("list");
	private List<String> values = new ArrayList<String>();
	private StyledLabel text = new StyledLabel("value", "");
	private TextBox hiddenValue = new TextBox();

	public DropdownList(List<String> values) {

		this.values = values;
		addStyleName("dropdown");

		TooltipPushButton select = new TooltipPushButton(new Image(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-arrowdown.png"), "Select a value");

		select.addStyleName("button");
		hiddenValue.getElement().setAttribute("type", "hidden");

		add(text);
		add(hiddenValue);
		add(select);
		add(list);

		populateList();
		list.setVisible(false);

		select.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggleList();
			}
		});

	}

	private void populateList() {
		Iterator<String> it = values.iterator();
		while (it.hasNext()) {
			StyledLabel value = new StyledLabel("listvalue", (String) it.next());
			list.add(value);

			value.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					text.setText(event.getRelativeElement().getInnerHTML());
					hiddenValue.setText(event.getRelativeElement()
							.getInnerHTML());
					toggleList();
				}
			});
		}
	}

	private void toggleList() {

		list.setVisible(!list.isVisible());

	}
}
