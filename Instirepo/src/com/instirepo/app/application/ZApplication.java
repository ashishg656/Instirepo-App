package com.instirepo.app.application;

import java.io.File;

import android.app.Application;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ZApplication extends Application {

	static ZApplication sInstance;
	private RequestQueue mRequestQueue;
	public static File cacheDir;
	public static final String IMAGE_DIRECTORY_NAME = "Instirepo";

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;

		// cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
		// "Faceless");
		// initImageLoader(getApplicationContext());
	}

	public synchronized static ZApplication getInstance() {
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

	public static String getBaseUrl() {
		return "http://django-instirepo.rhcloud.com/app/";
	}

	public static String getImageUrl(String imageUrl) {
		return "" + imageUrl;
	}
}
