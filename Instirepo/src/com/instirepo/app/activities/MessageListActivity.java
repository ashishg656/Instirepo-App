package com.instirepo.app.activities;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.MessageListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.AddMessageResponseObject;
import com.instirepo.app.objects.MessageListObject;
import com.instirepo.app.objects.MessageListObject.SingleMessage;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.CustomGoogleFloatingActionButton;

public class MessageListActivity extends BaseActivity implements AppConstants,
		ZUrls {

	String personName, personImage, personID;
	CircularImageView personImageView;
	TextView toolbarText;
	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;

	public MessageListAdapter adapter;

	boolean isMoreAllowed, isRequestRunning;
	int nextPage = 1;

	EditText sendMessageEditText;
	CustomGoogleFloatingActionButton sendButton;

	int localId = 0;

	public static MessageListActivity sInstance;
	public static String personIDStatic;

	@Override
	protected void onResume() {
		sInstance = this;
		personIDStatic = personID;
		super.onResume();
	}

	@Override
	protected void onStop() {
		sInstance = null;
		personIDStatic = null;
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		sInstance = null;
		personIDStatic = null;
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list_activity_layout);

		setProgressLayoutVariablesAndErrorVariables();

		personImageView = (CircularImageView) findViewById(R.id.toolbarimagecircular);
		toolbarText = (TextView) findViewById(R.id.toolbartextname);
		recyclerView = (RecyclerView) findViewById(R.id.mesaagelistrecycelr);
		sendMessageEditText = (EditText) findViewById(R.id.sendmessagedittext);
		sendButton = (CustomGoogleFloatingActionButton) findViewById(R.id.sendmessagebfab);

		sendButton.setImageResource(R.drawable.ic_send_grey_fab);

		sendMessageEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().trim().length() == 0) {
					sendButton.setImageResource(R.drawable.ic_send_grey_fab);
				} else {
					sendButton.setImageResource(R.drawable.ic_send_white_fab);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sendMessageEditText.getText().toString().length() != 0) {
					addMessageInRecyclerViewAndClearEditText();
				}
			}
		});

		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, true);
		recyclerView.setLayoutManager(layoutManager);

		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (recyclerView.getAdapter() != null) {
					int lastitem = layoutManager.findLastVisibleItemPosition();
					int totalitems = recyclerView.getAdapter().getItemCount();
					int diff = totalitems - lastitem;
					if (diff < 6 && !isRequestRunning && isMoreAllowed) {
						loadData();
					}
				}
			}
		});

		personName = getIntent().getStringExtra("person_name");
		personImage = getIntent().getStringExtra("person_image");
		personID = getIntent().getExtras().getInt("person_id") + "";
		personIDStatic = personID;

		ImageRequestManager.get(this).requestImage(this, personImageView,
				personImage, -1);
		toolbarText.setText(personName);

		loadData();
	}

	protected void addMessageInRecyclerViewAndClearEditText() {
		localId++;
		adapter.addListItemAtBeginningOfList(sendMessageEditText.getText()
				.toString().trim(), localId);

		sendPostMessageRequestToServer(localId, sendMessageEditText.getText()
				.toString().trim());

		sendMessageEditText.setText("");
		sendButton.setImageResource(R.drawable.ic_send_grey_fab);
	}

	public void sendPostMessageRequestToServer(final int localId2,
			final String messageText) {
		StringRequest req = new StringRequest(Method.POST, addMessageToChats,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						AddMessageResponseObject obj = new Gson().fromJson(
								arg0, AddMessageResponseObject.class);

						adapter.notifyMessageResponseAsCorrectAndAddTickAndServerId(
								obj.getLocal_id(), obj.getServer_id());
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						adapter.notifyThatRequestFailed();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("message", messageText);
				p.put("local_id", localId2 + "");
				p.put("user_id",
						ZPreferences.getUserProfileID(MessageListActivity.this));
				p.put("person_id", personID + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, addMessageToChats);
	}

	private void loadData() {
		isRequestRunning = true;
		if (adapter == null) {
			showLoadingLayout();
			hideErrorLayout();
		}

		final String url = getMessagesForOneChat + "?person_id=" + personID
				+ "&pagenumber=" + nextPage;
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						isRequestRunning = false;

						MessageListObject obj = new Gson().fromJson(res,
								MessageListObject.class);
						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						isRequestRunning = false;
						try {
							Entry entry = ZApplication.getInstance()
									.getRequestQueue().getCache().get(url);
							String data = new String(entry.data, "UTF-8");
							MessageListObject obj = new Gson().fromJson(data,
									MessageListObject.class);
							setAdapterData(obj);
						} catch (Exception e) {
							if (adapter == null) {
								hideLoadingLayout();
								showErrorLayout();
							}
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(MessageListActivity.this));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, url);
	}

	protected void setAdapterData(MessageListObject obj) {
		if (obj.getNext_page() == null) {
			isMoreAllowed = false;
		} else {
			isMoreAllowed = true;
			nextPage = obj.getNext_page();
		}

		if (adapter == null) {
			hideLoadingLayout();
			hideErrorLayout();
			adapter = new MessageListAdapter(obj.getMessages(), isMoreAllowed,
					this);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.addData(isMoreAllowed, obj.getMessages());
		}
	}

	@Override
	public void onBackPressed() {
		if (adapter != null) {
			Intent data = new Intent();
			data.putExtra("person_id", personID);
			for (SingleMessage msg : adapter.mData) {
				if (msg.getServer_id() != null) {
					data.putExtra("last_message", msg.getMessage());
					data.putExtra("time", msg.getTime());
					break;
				}
			}
			setResult(RESULT_OK, data);
			finish();
		} else {
			finish();
		}
	}
}
