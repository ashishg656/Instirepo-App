package com.instirepo.app.application;


import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.io.File;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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

public class ZApplication extends Application {

	static ZApplication sInstance;
	private RequestQueue mRequestQueue;
	public static File cacheDir;
	public static final String IMAGE_DIRECTORY_NAME = "Instirepo";

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		sInstance = this;

		cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "instirepo");
		initImageLoader(getApplicationContext());

		// setFonts("Hind-Light.ttf");
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

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(tag);
		getRequestQueue().add(req);
	}

	public static String getBaseUrl() {
		return "http://hola-instirepo.rhcloud.com/app/";
	}

	public static String getEcommerceUrl() {
		return "http://hola-instirepo.rhcloud.com/ecommerce/";
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

}