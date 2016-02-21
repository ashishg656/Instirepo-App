package com.instirepo.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.fragments.CommentsProductsFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.objects.ProductObjectSingle;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CirclePageIndicator;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;
import com.instirepo.app.widgets.ObservableScrollViewListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

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

	TextView name, price, mrp, youSave, warrantyPeriodLeft, description, uploaderName, uploadTime, numberOfSaves,
			numberOfComments, showMore;
	CheckBox billAvailable, warrantyAvailable;
	CircularImageView imageUploader;
	LinearLayout chatWithUser, CallUser;
	ImageView savePostImage;

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
		name = (TextView) findViewById(R.id.productname);
		price = (TextView) findViewById(R.id.productprice);
		mrp = (TextView) findViewById(R.id.productmrp);
		showMore = (TextView) findViewById(R.id.showmore);
		youSave = (TextView) findViewById(R.id.yousave);
		warrantyAvailable = (CheckBox) findViewById(R.id.warrantyavaailable);
		billAvailable = (CheckBox) findViewById(R.id.billavailable);
		warrantyPeriodLeft = (TextView) findViewById(R.id.wanntyleft);
		description = (TextView) findViewById(R.id.desc);
		uploaderName = (TextView) findViewById(R.id.seenbyname);
		imageUploader = (CircularImageView) findViewById(R.id.seenbyimage);
		uploadTime = (TextView) findViewById(R.id.seenbytime);
		chatWithUser = (LinearLayout) findViewById(R.id.chat);
		CallUser = (LinearLayout) findViewById(R.id.call);
		savePostImage = (ImageView) findViewById(R.id.savepostimage);
		numberOfSaves = (TextView) findViewById(R.id.numberofsaves);
		numberOfComments = (TextView) findViewById(R.id.numberofcomments);

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

	@SuppressLint("NewApi")
	void fillDataInScrollView() {
		hideLoadingLayout();
		hideErrorLayout();

		getSupportActionBar().setTitle(mData.getName());

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

		name.setText(mData.getName());
		price.setText(mData.getPrice() + "");
		mrp.setText("MRP : ₹ " + mData.getMrp());
		float save = 100 - ((float) mData.getPrice() / (float) mData.getMrp() * 100.0f);
		youSave.setText("You Save " + ((int) save) + "%");
		description.setText(mData.getDescription());
		uploadTime.setText(TimeUtils.getPostTime(mData.getTime()));
		uploaderName.setText(mData.getUser_name());
		ImageRequestManager.get(this).requestImage(this, imageUploader, mData.getUser_image(), -1);
		mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

		if (mData.isWarranty_availabe()) {
			warrantyAvailable.setChecked(true);
			warrantyAvailable.setText("Warranty Available");
			warrantyPeriodLeft.setText(mData.getWarranty_left());
		} else {
			warrantyAvailable.setText("Warranty Not Available");
			warrantyAvailable.setChecked(false);
			warrantyPeriodLeft.setVisibility(View.GONE);
		}

		billAvailable.setChecked(mData.isBill_availabe());
		if (mData.isBill_availabe()) {
			billAvailable.setText("Bill Available");
		} else {
			billAvailable.setText("Bill Not Available");
		}

		chatWithUser.setOnClickListener(this);
		CallUser.setOnClickListener(this);
		findViewById(R.id.openuserprofilepost).setOnClickListener(this);
		findViewById(R.id.viewcomments).setOnClickListener(this);
		findViewById(R.id.savepostimage).setOnClickListener(this);
		showMore.setOnClickListener(this);

		if (Integer.toString(mData.getUser_id()).equalsIgnoreCase(ZPreferences.getUserProfileID(this))) {
			findViewById(R.id.chatcontaner).setVisibility(View.GONE);
			((TextView) findViewById(R.id.calluplaodtext)).setText("Call uploader");
		}

		numberOfComments.setText(mData.getNumber_of_comments() + " Comments");
		savePostImage.setOnClickListener(this);
		findViewById(R.id.viewcomments).setOnClickListener(this);

		fillDataForSavePostImageSelected();

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			showMore.setVisibility(View.VISIBLE);
			description.setMaxLines(5);
		} else {
			showMore.setVisibility(View.GONE);
			description.setMaxLines(Integer.MAX_VALUE);
		}

		description.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT < 16) {
					description.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					description.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
				if (description.getLineCount() < 5) {
					showMore.setVisibility(View.GONE);
				}
			}
		});
	}

	void fillDataForSavePostImageSelected() {
		numberOfSaves.setText(mData.getNumber_of_likes() + " Saves");

		savePostImage.setSelected(mData.isHas_liked());
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
	public void onBackPressed() {
		Fragment fragmentUserProfile = getSupportFragmentManager()
				.findFragmentByTag(Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG);
		Fragment fragmentComments = getSupportFragmentManager()
				.findFragmentByTag(Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG);
		Fragment fragmentUserProfileOpenedFromCommentsListAdapter = getSupportFragmentManager()
				.findFragmentByTag(Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG_FROM_COMMENT_LIST_ADAPTER);

		if (fragmentUserProfileOpenedFromCommentsListAdapter != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentUserProfileOpenedFromCommentsListAdapter != null
				&& ((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter).fragmentDestroyed) {
			super.onBackPressed();
		} else if (fragmentUserProfile != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfile).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfile)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentComments != null) {
			CommentsProductsFragment frg = (CommentsProductsFragment) fragmentComments;
			if (frg.shouldGoBackOnBackButtonPress())
				super.onBackPressed();
		} else
			super.onBackPressed();
	}

	public void switchToUserProfileViewedByOtherFragment(int userid, String name, String image) {
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putInt("userid", userid);
		bundle.putString("image", image);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentcontainer, UserProfileViewedByOtherFragment.newInstance(bundle),
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
				.addToBackStack(Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG).commit();
	}

	public void switchToCommentsFragment(int productId) {
		Bundle bundle = new Bundle();
		bundle.putInt("postid", productId);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentcontainer, CommentsProductsFragment.newInstance(bundle),
						Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG)
				.addToBackStack("Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG").commit();
	}

	private void saveProductAsImportant() {
		if (mData.isHas_liked()) {
			mData.setHas_liked(false);
			mData.setNumber_of_likes(mData.getNumber_of_likes() - 1);
			showSnackBar("Removed Product From Favourites");
		} else {
			mData.setHas_liked(true);
			mData.setNumber_of_likes(mData.getNumber_of_likes() + 1);
			showSnackBar("Added Product To Favourites List");
		}
		markPostImportantRequestServer();
		fillDataForSavePostImageSelected();
	}

	private void reverseSavePostAsImportant() {
		if (mData.isHas_liked()) {
			mData.setHas_liked(false);
			mData.setNumber_of_likes(mData.getNumber_of_likes() - 1);
		} else {
			mData.setHas_liked(true);
			mData.setNumber_of_likes(mData.getNumber_of_likes() + 1);
		}
		fillDataForSavePostImageSelected();
	}

	public void markPostImportantRequestServer() {
		StringRequest req = new StringRequest(Method.POST, markProductAsFavourite, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				showSnackBar("Some error occured. Check internet connection");
				reverseSavePostAsImportant();
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(ProductDetailActivity.this));
				p.put("product_id", productId + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, markProductAsFavourite);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.openuserprofilepost:
			switchToUserProfileViewedByOtherFragment(mData.getUser_id(), mData.getUser_name(), mData.getUser_image());
			break;
		case R.id.chat:
			openUserChatWithPersonUserActivity(mData.getUser_id(), mData.getUser_name(), mData.getUser_image());
			break;
		case R.id.call:
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + mData.getContact_number()));
			if (intent.resolveActivity(this.getPackageManager()) != null) {
				startActivity(intent);
			}
			break;
		case R.id.viewcomments:
			switchToCommentsFragment(productId);
			break;
		case R.id.savepostimage:
			saveProductAsImportant();
			break;
		case R.id.backbuttonfake:
			super.onBackPressed();
			break;
		case R.id.showmore:
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				if (description.getMaxLines() == 5) {
					description.setMaxLines(Integer.MAX_VALUE);
					showMore.setText("SHOW LESS");
				} else {
					description.setMaxLines(5);
					showMore.setText("SHOW MORE");
				}
			}
			break;
		default:
			break;
		}
	}
}
