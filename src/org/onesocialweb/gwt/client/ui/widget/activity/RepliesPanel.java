/*
 *  Copyright 2010 Openliven S.r.l.
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
 *  author luca faggioli luca(dot)faggioli(at)openliven(dot)com
 *  
 */

package org.onesocialweb.gwt.client.ui.widget.activity;

import java.util.List;

import org.onesocialweb.gwt.client.handler.ActivityButtonHandler;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.service.StreamEvent;
import org.onesocialweb.gwt.service.StreamEvent.Type;
import org.onesocialweb.gwt.service.imp.GwtReplies.RepliesEvent;
import org.onesocialweb.gwt.util.Observer;
import org.onesocialweb.model.activity.ActivityEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class RepliesPanel<T> extends FlowPanel {

		
		// internationalization
		private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);				
		
		private Stream<T> model; // reference to the GwtReplies - the observable model	

		private DefaultTaskInfo task;

		private boolean update = true;
		
		private StyledLabel msg = new StyledLabel("message",
				uiText.NoActivitiesAvailable());
		
		public Stream<T> getModel() {
			return model;
		}
		
		public void setModel(Stream<T> model) {
			this.model = model;
			// Add a listener on the replies model
			this.model.registerEventHandler(new StreamListener());
			// Repaint a first time
			repaint();
		}
		
			
		
		public boolean isUpdating() {
			return update;
		}
		
		public void setUpdating(boolean shouldUpdate) {
			update = shouldUpdate;
		}
		
		
		
		private Widget render(ActivityEntry activityEntry) {				
				
			
			ReplyItemView sa = new ReplyItemView(activityEntry);
				sa.setButtonHandler(new ActivityButtonHandler() {
					public void handleShow(int top, ActivityItemView sa) {
					}

					public void handleHide() {
					}
				});				
				return sa;
		}		
		

		protected void repaint() {
			
			//addNewReplies();
			clear();
			
			if (model.isReady()) {
				List<T> items = getModelItems();
				if (items.size() > 0) {
					// render the items
					for (int i = items.size(); i >0 ; i--) {
						T item = items.get(i-1);
						showReply(item);
					}
				} 
				if (task != null) {
					task.complete("", Status.succes);
				}

			} else {
				if (task == null) {
				//	task = new DefaultTaskInfo(uiText.FetchingActivities(), false);
				//	TaskMonitor.getInstance().addTask(task);
				}
			}
			
		}

		private void showReply(T item) {
			remove(msg);
			Widget w =  render((ActivityEntry)item);
			int index= getWidgetCount();
			if (w != null) {
				insert(w, index);				
			}
		}
		

		private class StreamListener implements Observer<StreamEvent<T>> {

			@Override
			public void handleEvent(StreamEvent<T> event) {
					if ((event instanceof RepliesEvent) && (event.getType().equals(Type.added))) {
						for (T item : event.getItems()) {
							showReply(item);						
						}																							
					}				
					else {
						repaint();
					}			
			}

		}
		
		protected List<T> getModelItems() {
			return model.getItems();
		}
	

}
