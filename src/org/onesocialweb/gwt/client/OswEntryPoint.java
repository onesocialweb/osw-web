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
package org.onesocialweb.gwt.client;

import org.onesocialweb.gwt.client.ui.window.ContactsWindowFactory;
import org.onesocialweb.gwt.client.ui.window.FeedWindowFactory;
import org.onesocialweb.gwt.client.ui.window.PreferencesWindowFactory;
import org.onesocialweb.gwt.client.ui.window.ProfileWindowFactory;
import org.onesocialweb.gwt.client.ui.window.WindowFactory;

import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class OswEntryPoint implements EntryPoint {

	public void onModuleLoad() {

		// Prepare a few things to get our pattern right
		WindowFactory.getInstance().registerFactory(new ProfileWindowFactory());
		WindowFactory.getInstance().registerFactory(new FeedWindowFactory());
		WindowFactory.getInstance().registerFactory(
				new PreferencesWindowFactory());
		WindowFactory.getInstance()
				.registerFactory(new ContactsWindowFactory());

		// Hide logger before login
		FlowPanel hideLogger = new FlowPanel();
		hideLogger.addStyleName("hidelogger");
		Widget divLogger = Log.getLogger(DivLogger.class).getWidget();
		hideLogger.add(divLogger);

		// Start the application
		OswClient.getInstance().start();
	}

}