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

import java.util.Iterator;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.ui.event.ComponentHelper;
import org.onesocialweb.gwt.client.ui.event.ComponentListener;
import org.onesocialweb.gwt.util.ListEventHandler;
import org.onesocialweb.gwt.util.ListModel;
import org.onesocialweb.model.activity.ActivityObject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

public abstract class AbstractAttachmentPanel extends FlowPanel {

	private final PushButton buttonClose = new PushButton(new Image(OswClient
			.getInstance().getPreference("theme_folder")
			+ "assets/i-close2.png"));
	private final Label labelTitle = new Label();
	private final Image icon = new Image();
	private final FlowPanel titlebar = new FlowPanel();
	private final FlowPanel actions = new FlowPanel();
	private final FlowPanel titlebarContents = new FlowPanel();
	private final FlowPanel contents = new FlowPanel();
	private final FlowPanel fix = new FlowPanel();
	private final AttachmentEventHandler handler = new AttachmentEventHandler();
	private final ComponentHelper componentHelper = new ComponentHelper();

	private ListModel<ActivityObject> model;

	public AbstractAttachmentPanel() {
		composePanel();
	}

	public void addComponentListener(ComponentListener listener) {
		componentHelper.addComponentListener(listener);
	}

	public void removeComponentListener(ComponentListener listener) {
		componentHelper.removeComponentListener(listener);
	}

	public void setHeader(String title) {
		labelTitle.setText(title);
	}

	public void setIcon(String url) {
		icon.setUrl(url);
	}

	public void setCloseTooltip(String tooltip) {
		buttonClose.setTitle(tooltip);
	}

	public void setModel(ListModel<ActivityObject> model) {
		if (this.model != null) {
			this.model.unregisterEventHandler(handler);
		}

		this.model = model;
		this.model.registerEventHandler(handler);
		repaint();
	}

	public void show() {
		if (!isVisible()) {
			setVisible(true);
			fireComponentShown();
		}
	}

	public void hide() {
		if (isVisible()) {
			setVisible(false);
			fireComponentHidden();
		}
	}

	public void reset() {
		model.clear();
	}

	protected ListModel<ActivityObject> getModel() {
		return model;
	}

	protected FlowPanel getPanel() {
		return contents;
	}

	protected void fireComponentResized() {
		componentHelper.fireComponentResized(this);
	}

	protected void fireComponentShown() {
		componentHelper.fireComponentShown(this);
	}

	protected void fireComponentHidden() {
		componentHelper.fireComponentHidden(this);
	}

	protected void fireComponentMoved() {
		componentHelper.fireComponentMoved(this);
	}

	protected abstract void render(ActivityObject object);

	private void composePanel() {
		// Set CSS styling
		labelTitle.addStyleName("attachmentTitle");
		addStyleName("attachmentPanel");
		actions.addStyleName("actions");
		icon.addStyleName("icon");
		contents.addStyleName("contents");
		titlebarContents.addStyleName("titlebarContents");
		titlebar.addStyleName("titlebar");
		fix.addStyleName("fix");

		// Compose the individual elements
		actions.add(buttonClose);
		titlebar.add(actions);
		titlebar.add(icon);
		titlebar.add(labelTitle);
		titlebar.add(titlebarContents);

		// Adds elements to the panel
		add(titlebar);
		add(contents);
		add(fix);

		// Add handlers for closing the panel
		buttonClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				reset();
			}
		});
	}

	private void repaint() {
		final Iterator<ActivityObject> iterator = getModel().iterator();

		// First we clear all objects
		getPanel().clear();

		// And we add them one by one
		while (iterator.hasNext()) {
			ActivityObject object = iterator.next();
			render(object);
		}

		// Notify our parent that our size has changed
		// TODO not ideal to do this here
		fireComponentResized();
	}

	private class AttachmentEventHandler implements
			ListEventHandler<ActivityObject> {

		@Override
		public void elementAdded(ActivityObject element) {
			repaint();
			show();
		}

		@Override
		public void elementRemoved(ActivityObject element) {
			repaint();
			if (model.size() == 0)
				hide();
			
		}

		@Override
		public void listCleared() {
			getPanel().clear();
			hide();
		}

	}

}
