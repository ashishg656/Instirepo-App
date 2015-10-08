package com.bookncart.app.gcm;

import com.bookncart.app.preferences.ZPreferences;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityWatcherService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

			if (ni != null && ni.isConnectedOrConnecting()) {
				if (ZPreferences.isGcmRegistered(context)) {
					ComponentName receiver = new ComponentName(context,
							ConnectivityWatcherService.class);
					context.getPackageManager().setComponentEnabledSetting(
							receiver,
							PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
							PackageManager.DONT_KILL_APP);
				} else {
					Intent intentService = new Intent(context,
							RegistrationIntentService.class);
					context.startService(intentService);
				}
			} else {
			}
		} catch (Exception e) {
		}
	}
}
