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
package org.onesocialweb.gwt.client.ui.widget.activity;

import java.util.List;

import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.service.StreamEvent;
import org.onesocialweb.gwt.service.StreamEvent.Type;
import org.onesocialweb.gwt.util.Observer;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractActivityPanel<T> extends FlowPanel {

	private Stream<T> model;

	private DefaultTaskInfo task;

	public void setModel(Stream<T> model) {
		this.model = model;

		// Add a listener on the inbox
		model.registerEventHandler(new StreamListener());

		// Repaint a first time
		repaint();
	}

	protected abstract Widget render(T item);

	protected void repaint() {
		clear();
		List<T> items = model.getItems();

		if (model.isReady()) {
			if (items.size() > 0) {
				// render the items
				for (int i = items.size(); i >= 1; i--) {
					T item = items.get(i - 1);
					showItem(item);
				}
			} else {
				// if there are no results
				StyledLabel msg = new StyledLabel("message",
						"There are no status updates available.");
				add(msg);
			}

			if (task != null) {
				task.complete("", Status.succes);
			}

		} else {
			if (task == null) {
				task = new DefaultTaskInfo("Fetching the activities", false);
				TaskMonitor.getInstance().addTask(task);
			}
		}
	}

	private void showItem(T item) {
		Widget w = render(item);
		if (w != null) {
			insert(render(item), 0);
		}
	}

	private class StreamListener implements Observer<StreamEvent<T>> {

		@Override
		public void handleEvent(StreamEvent<T> event) {
			if (event.getType().equals(Type.added)) {
				for (T item : event.getItems()) {
					showItem(item);
				}
			} else {
				repaint();
			}
		}

	}

}
