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
package org.onesocialweb.gwt.client.ui.window;

import static org.onesocialweb.gwt.client.util.FormLayoutHelper.addHTMLLabelRow;
import static org.onesocialweb.gwt.client.util.FormLayoutHelper.addWidgetRow;

import java.util.Date;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.widget.PrivacySelector;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.client.ui.widget.StyledTextBox;
import org.onesocialweb.gwt.client.ui.widget.TooltipPushButton;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.xml.XMLHelper;
import org.onesocialweb.model.vcard4.BirthdayField;
import org.onesocialweb.model.vcard4.FullNameField;
import org.onesocialweb.model.vcard4.GenderField;
import org.onesocialweb.model.vcard4.NoteField;
import org.onesocialweb.model.vcard4.PhotoField;
import org.onesocialweb.model.vcard4.Profile;
import org.onesocialweb.model.vcard4.VCard4Factory;
import org.onesocialweb.model.vcard4.exception.CardinalityException;
import org.onesocialweb.model.vcard4.exception.UnsupportedFieldException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabPanel;

public class PreferencesWindow extends AbstractWindow {

	private String jid;
	private Profile model;

	private TabPanel tabpanel = new TabPanel();
	private FlowPanel account = new FlowPanel();
	private FlowPanel privacy = new FlowPanel();
	private FlowPanel profile = new FlowPanel();

	private boolean IsProfileLoaded;
	private boolean IsAccountLoaded;
	private boolean IsPrivacyLoaded;

	private StyledFlowPanel sectionGeneral;
	private FlexTable general;
	private FlexTable generalEdit;
	private TooltipPushButton buttonEditG;
	private StyledLabel instructionG;

	@Override
	protected void onInit() {

		setStyle("preferencesWindow");
		setWindowTitle("Preferences");

		// TODO get data

		// show data
		composeWindow();
	}

	private void composeWindow() {

		// add components
		tabpanel.add(account, "Account");
		tabpanel.add(profile, "Profile & Contact");
		// tabpanel.add(privacy, "Privacy");
		tabpanel.selectTab(0);

		getContents().add(tabpanel);

		// load first tab
		getAccountPreferences();

		// styles
		account.setStyleName("tabcontentflowlayout");
		profile.setStyleName("tabcontentflowlayout");
		privacy.setStyleName("tabcontentflowlayout");

		tabpanel.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {

				if (event.getSelectedItem() == 0) {
					getAccountPreferences();
				}
				if (event.getSelectedItem() == 1) {
					getProfilePreferences();
				}
				if (event.getSelectedItem() == 2) {
					getPrivacyPreferences();
				}
			}
		});

	}

	private void getProfilePreferences() {

		// Used for dynamically loading the profile tab

		if (!IsProfileLoaded) {
			loadProfile();
			IsProfileLoaded = true;
		} else {
			refreshProfile();
		}
	}

	private void getPrivacyPreferences() {

		// Used for dynamically loading the preferences tab

		if (!IsPrivacyLoaded) {
			loadPrivacy();
			IsPrivacyLoaded = true;
		} else {
			// TODO refresh data
		}
	}

	private void getAccountPreferences() {

		// Used for dynamically loading the account tab

		if (!IsAccountLoaded) {
			loadAccount();
			IsAccountLoaded = true;
		} else {
			// TODO refresh data
		}
	}

	private void loadPrivacy() {

		// Compose privacy tab
		StyledFlowPanel sectionSimple = new StyledFlowPanel("section");

		Button buttonAdvanced = new Button("Advanced");
		buttonAdvanced.addStyleName("sectionedit");

		StyledLabel titleUN = new StyledLabel("grouplabel", "Privacy settings");
		StyledLabel instructionUN = new StyledLabel("instruction",
				"Choose the defaults for who can see and do what.");
		sectionSimple.add(buttonAdvanced);
		sectionSimple.add(titleUN);
		sectionSimple.add(instructionUN);
		DisclosurePanel disclosureStatus = new DisclosurePanel(
				"More options for status updates");

		// Advanced settings status
		FlexTable advancedStatus = new FlexTable();
		PrivacySelector advancedstatus = new PrivacySelector("advancedstatus");
		addWidgetRow(advancedStatus, "See your status updates", advancedstatus,
				"");
		disclosureStatus.add(advancedStatus);

		// Simple privacy settings
		FlexTable simple = new FlexTable();

		simple.addStyleName("privacy");

		PrivacySelector status = new PrivacySelector("status");
		addWidgetRow(simple, "See your posts", status, "");

		PrivacySelector comment = new PrivacySelector("status");
		addWidgetRow(simple, "Comment on your posts", comment, "");

		PrivacySelector profile = new PrivacySelector("profile");
		addWidgetRow(simple, "See your profile information", profile, "");

		PrivacySelector contact = new PrivacySelector("contact");
		addWidgetRow(simple, "See your contact information", contact, "");

		PrivacySelector following = new PrivacySelector("following");
		addWidgetRow(simple, "See people you're following", following, "");

		PrivacySelector friends = new PrivacySelector("friends");
		addWidgetRow(simple, "See your friends", friends, "");

		sectionSimple.add(simple);
		privacy.add(sectionSimple);

	}

	private void loadProfile() {

		// get a handle to the jid
		jid = OswServiceFactory.getService().getUserBareJID();

		// Fetch the new profile
		final DefaultTaskInfo task = new DefaultTaskInfo(
				"Fetching the profile", false);
		TaskMonitor.getInstance().addTask(task);

		OswServiceFactory.getService().getProfile(jid,
				new RequestCallback<Profile>() {

					@Override
					public void onFailure() {
						// no profile available
						task.complete("", Status.succes);
						model = null;
						composeProfile();
					}

					@Override
					public void onSuccess(Profile result) {
						task.complete("", Status.succes);
						model = result;
						composeProfile();
					}

				});
	}

	private void refreshProfile() {
		general.removeAllRows();

		if (model.hasField(PhotoField.NAME)) {
			String avatar = model.getPhotoUri();
			if (avatar != null && avatar.length() > 0)
				addHTMLLabelRow(general, "Avatar url", avatar);
		}

		if (model.hasField(FullNameField.NAME)) {
			String displayname = model.getFullName();
			if (displayname != null && displayname.length() > 0)
				addHTMLLabelRow(general, "Display name", displayname);
		}

		if (model.hasField(BirthdayField.NAME)) {
			Date birthday = model.getBirthday();
			if (birthday != null)
				addHTMLLabelRow(general, "Birthday", birthday.toString());
		}

		if (model.hasField(GenderField.NAME)) {
			GenderField.Type gender = model.getGender();
			if (gender != null)
				addHTMLLabelRow(general, "Gender", gender.toString());
		}

		if (model.hasField(NoteField.NAME)) {
			String bio = model.getNote();
			if (bio != null && bio.length() > 0)
				addHTMLLabelRow(general, "Bio", bio);
		}
	}

	private void composeProfile() {

		// compose profile tab
		sectionGeneral = new StyledFlowPanel("section");
		buttonEditG = new TooltipPushButton(new Image(OswClient.getInstance()
				.getPreference("theme_folder")
				+ "assets/i-edit.png"), "Edit profile fields and visibility");
		buttonEditG.addStyleName("sectionedit");
		StyledLabel titleG = new StyledLabel("grouplabel", "General");
		instructionG = new StyledLabel("instruction",
				"This general information is visible by 'Everyone'.");
		sectionGeneral.add(buttonEditG);
		sectionGeneral.add(titleG);
		// sectionGeneral.add(instructionG);
		this.profile.add(sectionGeneral);

		// Editor version of general information
		generalEdit = new FlexTable();
		generalEdit.addStyleName("edit");

		final StyledTextBox avatarF = new StyledTextBox("", "", "250px");
		addWidgetRow(generalEdit, "Avatar url", avatarF, "Everyone");

		final StyledTextBox displaynameF = new StyledTextBox("", "", "250px");
		addWidgetRow(generalEdit, "Displayname", displaynameF, "Everyone");

		final StyledTextBox birthdayF = new StyledTextBox("", "", "250px");
		addWidgetRow(generalEdit, "Birthday", birthdayF, "Friends");

		final StyledTextBox genderF = new StyledTextBox("", "", "250px");
		addWidgetRow(generalEdit, "Gender", genderF, "Only you");

		final StyledTextBox bioF = new StyledTextBox("", "", "250px");
		addWidgetRow(generalEdit, "Bio", bioF, "Everyone");

		// Text only version of general information
		general = new FlexTable();
		sectionGeneral.add(general);

		// make sure the model is not empty when there is no profile available
		if (model != null) {
			if (model.hasField(PhotoField.NAME)) {
				String avatar = model.getPhotoUri();
				avatarF.setText(avatar);
				if (avatar != null && avatar.length() > 0)
					addHTMLLabelRow(general, "Avatar url", avatar);
			}

			if (model.hasField(FullNameField.NAME)) {
				String displayname = model.getFullName();
				displaynameF.setText(displayname);
				if (displayname != null && displayname.length() > 0)
					addHTMLLabelRow(general, "Display name", displayname);
			}

			// if (model.hasField(BirthdayField.NAME)) {
			// Date birthday = model.getBirthday();
			// birthdayF.setText(birthday.toString());
			// if (birthday != null) addHTMLLabelRow(general, "Birthday",
			// birthday);
			// }

			// if (model.hasField("gender")) {
			// GenderField.Type gender = model.getGender();
			// genderF.setText(gender);
			// if (gender != null && gender.length() > 0)
			// addHTMLLabelRow(general, "Gender", gender);
			// }

			if (model.hasField(NoteField.NAME)) {
				String bio = model.getNote();
				bioF.setText(bio);
				if (bio != null && bio.length() > 0)
					addHTMLLabelRow(general, "Bio", model.getField("note")
							.getValue());
			}
		}

		StyledFlowPanel confirmG = new StyledFlowPanel("confirm");
		Button buttonSaveG = new Button("Save");
		Button buttonCancelG = new Button("Cancel");
		confirmG.add(buttonSaveG);
		confirmG.add(buttonCancelG);

		addWidgetRow(generalEdit, "", confirmG, "");

		// handlers
		buttonEditG.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				sectionGeneral.remove(general);
				sectionGeneral.add(generalEdit);
				buttonEditG.setVisible(false);
				instructionG
						.setHTML("Edit the fields below and define who can see this information.");
			}
		});

		buttonCancelG.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				sectionGeneral.add(general);
				sectionGeneral.remove(generalEdit);
				buttonEditG.setVisible(true);
				instructionG
						.setHTML("This general information is visible by 'Everyone'.");
			}
		});

		buttonSaveG.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				final VCard4Factory profileFactory = OswServiceFactory
						.getService().getProfileFactory();
				final Profile profile = profileFactory.profile();

				try {
					// prepare for updating and check for empty values
					if (avatarF.getText().length() > 0)
						profile.addField(profileFactory.photo(XMLHelper
								.encode(avatarF.getText())));
					if (displaynameF.getText().length() > 0)
						profile.addField(profileFactory.fullname(XMLHelper
								.encode(displaynameF.getText())));
					// if (birthdayF.getText().length() > 0)
					// profile.addField(profileFactory.birthday(birthdayF.getText()));
					// if (genderF.getText().length() > 0)
					// profile.addField(profileFactory.gender(genderF.getText());
					if (bioF.getText().length() > 0)
						profile.addField(profileFactory.note(XMLHelper
								.encode(bioF.getText())));
					// if (utcOffsetF.getText().length() > 0)
					// profile.addField(profileFactory.singularField("utcOffset",
					// utcOffsetF.getText()));
				} catch (CardinalityException e) {

				} catch (UnsupportedFieldException e) {

				}

				OswServiceFactory.getService().setProfile(profile,
						new RequestCallback<Object>() {

							@Override
							public void onFailure() {
								// TODO Auto-generated method stub
							}

							@Override
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								model = profile;
								refreshProfile();
							}
						});

				sectionGeneral.add(general);
				sectionGeneral.remove(generalEdit);
				buttonEditG.setVisible(true);
				sectionGeneral.removeStyleName("edit");

			}
		});
	}

	private void loadAccount() {

		// username
		StyledFlowPanel sectionUsername = new StyledFlowPanel("section");
		FlexTable username = new FlexTable();
		StyledLabel titleUN = new StyledLabel("grouplabel", "Your identity");
		StyledLabel instructionUN = new StyledLabel("instruction",
				"Please note that this identity cannot be changed.");
		sectionUsername.add(titleUN);
		sectionUsername.add(instructionUN);
		sectionUsername.add(username);
		account.add(sectionUsername);

		addHTMLLabelRow(username, "User name", OswServiceFactory.getService()
				.getUserBareJID());

		// change password
		// StyledFlowPanel sectionChangePassword = new
		// StyledFlowPanel("section");
		// FlexTable changePassword = new FlexTable();
		// StyledLabel titlePW = new StyledLabel("grouplabel",
		// "Change password");
		// StyledLabel instructionPW = new StyledLabel("instruction",
		// "Pick a new password below.");
		// sectionChangePassword.add(titlePW);
		// sectionChangePassword.add(instructionPW);
		// sectionChangePassword.add(changePassword);
		// account.add(sectionChangePassword);
		//		
		// addWidgetRow(changePassword, "New password", new PasswordTextBox(),
		// "");
		// addWidgetRow(changePassword, "Confirm password", new
		// PasswordTextBox(), "");
		// addWidgetRow(changePassword, "", new Button("Save"), "");

		// change email
		// StyledFlowPanel sectionChangeEmail = new StyledFlowPanel("section");
		// FlexTable changeEmail = new FlexTable();
		// StyledLabel titleEM = new StyledLabel("grouplabel", "Change email");
		// StyledLabel instructionEM = new StyledLabel("instruction",
		// "Change your email below and confirm the change in the new email inbox.");
		// sectionChangePassword.add(titleEM);
		// sectionChangePassword.add(instructionEM);
		// sectionChangePassword.add(changeEmail);
		// account.add(sectionChangeEmail);
		//		
		// addHTMLLabelRow(changeEmail, "Current email",
		// "alardweisscher@gmail.com");
		// addWidgetRow(changeEmail, "New email", new TextBox(), "");
		// addWidgetRow(changeEmail, "", new Button("Save"), "");
	}

}
