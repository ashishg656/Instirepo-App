package com.instirepo.app.activities;

import java.util.HashMap;
import java.util.Map;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.floatingactionbutton.FloatingActionButton;
import com.instirepo.app.floatingactionbutton.FloatingActionMenu;
import com.instirepo.app.fragments.CommentsFragment;
import com.instirepo.app.fragments.PostsByStudentsFragment;
import com.instirepo.app.fragments.PostsByTeachersFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.SelectPostCategoryFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.serverApi.ImageRequestManager.RequestBitmap;
import com.instirepo.app.widgets.CircularImageView;

public class HomeActivity extends BaseActivity implements OnPageChangeListener,
		AppConstants, OnClickListener, ZUrls, ConnectionCallbacks,
		OnConnectionFailedListener {

	ViewPager viewPager;
	TabLayout tabLayout;
	MyPagerAdapter adapter;
	DrawerLayout drawerLayout;
	NavigationView navigationView;
	TextView navigationDrawerUserName, navigationDrawerEmail;
	CircularImageView navigationDrawerImageUser;
	ImageView navigationDrawerImageDefault;
	FrameLayout navigationDrawerHeaderLayout;
	public static final int TRANSLATION_DURATION = 200;
	boolean isToolbarAnimRunning;
	AppBarLayout appBarLayout;

	FloatingActionMenu floatingActionMenu;
	FloatingActionButton createPostButton;
	View fabBackground;
	int fabRevealMargin;
	int deviceHeight, deviceWidth;
	boolean isFabAnimRunning;
	int maxFloatingActionButtonTranslation;
	ProgressDialog progressDialog;
	Snackbar snackbar;

	public AllPostCategoriesObject allPostCategoriesObject;
	private GoogleApiClient mGoogleApiClient;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		fabRevealMargin = getResources().getDimensionPixelSize(
				R.dimen.z_fab_home_dimension_for_reveal);
		deviceHeight = getResources().getDisplayMetrics().heightPixels;
		deviceWidth = getResources().getDisplayMetrics().widthPixels;
		maxFloatingActionButtonTranslation = getResources()
				.getDimensionPixelSize(
						R.dimen.floating_action_button_height_with_margin_bottom_considered);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(new Scope(Scopes.PROFILE)).build();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tabLayout = (TabLayout) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabmenuy);
		fabBackground = (View) findViewById(R.id.revealviewfab);
		createPostButton = (FloatingActionButton) findViewById(R.id.createpostbutton);
		navigationDrawerEmail = (TextView) findViewById(R.id.navdraweremail);
		navigationDrawerImageUser = (CircularImageView) findViewById(R.id.avatar);
		navigationDrawerImageDefault = (ImageView) findViewById(R.id.avatardefault);
		navigationDrawerUserName = (TextView) findViewById(R.id.navdrawerusername);
		navigationDrawerHeaderLayout = (FrameLayout) findViewById(R.id.navigationdrawerheader);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		createPostButton.setOnClickListener(this);

		floatingActionMenu.setOnMenuButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fabBackground.getVisibility() == View.GONE) {
					showFloatingActionMenu();
				} else {
					hideFloatingActionMenu();
				}
			}
		});

		toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						try {
							toolbar.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} catch (Exception e) {
							toolbar.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
						toolbarHeight = toolbar.getHeight();
					}
				});

		if (ZPreferences.isUserLogIn(this)) {
			navigationDrawerEmail.setText(ZPreferences.getUserEmail(this));
			navigationDrawerUserName.setText(ZPreferences.getUserName(this));
			ImageRequestManager.get(this).requestImage2(this,
					navigationDrawerImageUser,
					ZPreferences.getUserImageURL(this), new RequestBitmap() {

						@Override
						public void onRequestCompleted(Bitmap bitmap) {
							navigationDrawerImageUser.setImageBitmap(bitmap);
							navigationDrawerImageDefault
									.setVisibility(View.GONE);
						}
					}, -1);
		} else {
			navigationDrawerEmail.setText("Please Login");
			navigationDrawerUserName.setText("BookNCart");
			navigationDrawerImageUser.setVisibility(View.GONE);
		}
		navigationDrawerHeaderLayout.setOnClickListener(this);

		setDrawerActionBarToggle();
		setDrawerItemClickListener();
		viewPager.setOnPageChangeListener(this);

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		if (mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
		super.onStop();
	}

	protected void hideFloatingActionMenu() {
		if (!isFabAnimRunning) {
			isFabAnimRunning = true;
			SupportAnimator anim = ViewAnimationUtils.createCircularReveal(
					fabBackground, deviceWidth - fabRevealMargin, deviceHeight
							- 2 * fabRevealMargin, deviceHeight, 0);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			anim.setDuration(400);
			anim.addListener(new ZCircularAnimatorListener() {
				@Override
				public void onAnimationEnd() {
					isFabAnimRunning = false;
					fabBackground.setVisibility(View.GONE);
				}
			});
			anim.start();
			floatingActionMenu.toggle(true);
		}
	}

	protected void showFloatingActionMenu() {
		if (!isFabAnimRunning) {
			isFabAnimRunning = true;
			fabBackground.setVisibility(View.VISIBLE);
			SupportAnimator anim = ViewAnimationUtils.createCircularReveal(
					fabBackground, deviceWidth - fabRevealMargin, deviceHeight
							- 2 * fabRevealMargin, 0, deviceHeight);
			anim.setDuration(500);
			anim.addListener(new ZCircularAnimatorListener() {
				@Override
				public void onAnimationEnd() {
					isFabAnimRunning = false;
				}
			});
			anim.start();
			floatingActionMenu.toggle(true);
		}
	}

	private void setDrawerItemClickListener() {
		navigationView
				.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

					@Override
					public boolean onNavigationItemSelected(MenuItem item) {
						item.setChecked(true);
						drawerLayout.closeDrawers();
						switch (item.getItemId()) {
						case R.id.logoutfromapp:
							ZPreferences.setIsUserLogin(HomeActivity.this,
									false);
							ZPreferences.setUserProfileID(HomeActivity.this,
									null);
							if (mGoogleApiClient.isConnected()) {
								Plus.AccountApi
										.clearDefaultAccount(mGoogleApiClient);
								mGoogleApiClient.disconnect();
							}
							Intent intent = new Intent(HomeActivity.this,
									LaunchActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							HomeActivity.this.finish();
							return true;
						case R.id.allmessagesactivity:
							openMessagesListActivity();
							return true;
						case R.id.opennotificaton:
							openNotificationsActivity();
							return true;
						default:
							return true;
						}
					}
				});
	}

	private void setDrawerActionBarToggle() {
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
				this, drawerLayout, toolbar, R.string.z_open_drawer,
				R.string.z_close_drawer) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();
		navigationView.setItemIconTintList(null);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			if (pos == 0)
				return PostsByTeachersFragment.newInstance(bundle);
			else
				return PostsByStudentsFragment.newInstance(bundle);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return "Posts By Teachers";
			} else
				return "Posts By Students";
		}
	}

	@SuppressLint("NewApi")
	public void scrollToolbarBy(int dy) {
		float requestedTranslation = appBarLayout.getTranslationY() + dy;
		if (requestedTranslation < -toolbarHeight) {
			requestedTranslation = -toolbarHeight;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation > 0) {
			requestedTranslation = 0;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation >= -toolbarHeight
				&& requestedTranslation <= 0) {
			appBarLayout.setTranslationY(requestedTranslation);
		}

		scrollFABBy(-dy);
	}

	void scrollFABBy(int dy) {
		float requestedTranslation = floatingActionMenu.getTranslationY() + dy;
		if (requestedTranslation > maxFloatingActionButtonTranslation) {
			requestedTranslation = maxFloatingActionButtonTranslation;
			floatingActionMenu.setTranslationY(requestedTranslation);
		} else if (requestedTranslation < 0) {
			requestedTranslation = 0;
			floatingActionMenu.setTranslationY(requestedTranslation);
		} else if (requestedTranslation <= maxFloatingActionButtonTranslation
				&& requestedTranslation >= 0) {
			floatingActionMenu.setTranslationY(requestedTranslation);
		}
	}

	@SuppressLint("NewApi")
	public void scrollToolbarAfterTouchEnds() {
		float currentTranslation = -appBarLayout.getTranslationY();
		if (currentTranslation > toolbarHeight / 2) {
			animateToolbarLayout(-toolbarHeight);
			animateFABLayout(maxFloatingActionButtonTranslation);
		} else {
			animateToolbarLayout(0);
			animateFABLayout(0);
		}
	}

	public void setToolbarTranslation(View firstChild) {
		if (firstChild.getTop() > appBarLayout.getHeight()) {
			animateToolbarLayout(0);
			animateFABLayout(0);
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	public void setToolbarTranslationFromPositionOfTopChildAfterTouchEnd(int pos) {
		if (pos > appBarLayout.getHeight()) {
			animateToolbarLayout(0);
			animateFABLayout(0);
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	void animateToolbarLayout(int trans) {
		appBarLayout.animate().translationY(trans)
				.setDuration(TRANSLATION_DURATION)
				.setInterpolator(new DecelerateInterpolator())
				.setListener(new ZAnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
						isToolbarAnimRunning = true;
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						isToolbarAnimRunning = false;
					}
				});
	}

	void animateFABLayout(int trans) {
		floatingActionMenu.animate().translationY(trans)
				.setDuration(TRANSLATION_DURATION)
				.setInterpolator(new DecelerateInterpolator())
				.setListener(new ZAnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
					}
				});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (appBarLayout.getTranslationY() != 0 && !isToolbarAnimRunning) {
			animateToolbarLayout(0);
		}
	}

	@Override
	public void onPageSelected(int arg0) {

	}

	public void switchToSeenByPeopleFragment(int postid) {
		Bundle bundle = new Bundle();
		bundle.putInt("postid", postid);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						SeenByPeopleFragment.newInstance(bundle))
				.addToBackStack("tag").commit();
	}

	public void switchToCommentsFragment(int postid) {
		Bundle bundle = new Bundle();
		bundle.putInt("postid", postid);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						CommentsFragment.newInstance(bundle),
						Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG)
				.addToBackStack("Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG")
				.commit();
	}

	void switchToSelectPostCategoryFragment() {
		Bundle bundle = new Bundle();

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						SelectPostCategoryFragment.newInstance(bundle))
				.addToBackStack("").commit();
	}

	public void switchToUserProfileViewedByOtherFragment(int userid,
			String name, String image) {
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putInt("userid", userid);
		bundle.putString("image", image);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						UserProfileViewedByOtherFragment.newInstance(bundle),
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
				.addToBackStack(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
				.commit();
	}

	@Override
	public void onBackPressed() {
		Fragment fragmentUserProfile = getSupportFragmentManager()
				.findFragmentByTag(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG);
		Fragment fragmentComments = getSupportFragmentManager()
				.findFragmentByTag(Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG);
		Fragment fragmentUserProfileOpenedFromCommentsListAdapter = getSupportFragmentManager()
				.findFragmentByTag(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG_FROM_COMMENT_LIST_ADAPTER);

		if (fragmentUserProfileOpenedFromCommentsListAdapter != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentUserProfile != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfile).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfile)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentComments != null) {
			CommentsFragment frg = (CommentsFragment) fragmentComments;
			if (frg.shouldGoBackOnBackButtonPress())
				super.onBackPressed();
		} else if (fabBackground.getVisibility() == View.VISIBLE) {
			hideFloatingActionMenu();
		} else if (drawerLayout.isDrawerVisible(navigationView)) {
			drawerLayout.closeDrawers();
		} else
			super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createpostbutton:
			requestAllPostCategories();
			break;
		case R.id.navigationdrawerheader:
			if (ZPreferences.isUserLogIn(this)) {
				Intent intent = new Intent(this, UserProfileActivity.class);
				startActivity(intent);
			} else {
				makeToast("Please Login");
				Intent i = new Intent(this, LaunchActivity.class);
				startActivity(i);
			}
			break;
		default:
			break;
		}
	}

	private void requestAllPostCategories() {
		progressDialog = ProgressDialog.show(this, "Loading",
				"Verifying and getting categories..");

		StringRequest req = new StringRequest(Method.POST,
				getAllPostCategories, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						AllPostCategoriesObject obj = new Gson().fromJson(arg0,
								AllPostCategoriesObject.class);
						HomeActivity.this.allPostCategoriesObject = obj;

						if (obj.isError()) {
							snackbar = Snackbar.make(
									findViewById(R.id.coordinatorlayout),
									obj.getMessage(),
									Snackbar.LENGTH_INDEFINITE);
							snackbar.show();
							hideFloatingActionMenu();
						} else {
							hideFloatingActionMenu();
							switchToSelectPostCategoryFragment();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						showSnackBar("Error..Check Internet Connection");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(HomeActivity.this));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, getAllPostCategories);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnected(Bundle arg0) {

	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}
}
