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
import org.onesocialweb.gwt.client.handler.PictureHandler;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.dialog.PictureChooserDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class AvatarUploadField extends Composite {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private FlowPanel thumbnailContainer = new FlowPanel();
	private StyledFlowPanel container = new StyledFlowPanel("avatarupload");
	private PictureChooserDialog pictureChooserDialog;
	private final PushButton addPhoto = new PushButton(new Image(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-camera2.png"));
	private String avatarUri = "";
	private final PictureThumbnail thumb = new PictureThumbnail("");
	
	public AvatarUploadField() {

		init();
		initWidget(container);

	}
	
	public String getAvatarUri() {
		
		return avatarUri; 
	}
	
	private void init() {
		
		// hide thumbnail
		setStateNoAvatar();
		
		// add instruction
		addPhoto.setTitle(uiText.AddAvatar());
		
		pictureChooserDialog = new PictureChooserDialog(new PictureHandler() {
			public void handlePicture(String pictureUrl) {
				if (pictureUrl != null && pictureUrl.length() > 0) {
					thumb.setPicture(pictureUrl);
					avatarUri = pictureUrl;
					setStateAvatarLoaded();
				}
			}
		});
		
		thumbnailContainer.add(thumb);
		container.add(thumbnailContainer);
		container.add(addPhoto);
		
		thumb.setCloseHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setStateNoAvatar();
			}
		});
		
		addPhoto.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				pictureChooserDialog.show();
			}
		});
	}
	
	public void setAvatar(String url) {
		thumb.setPicture(url);
		setStateAvatarLoaded();
	}
	
	private void setStateAvatarLoaded() {
		thumbnailContainer.setVisible(true);
		addPhoto.setVisible(false);
	}
	
	private void setStateNoAvatar() {
		thumbnailContainer.setVisible(false);
		thumb.setPicture("");
		avatarUri="";
		addPhoto.setVisible(true);
	}
}
