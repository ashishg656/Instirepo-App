package com.example.faceless.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.faceless.R;
import com.example.faceless.activities.ChatActivity;
import com.example.faceless.activities.SplashActivity;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

	private static final String TAG = "MyGcmListenerService";

	@Override
	public void onMessageReceived(String from, Bundle data) {
		String message = data.getString("message");
		Log.d(TAG, "From: " + from);
		Log.d(TAG, "Message: " + message);

		// if (from.startsWith("/topics/")) {
		// // message received from some topic.
		// } else {
		// // normal downstream message.
		// }

		try {
			if (ChatActivity.isChatActivityResumed) {
				Log.w("as", "chat activity resumes");
			} else {
				Log.w("as", "chat activity not resumes");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sendNotification(message);
	}

	private void sendNotification(String message) {
		Intent intent = new Intent(this, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("GCM Message").setContentText(message)
				.setAutoCancel(true).setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0, notificationBuilder.build());
	}
}
