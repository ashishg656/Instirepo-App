package com.example.faceless.application;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ZApplication extends Application {

	static ZApplication sInstance;
	private RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

	public static String getBaseUrl() {
		return "http://instirepo-instirepo.rhcloud.com/faceless/";
	}

	public static synchronized ZApplication getInstance() {
		if (sInstance == null)
			sInstance = new ZApplication();
		return sInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, @NonNull String tag) {
		req.setTag(tag);
		getRequestQueue().add(req);
	}
}
