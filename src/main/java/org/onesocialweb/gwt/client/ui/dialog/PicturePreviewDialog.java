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
package org.onesocialweb.gwt.client.ui.dialog;

import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PicturePreviewDialog extends AbstractDialog {

	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT
			.create(UserInterfaceText.class);

	// private final LoginHandler loginHandler;
	private static PicturePreviewDialog instance;
	private Button buttonOK = new Button(uiText.OK());
	private final Image image = new Image();
	private final StyledFlowPanel wrapper = new StyledFlowPanel("wrapper");

	private HandlerRegistration resizehandler;

	public static PicturePreviewDialog getInstance() {
		if (instance == null) {
			instance = new PicturePreviewDialog();
		}
		return instance;
	}

	public void showDialog(String url, String title) {
		setText(title);
		image.setUrl(url);
		center();
		buttonOK.setFocus(true);
	}

	private PicturePreviewDialog() {

		// Create dialog
		VerticalPanel vpanel1 = new VerticalPanel();
		HorizontalPanel buttoncontainer = new HorizontalPanel();

		wrapper.add(image);
		vpanel1.add(wrapper);
		vpanel1.add(buttoncontainer);

		buttoncontainer.add(buttonOK);
		buttoncontainer.setStyleName("dialogButtons");

		setWidget(vpanel1);

		buttonOK.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				PicturePreviewDialog.getInstance().hide();
			}
		});

	}

	public void updateSize() {
		Integer height = Math.round((Window.getClientHeight())) - 200;
		Integer width = Math.round((Window.getClientWidth())) - 100;

		if (image.getHeight() > height && image.getWidth() > width) {
			wrapper.getElement().setAttribute(
					"style",
					"height: " + Integer.toString(height) + "px; width: "
							+ Integer.toString(width) + "px;");
			center();
		} else if (image.getWidth() > width) {
			wrapper.getElement().setAttribute("style",
					"width: " + Integer.toString(width) + "px;");
			center();
		} else if (image.getHeight() > height) {
			wrapper.getElement().setAttribute(
					"style",
					"height: " + Integer.toString(height) + "px; width: "
							+ Integer.toString(image.getWidth() + 15) + "px;");
			center();
		} else {
			wrapper.getElement().setAttribute("style", "");
			center();
		}

	}

	@Override
	protected void onHide() {
		resizehandler.removeHandler();
		;

	}

	@Override
	protected void onShow() {
		updateSize();
		// handlers
		resizehandler = Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				updateSize();
			}
		});
	}

}
