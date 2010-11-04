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

import org.onesocialweb.gwt.client.task.TaskEvent.Type;
import org.onesocialweb.gwt.util.ObservableHelper;
import org.onesocialweb.gwt.util.Observer;

public class DefaultTaskInfo implements TaskInfo {

	private final ObservableHelper<TaskEvent> observableHelper = new ObservableHelper<TaskEvent>();

	private final boolean hasProgress;

	private float progress = 0;

	private String message;

	private Status status = Status.running;

	public DefaultTaskInfo(String message, boolean hasProgress) {
		this.message = message;
		this.hasProgress = hasProgress;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public float getProgress() {
		return progress;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public boolean hasProgress() {
		return hasProgress;
	}

	@Override
	public void registerEventHandler(Observer<TaskEvent> handler) {
		observableHelper.registerEventHandler(handler);
	}

	@Override
	public void unregisterEventHandler(Observer<TaskEvent> handler) {
		observableHelper.unregisterEventHandler(handler);
	}

	public void setMessage(String message) {
		this.message = message;
		observableHelper.fireEvent(new DefaultTaskEvent(Type.updated, this));
	}

	public void setProgress(float progress) {
		this.progress = progress;
		observableHelper.fireEvent(new DefaultTaskEvent(Type.updated, this));
	}

	public void complete(String message, Status status) {
		this.status = status;
		this.message = message;
		observableHelper.fireEvent(new DefaultTaskEvent(Type.completed, this));
	}

}
