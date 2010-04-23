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
package org.onesocialweb.gwt.client.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.onesocialweb.gwt.client.task.TaskEvent.Type;
import org.onesocialweb.gwt.util.Observable;
import org.onesocialweb.gwt.util.ObservableHelper;
import org.onesocialweb.gwt.util.Observer;

public class TaskMonitor implements Observable<TaskEvent> {

	private static TaskMonitor instance;

	private final ObservableHelper<TaskEvent> observableHelper = new ObservableHelper<TaskEvent>();

	private final List<TaskInfo> tasks = new ArrayList<TaskInfo>();

	private final TaskHandler handler = new TaskHandler();

	public static TaskMonitor getInstance() {
		if (instance == null) {
			instance = new TaskMonitor();
		}
		return instance;
	}

	@Override
	public void registerEventHandler(Observer<TaskEvent> handler) {
		observableHelper.registerEventHandler(handler);
	}

	@Override
	public void unregisterEventHandler(Observer<TaskEvent> handler) {
		observableHelper.unregisterEventHandler(handler);
	}

	public void addTask(TaskInfo task) {
		tasks.add(task);
		task.registerEventHandler(handler);
		observableHelper.fireEvent(new DefaultTaskEvent(TaskEvent.Type.added,
				task));
	}

	public List<TaskInfo> getTasks() {
		return Collections.unmodifiableList(tasks);
	}

	private class TaskHandler implements Observer<TaskEvent> {

		@Override
		public void handleEvent(TaskEvent event) {
			if (event.getType().equals(Type.completed)) {
				tasks.remove(event.getTask());
				observableHelper.fireEvent(event);
			}
		}

	}

	// private constructor to maintain singleton
	private TaskMonitor() {
		//
	}

}
