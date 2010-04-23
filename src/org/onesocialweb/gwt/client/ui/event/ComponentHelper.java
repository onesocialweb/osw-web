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
package org.onesocialweb.gwt.client.ui.event;

import java.util.ArrayList;
import java.util.List;

public class ComponentHelper {

	private final List<ComponentListener> listeners = new ArrayList<ComponentListener>();

	public void addComponentListener(ComponentListener listener) {
		listeners.add(listener);
	}

	public void removeComponentListener(ComponentListener listener) {
		listeners.remove(listener);
	}

	public void fireComponentHidden(Object source) {
		final ComponentEvent event = new ComponentEvent(source);
		fireComponentHidden(event);
	}

	public void fireComponentHidden(ComponentEvent event) {
		for (ComponentListener listener : listeners) {
			listener.componentHidden(event);
		}
	}

	public void fireComponentShown(Object source) {
		final ComponentEvent event = new ComponentEvent(source);
		fireComponentShown(event);
	}

	public void fireComponentShown(ComponentEvent event) {
		for (ComponentListener listener : listeners) {
			listener.componentShown(event);
		}
	}

	public void fireComponentMoved(Object source) {
		final ComponentEvent event = new ComponentEvent(source);
		fireComponentMoved(event);
	}

	public void fireComponentMoved(ComponentEvent event) {
		for (ComponentListener listener : listeners) {
			listener.componentMoved(event);
		}
	}

	public void fireComponentResized(Object source) {
		final ComponentEvent event = new ComponentEvent(source);
		fireComponentResized(event);
	}

	public void fireComponentResized(ComponentEvent event) {
		for (ComponentListener listener : listeners) {
			listener.componentResized(event);
		}
	}
}
