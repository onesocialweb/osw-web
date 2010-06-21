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

import org.onesocialweb.gwt.client.handler.PictureHandler;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.widget.SingleUploader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PictureChooserDialog extends AbstractDialog {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private final PictureHandler handler;
	private TabPanel tabpanel = new TabPanel();
	private int currentTab = 0;
	private TextBox url = new TextBox();
	// Create a new uploader panel and attach it to the document
	private SingleUploader defaultUploader = new SingleUploader();

	public PictureChooserDialog(PictureHandler handler) {

		this.handler = handler;

		// overall widgets
		Button buttonAdd = new Button(uiText.Add());
		Button buttonCancel = new Button(uiText.Cancel());
		VerticalPanel vpanel1 = new VerticalPanel();
		HorizontalPanel buttoncontainer = new HorizontalPanel();

		// various tab widgets
		FlowPanel uploadflow = new FlowPanel();
		FlowPanel urlflow = new FlowPanel();
		Label label1 = new Label(uiText.SelectFile());
		Label label2 = new Label(uiText.SelectPictureUrl());
		Grid urlcontainer = new Grid(1, 2);

		// set styles
		buttoncontainer.setStyleName("dialogButtons");
		uploadflow.setStyleName("tabcontentflowlayout");
		uploadflow.setStyleName("upload");
		urlflow.setStyleName("tabcontentflowlayout");
		url.setStyleName("simplefield");

		// create parts
		uploadflow.add(label1);
		uploadflow.add(defaultUploader);
		urlcontainer.setWidget(0, 0, new Label(uiText.Url()));
		urlcontainer.setWidget(0, 1, url);

		urlflow.add(label2);
		urlflow.add(urlcontainer);

		tabpanel.add(urlflow, uiText.FromUrl());
		tabpanel.add(uploadflow, uiText.Upload());
		// tabpanel.add(label3, "From Gallery");

		buttoncontainer.add(buttonAdd);
		buttoncontainer.add(buttonCancel);

		vpanel1.add(tabpanel);
		vpanel1.add(buttoncontainer);

		// build and init dialog
		setText(uiText.AddPicture());
		url.setText("");
		setWidget(vpanel1);
		tabpanel.selectTab(0);

		this.setAutoHideEnabled(true);

		// handlers
		buttonCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromParent();
				hide();
			}
		});

		buttonAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (defaultUploader.getIsUploading() == false) {
					complete();
				} else {
					AlertDialog
							.getInstance()
							.showDialog(
									uiText.WaitForUpload(),
									uiText.CannotAddPicture());
				}
			}
		});

		tabpanel.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				setCurrentTab(event.getSelectedItem());
				center();
			}
		});

	}

	public void showPictureChooser() {
		center();
		tabpanel.setWidth(Integer.toString(this.getOffsetWidth() - 20));

		// TODO catch set focus cases for all tabs
		url.setFocus(true);
	}

	protected void complete() {

		String result = "";

		if (currentTab == 0) {
			// link to image
			result = url.getText();

			handler.handlePicture(result);
			hide();
		}
		if (currentTab == 1) {
			// file upload
			result = defaultUploader.getFileUrl();
			handler.handlePicture(result);
			hide();
		}

	}

	protected void setCurrentTab(int index) {
		currentTab = index;
	}

	@Override
	protected void onHide() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onShow() {

		// reset everything
		url.setText("");
		defaultUploader.reset();
		tabpanel.selectTab(0);

	}

}
