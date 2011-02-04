package org.onesocialweb.gwt.client.ui.dialog;

import org.onesocialweb.gwt.client.i18n.UserInterfaceText;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CheckEmailDialog extends AbstractDialog {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);			
	private FlowPanel panel= new FlowPanel();
	
	public CheckEmailDialog() {
				
		
		Button buttonOk = new Button(uiText.OK());
		Label label1 = new Label(uiText.CheckEmail());
				
		HorizontalPanel buttonPanel = new HorizontalPanel();										
		
		panel.setStyleName("tabcontentflowlayout");
		
		add(panel);
		panel.add(label1);
		buttonPanel.add(buttonOk);
		buttonPanel.setStyleName("dialogButtons");
		panel.add(buttonPanel);	
	
		setWidth("280px");
		setHeight("175");
		
		
		buttonOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromParent();
				hide();
			}
		});
				
		
		setText(uiText.Thanks());
		setVisible(false);	
		
	}
	
	

	@Override
	protected void onHide() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onShow() {
		setVisible(true);
		center();
		setWidth(Integer.toString(this.getOffsetWidth() - 20));
		
	}
	
	public void addToPanel(Widget w){
		panel.add(w);
	}
		

}
