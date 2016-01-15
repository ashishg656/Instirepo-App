package com.instirepo.app.activities;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.NotificationsListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.NotificationsListObject;
import com.instirepo.app.preferences.ZPreferences;

public class NotificationsActivity extends BaseActivity implements ZUrls {

	ListView listView;
	NotificationsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications_activity_layout);

		listView = (ListView) findViewById(R.id.notificatinslist);
		setProgressLayoutVariablesAndErrorVariables();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Notifications");

		loadData();
	}

	private void loadData() {
		showLoadingLayout();
		hideErrorLayout();

		StringRequest req = new StringRequest(Method.POST,
				getNotificationsForUser, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						NotificationsListObject obj = new Gson().fromJson(arg0,
								NotificationsListObject.class);

						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						try {
							Cache cache = ZApplication.getInstance()
									.getRequestQueue().getCache();
							Entry entry = cache.get(getNotificationsForUser);
							String data = new String(entry.data, "UTF-8");
							NotificationsListObject obj = new Gson().fromJson(
									data, NotificationsListObject.class);
							setAdapterData(obj);
						} catch (Exception e) {
							hideLoadingLayout();
							showErrorLayout();
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences
						.getUserProfileID(NotificationsActivity.this));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req,
				getNotificationsForUser);
	}

	protected void setAdapterData(NotificationsListObject obj) {
		hideLoadingLayout();
		hideErrorLayout();

		adapter = new NotificationsListAdapter(obj.getNotifications(), this);
		listView.setAdapter(adapter);
	}

}
