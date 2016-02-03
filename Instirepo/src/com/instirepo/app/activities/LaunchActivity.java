package com.instirepo.app.activities;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.accounts.Account;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.fragments.LaunchScreen1Fragment;
import com.instirepo.app.fragments.LaunchScreen2Fragment;
import com.instirepo.app.fragments.LaunchScreen3Fragment;
import com.instirepo.app.objects.GoogleLoginObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CirclePageIndicator;
import com.instirepo.app.widgets.LaunchActiviityViewPagerTransformer;

public class LaunchActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		ResultCallback<LoadPeopleResult>, ZUrls, PageTransformer {

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

	// GOOGLE API
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	/* Is there a ConnectionResult resolution in progress? */
	private boolean mIsResolving = false;
	/* Should we automatically resolve ConnectionResults when possible? */
	private boolean mShouldResolve = false;
	private static final int PROFILE_PIC_SIZE = 400;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	// DATA TO SEND
	String emailToSend, idToSend, imageUrlToSend, nameToSend,
			accessTokenToSend, additionalDataToSend;

	int deviceWidth;
	int thetaChange = 45;

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

		progressDialog = new ProgressDialog(this);
		progressDialog.dismiss();

		deviceWidth = getResources().getDisplayMetrics().widthPixels;

		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Scroller scroller = new Scroller(this,
					new DecelerateInterpolator(), true);
			mScroller.set(viewPager, scroller);
		} catch (Exception e) {
		}

		loginButtonsLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							mainContainerLayout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						} else {
							mainContainerLayout.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						}
						loginButtonsLayoutHeight = loginButtonsLayout
								.getHeight();
						skipButtonHeight = skipButtonLayout.getHeight();
					}
				});

		mainContainerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							mainContainerLayout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						} else {
							mainContainerLayout.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
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

		viewPager.setPageTransformer(false, this);

		skipButton.setOnClickListener(this);
		googleLoginButton.setOnClickListener(this);

		ZPreferences.setIsUserLogin(this, false);

		initialiseGoogleApiClient();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				makeToast("This device doesn't support Play services, App will not work normally");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	@Override
	protected void onStop() {
		if (mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
		super.onStop();
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			bundle.putInt("position", pos);

			Fragment fragment = null;

			switch (pos) {
			case 1:
				fragment = LaunchScreen2Fragment.newInstance(bundle);
				break;
			case 2:
				fragment = LaunchScreen3Fragment.newInstance(bundle);
				break;
			default:
				fragment = LaunchScreen1Fragment.newInstance(bundle);
				break;
			}

			fragment.getView().setTag(pos);
			return fragment;
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
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (position < (adapter.getCount() - 1)
				&& position < (colors.size() - 1)) {
			mainContainerLayout.setBackgroundColor((Integer) argbEvaluator
					.evaluate(positionOffset, colors.get(position),
							colors.get(position + 1)));
		} else {
			mainContainerLayout
					.setBackgroundColor(colors.get(colors.size() - 1));
		}
		gradientBg.setImageAlpha(0);
		skipButtonBg.setImageAlpha(0);
		if ((viewPager.getCurrentItem() == 0 && position == 0)
				|| (viewPager.getCurrentItem() == 1 && position == 0)) {
			translateLauncherIconUp(positionOffset);
			scaleLauncherIcon(positionOffset);
			fadeLauncherIcon(positionOffset);
			translateLoginButtons(positionOffset);
			translateSkipButton(positionOffset);
		} else if ((viewPager.getCurrentItem() == 2 && position == 2)
				|| (viewPager.getCurrentItem() == 3 && position == 2 && positionOffset != 0)) {
			fadeSkipButtonAndLastFragmentBg(positionOffset);
			translateLauncherIconUp(1 - positionOffset);
			fadeLauncherIcon(1 - positionOffset);
			scaleLauncherIcon(1 - positionOffset);
			translateLoginButtons(1 - positionOffset);
			translateSkipButton(1 - positionOffset);
		} else if ((viewPager.getCurrentItem() == 2 && position == 2)
				|| (viewPager.getCurrentItem() == 3 && position == 3)) {
			gradientBg.setImageAlpha(255);
			skipButtonBg.setImageAlpha(255);
		} else if (viewPager.getCurrentItem() == 2 && position == 3) {
			gradientBg.setImageAlpha(255);
			skipButtonBg.setImageAlpha(255);
		}
	}

	@Override
	public void transformPage(View view, float position) {
		int tag = Integer.parseInt(String.valueOf(view.getTag()));
		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
		} else if (position < 1) { // [-1,1]
			animateImageForScreenshot(position, tag);
		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
		}

		if (view.findViewById(R.id.framelayoutfragment3launch) != null) {
			FrameLayout frame = (FrameLayout) view
					.findViewById(R.id.framelayoutfragment3launch);
			frame.setRotation(360 * position + thetaChange);
		}

		antiRotateThirdFragmentImage(R.id.firstimage, view, position);
		antiRotateThirdFragmentImage(R.id.secondimage, view, position);
		antiRotateThirdFragmentImage(R.id.thirdimage, view, position);
		antiRotateThirdFragmentImage(R.id.fourthimage, view, position);
	}

	private void antiRotateThirdFragmentImage(int imageID, View view,
			float position) {
		if (view.findViewById(imageID) != null) {
			LinearLayout image = (LinearLayout) view.findViewById(imageID);
			image.setRotation(-360 * position - thetaChange);
		}
	}

	public void animateImageForScreenshot(float pos, int tag) {
		ImageView image = (ImageView) findViewById(R.id.screenshitimaege);

		// Decide which fragment is sliding left and which one is sliding right.
		if (tag == 0) {
			// Transformation between first and second fragment
			float trans = deviceWidth * (1 - pos);
			image.setTranslationX(trans);
		} else if (tag == 1) {
			// Transformation between first, second and third fragment
		} else if (tag == 2) {
			// Transformation between third and fourth fragment
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
		float trans = (deviceHeight - getResources().getDimensionPixelSize(
				R.dimen.z_launch_app_icon_margin))
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
		mShouldResolve = true;
		mGoogleApiClient.connect();

		progressDialog = ProgressDialog.show(this, "Google Login",
				"Getting Google login details. Please wait..", true, false);
	}

	private void initialiseGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(new Scope(Scopes.PROFILE)).build();

		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mShouldResolve = false;
				if (progressDialog != null)
					progressDialog.dismiss();
			}

			mIsResolving = false;
			mGoogleApiClient.connect();
			if (!progressDialog.isShowing())
				progressDialog = ProgressDialog
						.show(this, "Google Login",
								"Logging in through Google..Please Wait..",
								true, false);
		} else {

		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (!mIsResolving && mShouldResolve) {
			if (connectionResult.hasResolution()) {
				try {
					if (progressDialog != null)
						progressDialog.dismiss();
					connectionResult.startResolutionForResult(this, RC_SIGN_IN);
					mIsResolving = true;
				} catch (IntentSender.SendIntentException e) {
					progressDialog = ProgressDialog.show(this, null,
							"Getting user account details");
					mIsResolving = false;
					mGoogleApiClient.connect();
				}
			} else {
				if (progressDialog != null)
					progressDialog.dismiss();
				makeToast("Login error...Please try again and check your internet connection "
						+ connectionResult.describeContents());
			}
		} else {
			if (progressDialog != null)
				progressDialog.dismiss();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		mShouldResolve = false;

		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if (progressDialog != null && !progressDialog.isShowing())
			progressDialog = ProgressDialog.show(this, null,
					"Getting user details.Please wait..");

		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
				this);

		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi
					.getCurrentPerson(mGoogleApiClient);
			String personName = currentPerson.getDisplayName();
			String personPhoto = currentPerson.getImage().getUrl();
			String personGooglePlusProfile = currentPerson.getUrl();
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			String id = currentPerson.getId();

			personPhoto = personPhoto.substring(0, personPhoto.length() - 2)
					+ PROFILE_PIC_SIZE;

			nameToSend = personName;
			imageUrlToSend = personPhoto;
			emailToSend = email;
			idToSend = id;
			additionalDataToSend = currentPerson
					+ "  ---   profile url   --   " + personGooglePlusProfile;
		} else {

		}

		GetGoogleIdTokenTask task = new GetGoogleIdTokenTask();
		task.execute();
	}

	private void onGoogleSignOutClicked() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	@Override
	public void onResult(LoadPeopleResult arg0) {

	}

	private class GetGoogleIdTokenTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			progressDialog = ProgressDialog.show(LaunchActivity.this, null,
					"Getting Google Plus Access Token");
		}

		@Override
		protected String doInBackground(Void... params) {
			String accountName = Plus.AccountApi
					.getAccountName(mGoogleApiClient);
			Account account = new Account(accountName,
					GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
			String scopes = "audience:server:client_id:"
					+ "860159170646-etjq78ciilius08ehhoq0k63ttco4s2j.apps.googleusercontent.com";
			try {
				return GoogleAuthUtil.getToken(getApplicationContext(),
						account, scopes);
			} catch (IOException e) {
				return null;
			} catch (GoogleAuthException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (result != null) {
				accessTokenToSend = result;
				makeLoginRequestToServer();
			} else {

			}
		}
	}

	public void makeLoginRequestToServer() {
		progressDialog = ProgressDialog.show(this, null, "Logging In");
		StringRequest request = new StringRequest(Method.POST, loginUrl,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						if (progressDialog != null)
							progressDialog.dismiss();
						GoogleLoginObject obj = new Gson().fromJson(res,
								GoogleLoginObject.class);
						ZPreferences.setUserProfileID(LaunchActivity.this,
								obj.getUser_id() + "");
						ZPreferences.setUserImageURL(LaunchActivity.this,
								imageUrlToSend);
						ZPreferences.setUserName(LaunchActivity.this,
								nameToSend);
						ZPreferences.setUserEmail(LaunchActivity.this,
								emailToSend);
						if (obj.isIs_details_found_on_server()) {
							ZPreferences.setIsUserLogin(LaunchActivity.this,
									true);
							Intent intent = new Intent(LaunchActivity.this,
									HomeActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							LaunchActivity.this.finish();
						} else {
							Intent intent = new Intent(LaunchActivity.this,
									LoginActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_CLEAR_TASK
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
				p.put("access_token", accessTokenToSend);
				p.put("user_id", idToSend);
				p.put("additional_data", additionalDataToSend);
				p.put("email", emailToSend);
				p.put("name", nameToSend);
				p.put("image_url", imageUrlToSend);
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(request, loginUrl);
	}

}
