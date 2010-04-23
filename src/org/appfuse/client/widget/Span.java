package org.appfuse.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;

/**
 * This widget is to create <span> elements in a page.
 */
public class Span extends HTML implements HasText {

	public Span() {
		super(DOM.createElement("span"));
	}

	public Span(String text) {
		this();
		setText(text);
	}
}
