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
import com.google.gwt.user.client.ui.ListBox;

public class DateField extends FlowPanel {

	public DateField(Integer day, Integer month, Integer year) {

		ListBox dayL = new ListBox();
		ListBox monthL = new ListBox();
		ListBox yearL = new ListBox();

		for (int i = 1; i < 32; i++) {
			dayL.addItem(Integer.toString(i), Integer.toString(i));
		}
		;
		// TODO select the right day

		monthL.addItem("January", "1");
		monthL.addItem("February", "2");
		monthL.addItem("March", "3");
		monthL.addItem("April", "4");
		monthL.addItem("May", "5");
		monthL.addItem("June", "6");
		monthL.addItem("July", "7");
		monthL.addItem("August", "8");
		monthL.addItem("September", "9");
		monthL.addItem("October", "10");
		monthL.addItem("November", "11");
		monthL.addItem("December", "12");
		// TODO select right month

		for (int i = 2010; i >= 1900; i--) {
			yearL.addItem(Integer.toString(i), Integer.toString(i));
		}
		;
		// TODO select right year

		StyledFlowPanel daypanel = new StyledFlowPanel("subpanel");
		StyledFlowPanel monthpanel = new StyledFlowPanel("subpanel");
		StyledFlowPanel yearpanel = new StyledFlowPanel("subpanel");

		daypanel.add(dayL);
		add(daypanel);

		monthpanel.add(monthL);
		add(monthpanel);

		yearpanel.add(yearL);
		add(yearpanel);

	}
}
