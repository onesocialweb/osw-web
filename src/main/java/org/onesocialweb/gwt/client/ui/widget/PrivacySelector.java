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
package org.onesocialweb.gwt.client.ui.widget;

import java.util.Set;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.Roster;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.util.Observer;

import com.google.gwt.user.client.ui.ListBox;

public class PrivacySelector extends ListBox {

	private String value;
	public static final String EVERYONE = "Everyone";
	public static final String NETWORK = "Anyone on "
			+ OswClient.getInstance().getPreference("xmpp_domain");
	public static final String FRIENDS = "Friends";
	public static final String PRIVATE = "Only me";
	public static final String CUSTOM = "Custom ...";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public PrivacySelector(String fieldname) {

		addStyleName("privacyselector");

		// construct
		final Roster roster = OswServiceFactory.getService().getRoster();
		roster.registerEventHandler(new Observer<RosterEvent>() {

			@Override
			public void handleEvent(RosterEvent event) {
				updateVisibility(roster.getGroups());
			}

		});
		updateVisibility(roster.getGroups());
		setVisibleItemCount(1);

	}

	private void updateVisibility(Set<String> groups) {
		clear();
		addItem(EVERYONE);
		// addItem(NETWORK);
		addItem(FRIENDS);
		addItem(PRIVATE);
		for (String group : groups) {
			addItem("People you tagged with '" + group + "'");
		}
		addItem(CUSTOM);
	}

}
