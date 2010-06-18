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

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

public class DateField extends Composite {
	
	private ListBox dayL = new ListBox();
	private ListBox monthL = new ListBox();
	private ListBox yearL = new ListBox();
	private HorizontalPanel container = new HorizontalPanel();
	
	public DateField() {
		StyledFlowPanel daypanel = new StyledFlowPanel("subpanel");
		StyledFlowPanel monthpanel = new StyledFlowPanel("subpanel");
		StyledFlowPanel yearpanel = new StyledFlowPanel("subpanel");

		daypanel.add(dayL);
		container.add(daypanel);

		monthpanel.add(monthL);
		container.add(monthpanel);

		yearpanel.add(yearL);
		container.add(yearpanel);
		
		initValues();
		
		initWidget(container);
	}
	
	public void setDate(Date birthday) {
		
		DateTimeFormat fmtDay = DateTimeFormat.getFormat("d");
		DateTimeFormat fmtMonth = DateTimeFormat.getFormat("MMMM");
		DateTimeFormat fmtYear = DateTimeFormat.getFormat("yyyy");
		
		String day ="";
		String month ="";
		String year = "";
		
		try {
			day = fmtDay.format(birthday);
			month = fmtMonth.format(birthday);
			year = fmtYear.format(birthday);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i=0;i<dayL.getItemCount();i++) {
			if (day.equals(dayL.getItemText(i))) {
				dayL.setSelectedIndex(i);
			}
		}
		
		for (int i=0;i<monthL.getItemCount();i++) {
			if (month.equals(monthL.getItemText(i))) {
				monthL.setSelectedIndex(i);
			}
		}
		
		for (int i=0;i<yearL.getItemCount();i++) {
			if (year.equals(yearL.getItemText(i))) {
				yearL.setSelectedIndex(i);
			}
		}
	}

	public Date getDate() {
		
		DateTimeFormat dtf = DateTimeFormat.getFormat("d/MMM/yyyy");
		
		Date birthday = null;
		
		try {
			birthday = dtf.parse(dayL.getItemText(dayL.getSelectedIndex()) + "/" + monthL.getItemText(monthL.getSelectedIndex()) + "/" + yearL.getItemText(yearL.getSelectedIndex()) );           
		} catch (Exception e) {
			e.printStackTrace();
		}

		return birthday;
		
	}
	
	private void initValues() {
		for (int i = 1; i < 32; i++) {
			dayL.addItem(Integer.toString(i), Integer.toString(i));
		}

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

		for (int i = 2010; i >= 1900; i--) {
			yearL.addItem(Integer.toString(i), Integer.toString(i));
		}
	}
}
