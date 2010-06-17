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
package org.onesocialweb.gwt.client.util;

import org.onesocialweb.gwt.client.ui.widget.PrivacySelectorHeader;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class FormLayoutHelper {

	public static void addHTMLLabelRow(FlexTable target, String label,
			String value) {

		target.insertRow(target.getRowCount());
		target.addCell(target.getRowCount() - 1);
		target.addCell(target.getRowCount() - 1);
		target.setText(target.getRowCount() - 1, 0, label);
		target.setText(target.getRowCount() - 1, 1, value);

		target.addStyleName("fields");

		// add styling
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 0,
				"label");
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 1,
				"value");

	}

	public static void addWidgetRow(FlexTable target, String label,
			Widget widget) {

		target.insertRow(target.getRowCount());
		target.addCell(target.getRowCount() - 1);
		target.addCell(target.getRowCount() - 1);
		target.addCell(target.getRowCount() - 1);
		target.setText(target.getRowCount() - 1, 0, label);
		target.setWidget(target.getRowCount() - 1, 1, widget);

		target.addStyleName("fields");

		// add styling
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 0,
				"label");
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 1,
				"value");
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 2,
				"visibility");

	}

	public static void addContactRow(FlexTable target, Widget avatar,
			Widget details) {

		target.insertRow(target.getRowCount());
		target.addCell(target.getRowCount() - 1);
		target.addCell(target.getRowCount() - 1);
		target.addCell(target.getRowCount() - 1);
		target.setWidget(target.getRowCount() - 1, 0, avatar);
		target.setWidget(target.getRowCount() - 1, 1, details);

		// add styling
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 0,
				"avatar");
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 1,
				"details");

	}

	public static void addWidgetRowHeader(FlexTable target, String label,
			Widget widget, String visibility) {

		target.insertRow(target.getRowCount());
		target.addCell(target.getRowCount() - 1);
		target.addCell(target.getRowCount() - 1);
		target.addCell(target.getRowCount() - 1);
		target.setText(target.getRowCount() - 1, 0, label);
		target.setWidget(target.getRowCount() - 1, 1, widget);

		// if relevant show the visibility settings for this field
		if (visibility != null && visibility.length() > 0) {
			// TooltipPushButton btnVisibility = new TooltipPushButton(new
			// Image(OswClient.getInstance().getPreference("theme_folder") +
			// "assets/i-visibility.png"), "Visible to: " + visibility);
			// Button btnVisibility = new Button("Friends");
			PrivacySelectorHeader selectorheader = new PrivacySelectorHeader();
			target.setWidget(target.getRowCount() - 1, 2, selectorheader);
		}

		target.addStyleName("fields");

		// add styling
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 0,
				"label");
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 1,
				"value");
		target.getCellFormatter().addStyleName(target.getRowCount() - 1, 2,
				"visibility");

	}
}
