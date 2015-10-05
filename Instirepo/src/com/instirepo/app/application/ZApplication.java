package com.instirepo.app.application;

import android.app.Application;

public class ZApplication extends Application {

	static ZApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

	public ZApplication getInstance() {
		if (sInstance == null)
			sInstance = new ZApplication();
		return sInstance;
	}
}
