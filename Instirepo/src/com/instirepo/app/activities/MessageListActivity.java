package com.instirepo.app.activities;

import java.util.HashMap;
import java.util.Map;

import serverApi.ImageRequestManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.MessageListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.MessageListObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CircularImageView;

public class MessageListActivity extends BaseActivity implements AppConstants,
		ZUrls {

	String personName, personImage, personID;
	CircularImageView personImageView;
	TextView toolbarText;
	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;

	MessageListAdapter adapter;

	boolean isMoreAllowed, isRequestRunning;
	int nextPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list_activity_layout);

		setProgressLayoutVariablesAndErrorVariables();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		personImageView = (CircularImageView) findViewById(R.id.toolbarimagecircular);
		toolbarText = (TextView) findViewById(R.id.toolbartextname);
		recyclerView = (RecyclerView) findViewById(R.id.mesaagelistrecycelr);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, true);
		recyclerView.setLayoutManager(layoutManager);

		personName = getIntent().getStringExtra("person_name");
		personImage = getIntent().getStringExtra("person_image");
		personID = getIntent().getExtras().getInt("person_id") + "";

		ImageRequestManager.get(this).requestImage(this, personImageView,
				personImage, -1);
		toolbarText.setText(personName);

		loadData();
	}

	private void loadData() {
		isRequestRunning = true;
		if (adapter == null) {
			showLoadingLayout();
			hideErrorLayout();
		}

		String url = getMessagesForOneChat + "?person_id=" + personID
				+ "&pagenumber=" + nextPage;
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						hideLoadingLayout();
						hideErrorLayout();
						isRequestRunning = false;

						MessageListObject obj = new Gson().fromJson(res,
								MessageListObject.class);
						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						isRequestRunning = false;
						if (adapter == null) {
							showErrorLayout();
							hideLoadingLayout();
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
			adapter = new MessageListAdapter(obj.getMessages(), isMoreAllowed,
					this);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.addData(isMoreAllowed, obj.getMessages());
		}
	}
}
