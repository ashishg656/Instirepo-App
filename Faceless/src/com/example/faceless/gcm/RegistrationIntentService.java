/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.faceless.gcm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.CommonLib;
import com.example.faceless.preferences.ZPreferences;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

public class RegistrationIntentService extends IntentService {

	private static final String TAG = "RegIntentService";
	private static final String[] TOPICS = { "global" };

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

			subscribeTopics(token);
		} catch (Exception e) {
		}
		Intent registrationComplete = new Intent(CommonLib.GCM_TAG);
		LocalBroadcastManager.getInstance(this).sendBroadcast(
				registrationComplete);
	}

	private void sendRegistrationToServer(String token) {
		ZPreferences.setGcmToken(this, token);
		StringRequest req = new StringRequest(Method.POST,
				ZApplication.getBaseUrl() + "add_gcm_token/",
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
				p.put("gcm_token", ZPreferences
						.getGcmToken(RegistrationIntentService.this));
				p.put("user_profile_id",
						ZPreferences.getUserId(RegistrationIntentService.this));
				TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				p.put("device_id", tManager.getDeviceId());
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, "gcm");
	}

	private void subscribeTopics(String token) throws IOException {
		GcmPubSub pubSub = GcmPubSub.getInstance(this);
		for (String topic : TOPICS) {
			pubSub.subscribe(token, "/topics/" + topic, null);
		}
	}
}
