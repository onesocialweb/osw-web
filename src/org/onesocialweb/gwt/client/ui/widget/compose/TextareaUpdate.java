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
package org.onesocialweb.gwt.client.ui.widget.compose;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;

public class TextareaUpdate extends FlowPanel {

	private TextArea update = new TextArea();

	// private SuggestBox suggest = new SuggestBox();

	public TextareaUpdate() {

		// compose widget
		add(update);
		// add(suggest);

		// suggest = new
		// SuggestBox(RecipientOracle.getInstance().getSuggestions());

		// style
		update.addStyleName("textareaUpdate");

		// handlers
		// Listen for keyboard events in the status update
		update.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {

				char keyCode = event.getCharCode();
				char at = '@';

				if (keyCode == at) {

				}
			}
		});

	}

	public void setText(String text) {
		update.setText(text);
	}

	public String getText() {
		return update.getText();
	}

}
