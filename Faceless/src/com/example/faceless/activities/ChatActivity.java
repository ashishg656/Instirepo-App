package com.example.faceless.activities;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.adapters.ChatListAdapter;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.objects.ZChatObject;
import com.example.faceless.preferences.ZPreferences;
import com.google.gson.Gson;

public class ChatActivity extends BaseActivity implements RequestTags {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;

	int channelId;
	boolean isMoreAllowed, isRequestRunning;
	Integer nextPage = 1;
	ChatListAdapter adapter;
	EditText messageBox;

	public static boolean isChatActivityResumed;

	@Override
	protected void onResume() {
		isChatActivityResumed = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isChatActivityResumed = false;
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity_layout);

		recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
		messageBox = (EditText) findViewById(R.id.messagetosend);

		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, true);
		recyclerView.setLayoutManager(layoutManager);

		channelId = getIntent().getExtras().getInt("channel_id");

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

		findViewById(R.id.sendbutton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (messageBox.getText().toString().trim().length() > 0) {
					sendMessageToServer();
				}
			}
		});

		loadData();
	}

	protected void sendMessageToServer() {
		final String message = messageBox.getText().toString().trim();
		StringRequest req = new StringRequest(Method.POST,
				ZApplication.getBaseUrl() + "add_chat_message/",
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						ZChatObject.ChatItem item = new ZChatObject().new ChatItem();
						item.setText(message);
						item.setTime("now");
						adapter.addItemToListAtBeggining(item);
						layoutManager.scrollToPosition(0);
						messageBox.setText("");
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						makeToast("Unable to send message.Try again");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("message", message);
				params.put("channel_id", channelId + "");
				params.put("user_profile_id",
						ZPreferences.getUserId(ChatActivity.this));
				return params;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, Z_ADD_CHAT);
	}

	private void loadData() {
		isRequestRunning = true;
		StringRequest req = new StringRequest(Method.POST,
				ZApplication.getBaseUrl() + "get_chats_for_channel/",
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						isRequestRunning = false;
						ZChatObject obj = new Gson().fromJson(res,
								ZChatObject.class);
						nextPage = obj.getNext_page();
						if (obj.getNext_page() == null)
							isMoreAllowed = false;
						else
							isMoreAllowed = true;
						if (adapter == null) {
							adapter = new ChatListAdapter(ChatActivity.this,
									obj.getChats(), isMoreAllowed);
							recyclerView.setAdapter(adapter);
						} else {
							adapter.addItemsToListAtEnd(obj.getChats());
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						isRequestRunning = false;
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("channel_id", channelId + "");
				params.put("pagenumber", nextPage + "");
				return params;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, Z_CHATS);
	}

}
