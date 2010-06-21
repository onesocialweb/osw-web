package org.onesocialweb.gwt.client.ui.widget.compose;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.client.widget.BulletList;
import org.appfuse.client.widget.ListItem;
import org.appfuse.client.widget.Paragraph;
import org.appfuse.client.widget.Span;
import org.onesocialweb.gwt.client.ui.event.ComponentHelper;
import org.onesocialweb.gwt.client.ui.event.ComponentListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

public class RecipientList extends Composite {

	private final RecipientOracle oracle = RecipientOracle.getInstance();

	private final TextBox itemBox = new TextBox();

	private final SuggestBox box = new SuggestBox(oracle, itemBox);

	private List<String> itemsSelected = new ArrayList<String>();
	
	private final ComponentHelper componentHelper = new ComponentHelper();

	public RecipientList() {
		FlowPanel panel = new FlowPanel();
		initWidget(panel);
		// 2. Show the following element structure and set the last <div> to
		// display: block
		/*
		 * <ul class="token-input-list-facebook"> <li
		 * class="token-input-input-token-facebook"> <input type="text"style=
		 * "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"
		 * /> </li> </ul> <div class="token-input-dropdown-facebook"
		 * style="display: none;"/>
		 */
		final Label grow = new Label();
		panel.add(grow);
		grow.addStyleName("grow");
		final BulletList list = new BulletList();
		list.setStyleName("token-input-list-facebook");
		final ListItem item = new ListItem();
		item.setStyleName("token-input-input-token-facebook");
		itemBox
				.getElement()
				.setAttribute(
						"style",
						"outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		box.getElement().setId("suggestion_box");
		item.add(box);
		list.add(item);

		// this needs to be on the itemBox rather than box, or backspace will
		// get executed twice
		itemBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					// only allow manual entries with @ signs (assumed email
					// addresses)
					if (itemBox.getValue().contains("@"))
						deselectItem(itemBox, list);
				}
				// handle backspace
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
					if ("".equals(itemBox.getValue().trim())) {
						ListItem li = (ListItem) list.getWidget(list
								.getWidgetCount() - 2);
						Paragraph p = (Paragraph) li.getWidget(0);
						if (itemsSelected.contains(p.getText())) {
							itemsSelected.remove(p.getText());
							GWT.log("Removing selected item '" + p.getText()
									+ "'", null);
							GWT.log("Remaining: " + itemsSelected, null);
						}
						list.remove(li);
						itemBox.setFocus(true);
						fireComponentResized();
					}
				}

			}
		});

		// also capture separators like , and semicolon to trigger
		itemBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {

				char keyCode = event.getCharCode();
				char comma = ',';
				char semicolon = ';';
				char space = ' ';

				if (keyCode == comma || keyCode == semicolon
						|| keyCode == space) {
					// only allow manual entries with @ signs (assumed email
					// addresses)
					if (itemBox.getValue().contains("@")) {
						deselectItem(itemBox, list);
					}

					// stop these characters from appearing in the textbox
					event.stopPropagation();
					event.preventDefault();

				}

				// grow the size of the input box
				grow.setText(itemBox.getText());

			}
		});

		// used for growing the text input
		itemBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {

				// grow the size of the input box
				grow.setText(itemBox.getText());
				Integer width = grow.getOffsetWidth() + 25;
				itemBox.setWidth(width.toString());

			}
		});

		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent selectionEvent) {
				deselectItem(itemBox, list);
			}
		});

		panel.add(list);

		panel.getElement().setAttribute("onclick",
				"document.getElementById('suggestion_box').focus()");
		box.setFocus(true);
		/*
		 * Div structure after a few elements have been added: <ul
		 * class="token-input-list-facebook"> <li
		 * class="token-input-token-facebook"> <p>What's New Scooby-Doo?</p>
		 * <span class="token-input-delete-token-facebook">x</span> </li> <li
		 * class="token-input-token-facebook"> <p>Fear Factor</p> <span
		 * class="token-input-delete-token-facebook">x</span> </li> <li
		 * class="token-input-input-token-facebook"> <input type="text"style=
		 * "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"
		 * /> </li> </ul>
		 */
	}
	
	public void addComponentListener(ComponentListener listener) {
		componentHelper.addComponentListener(listener);
	}

	public void removeComponentListener(ComponentListener listener) {
		componentHelper.removeComponentListener(listener);
	}

	public void setFocus() {
		box.setFocus(true);
	}

	public List<String> getRecipients() {
		List<String> result = new ArrayList<String>();
		for (String recipient : itemsSelected) {
			result.add(oracle.getJid(recipient));
		}
		return result;
	}
	
	protected void fireComponentResized() {
		componentHelper.fireComponentResized(this);
	}

	protected void fireComponentShown() {
		componentHelper.fireComponentShown(this);
	}

	protected void fireComponentHidden() {
		componentHelper.fireComponentHidden(this);
	}

	protected void fireComponentMoved() {
		componentHelper.fireComponentMoved(this);
	}

	private void deselectItem(final TextBox itemBox, final BulletList list) {
		if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {
			/**
			 * Change to the following structure: <li class="token-input-token-facebook">
			 * <p>
			 * What's New Scooby-Doo?
			 * </p>
			 * <span class="token-input-delete-token-facebook">x</span></li>
			 */

			final ListItem displayItem = new ListItem();
			displayItem.setStyleName("token-input-token-facebook");
			Paragraph p = new Paragraph(itemBox.getValue());

			displayItem.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent clickEvent) {
					displayItem
							.addStyleName("token-input-selected-token-facebook");
				}
			});

			/**
			 * TODO: Figure out how to select item and allow deleting with
			 * backspace key displayItem.addKeyDownHandler(new KeyDownHandler()
			 * { public void onKeyDown(KeyDownEvent event) { if
			 * (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
			 * removeListItem(displayItem, list); } } });
			 * displayItem.addBlurHandler(new BlurHandler() { public void
			 * onBlur(BlurEvent blurEvent) { displayItem.removeStyleName(
			 * "token-input-selected-token-facebook"); } });
			 */

			Span span = new Span("x");
			span.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent clickEvent) {
					removeListItem(displayItem, list);
				}
			});

			displayItem.add(p);
			displayItem.add(span);
			// hold the original value of the item selected

			GWT.log("Adding selected item '" + itemBox.getValue() + "'", null);
			itemsSelected.add(itemBox.getValue());
			GWT.log("Total: " + itemsSelected, null);

			list.insert(displayItem, list.getWidgetCount() - 1);
			itemBox.setValue("");
			itemBox.setFocus(true);
			
			// notify the container that the List has changed
			fireComponentResized();
		}
	}

	private void removeListItem(ListItem displayItem, BulletList list) {
		GWT.log("Removing: "
				+ displayItem.getWidget(0).getElement().getInnerHTML(), null);
		itemsSelected.remove(displayItem.getWidget(0).getElement()
				.getInnerHTML());
		list.remove(displayItem);
		
		fireComponentResized();
	}

}
