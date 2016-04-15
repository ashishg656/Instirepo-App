package com.instirepo.app.application;

import java.io.File;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.instirepo.app.R;
import com.instirepo.app.extras.FontsOverride;
import com.instirepo.app.serverApi.NutraBaseImageDecoder;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.support.annotation.NonNull;
import io.fabric.sdk.android.Fabric;

public class ZApplication extends Application {

	static ZApplication sInstance;
	private RequestQueue mRequestQueue;
	public static File cacheDir;
	public static final String IMAGE_DIRECTORY_NAME = "Instirepo";

	public static final String TAG = ZApplication.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		sInstance = this;

		cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "instirepo");
		initImageLoader(getApplicationContext());

		// setFonts("Hind-Light.ttf");

		AnalyticsTrackers.initialize(this);
		AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
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

	public static void initImageLoader(Context context) {
		Options decodingOptions = new Options();

		DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.symphony)
				.decodingOptions(decodingOptions).bitmapConfig(Bitmap.Config.ARGB_8888).build();

		final int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		final int cacheSize = 1024 * 1024 * memClass / 8;

		System.out.println("Memory cache size" + cacheSize);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(5).memoryCacheSize(cacheSize)
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(300)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDecoder(new NutraBaseImageDecoder(true)).denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(options).tasksProcessingOrder(QueueProcessingType.FIFO).build();

		ImageLoader.getInstance().init(config);
	}

	public <T> void addToRequestQueue(Request<T> req, @NonNull String tag) {
		req.setTag(tag);
		getRequestQueue().add(req);
	}

	public static String getBaseUrl() {
		return "http://hola-instirepo.rhcloud.com/app/";
	}

	public static String getEcommerceUrl() {
		return "http://hola-instirepo.rhcloud.com/ecommerce/";
	}

	public static String getTravelUrl() {
		return "http://hola-instirepo.rhcloud.com/ride_share/";
	}

	public static String getImageUrl(String s) {
		return "http://hola-instirepo.rhcloud.com" + s;
	}

	void setFonts(String font) {
		String[] types = { "DEFAULT", "MONOSPACE", "SERIF", "SANS_SERIF" };
		for (int i = 0; i < types.length; i++) {
			FontsOverride.setDefaultFont(getApplicationContext(), types[i], "fonts/" + font);
		}
	}

	public synchronized Tracker getGoogleAnalyticsTracker() {
		AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
		return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
	}

	/***
	 * Tracking screen view
	 *
	 * @param screenName
	 *            screen name to be displayed on GA dashboard
	 */
	public void trackScreenView(String screenName) {
		Tracker t = getGoogleAnalyticsTracker();

		// Set screen name.
		t.setScreenName(screenName);

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		GoogleAnalytics.getInstance(this).dispatchLocalHits();
	}

	/***
	 * Tracking exception
	 *
	 * @param e
	 *            exception to be tracked
	 */
	public void trackException(Exception e) {
		if (e != null) {
			Tracker t = getGoogleAnalyticsTracker();

			t.send(new HitBuilders.ExceptionBuilder()
					.setDescription(
							new StandardExceptionParser(this, null).getDescription(Thread.currentThread().getName(), e))
					.setFatal(false).build());
		}
	}

	/***
	 * Tracking event
	 *
	 * @param category
	 *            event category
	 * @param action
	 *            action of the event
	 * @param label
	 *            label
	 */
	public void trackEvent(String category, String action, String label) {
		Tracker t = getGoogleAnalyticsTracker();

		// Build and send an Event.
		t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
	}
}
