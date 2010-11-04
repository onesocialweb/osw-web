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

import com.google.gwt.user.client.ui.FlowPanel;

public class ProgressBar extends FlowPanel {

	private FlowPanel back = new FlowPanel();
	private FlowPanel progress = new FlowPanel();

	private Long value;

	public ProgressBar() {
		addStyleName("progressbar");
		back.addStyleName("back");
		progress.addStyleName("progress");

		StyledFlowPanel back = new StyledFlowPanel("back");
		StyledFlowPanel progress = new StyledFlowPanel("progress");

		// back.add(progress);
		// add(back);

		getElement().setAttribute(
				"style",
				"background-image: url('"
						+ OswClient.getInstance().getPreference("theme_folder")
						+ "assets/avatar-loader.gif');");

	}

	public void setProgress(Long value) {
		this.value = value;
		progress.setWidth(value.toString() + "px");
		// setWidth("30px");
	}

	public void reset() {
		value = null;
		progress.setWidth("0px");
	}
}
