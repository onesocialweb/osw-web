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
 *  2010-08-17 Modified by Luca Faggioli Copyright 2010 Openliven S.r.l
 *  
 *  added addEmptyModelMessage so that nothing is shown in the subclass 
 *  RepliesPanel when the model is empty and getModelItems() so we can
 *  sort the activities in RepliesPanel (for replies we put the oldest
 *  on the top and the newest at the bottom 
 *    
 */
package org.onesocialweb.gwt.client.ui.widget.activity;

import java.util.List;

import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.service.StreamEvent;
import org.onesocialweb.gwt.service.StreamEvent.Type;
import org.onesocialweb.gwt.util.Observer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractActivityPanel<T> extends FlowPanel {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	protected Stream<T> model;

	private DefaultTaskInfo task;

	private boolean update = true;
	
	private StyledLabel msg = new StyledLabel("message",
			uiText.NoActivitiesAvailable());

	public void setModel(Stream<T> model) {
		this.model = model;

		// Add a listener on the inbox
		model.registerEventHandler(new StreamListener());

		// Repaint a first time
		repaint();
	}
	
	public boolean isUpdating() {
		return update;
	}
	
	public void setUpdating(boolean shouldUpdate) {
		update = shouldUpdate;
	}

	protected abstract Widget render(T item);

	protected void repaint() {
		clear();
		

		if (model.isReady()) {
			List<T> items = getModelItems();
			if (items.size() > 0) {
				// render the items
				for (int i = items.size(); i >= 1; i--) {
					T item = items.get(i - 1);
					showItem(item);
				}
			} else {
				// if there are no results
				addEmptyModelMessage();
			}

			if (task != null) {
				task.complete("", Status.succes);
			}

		} else {
			if (task == null) {
				task = new DefaultTaskInfo(uiText.FetchingActivities(), false);
				TaskMonitor.getInstance().addTask(task);
			}
		}
	}

	private void showItem(T item) {
		remove(msg);
		Widget w = render(item);
		if (w != null) {
			insert(render(item), 0);
		}
	}

	private class StreamListener implements Observer<StreamEvent<T>> {

		@Override
		public void handleEvent(StreamEvent<T> event) {
			if (update) {
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
	
	protected void addEmptyModelMessage() {
		StyledLabel msg = new StyledLabel("message",
			"There are no status updates available.");
		add(msg);
	}

	protected List<T> getModelItems() {
		return model.getItems();
	}
}
