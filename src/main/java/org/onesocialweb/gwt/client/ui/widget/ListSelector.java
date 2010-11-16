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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

public class ListSelector extends FlowPanel {

	private StyledFlowPanel wrapper = new StyledFlowPanel("wrapper");

	private final RosterItem rosterItem;
	private List<String> listed = null;

	public ListSelector(final RosterItem rosterItem) {

		this.rosterItem = rosterItem;

		addStyleName("listselector");
		StyledFlowPanel buttons = new StyledFlowPanel("buttons");
		add(wrapper);
		add(buttons);

		final TextBox input = new TextBox();
		Button add = new Button("Create new list");
		buttons.add(input);
		buttons.add(add);

		// get all available lists for your complete roster
		OswService service = OswServiceFactory.getService();
		Set<String> groups = service.getRoster().getGroups();
		
		

		// try to get all the lists this person is on
		try {
			listed = rosterItem.getGroups();
		} catch (NullPointerException e) {
			listed = new ArrayList<String>();
		}

		// add all lists as checkboxes
		if (groups.size() > 0) {
			for (String list : groups) {
				if (list!=null)
					addCheckbox(list, false);
			}
		}

		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// add a new option to the list and check it
				addCheckbox(input.getText(), true);
				// empty the input field
				input.setText("");
			}
		});

	}

	public void addCheckbox(String list, Boolean value) {

		final CheckBox checkbox = new CheckBox(list);
		StyledFlowPanel fix = new StyledFlowPanel("fix");
		checkbox.addStyleName("checkbox");

		// manage checks and unchecks
		checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				// is the item is checked?
				if (event.getValue() == true
						&& !listed.contains(checkbox.getText()) && checkbox.getText()!=null  && checkbox.getText().length()!=0)  {
					// set the values
					listed.add(checkbox.getText());
					rosterItem.setGroups(listed);
					// disable during processing
					checkbox.setEnabled(false);

					// show task
					final DefaultTaskInfo task = new DefaultTaskInfo(
							"Adding person to the list", false);
					TaskMonitor.getInstance().addTask(task);

					// save new state
					rosterItem.save(new RequestCallback<RosterItem>() {

						@Override
						public void onFailure() {
							// return to original state and notify user
							checkbox.setEnabled(true);
							checkbox.setValue(true);
							task.complete("Could not add person to list.",
									Status.failure);
						}

						@Override
						public void onSuccess(RosterItem result) {
							// enable the checkbox again
							checkbox.setEnabled(true);
							task.complete("", Status.succes);
						}

					});
				} else if (event.getValue() == false
						&& listed.contains(checkbox.getText())) {
					// set the values
					listed.remove(checkbox.getText());
					rosterItem.setGroups(listed);
					// disable during processing
					checkbox.setEnabled(false);

					// show task
					final DefaultTaskInfo task = new DefaultTaskInfo(
							"Removing person from the list", false);
					TaskMonitor.getInstance().addTask(task);

					// save new state
					rosterItem.save(new RequestCallback<RosterItem>() {

						@Override
						public void onFailure() {
							// return to original state and notify user
							checkbox.setEnabled(true);
							checkbox.setValue(true);
							task.complete(
									"Could not remove person from the list.",
									Status.failure);
						}

						@Override
						public void onSuccess(RosterItem result) {
							// enable the checkbox again
							checkbox.setEnabled(true);
							task.complete("", Status.succes);
						}

					});
				}
			}

		});

		// if this person is already on the list make sure the checkbox is
		// checked
		if (listed != null && list.length() > 0 && listed.contains(list)) {
			checkbox.setValue(true);
		} else if (value == true) {
			// if a new checkbox is added automatically check it and fire a
			// change event
			checkbox.setValue(true);
			checkbox.fireEvent(new ValueChangeEvent<Boolean>(true) {
			});
		}
		if  (checkbox.getText()!=null  && checkbox.getText().length()!=0) {
			wrapper.add(checkbox);
			wrapper.add(fix);
		}

	}
}
