package org.onesocialweb.gwt.client.ui.dialog;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.CustomValidation;
import org.onesocialweb.gwt.client.i18n.CustomValidationMessages;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.ui.widget.ErrorLabel;
import org.onesocialweb.gwt.client.util.Configuration;
import org.onesocialweb.gwt.client.validation.CustomLabelTextAction;

import com.claudiushauptmann.gwt.recaptcha.client.RecaptchaWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationProcessor;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.standard.NotEmptyValidator;
import eu.maydu.gwt.validation.client.validators.strings.EmailValidator;

public class RequestCodeDialog extends AbstractDialog {
	
	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);			
	private Configuration config = (Configuration) GWT.create(Configuration.class);			
	VerticalPanel vpanel1 = new VerticalPanel();	
	private CustomValidationMessages customValidationMessages = new CustomValidationMessages();
	private ValidationProcessor validator = new DefaultValidationProcessor(new CustomValidation(customValidationMessages));	
	private RecaptchaWidget rw;

	
	public RequestCodeDialog() {
						
		//captcha here...
		
		rw = new RecaptchaWidget(config.PublicKey());
		
		Button buttonRequest = new Button(uiText.Request());
		Button buttonCancel = new Button(uiText.Cancel());
		Label label1 = new Label(uiText.EnterYourEmail());
		Label captchaLabel =new Label(uiText.Captcha());
		HorizontalPanel buttoncontainer = new HorizontalPanel();
		
		final TextBox emailTextRegister = new TextBox();
		FlowPanel labelflow = new FlowPanel();
		ErrorLabel emailRegisterError = new ErrorLabel();
		
		ErrorLabel captchaError = new ErrorLabel();
		final TextBox captchaHidden = new TextBox();
		
		labelflow.setStylePrimaryName("tabcontentflowlayout");
				
		buttoncontainer.add(buttonRequest);
		buttoncontainer.add(buttonCancel);	
		buttoncontainer.setStyleName("dialogButtons");
		
		labelflow.add(label1);
		labelflow.add(emailTextRegister);		
		labelflow.add(emailRegisterError);
		labelflow.add(captchaLabel);
		labelflow.add(captchaError);

		
		vpanel1.add(labelflow);		
		vpanel1.add(rw);
		vpanel1.add(buttoncontainer);
		
		
		add(vpanel1);
		
		//addStyleName("attachmentPanel");
		setWidth("230px");
		
		buttonCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromParent();
				hide();
			}
		});
		
		buttonRequest.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {	
				//set the captcha user input to a hidden Textbox so it can be validated with Validators...
				captchaHidden.setText(rw.getResponse());
				boolean success = validator.validate();
				if (success) {
					
					String challenge =rw.getChallenge();
					String response = rw.getResponse();
					String url= "/captcha" + "?recaptcha_challenge_field=" + challenge +"&recaptcha_response_field=" +response;
			    	RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
			    	try {
						Request request2 = builder.sendRequest(null, new RequestCallback() {
					    public void onError(Request request, Throwable exception) {
					       // Couldn't connect to server DO SMTH here!!!
					    }

					    public void onResponseReceived(Request request, Response response) {
					      if (200 == response.getStatusCode()) {
					    	//found the captcha validator and validated succesfully so we proceed with code creation
					       	  String url= OswClient.getInstance().getPreference("email_endpoint") + "?to=" + emailTextRegister.getText();
					    	  RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));

								try {
									Request request2 = builder.sendRequest(null, new RequestCallback() {
								    public void onError(Request request, Throwable exception) {
								       // Couldn't connect to server DO SMTH here!!!
								    }

								    public void onResponseReceived(Request request, Response response) {
								      if (200 == response.getStatusCode()) {
								          // Process the response in response.getText()
								    	  CheckEmailDialog dialog = new CheckEmailDialog();
										  dialog.show();
										  removeFromParent();
										  hide();	
								      } else {
								    	  //this is not working properly								         
								         AlertDialog.getInstance().showDialog(response.getStatusText(), uiText.RegistrationFailure());
								         
								      }
								    }       
								  });
								} catch (RequestException e) {
									AlertDialog.getInstance().showDialog(uiText.CouldntConnect(), uiText.Oops());    
								}
					    	  
					    	  
					      } else {
					        //captcha not valid or some other error
					    	  AlertDialog.getInstance().showDialog(response.getStatusText(), uiText.RegistrationFailure());
					    	  rw.reload();
					      }
					    }       
					  });
					} catch (RequestException e) {
					  // Couldn't connect to server        
					}

				}													   
				}
							
		});
		
		setText(uiText.RequestCodeShort());
		setVisible(false);	
		
		validator.addValidators("Email", new EmailValidator(emailTextRegister)
		.addActionForFailure(new StyleAction("validationFailed"))
		.addActionForFailure(
				new CustomLabelTextAction(emailRegisterError, false)));			
		
		validator.addValidators("Captcha", new NotEmptyValidator(captchaHidden)
		.addActionForFailure(new StyleAction("validationFailed"))
		.addActionForFailure(
				new CustomLabelTextAction(captchaError, false)));			
				
		
				
		
	}
	
	/*private String readPublicKey(){
		String publicKey=null;
		Properties properties = new Properties();
		try {
		    properties.load(new FileInputStream("captcha.properties"));
		    publicKey =properties.getProperty("public.key");
		} catch (IOException e) {
		}
		return publicKey;
	} */
	

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
		

}
