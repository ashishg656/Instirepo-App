package com.instirepo.app.activities;

import java.util.HashMap;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimationListener;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.fragments.CommentsFragment;
import com.instirepo.app.fragments.MyPostsFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;

public class UserProfileActivity extends BaseActivity implements AppConstants,
		OnPageChangeListener {

	public int headerHeight, deviceHeight;
	ViewPager viewPager;
	TabLayout tabLayout, tabLayoutFake;
	MyPagerAdapter adapter;
	public static final int TRANSLATION_DURATION = 200;
	boolean isToolbarAnimRunning;
	AppBarLayout appBarLayout;
	HashMap<Integer, Fragment> fragmentHashMap;
	LinearLayout appbarContainer;
	int appbarLayoutHeight;
	FrameLayout actualHeader;
	int height48dp;
	boolean isAppbarAlpharunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_activity_layout);
		fragmentHashMap = new HashMap<>();

		deviceHeight = getResources().getDisplayMetrics().heightPixels;
		headerHeight = (int) (deviceHeight / 2.1);
		toolbarHeight = getResources().getDimensionPixelSize(
				R.dimen.z_toolbar_height);
		height48dp = getResources().getDimensionPixelSize(
				R.dimen.z_button_height);
		appbarLayoutHeight = getResources().getDimensionPixelSize(
				R.dimen.z_appbar_layout_height);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tabLayout = (TabLayout) findViewById(R.id.indicator);
		tabLayoutFake = (TabLayout) findViewById(R.id.indicator2);
		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		appbarContainer = (LinearLayout) findViewById(R.id.appbarlayoutcontainer);
		actualHeader = (FrameLayout) findViewById(R.id.actualheader);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);
		tabLayout.setupWithViewPager(viewPager);
		tabLayoutFake.setupWithViewPager(viewPager);

		appbarContainer.setVisibility(View.GONE);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) actualHeader
				.getLayoutParams();
		params.height = headerHeight;
		actualHeader.setLayoutParams(params);

		viewPager.setOnPageChangeListener(this);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			if (pos == 0)
				return MyPostsFragment.newInstance(bundle);
			else if (pos == 1)
				return MyPostsFragment.newInstance(bundle);
			else
				return MyPostsFragment.newInstance(bundle);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return "My Profile";
			} else if (position == 1)
				return "My Posts";
			else
				return "Marked Posts";
		}
	}

	@SuppressLint("NewApi")
	public void scrollToolbarBy(int dy) {
		float requestedTranslation = appbarContainer.getTranslationY() + dy;
		if (requestedTranslation < -appbarLayoutHeight) {
			requestedTranslation = -appbarLayoutHeight;
			appbarContainer.setTranslationY(requestedTranslation);
		} else if (requestedTranslation > 0) {
			requestedTranslation = 0;
			appbarContainer.setTranslationY(requestedTranslation);
		} else if (requestedTranslation >= -appbarLayoutHeight
				&& requestedTranslation <= 0) {
			appbarContainer.setTranslationY(requestedTranslation);
		}
	}

	@SuppressLint("NewApi")
	public void scrollToolbarAfterTouchEnds() {
		float currentTranslation = -appbarContainer.getTranslationY();
		if (currentTranslation > appbarLayoutHeight / 2) {
			animateToolbarLayout(-appbarLayoutHeight);
		} else {
			animateToolbarLayout(0);
		}
	}

	public void setToolbarTranslation(View firstChild) {
		if (firstChild.getTop() > appbarContainer.getHeight()) {
			animateToolbarLayout(0);
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	public void setToolbarTranslationFromPositionOfTopChildAfterTouchEnd(int pos) {
		if (pos > appbarContainer.getHeight()) {
			animateToolbarLayout(0);
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	void animateToolbarLayout(int trans) {
		appbarContainer.animate().translationY(trans)
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

	public void scrollFragmentRecycler(int findFirstVisibleItemPosition,
			int top, int dy, int scrollY) {
		if (findFirstVisibleItemPosition == 0) {
			actualHeader.setTranslationY(top);

			if (appbarContainer.getVisibility() == View.VISIBLE) {
				if (-top < headerHeight - height48dp && !isAppbarAlpharunning) {
					isAppbarAlpharunning = true;
					AlphaAnimation anim = new AlphaAnimation(
							appbarContainer.getAlpha(), 0f);
					anim.setDuration(300);
					anim.setInterpolator(new AccelerateDecelerateInterpolator());
					anim.setAnimationListener(new ZAnimationListener() {
						@Override
						public void onAnimationEnd(Animation animation) {
							isAppbarAlpharunning = false;
							appbarContainer.setAlpha(1f);
							appbarContainer.setVisibility(View.GONE);
						}
					});
					appbarContainer.startAnimation(anim);
				}
			}
		} else {
			appbarContainer.setAlpha(1f);
			appbarContainer.setVisibility(View.VISIBLE);
			actualHeader.setTranslationY(-headerHeight);
		}
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

		if (fragmentUserProfile != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfile).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfile)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentComments != null) {
			CommentsFragment frg = (CommentsFragment) fragmentComments;
			if (frg.shouldGoBackOnBackButtonPress())
				super.onBackPressed();
		} else
			super.onBackPressed();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int pos, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

	}

}
