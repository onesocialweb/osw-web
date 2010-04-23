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

import java.util.Iterator;
import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.model.relation.Relation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;

public class EditableRelationsList extends FlowPanel {

	// nature
	public static String COLLEAGUE = "http://onesocialweb.org/spec/1.0/relations/nature/colleague";
	public static String FRIEND = "http://onesocialweb.org/spec/1.0/relations/nature/friend";

	// status
	public static String CONFIRMED = "http://onesocialweb.org/spec/1.0/relations/status/confirmed";
	public static String DENIED = "http://onesocialweb.org/spec/1.0/relations/status/denied";
	public static String PENDING = "http://onesocialweb.org/spec/1.0/relations/status/pending";
	public static String REQUEST = "http://onesocialweb.org/spec/1.0/relations/status/request";

	private FlowPanel labels = new FlowPanel();
	private String jid = new String();

	private final String sArray[] = new String[] { "friend", "colleague" };
	private final String vArray[] = new String[] { FRIEND, COLLEAGUE };

	public EditableRelationsList(final String jid) {

		// some basic stuff
		addStyleName("editableList");
		this.jid = jid;
		StyledFlowPanel addPanel = new StyledFlowPanel("addPanel");

		// these are the possible values for relationships with the controls to
		// set them
		final ListBox dropdown = new ListBox();
		Button add = new Button("Add relation");

		// add the values to the combobox
		for (String relationtype : sArray) {
			dropdown.addItem(relationtype);
		}

		// show the relations
		repaint();

		// add various components
		add(labels);
		addPanel.add(dropdown);
		addPanel.add(add);
		add(addPanel);

		// handlers
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				// create the relation
				final Relation relation = OswServiceFactory.getService()
						.getRelationFactory().relation();

				relation.setFrom("alardw@vodafonernd.com");
				relation.setTo(jid);
				relation.setNature(vArray[dropdown.getSelectedIndex()]);
				relation.setStatus(REQUEST);

				OswService service = OswServiceFactory.getService();
				service.setupRelation(relation, new RequestCallback<Object>() {

					@Override
					public void onFailure() {
						System.out.println("Relationship not processed.");
					}

					@Override
					public void onSuccess(Object result) {
						// Show the new relations
						repaint();
						System.out.println("Relationship worked out!");
					}
				});
			}
		});
	}

	private void addRelationshipWidget(String id, String nature, String status,
			String from, String to) {

		// get the user friendly representation of the nature
		String relation = "";
		System.out.println(vArray.length);

		for (int i = 0; i < vArray.length - 1; i++) {
			System.out.println(i);
			System.out.println(vArray[i]);
			System.out.println(nature);

			if (vArray[i].equals(nature)) {
				relation = sArray[i];
			}
		}

		// possible icons for relations
		StyledTooltipImage onewayto = new StyledTooltipImage(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-relation-oneway-to.png", "deletableIcon",
				"Only you set this relation");
		StyledTooltipImage onewayfrom = new StyledTooltipImage(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-relation-oneway-from.png", "confirmIcon",
				"Only the other set this relation");
		StyledTooltipImage twoway = new StyledTooltipImage(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-relation-twoway.png", "deletableIcon",
				"You both confirmed this relation");

		// process various states
		System.out.println(id);
		System.out.println(nature);
		System.out.println(status);
		System.out.println(from);
		System.out.println(to);

		// you originated the request & the request is pending
		if ((to.equals(jid) && status.equals(PENDING))
				|| (to.equals(jid) && status.equals(REQUEST))) {
			DeletableLabel label1 = new DeletableLabel(relation, onewayto);
			labels.add(label1);
			// the other originated and the request is pending
		} else if ((!from.equals(jid) && status.equals(PENDING))
				|| (!from.equals(jid) && status.equals(REQUEST))) {
			ConfirmLabel label2 = new ConfirmLabel(relation, onewayfrom);
			labels.add(label2);
			// you originated, the other declined
		} else if (from.equals(jid) && status.equals(DENIED)) {
			ConfirmLabel label3 = new ConfirmLabel(relation, onewayfrom);
			labels.add(label3);
			// you both confirmed the relationship
		} else if (status.equals(CONFIRMED)) {
			DeletableLabel label4 = new DeletableLabel(relation, twoway);
			labels.add(label4);
		}

	}

	private void repaint() {

		// clear existing labels
		labels.clear();

		// show task
		final DefaultTaskInfo task = new DefaultTaskInfo(
				"Updating your relations with this person", false);
		TaskMonitor.getInstance().addTask(task);

		// get the relations
		OswService service = OswServiceFactory.getService();
		service.getRelations(jid, new RequestCallback<List<Relation>>() {

			@Override
			public void onFailure() {
				task.complete("What a shame, failed to get the relations.",
						Status.failure);
			}

			@Override
			public void onSuccess(List<Relation> result) {

				task.complete("", Status.succes);

				Iterator<Relation> iterator = result.iterator();
				while (iterator.hasNext()) {

					// get the info from the relation
					Relation relation = iterator.next();
					String nature = new String();
					String status = new String();
					String from = new String();
					String to = new String();
					String id = new String();

					// check if values are not empty
					if (relation.getId() != null
							&& relation.getId().length() > 0) {
						id = relation.getId();
					}
					if (relation.getNature() != null
							&& relation.getNature().length() > 0) {
						nature = relation.getNature();
					}
					if (relation.getStatus() != null
							&& relation.getStatus().length() > 0) {
						status = relation.getStatus();
					}
					if (relation.getFrom() != null
							&& relation.getFrom().length() > 0) {
						from = relation.getFrom();
					}
					if (relation.getTo() != null
							&& relation.getTo().length() > 0) {
						to = relation.getTo();
					}

					if (from != null && nature != null && status != null
							&& to != null) {
						addRelationshipWidget(id, nature, status, from, to);
					}
				}
			}

		});
	}

}
