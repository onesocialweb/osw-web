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
package org.onesocialweb.gwt.client.ui.widget.compose;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.widget.PictureThumbnail;
import org.onesocialweb.model.activity.ActivityObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class MultiplePictureAttachmentPanel extends AbstractAttachmentPanel {

	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	public MultiplePictureAttachmentPanel() {
		setVisible(false);
		setHeader(uiText.Pictures());
		setIcon(OswClient.getInstance().getPreference("theme_folder")
				+ "assets/i-camera2.png");
		setCloseTooltip(uiText.RemovePictures());
	}

	@Override
	protected void render(final ActivityObject object) {
		PictureThumbnail thumb = new PictureThumbnail(object.getLinks().get(0)
				.getHref());
		thumb.setCloseHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getModel().remove(object);
			}
		});

		getPanel().add(thumb);
	}

}
