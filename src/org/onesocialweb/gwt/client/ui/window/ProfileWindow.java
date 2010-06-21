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

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.i18n.UserInterfaceText;
import org.onesocialweb.gwt.client.task.DefaultTaskInfo;
import org.onesocialweb.gwt.client.task.TaskMonitor;
import org.onesocialweb.gwt.client.task.TaskInfo.Status;
import org.onesocialweb.gwt.client.ui.application.AbstractApplication;
import org.onesocialweb.gwt.client.ui.dialog.AlertDialog;
import org.onesocialweb.gwt.client.ui.widget.ListSelector;
import org.onesocialweb.gwt.client.ui.widget.StyledFlowPanel;
import org.onesocialweb.gwt.client.ui.widget.StyledLabel;
import org.onesocialweb.gwt.client.ui.widget.TooltipPushButton;
import org.onesocialweb.gwt.client.ui.widget.activity.FeedPanel;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.util.HtmlHelper;
import org.onesocialweb.gwt.util.Observer;
import org.onesocialweb.model.activity.ActivityEntry;
import org.onesocialweb.model.vcard4.BirthdayField;
import org.onesocialweb.model.vcard4.DefaultGenderField;
import org.onesocialweb.model.vcard4.EmailField;
import org.onesocialweb.model.vcard4.FullNameField;
import org.onesocialweb.model.vcard4.GenderField;
import org.onesocialweb.model.vcard4.NameField;
import org.onesocialweb.model.vcard4.NoteField;
import org.onesocialweb.model.vcard4.PhotoField;
import org.onesocialweb.model.vcard4.Profile;
import org.onesocialweb.model.vcard4.TelField;
import org.onesocialweb.model.vcard4.URLField;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.reveregroup.gwt.imagepreloader.FitImage;

public class ProfileWindow extends AbstractWindow {

	// internationalization
	private UserInterfaceText uiText = (UserInterfaceText) GWT.create(UserInterfaceText.class);
	
	private String jid;
	private FlowPanel summaryWrapper = new FlowPanel();
	private FlowPanel summary = new FlowPanel();
	private TabPanel tabpanel = new TabPanel();
	private FlexTable profile = new FlexTable();
	final private StyledFlowPanel followersPanel = new StyledFlowPanel(
			"followers");
	final private StyledFlowPanel followingPanel = new StyledFlowPanel(
			"following");
	private FlowPanel details = new FlowPanel();
	private FlowPanel manage = new FlowPanel();
	private Profile model;

	private boolean IsProfileLoaded;
	private boolean IsManagerLoaded;

	private FlowPanel buttonPanel = new FlowPanel();
	private TooltipPushButton shoutButton = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-shout.png"),
			uiText.ShoutToContact());
	private TooltipPushButton followButton = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-following.png"), uiText.FollowPerson());
	private TooltipPushButton unfollowButton = new TooltipPushButton(new Image(
			OswClient.getInstance().getPreference("theme_folder")
					+ "assets/i-unfollowing.png"), uiText.UnfollowPerson());

	private boolean isFollowing = false;
	private boolean isSignedinUser = false;
	private boolean hasProfile = false;

	private final StyledLabel following = new StyledLabel("following",
			uiText.Following());
	private final StyledLabel tagged = new StyledLabel("tagged", "");

	private RosterEventHandler rosterhandler;
	private RosterItem rosterItem;

	public void setJID(String jid) {

		// setup window
		setStyle("profileWindow");
		setWindowTitle(uiText.Profile());
		enableClose();

		// set profile ID
		this.jid = jid;

		// keep track if this is a profile for the signed in user
		if (jid.equals(OswServiceFactory.getService().getUserBareJID()))
			isSignedinUser = true;

		// Fetch the new profile
		final DefaultTaskInfo task = new DefaultTaskInfo(
				uiText.FetchingProfile(), false);
		TaskMonitor.getInstance().addTask(task);
		OswServiceFactory.getService().getProfile(jid,
				new RequestCallback<Profile>() {

					@Override
					public void onFailure() {

						// TODO: add behaviours for different errors like server
						// not available, etc.

						task.complete("", Status.failure);
						model = null;
						AlertDialog.getInstance().showDialog(
										uiText.AccountUnavailable(),
										uiText.Oops());
						composeWindow();
						hasProfile = false;
					}

					@Override
					public void onSuccess(Profile result) {
						task.complete("", Status.succes);
						model = result;
						composeWindow();
						hasProfile = true;
					}

				});
	}

	@Override
	protected void onDestroy() {
		// make sure to kill the roster event handler
		if (rosterhandler != null) {
			rosterItem.unregisterEventHandler(rosterhandler);
		}
	}

	private void composeWindow() {

		TooltipPushButton message = new TooltipPushButton(new Image(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-msg.png"), uiText.SendPrivateMessage());
		TooltipPushButton chat = new TooltipPushButton(new Image(OswClient
				.getInstance().getPreference("theme_folder")
				+ "assets/i-chat.png"), uiText.ChatWithPerson());
		FlowPanel activities = new FlowPanel();
		FeedPanel activityPanel = new FeedPanel();
		Stream<ActivityEntry> userActivities = OswServiceFactory.getService().getActivities(jid);
		activityPanel.setModel(userActivities);

		// User avatar
		Image avatar = new Image();
		if (model != null) {
			final String avatarUri = model.getPhotoUri();
			if (avatarUri != null && avatarUri.length() > 0) {
				avatar.setUrl(avatarUri);
			} else {
				avatar.setUrl(OswClient.getInstance().getPreference(
						"theme_folder")
						+ "assets/default-avatar-big.png");
			}
		} else {
			avatar.setUrl(OswClient.getInstance().getPreference("theme_folder")
					+ "assets/default-avatar-big.png");
		}
		summaryWrapper.add(avatar);

		// Display name
		StyledFlowPanel displaynamewrapper = new StyledFlowPanel("wrapper");
		StyledLabel displayname = new StyledLabel("displayname", jid);
		displayname.setTitle(jid);
		String fullName;
		if (model != null) {
			fullName = model.getFullName();
			if (fullName != null && fullName.length() > 0) {
				displayname.setText(fullName);
			} else {
				displayname.setText(jid);
			}
		}

		StyledLabel you = new StyledLabel("you", uiText.You());

		displaynamewrapper.add(displayname);
		if (isSignedinUser)
			displaynamewrapper.add(you);
		summary.add(displaynamewrapper);

		// Get tags and following status
		String tags = "";
		OswService service = OswServiceFactory.getService();
		rosterItem = service.getRoster().getItem(jid);
		// make sure there is a rosterItem e.g. in the case it's you
		if (rosterItem != null) {
			for (int i = 0; i < rosterItem.getGroups().size(); i++) {
				String tag = rosterItem.getGroups().get(i);
				if (i < (rosterItem.getGroups().size() - 1)) {
					tags += "" + tag + ", ";
				} else {
					tags += "" + tag + "";
				}
			}
		}

		// Add the generic following icon
		following.setVisible(false);
		summary.add(following);

		// Are we following this person ?
		OswServiceFactory.getService().getSubscriptions(
				OswServiceFactory.getService().getUserBareJID(),
				new RequestCallback<List<String>>() {

					@Override
					public void onFailure() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<String> result) {
						// Show following green arow and label
						if (result.contains(jid)) {
							isFollowing = true;
							following.setVisible(true);
						} else {
							isFollowing = false;
							following.setVisible(false);
						}

						// Add the follow/unfollow button
						addButtons();
					}

				});

		buttonPanel.addStyleName("panel");

		// Bio
		if (model != null) {
			final String bio = model.getNote();
			if (model.getNote() != null) {
				if (model != null && bio != null && bio.length() > 0) {
					StyledLabel aboutme = new StyledLabel("aboutme",
							"<span>" + uiText.Bio() + "</span> " + HtmlHelper.encode(bio));
					summary.add(aboutme);
				}
			}
		}

		// add tags if any
		if (tags.length() > 0) {
			tagged.setHTML("<span>" + uiText.OnYourLists() + "</span> " + HtmlHelper.encode(tags));
		} else {
			tagged.setHTML("");
		}

		// add lists
		summary.add(tagged);

		// add actions
		summary.add(buttonPanel);

		summaryWrapper.add(summary);

		activities.add(activityPanel);

		tabpanel.add(activities, uiText.Activities());
		tabpanel.add(details, uiText.Profile());

		// You don't need to manage yourself
		if (!isSignedinUser)
			tabpanel.add(manage, uiText.ManagePerson());

		tabpanel.selectTab(0);

		getContents().add(summaryWrapper);
		getContents().add(tabpanel);

		// styles
		summaryWrapper.addStyleName("summaryWrapper");
		summary.addStyleName("summary");
		avatar.addStyleName("avatar");
		details.setStyleName("tabcontentflowlayout");
		details.addStyleName("profiletabcontent");
		activities.setStyleName("tabcontentflowlayout");
		manage.addStyleName("tabcontentflowlayout");
		manage.addStyleName("managetabcontent");

		// handlers
		followButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				// temporarily disable the button
				followButton.setEnabled(false);

				OswServiceFactory.getService().subscribe(jid,
						new RequestCallback<Object>() {

							@Override
							public void onFailure() {
								// enable the button again
								followButton.setEnabled(true);
								AlertDialog
										.getInstance()
										.showDialog(
												uiText.AccountUnavailable(), uiText.Oops());
							}

							@Override
							public void onSuccess(Object result) {
								isFollowing = true;

								// the following label
								following.setVisible(true);

								// enable the button again
								followButton.setEnabled(true);
								addButtons();

								// show the manage tab
								manage.setVisible(true);
							}

						});
			}
		});

		unfollowButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				// temporarily disable the button
				unfollowButton.setEnabled(false);

				OswServiceFactory.getService().unsubscribe(jid,
						new RequestCallback<Object>() {

							@Override
							public void onFailure() {
								unfollowButton.setEnabled(true);
								AlertDialog
										.getInstance()
										.showDialog(
												uiText.AccountUnavailable(), uiText.Oops());
							}

							@Override
							public void onSuccess(Object result) {
								isFollowing = false;

								// the following label
								following.setVisible(false);

								// enable the button again
								unfollowButton.setEnabled(true);
								addButtons();

								// show the manage tab
								manage.setVisible(false);
							}

						});
			}
		});

		// tab selection handler
		tabpanel.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() == 1) {
					updateFullProfile();
				}
				if (event.getSelectedItem() == 2) {
					updateManager();
				}
			}
		});

		// Keep track of list changes
		if (rosterItem != null) {
			rosterhandler = new RosterEventHandler();
			rosterItem.registerEventHandler(rosterhandler);
		}
	}

	private void updateFullProfile() {
		if (!IsProfileLoaded) {
			loadFullProfile();
			IsProfileLoaded = true;
		} else {
			// TODO refresh data
		}
	}

	private void updateManager() {
		if (!IsManagerLoaded) {
			loadManager();
			IsManagerLoaded = true;
		} else {
			// TODO refresh data
		}
	}

	private void loadFullProfile() {

		// setup layout

		// profile section
		StyledFlowPanel sectionProfile = new StyledFlowPanel("section");
		StyledLabel profileLabel = new StyledLabel("grouplabel", uiText.Profile());
		StyledLabel profileInstruction = new StyledLabel("instruction", "");
		details.add(sectionProfile);
		sectionProfile.add(profileLabel);
		sectionProfile.add(profileInstruction);
		sectionProfile.add(profile);

		// followers section
		StyledFlowPanel sectionFollowers = new StyledFlowPanel("section");
		StyledLabel followersLabel = new StyledLabel("grouplabel", uiText.Followers());
		final StyledLabel followersInstruction = new StyledLabel("instruction",
				"");
		details.add(sectionFollowers);
		sectionFollowers.add(followersLabel);
		sectionFollowers.add(followersInstruction);
		sectionFollowers.add(followersPanel);

		// following section
		StyledFlowPanel sectionFollowing = new StyledFlowPanel("section");
		StyledLabel followingLabel = new StyledLabel("grouplabel", uiText.FollowingPeople());
		final StyledLabel followingInstruction = new StyledLabel("instruction",
				"");
		details.add(sectionFollowing);
		sectionFollowing.add(followingLabel);
		sectionFollowing.add(followingInstruction);
		sectionFollowing.add(followingPanel);

		profileInstruction.setVisible(false);

		if (model != null && model.getFields().size() > 0) {
			
			// show the profile
			sectionProfile.setVisible(true);
			
			if (model.hasField(FullNameField.NAME)) {
				String displayname = model.getFullName();
				if (displayname != null && displayname.length() > 0)
					addHTMLLabelRow(profile, uiText.DisplayName(), displayname);
			}
			
			if (model.hasField(NameField.NAME)) {
				String name = model.getName();
				if (name != null && name.length() > 0)
					addHTMLLabelRow(profile, uiText.FullName(), name);
			}

			if (model.hasField(BirthdayField.NAME)) {
				Date birthday = model.getBirthday();
				if (birthday != null) {
					DateTimeFormat dtf = DateTimeFormat.getFormat("d MMMM yyyy");
					String bday = dtf.format(birthday);
					addHTMLLabelRow(profile, uiText.Birthday(), bday);
				}	
			}

			if (model.hasField(GenderField.NAME)) {
				
				String value = new String("");
				
				GenderField.Type gender = model.getGender();
				
				if (gender.equals(GenderField.Type.MALE)) {
					value = uiText.Male();
				} else if (gender.equals(GenderField.Type.FEMALE)) {
					value = uiText.Female();
				} else if (gender.equals(GenderField.Type.NOTKNOWN)) {
					value = uiText.NotKnown();
				} else if (gender.equals(GenderField.Type.NOTAPPLICABLE)) {
					value = uiText.NotApplicable();
				}
				if (gender != null)
					addHTMLLabelRow(profile, uiText.Gender(), value);
				
			}

			if (model.hasField(NoteField.NAME)) {
				String bio = model.getNote();
				if (bio != null && bio.length() > 0)
					addHTMLLabelRow(profile, uiText.Bio(), bio);
			}
			
			if (model.hasField(EmailField.NAME)) {
				String email = model.getEmail();
				if (email != null && email.length() > 0)
					addHTMLLabelRow(profile, uiText.Email(), email);
			}
			
			if (model.hasField(TelField.NAME)) {
				String tel = model.getTel();
				if (tel != null && tel.length() > 0)
					addHTMLLabelRow(profile, uiText.Telephone(), tel);
			}
			
			if (model.hasField(URLField.NAME)) {
				String url = model.getUrl();
				if (url != null && url.length() > 0)
					addHTMLLabelRow(profile, uiText.Website(), url);
			}

		} else {
			// if there are no results

			// hide the profile table
			profile.setVisible(false);

			// show message
			String msg = uiText.FailedToGetProfile();
			profileInstruction.setText(msg);
			profileInstruction.setVisible(true);

		}

		// get the followers of this person
		final DefaultTaskInfo task1 = new DefaultTaskInfo(
				uiText.FetchingFollowers(), false);
		TaskMonitor.getInstance().addTask(task1);

		OswServiceFactory.getService().getSubscribers(jid,
				new RequestCallback<List<String>>() {

					@Override
					public void onFailure() {
						// TODO Auto-generated method stub
						task1.complete("", Status.failure);
					}

					@Override
					public void onSuccess(List<String> result) {

						task1.complete("", Status.succes);

						// see if there are any results
						if (result.size() > 0) {
							// sort the list alphabetically
							Collections.sort(result, new Comparator<String>() {

								@Override
								public int compare(String o1, String o2) {
									return o1.compareToIgnoreCase(o2);
								}

							});

							// add all the jids to the list
							for (final String jid : result) {

								Label follower = new Label(jid);
								follower.addStyleName("link");
								follower.setTitle(uiText.ViewProfileOf() + " " + jid);
								followersPanel.add(follower);
								follower.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {

										// get the app instance from the session
										// manager
										AbstractApplication app = OswClient
												.getInstance()
												.getCurrentApplication();
										ProfileWindow profileWindow = (ProfileWindow) app
												.addWindow(ProfileWindow.class
														.toString(), 1);
										profileWindow.setJID(jid);
										profileWindow.show();
									}

								});
							}
						} else {
							// give a message if there are no followers
							followersInstruction
									.setText(uiText.NoFollowers());
						}

					}

				});

		// get the people who are being followed by this person
		final DefaultTaskInfo task2 = new DefaultTaskInfo(
				uiText.FetchingFollowing(), false);
		TaskMonitor.getInstance().addTask(task2);

		OswServiceFactory.getService().getSubscriptions(jid,
				new RequestCallback<List<String>>() {

					@Override
					public void onFailure() {
						// TODO Auto-generated method stub
						task2.complete("", Status.failure);
					}

					@Override
					public void onSuccess(List<String> result) {

						task2.complete("", Status.succes);

						// see if there are any results
						if (result.size() > 0) {
							// sort the list alphabetically
							Collections.sort(result, new Comparator<String>() {

								@Override
								public int compare(String o1, String o2) {
									return o1.compareToIgnoreCase(o2);
								}

							});

							// add all the jids to the list
							for (final String jid : result) {

								Label following = new Label(jid);
								following.addStyleName("link");
								following.setTitle(uiText.ViewProfileOf() + " " + jid);
								followingPanel.add(following);
								following.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {

										// get the app instance from the session
										// manager
										AbstractApplication app = OswClient
												.getInstance()
												.getCurrentApplication();
										ProfileWindow profileWindow = (ProfileWindow) app
												.addWindow(ProfileWindow.class
														.toString(), 1);
										profileWindow.setJID(jid);
										profileWindow.show();
									}

								});
							}
						} else {
							// give a message if no one is followed
							followingInstruction
									.setText(uiText.NoFollowing());
						}

					}

				});

	}

	private void addButtons() {
		// delete existing buttons
		buttonPanel.clear();
		// Add buttons - removed unimplemented buttons
		// buttonPanel.add(message);
		// buttonPanel.add(chat);
		if (isSignedinUser == false)
			buttonPanel.add(shoutButton);

		// check if this person is being followed and show the right actions
		if (isFollowing == true) {
			buttonPanel.add(unfollowButton);
			// if it's the signed in user don't show follow button
		} else if (isSignedinUser == false) {
			buttonPanel.add(followButton);
		}
	}

	private void loadManager() {

		/*
		 * // create the Manage person tab StyledLabel followingLabel = new
		 * StyledLabel("grouplabel", "Following"); StyledLabel
		 * followingInstruction = new StyledLabel("instruction",
		 * "What you are following of this person, company or organisation.");
		 * EditableFollowingList manageFollowing = new EditableFollowingList();
		 * 
		 * manage.add(followingLabel); manage.add(followingInstruction);
		 * manage.add(manageFollowing);
		 */

		// Add the lists
		StyledFlowPanel sectionLists = new StyledFlowPanel("section");
		StyledLabel tagsLabel = new StyledLabel("grouplabel", uiText.YourLists());
		StyledLabel tagsInstruction = new StyledLabel("instruction",
				uiText.AddOrRemovePerson());
		// EditableTagsList manageTags = new EditableTagsList(jid);

		OswService service = OswServiceFactory.getService();
		RosterItem rosterItem = service.getRoster().getItem(jid);

		ListSelector listSelector = new ListSelector(rosterItem);

		sectionLists.add(tagsLabel);
		sectionLists.add(tagsInstruction);
		sectionLists.add(listSelector);

		manage.add(sectionLists);

		// Add the relations
		/*
		 * StyledFlowPanel sectionRelations = new StyledFlowPanel("section");
		 * StyledLabel relationsLabel = new StyledLabel("grouplabel",
		 * "Your relations"); StyledLabel relationsInstruction = new
		 * StyledLabel("instruction",
		 * "Your relations can be confirmed one way or both ways.");
		 * EditableRelationsList manageRelations = new
		 * EditableRelationsList(jid);
		 * 
		 * sectionRelations.add(relationsLabel);
		 * sectionRelations.add(relationsInstruction);
		 * sectionRelations.add(manageRelations);
		 * 
		 * manage.add(sectionRelations);
		 */
	}

	private class RosterEventHandler implements Observer<RosterEvent> {

		@Override
		public void handleEvent(RosterEvent event) {

			// get lists
			String tags = "";
			OswService service = OswServiceFactory.getService();
			RosterItem rosterItem = service.getRoster().getItem(jid);
			// make sure there is a rosterItem e.g. in the case it's you
			if (rosterItem != null) {
				for (int i = 0; i < rosterItem.getGroups().size(); i++) {
					String tag = rosterItem.getGroups().get(i);
					if (i < (rosterItem.getGroups().size() - 1)) {
						tags += "" + tag + ", ";
					} else {
						tags += "" + tag + "";
					}
				}
			}

			// change lists if any
			if (tags.length() > 0) {
				tagged.setHTML("<span>" + uiText.OnYourLists() + "</span>" + tags);
			} else {
				tagged.setHTML("");
			}

		}

	}

}
