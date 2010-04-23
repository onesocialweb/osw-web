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
package org.onesocialweb.gwt.client.ui.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.onesocialweb.gwt.client.exception.MissingWindowFactoryException;
import org.onesocialweb.gwt.client.ui.window.AbstractWindow;
import org.onesocialweb.gwt.client.ui.window.WindowFactory;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class AbstractApplication extends FlowPanel {

	private FlowPanel appContainer = new FlowPanel();

	private final HashMap<Slot, AbstractWindow> windows; // keeps track of the
															// actual windows
	private final List<Slot> slots; // keeps track of the slot dimensions

	public AbstractApplication(int slotsCount) {
		setVisible(false);
		slots = new ArrayList<Slot>(slotsCount);
		windows = new HashMap<Slot, AbstractWindow>(slotsCount);

		for (int i = 0; i < slotsCount; i++) {
			slots.add(new Slot());
		}
	}

	public void init() {
		// Let the implementing class know we are initializing
		onInit();
	}

	public void show() {
		// Let the implementing class know we are going to show
		onShow();

		// Check if application is already attached to DOM
		if (!isAttached()) {
			// attach application to page
			RootPanel.get("applicationwrapper").add(appContainer);
			appContainer.setHeight("100%");
			appContainer.setStyleName("application");
		}

		// Check if application is already showing
		setVisible(true);

		// Show the windows
		for (AbstractWindow w : windows.values()) {
			w.show();
		}
	}

	public void hide() {
		// Let the implementor know we are going to hide
		onHide();

		// Hide the content
		setVisible(false);

		// Cycle through the windows and inform of hide
		for (AbstractWindow w : windows.values()) {
			w.hide();
		}
	}

	public void destroy() {
		// Let the implementor know we are going to destroy
		onDestroy();

		// Get out of the view of the user to do our dirty stuff
		setVisible(false);

		// Destroy the windows
		for (AbstractWindow w : windows.values()) {
			w.destroy();
		}

		// Remove the container
		RootPanel.get("applicationwrapper").remove(appContainer);
	}

	public AbstractWindow addWindow(String className, int slotId) {
		try {
			// Get the slot
			final Slot slot = slots.get(slotId);

			// Clean up current slot
			if (windows.containsKey(slot)) {
				windows.get(slot).destroy();
				windows.remove(slot);
			}

			// Build a new window in the factory of the right type
			final AbstractWindow window = WindowFactory.getInstance()
					.newWindow(className);
			window.setSlot(slot);
			window.setParent(this);

			// Allocate it to the slot
			windows.put(slot, window);

			// Attach window
			appContainer.add(window);

			// Return the new window to the caller
			return window;

		} catch (MissingWindowFactoryException e) {
			return null;
		}
	}

	public void removeWindow(int slotId) {
		windows.remove(slotId);
	}

	public void removeWindow(AbstractWindow window) {
		for (Slot s : slots) {
			AbstractWindow w = windows.get(s);
			if (w != null && w.equals(window)) {
				windows.remove(s);
				break;
			}
		}
	}

	protected AbstractWindow getWindow(int slotId) {
		return windows.get(slotId);
	}

	protected Slot getSlot(int slotId) {
		return slots.get(slotId);
	}

	protected void onInit() {
	}

	protected void onShow() {
	}

	protected void onHide() {
	}

	protected void onDestroy() {
	}

	// class to keep track of the slot dimensions
	public class Slot {

		public int width, height, left, top;

		public void setSlotDimensions(int top, int left, int width, int height) {
			this.top = top;
			this.left = left;
			this.width = width;
			this.height = height;
		}

	}

}
