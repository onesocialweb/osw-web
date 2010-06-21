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
package org.onesocialweb.gwt.client.ui.window;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.event.ComponentEvent;
import org.onesocialweb.gwt.client.ui.event.ComponentListener;
import org.onesocialweb.gwt.client.ui.widget.activity.InboxPanel;
import org.onesocialweb.gwt.client.ui.widget.compose.NewActivityPanel;
import org.onesocialweb.gwt.service.OswServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

public class FeedWindow extends AbstractWindow {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private final ToggleButton toggleStatusPanel = new ToggleButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/08-chat.png"));
	private InboxPanel inboxPanel;
	private NewActivityPanel statusPanel;
	private StatusPanelListener statusPanelListener;

	@Override
	protected void onInit() {
		composeWindow();
	}

	@Override
	protected void onResize() {
		updateLayout();
	}

	@Override
	protected void onRepaint() {
		updateLayout();
	}

	@Override
	protected void onDestroy() {
		statusPanel.removeComponentListener(statusPanelListener);
	}

	private void composeWindow() {

		// Setup window elements
		setStyle("feedWindow");
		setWindowTitle(uiText.Activities());

		// Prepare the panels
		statusPanelListener = new StatusPanelListener();
		statusPanel = new NewActivityPanel();
		
		statusPanel.setVisible(true);
		statusPanel.addComponentListener(statusPanelListener);

		toggleStatusPanel.setDown(true);
		toggleStatusPanel.setTitle(uiText.HideUpdatePanel());
		toggleStatusPanel.addStyleDependentName("toggleShowUpdate");
		toggleStatusPanel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggleStatusPanel();
			}
		});

		inboxPanel = new InboxPanel();
		inboxPanel.setModel(OswServiceFactory.getService().getInbox());

		// Add the panels to the window
		getTopbar().add(statusPanel);
		getContents().add(inboxPanel);
		getActions().add(toggleStatusPanel);
		
		statusPanel.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				updateLayout();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
	}

	private void toggleStatusPanel() {
		if (statusPanel.isVisible()) {
			statusPanel.setVisible(false);
			toggleStatusPanel.setTitle(uiText.ShowUpdatePanel());
		} else {
			statusPanel.setVisible(true);
			toggleStatusPanel.setTitle(uiText.HideUpdatePanel());
		}

		// Force layout update on the Feed window itself
		updateLayout();
	}

	private void updateLayout() {
		// Correct the size of the contents based on visibility of statusPanel
		if (statusPanel.isVisible()) {
			getContents().getElement()
					.setAttribute(
							"style",
							"top:"
									+ (getTitlebar().getElement()
											.getClientHeight() + statusPanel
											.getElement().getClientHeight())
									+ ";");
		} else {
			getContents().getElement().setAttribute(
					"style",
					"top:" + (getTitlebar().getElement().getClientHeight())
							+ ";");
		}
	}

	private class StatusPanelListener implements ComponentListener {

		@Override
		public void componentHidden(ComponentEvent e) {
			updateLayout();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			updateLayout();
		}

		@Override
		public void componentResized(ComponentEvent e) {
			updateLayout();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			updateLayout();
		}

	}
}
