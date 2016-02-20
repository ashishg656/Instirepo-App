package com.instirepo.app.activities;

import java.util.ArrayList;

import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.ProductObjectSingle;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CirclePageIndicator;
import com.instirepo.app.widgets.ObservableScrollView;
import com.instirepo.app.widgets.ObservableScrollViewListener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class ProductDetailActivity extends BaseActivity
		implements AppConstants, OnClickListener, ZUrls, ObservableScrollViewListener, AppRequestListener {

	int productId;

	ObservableScrollView scrollView;
	int statusBarHeight, toolbarHeight;
	LinearLayout transparentToolbar;
	FrameLayout appbarContainer;
	View bookImageDividerView;

	ViewPager productImagesViewPager;
	ArrayList<String> productImages;

	ProductObjectSingle mData;
	String url;

	ImagesPagerAdapter adapter;
	ViewPager viewPager;
	int deviceWidth;
	CirclePageIndicator pageIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail_activity_layout);

		productId = getIntent().getExtras().getInt("productid");
		productImages = new ArrayList<>();

		url = getProductDetailUrl + "user_id=" + ZPreferences.getUserProfileID(this) + "&product_id=" + productId;

		setProgressLayoutVariablesAndErrorVariables();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		transparentToolbar = (LinearLayout) findViewById(R.id.faketoolbarshopdetail);
		appbarContainer = (FrameLayout) findViewById(R.id.appbarcontainer);
		scrollView = (ObservableScrollView) findViewById(R.id.postdetailscrollview);
		bookImageDividerView = (View) findViewById(R.id.boomimagivideview);
		viewPager = (ViewPager) findViewById(R.id.postimage);
		pageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator);

		// deviceWidth = getResources().getDisplayMetrics().widthPixels;
		// FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
		// viewPager
		// .getLayoutParams();
		// params.height = deviceWidth;
		// viewPager.setLayoutParams(params);

		scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				try {
					scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} catch (Exception e) {
					scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
				statusBarHeight = getResources().getDisplayMetrics().heightPixels - scrollView.getHeight();
			}
		});

		toolbarHeight = getResources().getDimensionPixelSize(R.dimen.z_toolbar_height);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");

		findViewById(R.id.backbuttonfake).setOnClickListener(this);

		appbarContainer.setTranslationY(-toolbarHeight);

		scrollView.setScrollListnerer(this);

		loadData();
	}

	void loadData() {
		CustomStringRequest req = new CustomStringRequest(Method.GET, url, getProductDetailUrl, this, null);
		ZApplication.getInstance().addToRequestQueue(req, getProductDetailUrl);
	}

	void fillDataInScrollView() {
		hideLoadingLayout();
		hideErrorLayout();

		addImageInArraylistIfNotNull(mData.getImage());
		addImageInArraylistIfNotNull(mData.getImage2());
		addImageInArraylistIfNotNull(mData.getImage3());
		addImageInArraylistIfNotNull(mData.getImage4());
		addImageInArraylistIfNotNull(mData.getImage5());
		addImageInArraylistIfNotNull(mData.getImage6());
		addImageInArraylistIfNotNull(mData.getImage7());
		addImageInArraylistIfNotNull(mData.getImage8());

		adapter = new ImagesPagerAdapter();
		viewPager.setAdapter(adapter);
		pageIndicator.setViewPager(viewPager);
	}

	private void addImageInArraylistIfNotNull(String image) {
		if (image != null && image.length() > 0) {
			productImages.add(image);
		}
	}

	@Override
	public void onScrollStopped() {

	}

	@Override
	public void onScroll(int x, int y, int oldx, int oldy) {
		float trans = y / 3;
		viewPager.setTranslationY(trans);

		toolbarScrollChanges(y, oldy);
	}

	private void toolbarScrollChanges(int y, int oldy) {
		int dy = -1 * (y - oldy);
		int location[] = new int[5];
		bookImageDividerView.getLocationOnScreen(location);
		int actualLocation = location[1] - statusBarHeight;
		if (actualLocation < 0) {
			float trans = appbarContainer.getTranslationY() + dy;
			if (trans > 0)
				trans = 0;
			else if (trans < -toolbarHeight)
				trans = -toolbarHeight;
			appbarContainer.setTranslationY(trans);
		} else {
			if (actualLocation < toolbarHeight) {
				float trans = transparentToolbar.getTranslationY() + dy;
				if (trans > 0)
					trans = 0;
				else if (trans < -toolbarHeight)
					trans = -toolbarHeight;
				transparentToolbar.setTranslationY(trans);
				if (appbarContainer.getTranslationY() > -toolbarHeight) {
					float transAppbar = appbarContainer.getTranslationY() + dy;
					if (transAppbar > 0)
						transAppbar = 0;
					else if (transAppbar < -toolbarHeight)
						transAppbar = -toolbarHeight;
					appbarContainer.setTranslationY(transAppbar);
				}
			} else {
				transparentToolbar.setTranslationY(0);
				appbarContainer.animate().translationY(-toolbarHeight).setDuration(100).start();
			}
		}
	}

	@Override
	public void onRequestStarted(String requestTag) {
		if (requestTag.equals(getProductDetailUrl)) {
			hideErrorLayout();
			showLoadingLayout();
		}
	}

	@Override
	public void onRequestFailed(String requestTag, VolleyError error) {
		if (requestTag.equals(getProductDetailUrl)) {
			try {
				Entry entry = ZApplication.getInstance().getRequestQueue().getCache().get(url);
				String data = new String(entry.data, "UTF-8");
				mData = new Gson().fromJson(data, ProductObjectSingle.class);
				fillDataInScrollView();
			} catch (Exception e) {
				hideLoadingLayout();
				showErrorLayout();
			}
		}
	}

	@Override
	public void onRequestCompleted(String requestTag, String response) {
		if (requestTag.equals(getProductDetailUrl)) {
			mData = new Gson().fromJson(response, ProductObjectSingle.class);
			fillDataInScrollView();
		}
	}

	class ImagesPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return productImages.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = LayoutInflater.from(ProductDetailActivity.this)
					.inflate(R.layout.product_detail_imageview_viewpager, container, false);

			ImageView image = (ImageView) v.findViewById(R.id.productdetailimaheview);
			ImageRequestManager.get(ProductDetailActivity.this).requestImage(ProductDetailActivity.this, image,
					ZApplication.getImageUrl(productImages.get(position)), -1);
			image.setScaleType(ScaleType.CENTER_CROP);

			container.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View v, Object o) {
			return v == o;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.postcategoy:

		// break;
		case R.id.backbuttonfake:
			super.onBackPressed();
			break;

		default:
			break;
		}
	}
}
