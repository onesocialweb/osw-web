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

import org.onesocialweb.gwt.client.OswClient;
import org.onesocialweb.gwt.client.ui.dialog.AlertDialog;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.UpdateCallback;
import org.onesocialweb.gwt.service.UploadStatus;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

public class SingleUploader extends FlowPanel {

	private final FileUpload upload = new FileUpload();
	private final FormPanel form = new FormPanel();
	private final ProgressBar progress = new ProgressBar();
	private final StyledLabel fileLabel = new StyledLabel("filelabel", "");
	private FlowPanel contents = new FlowPanel();
	private Hidden jid = new Hidden();
	private Hidden signature = new Hidden();
	private Hidden requestId = new Hidden();
	private String filename;
	private String fileurl;

	private Boolean isUploading = false;

	public SingleUploader() {

		addStyleName("uploader");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// add the form
		form.setWidget(contents);
		add(form);

		// add the fileupload and hidden fields
		contents.add(upload);
		add(fileLabel);
		add(progress);
		fileLabel.setVisible(false);
		progress.setVisible(true);

		upload.setName("fileId");

		upload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				// check if we have a file
				if (upload.getFilename() != null
						&& upload.getFilename().length() > 0) {

					// TODO check for valid extensions

					// change state to is uploading
					setIsUploading(true);

					// prepare for posting
					String token = getToken();
					signature.setName("signature");
					signature.setValue(token);

					jid.setName("jid");
					jid.setValue(OswServiceFactory.getService()
							.getUserFullJID());

					requestId.setName("requestId");
					requestId.setValue("123456");

					form.setAction(OswClient.getInstance().getPreference(
							"upload_endpoint")
							+ "?"
							+ "requestId=123456&jid="
							+ URL.encode(OswServiceFactory.getService()
									.getUserFullJID()) + "&signature=" + token);

					// and fire in the hole
					form.submit();

					// get basic file information
					filename = upload.getFilename();
					fileLabel.setText(filename);

					// hide upload selector and show progressbar
					upload.setVisible(false);
					progress.setVisible(true);
					fileLabel.setVisible(true);
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// we are going to use the xmpp channel to monitor progress
				// instead

			}
		});
	}

	public String getFileUrl() {
		return fileurl;
	}

	public void reset() {
		upload.setVisible(true);
		progress.setVisible(false);
		fileLabel.setText("");
		fileLabel.setVisible(false);
		form.reset();
	}

	private String getToken() {

		// get a token from the server
		String token = OswServiceFactory.getService().startUpload("123456",
				new UpdateCallback<UploadStatus>() {

					@Override
					public void onError() {
						AlertDialog
								.getInstance()
								.showDialog(
										"Oops, something went wrong with the upload. Please try again.",
										"Upload failed");
					}

					@Override
					public void onUpdate(UploadStatus update) {

						System.out.println(update.getBytesRead());

						// display progress
						if (update.getStatus().equals("progress")) {

							progress.setProgress(update.getBytesRead());

							return;
						}

						// if the file is complete
						if (update.getStatus().equals("completed")) {

							// hide the progressbar
							progress.setVisible(false);

							// get the file size
							fileLabel.setText(fileLabel.getText() + " ("
									+ Math.round(update.getSize() / 1024)
									+ " kB)");

							// process the result
							fileurl = OswClient.getInstance().getPreference(
									"file_retrieval_endpoint")
									+ "?" + "fileId=" + update.getFileId();
							processResult(fileurl);

							// we're no longer uploading
							setIsUploading(false);

							return;
						}

						if (update.getStatus().equals("error")) {

							AlertDialog
									.getInstance()
									.showDialog(
											"Oops, something went wrong with the upload. Please try again.",
											"Upload failed");

							// we're no longer uploading
							setIsUploading(false);

							return;
						}
					}

				});

		return token;
	}

	private void processResult(String filename) {

	}

	public void setIsUploading(Boolean isUploading) {
		this.isUploading = isUploading;
	}

	public Boolean getIsUploading() {
		return isUploading;
	}

}
