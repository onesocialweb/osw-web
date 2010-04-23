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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onesocialweb.gwt.client.handler.LoginHandler;
import org.onesocialweb.gwt.client.ui.application.AbstractApplication;
import org.onesocialweb.gwt.client.ui.application.ActivityApplication;
import org.onesocialweb.gwt.client.ui.application.ContactsApplication;
import org.onesocialweb.gwt.client.ui.application.PreferencesApplication;
import org.onesocialweb.gwt.client.ui.dialog.LoginDialog;
import org.onesocialweb.gwt.client.ui.menu.MainMenu;
import org.onesocialweb.gwt.client.ui.menu.MenuCommand;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;

import com.google.code.gwt.storage.client.Storage;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.History;

public class OswClient {

	private static OswClient instance;

	private Map<String, String> preferences = new HashMap<String, String>();

	private OswService service;
	private MainMenu mainMenu;
	private AbstractApplication currentApplication;
	private HashMap<String, MenuCommand> applicationCommands = new HashMap<String, MenuCommand>();
	private boolean sessionActive;

	public static OswClient getInstance() {
		if (instance == null) {
			instance = new OswClient();
		}
		return instance;
	}

	public void start() {
		// Prepare the environment
		this.service = OswServiceFactory.getService();
		loadPreferences();

		// Start the XMPP connection
		service.setup(getPreference("bosh_path"), getPreference("bosh_host"),
				getPreference("xmpp_domain"));

		// Prepare the history manager (for browser history support)
		History.addValueChangeHandler(new HistoryEventHandler());
		History.fireCurrentHistoryState();

		// Check local storage for username & password
		if (Storage.isSupported()) {
			Storage localStorage = Storage.getLocalStorage();
			String username = localStorage.getItem("username");
			String password = localStorage.getItem("password");
			if (username != null && password != null) {
				// if present automatically login
				login(username, password);
				return;
			}
		}

		// Could not login based on stored credentials,
		// so we show the login dialog
		if (!sessionActive) {
			showLogin();
		}
	}

	public OswService getService() {
		return service;
	}

	public String getPreference(String key) {
		return preferences.get(key);
	}

	public String getPreference(String key, String defaultValue) {
		String result = getPreference(key);
		if (result != null) {
			return result;
		} else {
			return defaultValue;
		}
	}

	public void showApplication(String application) {

		// remove the current application
		if (currentApplication != null) {
			currentApplication.destroy();
			currentApplication = null;
		}

		// show the application
		// TODO Will be refactored with a factory or other form of dependency
		// injection
		if (application.equals("ActivityApplication")) {
			ActivityApplication activityApplication = new ActivityApplication();
			currentApplication = activityApplication;
		} else if (application.equals("PreferencesApplication")) {
			PreferencesApplication preferencesApplication = new PreferencesApplication();
			currentApplication = preferencesApplication;
		} else if (application.equals("ContactsApplication")) {
			ContactsApplication contactsApplication = new ContactsApplication();
			currentApplication = contactsApplication;
		}

		// Initialized and show the application
		currentApplication.init();
		currentApplication.show();

		// Inform the menu that we changed
		if (applicationCommands.containsKey(application)) {
			mainMenu.highlight(applicationCommands.get(application));
		}

		// set the application history
		History.newItem(application, false);

	}

	public AbstractApplication getCurrentApplication() {
		return currentApplication;
	}

	public void logout() {
		service.logout(new RequestCallback<Object>() {
			@Override
			public void onFailure() {
				destroySession();
			}

			@Override
			public void onSuccess(Object result) {
				destroySession();
			}
		});
	}

	private void showLogin() {
		// show login box and add a handler for success
		LoginDialog.getInstance().showDialog(new LoginHandler() {
			public void handleLogin(boolean success) {
				if (success) {
					LoginDialog.getInstance().hide();
					createSession();
				}
			}
		});
	}

	private void login(String username, String password) {
		OswServiceFactory.getService().login(username, password,
				new RequestCallback<Object>() {
					@Override
					public void onFailure() {
						showLogin();
					}

					@Override
					public void onSuccess(Object result) {
						createSession();
					}
				});
	}

	private void createSession() {
		sessionActive = true;

		// create a new mainmenu
		initMenu();

		// fire the first application
		showApplication("ActivityApplication");
	}

	private void destroySession() {
		sessionActive = false;
		// make sure to kill all apps
		killApplications();

		// hide the menu
		mainMenu.hideMenu();

		// clear the local storage
		Storage.getLocalStorage().clear();

		// show login dialog
		showLogin();
	}

	private void killApplications() {
		currentApplication.destroy();
		currentApplication = null;
	}

	private void initMenu() {

		List<MenuCommand> commands = new ArrayList<MenuCommand>();

		MenuCommand command = new ActivityAppCommand();
		applicationCommands.put("ActivityApplication", command);
		commands.add(command);

		command = new ContactsAppCommand();
		applicationCommands.put("ContactsApplication", command);
		commands.add(command);

		command = new PreferencesAppCommand();
		applicationCommands.put("PreferencesApplication", command);
		commands.add(command);

		command = new SignOutCommand();
		commands.add(command);

		mainMenu = new MainMenu(commands);
		mainMenu.showMenu();
	}

	private void loadPreferences() {
		Dictionary prefs = Dictionary.getDictionary("preferences");
		for (String key : prefs.keySet()) {
			preferences.put(key, prefs.get(key));
		}
	}

	// Private constructor to enforce the singleton
	private OswClient() {
		//
	}

	private class HistoryEventHandler implements ValueChangeHandler<String> {

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			if (sessionActive) {
				showApplication(event.getValue());
			}
		}

	}

	private class ActivityAppCommand implements MenuCommand {

		@Override
		public String getLabel() {
			return "What's up";
		}

		@Override
		public void execute() {
			showApplication("ActivityApplication");
		}

	}

	private class ContactsAppCommand implements MenuCommand {

		@Override
		public String getLabel() {
			return "Contacts";
		}

		@Override
		public void execute() {
			showApplication("ContactsApplication");
		}

	}

	private class PreferencesAppCommand implements MenuCommand {

		@Override
		public String getLabel() {
			return "Preferences";
		}

		@Override
		public void execute() {
			showApplication("PreferencesApplication");
		}

	}

	private class SignOutCommand implements MenuCommand {

		@Override
		public String getLabel() {
			return "Sign out";
		}

		@Override
		public void execute() {
			logout();
		}

	}

}
