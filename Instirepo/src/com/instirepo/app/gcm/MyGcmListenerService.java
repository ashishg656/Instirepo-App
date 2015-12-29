package com.instirepo.app.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.instirepo.app.R;
import com.instirepo.app.activities.MessageListActivity;
import com.instirepo.app.activities.SplashActivity;

public class MyGcmListenerService extends GcmListenerService {

	private static final String TAG = "MyGcmListenerService";

	@Override
	public void onMessageReceived(String from, Bundle data) {
		final String message = data.getString("message");
		String senderIdStr = data.getString("sender_id");
		int senderId = Integer.parseInt(senderIdStr);
		final String messageIdStr = data.getString("id");
		final int messageId = Integer.parseInt(messageIdStr);
		String senderName = data.getString("sender_name");

		if (MessageListActivity.sInstance != null
				&& MessageListActivity.personIDStatic != null
				&& MessageListActivity.personIDStatic.equals(senderId + "")
				&& MessageListActivity.sInstance.adapter != null) {
			MessageListActivity.sInstance.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					MessageListActivity.sInstance.adapter
							.addDataAtBegginingWhenReceicedThroughGCM(message,
									messageId);
				}
			});
		} else {
			sendNotification(message);
		}
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

		notificationManager.notify(0 /* ID of notification */,
				notificationBuilder.build());
	}
}
