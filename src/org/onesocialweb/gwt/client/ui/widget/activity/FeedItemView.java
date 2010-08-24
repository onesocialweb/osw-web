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
 *  Author: luca faggioli 	luca(dot)faggioli(at)openliven(dot)com
 *    
 */
package org.onesocialweb.gwt.client.ui.widget.activity;

import org.onesocialweb.model.activity.ActivityEntry;


public class FeedItemView extends ActivityItemView {


	public FeedItemView(ActivityEntry activity) {
		super(activity);
		
		//prevent users to post comments
		replieswrapper.clear();
		
	}

	
}
