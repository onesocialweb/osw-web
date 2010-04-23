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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class VideoThumbnail extends Composite {
	private FlowPanel thumbnailwrapper = new FlowPanel();
	private FlowPanel fix = new FlowPanel();
	private FlowPanel thumbnail = new FlowPanel();
	private FlowPanel actions = new FlowPanel();
	private PushButton buttonDelete = new PushButton(new Image(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-close2.png"));
	private HTMLPanel embed;

	public VideoThumbnail(String source) {

		embed = new HTMLPanel(source);
		// compose composite
		actions.add(buttonDelete);
		thumbnail.add(embed);
		thumbnail.add(actions);
		thumbnailwrapper.add(thumbnail);

		// set tooltips
		buttonDelete.setTitle("Remove video");

		// styles
		thumbnail.addStyleName("thumbnail");
		actions.addStyleName("actions");
		embed.addStyleName("embed");
		thumbnailwrapper.addStyleName("thumbnailwrapper");

		initWidget(thumbnailwrapper);

		// actions.addMouseOverHandler(new CustomMouseEventHandler());

	}

	public PushButton getButtonDelete() {
		return buttonDelete;
	}

}
