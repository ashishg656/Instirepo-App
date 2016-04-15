package com.instirepo.app.activities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.fragments.LaunchScreen1Fragment;
import com.instirepo.app.fragments.LaunchScreen2Fragment;
import com.instirepo.app.fragments.LaunchScreen3Fragment;
import com.instirepo.app.fragments.LaunchScreen4Fragment;
import com.instirepo.app.objects.GoogleLoginObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CirclePageIndicator;
import com.instirepo.app.widgets.LaunchActiviityViewPagerTransformer;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class LaunchActivity extends BaseActivity
		implements OnPageChangeListener, OnClickListener, ZUrls, OnConnectionFailedListener {

	ViewPager viewPager;
	ArgbEvaluator argbEvaluator;
	FrameLayout mainContainerLayout;
	ArrayList<Integer> colors = new ArrayList<Integer>();
	MyPagerAdapter adapter;
	ImageView launchIcon;
	int deviceHeight;
	CirclePageIndicator pageIndicator;
	ImageView gradientBg, skipButtonBg;
	LinearLayout loginButtonsContainerLayout, loginButtonsLayout;
	int loginButtonsLayoutHeight, skipButtonHeight;
	RelativeLayout skipButtonLayout;
	TextView skipButton;
	Button googleLoginButton;
	ProgressDialog progressDialog;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	GoogleApiClient mGoogleApiClient;

	// DATA TO SEND
	String emailToSend, idToSend, imageUrlToSend, nameToSend;

	int deviceWidth;
	float heightOfScreenShotImage;
	float factorofImageTobeshownAlongHeight = 0.5f, factorOfImageToBeTranslatedAlongWidth = 0.08f;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_activity_layout);

		if (!checkPlayServices())
			return;

		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		skipButton = (TextView) findViewById(R.id.launch_skip_text);
		mainContainerLayout = (FrameLayout) findViewById(R.id.launch_activity_main);
		launchIcon = (ImageView) findViewById(R.id.ic_app_icon_launch);
		gradientBg = (ImageView) findViewById(R.id.gradient_bg_launch);
		pageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator);
		skipButtonBg = (ImageView) findViewById(R.id.skip_button_bg);
		loginButtonsContainerLayout = (LinearLayout) findViewById(R.id.indicatorandbuttonslayout);
		loginButtonsLayout = (LinearLayout) findViewById(R.id.login_buttons);
		skipButtonLayout = (RelativeLayout) findViewById(R.id.skipbuttonlayout);
		googleLoginButton = (Button) findViewById(R.id.google_sign_in_button);

		deviceWidth = getResources().getDisplayMetrics().widthPixels;

		progressDialog = new ProgressDialog(this);
		progressDialog.dismiss();

		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Scroller scroller = new Scroller(this, new DecelerateInterpolator(), true);
			mScroller.set(viewPager, scroller);
		} catch (Exception e) {
		}

		loginButtonsLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					mainContainerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					mainContainerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
				loginButtonsLayoutHeight = loginButtonsLayout.getHeight();
				skipButtonHeight = skipButtonLayout.getHeight();
			}
		});

		mainContainerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					mainContainerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					mainContainerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
				deviceHeight = mainContainerLayout.getHeight();
			}
		});

		argbEvaluator = new ArgbEvaluator();
		colors.add(getResources().getColor(R.color.home_viewpager_color_3));
		colors.add(getResources().getColor(R.color.home_viewpager_color_2));
		colors.add(getResources().getColor(R.color.home_viewpager_color_1));
		colors.add(getResources().getColor(R.color.home_viewpager_color_4));

		pageIndicator.setOnPageChangeListener(this);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		pageIndicator.setViewPager(viewPager);

		viewPager.setPageTransformer(false, new LaunchActiviityViewPagerTransformer(this));

		skipButton.setOnClickListener(this);
		googleLoginButton.setOnClickListener(this);

		ZPreferences.setIsUserLogin(this, false);

		initialiseGoogleApiClient();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				makeToast("This device doesn't support Play services, App will not work normally");
				finish();
			}
			return false;
		}
		return true;
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			bundle.putInt("position", pos);
			switch (pos) {
			case 3:
				return LaunchScreen4Fragment.newInstance(bundle);
			case 1:
				return LaunchScreen2Fragment.newInstance(bundle);
			case 2:
				return LaunchScreen3Fragment.newInstance(bundle);
			default:
				return LaunchScreen1Fragment.newInstance(bundle);
			}
		}

		@Override
		public int getCount() {
			return colors.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@SuppressLint("NewApi")
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (position < (adapter.getCount() - 1) && position < (colors.size() - 1)) {
			mainContainerLayout.setBackgroundColor(
					(Integer) argbEvaluator.evaluate(positionOffset, colors.get(position), colors.get(position + 1)));
		} else {
			mainContainerLayout.setBackgroundColor(colors.get(colors.size() - 1));
		}
		gradientBg.setImageAlpha(0);
		skipButtonBg.setImageAlpha(0);

		int currentItem = viewPager.getCurrentItem();

		if ((currentItem == 0 && position == 0) || (currentItem == 1 && position == 0)) {
			translateLoginButtons(positionOffset);
			translateSkipButton(positionOffset);

			// TODO uncomment below 3 lines for launcher icon anim
			// translateLauncherIconUp(positionOffset);
			// scaleLauncherIcon(positionOffset);
			// fadeLauncherIcon(positionOffset);

			// TODO comment below 3 lines to show launcher icon anim
			fadeLauncherIcon(1);
			translateLauncherIconUp(1);
			scaleLauncherIcon(1);
		} else if ((currentItem == 2 && position == 2) || (currentItem == 3 && position == 2 && positionOffset != 0)) {
			fadeSkipButtonAndLastFragmentBg(positionOffset);
			translateLauncherIconUp(1 - positionOffset);
			fadeLauncherIcon(1 - positionOffset);
			scaleLauncherIcon(1 - positionOffset);
			translateLoginButtons(1 - positionOffset);
			translateSkipButton(1 - positionOffset);
		} else if ((currentItem == 2 && position == 2) || (currentItem == 3 && position == 3)) {
			gradientBg.setImageAlpha(255);
			skipButtonBg.setImageAlpha(255);
		} else if (currentItem == 2 && position == 3) {
			gradientBg.setImageAlpha(255);
			skipButtonBg.setImageAlpha(255);
		}

		animateImageForScreenshot(position, positionOffset, positionOffsetPixels, viewPager.getCurrentItem());
	}

	@SuppressLint("NewApi")
	private void animateImageForScreenshot(int position, float positionOffset, int positionOffsetPixels,
			int currentItem) {
		final ImageView image1 = (ImageView) findViewById(R.id.screenshitimaege);
		ImageView image2 = (ImageView) findViewById(R.id.screenshitimaege222);

		Log.w("as", "jsjc pos " + position + " - offset " + positionOffset + "  - current " + currentItem);

		if (heightOfScreenShotImage < 10) {
			image1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {

				@Override
				public boolean onPreDraw() {
					image1.getViewTreeObserver().removeOnPreDrawListener(this);
					heightOfScreenShotImage = image1.getMeasuredHeight();
					heightOfScreenShotImage = heightOfScreenShotImage * factorofImageTobeshownAlongHeight;
					return true;
				}
			});
		}

		if ((position == 0 && currentItem == 0) || (position == 0 && currentItem == 1)) {
			float trans = deviceWidth * (1 - positionOffset);
			image1.setTranslationX(trans);
			image2.setTranslationX(trans);
		} else if ((position == 1 && currentItem == 1) || (position == 1 && currentItem == 2)) {
			image1.setTranslationX(deviceWidth * (-positionOffset));

			float scale = Math.min(1.5f, 1 + positionOffset / 2);
			image2.setScaleX(scale);
			image2.setScaleY(scale);

			float transY = positionOffset * heightOfScreenShotImage;
			image2.setTranslationY(transY);

			if (factorOfImageToBeTranslatedAlongWidth != 0) {
				float transX = positionOffset * factorOfImageToBeTranslatedAlongWidth * deviceWidth;
				image2.setTranslationX(transX);
			}
		} else if ((position == 2 && currentItem == 2) || (position == 2 && currentItem == 3)) {
			image2.setTranslationX(
					deviceWidth * (-positionOffset) + (factorOfImageToBeTranslatedAlongWidth * deviceWidth));
		}
	}

	private void translateSkipButton(float positionOffset) {
		float trans = positionOffset * skipButtonHeight * -1;
		skipButtonLayout.setTranslationY(trans);
	}

	private void translateLoginButtons(float positionOffset) {
		float trans = positionOffset * loginButtonsLayoutHeight;
		loginButtonsContainerLayout.setTranslationY(trans);
	}

	private void scaleLauncherIcon(float positionOffset) {
		if (positionOffset <= 0.5) {
			launchIcon.setScaleX(1 - positionOffset);
			launchIcon.setScaleY(1 - positionOffset);
		} else {
			launchIcon.setScaleX(0.5f);
			launchIcon.setScaleY(0.5f);
		}
	}

	@SuppressLint("NewApi")
	private void fadeSkipButtonAndLastFragmentBg(float positionOffset) {
		int fade = (int) (positionOffset * 255);
		gradientBg.setImageAlpha(fade);
		skipButtonBg.setImageAlpha(fade);
	}

	@SuppressLint("NewApi")
	private void fadeLauncherIcon(float positionOffset) {
		if (positionOffset >= 0.5) {
			float fade = (float) ((((positionOffset - 0.5) * (0 - 255)) / (1 - 0.5)) + 255);
			launchIcon.setImageAlpha((int) fade);
		} else {
			launchIcon.setImageAlpha(255);
		}
	}

	private void translateLauncherIconUp(float positionOffset) {
		if (positionOffset >= 0.5)
			positionOffset = 0.5f;
		float trans = (deviceHeight - getResources().getDimensionPixelSize(R.dimen.z_launch_app_icon_margin))
				* positionOffset * -1;
		launchIcon.setTranslationY(trans);
	}

	@Override
	public void onPageSelected(int pos) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.launch_skip_text:
			Intent i = new Intent(this, HomeActivity.class);
			startActivity(i);
			break;
		case R.id.google_sign_in_button:
			onGoogleSignInClicked();
			break;

		default:
			break;
		}
	}

	private void onGoogleSignInClicked() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, PLAY_SERVICES_RESOLUTION_REQUEST);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from
		// GoogleSignInApi.getSignInIntent(...);
		if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {
		Log.d("Instirepo", "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();
			
			nameToSend = acct.getDisplayName();
			emailToSend = acct.getEmail();
			idToSend= acct.getId();
			try{
				imageUrlToSend= acct.getPhotoUrl().toString();
			}catch (Exception e){
				imageUrlToSend = "https://cdn1.iconfinder.com/data/icons/unique-round-blue/93/user-512.png";
			}

			makeLoginRequestToServer();
		} else {
			// Signed out, show unauthenticated UI.
			
		}
	}

	private void initialiseGoogleApiClient() {
		// Configure sign-in to request the user's ID, email address, and basic
		// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
				.build();

		// Build a GoogleApiClient with access to the Google Sign-In API and the
		// options specified by gso.
		mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
	}

	public void makeLoginRequestToServer() {
		progressDialog = ProgressDialog.show(this, null, "Logging In");
		StringRequest request = new StringRequest(Method.POST, loginUrl, new Listener<String>() {

			@Override
			public void onResponse(String res) {
				if (progressDialog != null)
					progressDialog.dismiss();
				GoogleLoginObject obj = new Gson().fromJson(res, GoogleLoginObject.class);
				ZPreferences.setUserProfileID(LaunchActivity.this, obj.getUser_id() + "");
				ZPreferences.setUserImageURL(LaunchActivity.this, imageUrlToSend);
				ZPreferences.setUserName(LaunchActivity.this, nameToSend);
				ZPreferences.setUserEmail(LaunchActivity.this, emailToSend);
				if (obj.isIs_details_found_on_server()) {
					ZPreferences.setIsUserLogin(LaunchActivity.this, true);
					Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					LaunchActivity.this.finish();
				} else {
					Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					LaunchActivity.this.finish();
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				makeToast("Some error occured.Try again");
				if (progressDialog != null)
					progressDialog.dismiss();
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id", idToSend);
				p.put("email", emailToSend);
				p.put("name", nameToSend);
				p.put("image_url", imageUrlToSend);
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(request, loginUrl);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

}
