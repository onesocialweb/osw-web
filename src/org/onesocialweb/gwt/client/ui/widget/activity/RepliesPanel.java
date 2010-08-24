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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.onesocialweb.gwt.client.handler.ActivityButtonHandler;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.model.activity.ActivityEntry;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

public class RepliesPanel extends AbstractActivityPanel<ActivityEntry> {

	//private final ActivityButtons buttons = new ActivityButtons();

	//private ActivityItemView lastSelected;

	public RepliesPanel() {		
	}
	
	public void addNewReplies() {
		
		if(model.isReady()) {
			List<ActivityEntry> items = getModelItems();
			List<ActivityEntry> toBeRemoved = new ArrayList<ActivityEntry>();
			for (ActivityEntry item : items) {
				WidgetCollection children = this.getChildren();
				if(children.size() == 0)
					break;
				Iterator it = children.iterator();
				while(it.hasNext()) {
					ReplyItemView riv = (ReplyItemView) it.next();
					if(riv.getActivity().getId().equals(item.getId())) {
						toBeRemoved.add(item);
						break;
					}
				}
			}
			items.removeAll(toBeRemoved);
			for (ActivityEntry item : items) {
			
				Widget w = render(item);
				if (w != null) {
					if(getWidgetCount() == 0) {
						insert(render(item), 0);
					} else {
						insert(render(item), getWidgetCount());
					}
				}
			}
		}
	}
	

	@Override
	protected Widget render(ActivityEntry activityEntry) {
		ActivityItemView sa = new ReplyItemView(activityEntry);
		
		sa.setButtonHandler(new ActivityButtonHandler() {
			public void handleShow(int top, ActivityItemView sa) {
			}

			public void handleHide() {
			}
		});
		
		return sa;
	}

	@Override
	public void repaint() {
		addNewReplies();
	}
	
	
	@Override
	protected void addEmptyModelMessage() {
		//do nothing: we show nothing when the 
		//are no replies for the given activity
	}
	
	@Override
	protected List<ActivityEntry> getModelItems() {
		
		List<ActivityEntry> items = model.getItems();
		Collections.sort(items, new ActivityComparator());
		return items;
		
	}
	
	private class ActivityComparator implements Comparator<ActivityEntry> {

		/*
		 * Compares activities based on the publishing date
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(ActivityEntry activity1, ActivityEntry activity2) {
			return activity1.getPublished().compareTo(activity2.getPublished());
		}

	}
	
	//private Stream<ActivityEntry> oldModel;
	
}
