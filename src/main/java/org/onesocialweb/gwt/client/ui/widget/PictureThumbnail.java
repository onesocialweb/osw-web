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
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.reveregroup.gwt.imagepreloader.FitImage;

public class PictureThumbnail extends Composite {

	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private final FlowPanel thumbnailwrapper = new FlowPanel();
	private final FlowPanel thumbnail = new FlowPanel();
	private final FlowPanel actions = new FlowPanel();
	private final PushButton buttonDelete = new PushButton(new Image(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-close2.png"));
	private final FitImage image = new FitImage();
	private final HorizontalPanel imagewrapper = new HorizontalPanel();

	private ClickHandler clickHandler;

	public PictureThumbnail(String url) {

		// set url and thumb size
		image.setUrl(url);
		image.setMaxSize(80, 80);

		// compose composite
		actions.add(buttonDelete);
		imagewrapper.add(image);
		thumbnail.add(actions);
		thumbnail.add(imagewrapper);
		thumbnailwrapper.add(thumbnail);

		// set tooltips
		buttonDelete.setTitle(uiText.RemovePicture());

		// styles
		thumbnail.addStyleName("thumbnail");
		actions.addStyleName("actions");
		image.addStyleName("image");
		thumbnailwrapper.addStyleName("thumbnailwrapper");

		// click handler
		buttonDelete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (clickHandler != null) {
					clickHandler.onClick(event);
				}
			}
		});

		initWidget(thumbnailwrapper);
	}

	public void setCloseHandler(ClickHandler handler) {
		this.clickHandler = handler;
	}

	public void removeOnClose() {
		this.clickHandler = null;
	}
	
	public void setPicture(String url) {
		image.setUrl(url);
	}

}
