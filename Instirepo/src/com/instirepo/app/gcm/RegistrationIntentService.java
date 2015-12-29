package com.instirepo.app.gcm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import serverApi.CommonLib;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.preferences.ZPreferences;

public class RegistrationIntentService extends IntentService implements ZUrls {

	private static final String TAG = "RegIntentService";

	public RegistrationIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			InstanceID instanceID = InstanceID.getInstance(this);
			String token = instanceID.getToken(CommonLib.GCM_SENDER_ID,
					GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

			sendRegistrationToServer(token);

			// subscribeTopics(token);
		} catch (Exception e) {
		}
	}

	private void sendRegistrationToServer(final String token) {
		StringRequest req = new StringRequest(Method.POST, addGCMTokenToServer,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("token", token);
				p.put("user_id", ZPreferences
						.getUserProfileID(RegistrationIntentService.this));
				TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				p.put("device_id", tManager.getDeviceId());
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, "GCMREGISTER");
	}

	private void subscribeTopics(String token) throws IOException {
		// GcmPubSub pubSub = GcmPubSub.getInstance(this);
		// for (String topic : TOPICS) {
		// pubSub.subscribe(token, "/topics/" + topic, null);
		// }
	}
}
