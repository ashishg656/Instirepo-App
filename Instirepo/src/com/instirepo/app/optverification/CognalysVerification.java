package com.instirepo.app.optverification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CognalysVerification extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String mobile = intent.getStringExtra("mobilenumber");
		String app_user_id = intent.getStringExtra("app_user_id");
		Toast.makeText(context, mobile, Toast.LENGTH_SHORT).show();
		Toast.makeText(context, app_user_id, Toast.LENGTH_SHORT).show();
	}

}
