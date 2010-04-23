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
package org.onesocialweb.gwt.client.ui.menu;

import java.util.ArrayList;
import java.util.List;

import org.onesocialweb.gwt.client.OswClient;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class MainMenu extends FlowPanel {

	private final List<MenuCommand> commands;
	private final List<Button> buttons = new ArrayList<Button>();

	private FlowPanel wrapper = new FlowPanel();
	private FlowPanel menu = new FlowPanel();
	private Image logo = new Image(OswClient.getInstance().getPreference(
			"theme_folder")
			+ "assets/logo.png");
	private Button selectedButton;

	public MainMenu(List<MenuCommand> commands) {
		// Initialize the command list
		this.commands = commands;

		// Set global style and UI
		wrapper.addStyleName("mainmenu");
		logo.addStyleName("logo");
		wrapper.add(menu);
		repaint();

		// Hide the wrapper and attach it to the DOM so that the menu is
		// initially hidden
		wrapper.setVisible(false);
		RootPanel.get("mainmenuwrapper").add(wrapper);
	}

	public MainMenu() {
		this(new ArrayList<MenuCommand>());
	}

	public void showMenu() {
		wrapper.setVisible(true);
	}

	public void hideMenu() {
		wrapper.setVisible(false);
	}

	public void highlight(MenuCommand command) {
		int index = commands.indexOf(command);
		if (index >= 0) {
			highlight(buttons.get(index));
		}
	}

	public void addCommand(MenuCommand command) {
		commands.add(command);
		repaint();
	}

	public void removeCommand(MenuCommand command) {
		commands.remove(command);
		repaint();
	}

	private void repaint() {
		// First remove all existing content
		menu.clear();

		// Build the new menu
		menu.add(logo);

		// Add the buttons
		buttons.clear();
		for (MenuCommand command : commands) {
			final Button button = new Button(command.getLabel());
			button.addStyleDependentName("mainmenu");
			button.addStyleName("awesome");
			button.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					onButtonClick(button);
				}
			});
			menu.add(button);
			buttons.add(button);
		}
	}

	private void highlight(Button button) {
		// Deselect the current button (if anything was selected)
		if (selectedButton != null) {
			selectedButton.removeStyleName("selected");
		}

		// Select this button
		button.addStyleName("selected");
		selectedButton = button;
	}

	private void onButtonClick(Button button) {
		// We highlight the button
		highlight(button);

		// And trigger its action
		commands.get(buttons.indexOf(button)).execute();
	}
}
