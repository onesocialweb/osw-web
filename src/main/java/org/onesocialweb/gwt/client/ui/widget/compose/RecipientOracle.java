package org.onesocialweb.gwt.client.ui.widget.compose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.Roster;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.util.Observer;
import org.onesocialweb.model.vcard4.Profile;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

public class RecipientOracle extends MultiWordSuggestOracle {

	private static RecipientOracle instance;

	private final Map<String, String> jidTable = new HashMap<String, String>();

	// singleton pattern so we only have one menu
	public static RecipientOracle getInstance() {
		if (instance == null) {
			instance = new RecipientOracle();
		}
		return instance;
	}

	public String getJid(String suggestion) {
		if (jidTable.containsKey(suggestion)) {
			return jidTable.get(suggestion);
		} else {
			return suggestion;
		}
	}

	private RecipientOracle() {

		// Populate the oracle
		updateOracle();

		// Listen to roster changes
		Roster roster = OswClient.getInstance().getService().getRoster();
		roster.registerEventHandler(new RosterObserver());
	}

	private void updateOracle() {
		Roster roster = OswClient.getInstance().getService().getRoster();
		List<RosterItem> rosterItems = roster.getItems();
		for (final RosterItem rosterItem : rosterItems) {
			addRosterItem(rosterItem);
		}
	}

	private void addRosterItem(final RosterItem item) {
		OswClient.getInstance().getService().getProfile(item.getJid(),
				new RequestCallback<Profile>() {

					@Override
					public void onFailure() {

						// if there is no profile show the jid
						add(item.getJid());

					}

					@Override
					public void onSuccess(Profile result) {

						// check for name and otherwise display jid
						String displayName = result.getFullName();

						if (displayName != null && displayName.length() > 0) {
							String displayString = displayName + " <"
									+ item.getJid() + ">";
							jidTable.put(displayString, item.getJid());
							add(displayString);

						} else {
							add(item.getJid());
						}
					}
				});
	}

	private class RosterObserver implements Observer<RosterEvent> {

		@Override
		public void handleEvent(RosterEvent event) {
			if (event.getType().equals(RosterEvent.Type.added)) {
				addRosterItem(event.getItem());
			} else if (event.getType().equals(RosterEvent.Type.refreshed)) {
				updateOracle();
			} else if (event.getType().equals(RosterEvent.Type.removed)) {
				updateOracle();
			}
		}
	}
}
