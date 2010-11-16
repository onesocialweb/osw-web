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

import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.task.TaskEvent;
import org.onesocialweb.gwt.client.task.TaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.application.AbstractApplication;
import org.onesocialweb.gwt.client.ui.application.AbstractApplication.Slot;
import org.onesocialweb.gwt.util.Observer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

public abstract class AbstractWindow extends Composite {

	private final FlowPanel window = new FlowPanel();
	private final FlowPanel titlebar = new FlowPanel();
	private final FlowPanel actions = new FlowPanel();
	private final FlowPanel topbar = new FlowPanel();
	private final FlowPanel contents = new FlowPanel();
	private final FlowPanel status = new FlowPanel();
	private final Label statusLabel = new Label();
	private final Label titleLabel = new Label();
	private final Image progress = new Image(OswClient.getInstance()
			.getPreference("theme_folder")
			+ "assets/ajax-loader.gif");

	private AbstractApplication parent;
	private Slot slot;
	private String windowTitle;
	private HandlerRegistration handlerRegistration;
	private ResizeHandler resizeHandler;
	private TaskObserver taskObserver;
	private boolean canClose = false;
	private boolean isLoading = false;
	private boolean isShown = false;

	public void init() {
		// Compose the UI
		composeWindow();
		initWidget(window);
		setVisible(false);

		// Initialize the handlers
		resizeHandler = new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				resize();
			}
		};

		taskObserver = new TaskObserver();

		// Let the implementing class know that we have initialized
		onInit();
	}

	public void show() {
		// If already shown, we don't do anything
		if (isShown)
			return;

		if (isAttached()) {
			// Let the implementing class know that we will be shown
			onShow();

			// Add resize handler
			handlerRegistration = Window.addResizeHandler(resizeHandler);

			// Register our task observer
			TaskMonitor.getInstance().registerEventHandler(taskObserver);

			// Update the taskbar with ongoig task if any
			List<TaskInfo> tasks = TaskMonitor.getInstance().getTasks();
			if (!tasks.isEmpty()) {
				taskObserver.setCurrentTask(tasks.get(tasks.size() - 1));
			}

			// Force a repaint
			repaint();

			// Display the window elements
			setVisible(true);

			// Keep track that we are now being shown
			isShown = true;
		}
	}

	public void hide() {
		// If already hidden, we don't do anything
		if (!isShown)
			return;

		if (isAttached()) {
			// Let the implementing class know that we will be hidden
			onHide();

			// Unregister the resize handler
			handlerRegistration.removeHandler();

			// Unregister the task observer
			TaskMonitor.getInstance().unregisterEventHandler(taskObserver);

			// Hide the window elements
			setVisible(true);

			// Keep track that we are now hidden
			isShown = false;
		}
	}

	public void destroy() {
		// First, we have to hide (in order to remove the handlers etc...)
		hide();

		// Let the implementing class know that we will be destroyed
		onDestroy();

		// Hide the window elements
		setVisible(false);

		// Remove from the DOM
		removeFromParent();
	}

	public void resize() {
		// Let the implementing class know that we will be resized
		onResize();

		// Perform a repaint (which will take into account the new size)
		repaint();
	}

	public void setParent(AbstractApplication parent) {
		this.parent = parent;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	/**
	 * A window that 'can close' is a window which exposes a close button to the
	 * user
	 * 
	 * @return true if the window can be closed by the user
	 */
	public boolean canClose() {
		return canClose;
	}

	/**
	 * Enable the user to close the window via a close button
	 */
	public void enableClose() {

		this.canClose = true;

		PushButton buttonClose = new PushButton(new Image(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-close2.png"));
		actions.add(buttonClose);

		buttonClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
				parent.removeWindow(AbstractWindow.this);
			}
		});
	}

	public void setStyle(String style) {
		window.addStyleName(style);
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
		titleLabel.setText(windowTitle);
	}

	public String getWindowStatus() {
		return statusLabel.getText();
	}

	public void setWindowStatus(String windowTitle) {
		statusLabel.setText(windowTitle);
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;

		if (isLoading) {
			progress.setVisible(true);
		} else {
			progress.setVisible(false);
		}
	}

	public void repaint() {
		// Compute the new dimensions based on the parent size
		double height = Math.round((this.getParent().getElement()
				.getClientHeight() - 20)
				* slot.height / 100) - 10;
		double width = Math.round((this.getParent().getElement()
				.getClientWidth() - 20)
				* slot.width / 100) - 10;
		double left = Math.round((this.getParent().getElement()
				.getClientWidth())
				* slot.left / 100);
		double top = Math.round((this.getParent().getElement()
				.getClientHeight())
				* slot.top / 100);

		// Apply the new dimension to the window panel
		window.getElement().setAttribute("style",
				"left:" + left + "; top:" + top + ";");
		window.setHeight(Double.toString(height) + "px");
		window.setWidth(Double.toString(width) + "px");

		// Notify the implementing class that a repaint is in progress
		onRepaint();
	}

	protected FlowPanel getActions() {
		return actions;
	}

	protected FlowPanel getTopbar() {
		return topbar;
	}

	protected FlowPanel getContents() {
		return contents;
	}

	protected FlowPanel getTitlebar() {
		return titlebar;
	}

	protected Label getTitleLabel() {
		return titleLabel;
	}

	protected void onInit() {
	}

	protected void onShow() {
	}

	protected void onHide() {
	}

	protected void onDestroy() {
	}

	protected void onResize() {
	}

	protected void onRepaint() {
	}

	private void composeWindow() {

		// components
		titlebar.add(titleLabel);
		titlebar.add(actions);
		status.add(progress);
		status.add(statusLabel);
		// compose
		window.add(titlebar);
		window.add(contents);
		window.add(topbar);
		window.add(status);

		// topbar.setVisible(false);
		progress.setVisible(false);

		// set classes
		window.addStyleName("window");
		titlebar.addStyleName("windowTitlebar");
		titleLabel.addStyleName("windowTitle");
		actions.addStyleName("windowActions");
		contents.addStyleName("windowContents");
		status.addStyleName("windowStatusbar");
		statusLabel.addStyleName("windowStatus");
		progress.addStyleName("windowProgress");
		topbar.addStyleName("windowTopbar");
	}

	private class TaskObserver implements Observer<TaskEvent> {

		private TaskInfo currentTask;

		@Override
		public void handleEvent(TaskEvent event) {
			if (event.getType().equals(
					org.onesocialweb.gwt.client.task.TaskEvent.Type.added)) {
				currentTask = event.getTask();
				currentTask.registerEventHandler(this);
				update();
			} else if (event.getType().equals(
					org.onesocialweb.gwt.client.task.TaskEvent.Type.updated)) {
				if (event.getTask().equals(currentTask)) {
					update();
				}
			} else if (event.getType().equals(
					org.onesocialweb.gwt.client.task.TaskEvent.Type.completed)) {
				if (event.getTask().equals(currentTask)) {
					update();
				}
			}
		}

		public void setCurrentTask(TaskInfo task) {
			currentTask = task;
			update();
		}

		private void update() {
			if (currentTask.getStatus().equals(Status.running)) {
				setLoading(true);
			} else {
				setLoading(false);
			}

			setWindowStatus(currentTask.getMessage());
		}

	}

}