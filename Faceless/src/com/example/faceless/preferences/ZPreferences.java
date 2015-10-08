package com.example.faceless.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ZPreferences {

	private static final String KEY = "faceless.prefs";

	private static final String IS_USER_LOGIN = "is_user_login";
	private static final String USER_ID = "user_id";
	private static final String GCM_TOKEN = "GCM_TOKEN";
	private static final String IS_ADMIN = "is_admin";

	public static void setGcmToken(Context context, String text) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(GCM_TOKEN, text);
		editor.commit();
	}

	public static String getGcmToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(GCM_TOKEN, null);
	}

	public static void setUserId(Context context, String text) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(USER_ID, text);
		editor.commit();
	}

	public static String getUserId(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getString(USER_ID, null);
	}

	public static void setIsUserLogin(Context context, boolean isUserSignUp) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putBoolean(IS_USER_LOGIN, isUserSignUp);
		editor.commit();
	}

	public static boolean isUserLogIn(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getBoolean(IS_USER_LOGIN, false);
	}

	public static void setIsAdmin(Context context, boolean isUserSignUp) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putBoolean(IS_ADMIN, isUserSignUp);
		editor.commit();
	}

	public static boolean isAdmin(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return savedSession.getBoolean(IS_ADMIN, false);
	}

}
