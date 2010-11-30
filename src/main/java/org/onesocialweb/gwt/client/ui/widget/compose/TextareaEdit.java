package org.onesocialweb.gwt.client.ui.widget.compose;

import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.widget.activity.ActivityItemView;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.model.activity.ActivityEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.HTML;

public class TextareaEdit extends TextareaUpdate{
	
	final ActivityItemView view;
	
	public TextareaEdit(ActivityItemView aiv){
		
		view=aiv;		
		//add logic here to finish updating when ENTER is pressed...
		update.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {

				char keyCode = event.getCharCode();
				int ascii = (int) keyCode;				

				if (ascii == 13) {					
					TextareaEdit.this.removeFromParent();
					view.formatContent(view.getStatusWrapper(),update.getText());
					view.getStatusWrapper().setVisible(true);
					view.getActivity().setTitle(update.getText());
					final OswService service = OswServiceFactory.getService();
					
					final UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
					final DefaultTaskInfo task = new DefaultTaskInfo(uiText.UpdatingStatus(), false);
					TaskMonitor.getInstance().addTask(task);
					
					service.update(view.getActivity(), new RequestCallback<ActivityEntry>() {

						@Override
						public void onFailure() {
							task.complete(uiText.UpdateFailure(), Status.failure);
						}

						@Override
						public void onSuccess(ActivityEntry result) {
							task.complete(uiText.UpdateSuccess(), Status.succes);
						}

					});				
					
				}	
			}
		});
	}	
	

}
