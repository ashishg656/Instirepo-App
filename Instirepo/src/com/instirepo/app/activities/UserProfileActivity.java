package com.instirepo.app.activities;

import java.util.HashMap;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimationListener;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.fragments.CommentsFragment;
import com.instirepo.app.fragments.MyPostsFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.notboringactionbar.KenBurnsSupportView;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.PagerSlidingTabStrip;

public class UserProfileActivity extends BaseActivity implements AppConstants,
		OnPageChangeListener {

	public int headerHeight, deviceHeight, deviceWidth;
	ViewPager viewPager;
	TabLayout tabLayout;
	PagerSlidingTabStrip pagerSlidingTabStripFake;
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
	LinearLayout fakeToolbarLayout;
	KenBurnsSupportView kenBurnsSupportView;
	CircularImageView circularHeaderImage;
	View kenburnsImageBg, circularRevealView;
	SupportAnimator animator;
	float alpha;
	private int CIRCULAR_REVEAL_ANIMATION_DURATION = 700;
	private boolean isCircularRevealShown = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_activity_layout);
		fragmentHashMap = new HashMap<>();

		deviceHeight = getResources().getDisplayMetrics().heightPixels;
		deviceWidth = getResources().getDisplayMetrics().widthPixels;
		headerHeight = (int) (deviceHeight / 2.1);
		toolbarHeight = getResources().getDimensionPixelSize(
				R.dimen.z_toolbar_height);
		height48dp = getResources().getDimensionPixelSize(
				R.dimen.z_button_height);
		appbarLayoutHeight = getResources().getDimensionPixelSize(
				R.dimen.z_appbar_layout_height);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tabLayout = (TabLayout) findViewById(R.id.indicator);
		pagerSlidingTabStripFake = (PagerSlidingTabStrip) findViewById(R.id.tab_strip_category);
		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		appbarContainer = (LinearLayout) findViewById(R.id.appbarlayoutcontainer);
		actualHeader = (FrameLayout) findViewById(R.id.actualheader);
		fakeToolbarLayout = (LinearLayout) findViewById(R.id.faketoolbalayouy);
		kenBurnsSupportView = (KenBurnsSupportView) findViewById(R.id.kenburnssupoortview);
		circularHeaderImage = (CircularImageView) findViewById(R.id.userprofileimage);
		circularRevealView = (View) findViewById(R.id.circular_reveal_view);
		kenburnsImageBg = (View) findViewById(R.id.image_overlay_bg);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		setStatusBarColor(getResources().getColor(
				R.color.bnc_shop_by_category_color1));

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);
		tabLayout.setupWithViewPager(viewPager);
		pagerSlidingTabStripFake.setTextColor(getResources().getColor(
				R.color.z_white));
		pagerSlidingTabStripFake.setViewPager(viewPager);

		appbarContainer.setVisibility(View.GONE);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) actualHeader
				.getLayoutParams();
		params.height = headerHeight;
		actualHeader.setLayoutParams(params);

		pagerSlidingTabStripFake.setOnPageChangeListener(this);

		try {
			int position = getIntent().getExtras().getInt("position");
			if (position != 0)
				isCircularRevealShown = false;
			viewPager.setCurrentItem(position, false);
		} catch (Exception e) {
		}

		setDataBeforeLoadingFromServer();
	}

	private void setDataBeforeLoadingFromServer() {
		ImageRequestManager.get(this).requestImage(this, circularHeaderImage,
				ZPreferences.getUserImageURL(this), -1);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			if (pos == 0)
				fragmentHashMap.put(pos, MyPostsFragment.newInstance(bundle));
			else if (pos == 1)
				fragmentHashMap.put(pos, MyPostsFragment.newInstance(bundle));
			else
				fragmentHashMap.put(pos, MyPostsFragment.newInstance(bundle));

			return fragmentHashMap.get(pos);
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

	@SuppressLint("NewApi")
	public void scrollFragmentRecycler(int findFirstVisibleItemPosition,
			int top, int dy, int scrollY) {
		if (findFirstVisibleItemPosition == 0) {
			actualHeader.setTranslationY(top);

			if (appbarContainer.getVisibility() == View.VISIBLE) {
				if (-top < headerHeight - height48dp && !isAppbarAlpharunning) {
					isAppbarAlpharunning = true;
					AlphaAnimation anim = new AlphaAnimation(
							appbarContainer.getAlpha(), 0f);
					anim.setDuration(200);
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

			if (-top > headerHeight - appbarLayoutHeight) {

			} else {
				float trans = -top;
				fakeToolbarLayout.setTranslationY(trans);
			}
		} else {
			appbarContainer.setAlpha(1f);
			appbarContainer.setVisibility(View.VISIBLE);
			actualHeader.setTranslationY(-headerHeight);
		}

		alpha = headerHeight == 0 ? 255 : (int) (255 - 2 * (actualHeader
				.getTranslationY() / headerHeight * 255 * -1));
		if (alpha < 0)
			alpha = 0;
		else if (alpha > 255)
			alpha = 255;
		alpha = (float) alpha / 255.0f;
		((View) circularHeaderImage).setAlpha(alpha);
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

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(color);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
			makeHeightsOfRecyclerViewsEqualOnPageScroll();
		} else if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
			makeHeightsOfRecyclerViewsEqualOnPageScroll();
		}
	}

	@Override
	public void onPageScrolled(int pos, float arg1, int arg2) {
		// makeHeightsOfRecyclerViewsEqualOnPageScroll();
	}

	void pageSelectedAnimation(int color, int lightColor,
			final int circularrevealcolor) {
		setStatusBarColor(color);
		kenburnsImageBg.setBackgroundColor(lightColor);
		if (isCircularRevealShown) {
			makeHeightsOfRecyclerViewsEqualOnPageScroll();
			Animation anim = AnimationUtils.loadAnimation(this,
					R.anim.scale_down_anim);
			circularHeaderImage.startAnimation(anim);
			anim.setAnimationListener(new ZAnimationListener() {

				@SuppressLint("NewApi")
				@Override
				public void onAnimationEnd(Animation animation) {
					((View) circularHeaderImage).setAlpha(alpha);
					Animation anim1 = AnimationUtils.loadAnimation(
							UserProfileActivity.this, R.anim.scale_up_anim);
					circularHeaderImage.startAnimation(anim1);
				}
			});

			int cx = (circularRevealView.getLeft() + circularRevealView
					.getRight()) / 2;
			int cy = (circularRevealView.getTop() + circularRevealView
					.getBottom()) / 2;
			int finalRadius = Math.max(headerHeight, deviceWidth);

			animator = ViewAnimationUtils.createCircularReveal(
					circularRevealView, cx, cy, 0, finalRadius);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.setDuration(CIRCULAR_REVEAL_ANIMATION_DURATION);
			animator.addListener(new SupportAnimator.AnimatorListener() {
				@Override
				public void onAnimationStart() {
					circularRevealView.setBackgroundColor(circularrevealcolor);
				}

				@Override
				public void onAnimationRepeat() {
				}

				@Override
				public void onAnimationEnd() {
					circularRevealView.setBackgroundColor(getResources()
							.getColor(android.R.color.transparent));
				}

				@Override
				public void onAnimationCancel() {
				}
			});
			animator.start();
		} else {
			isCircularRevealShown = true;
		}
	}

	private void makeHeightsOfRecyclerViewsEqualOnPageScroll() {
		for (int i = 0; i < 3; i++) {
			if (i != viewPager.getCurrentItem()) {
				((MyPostsFragment) fragmentHashMap.get(i)).layoutManager
						.scrollToPositionWithOffset(0,
								(int) actualHeader.getTranslationY());
			}
		}
	}

	@Override
	public void onPageSelected(int pos) {
		for (int i = 0; i < 3; i++) {
			((MyPostsFragment) fragmentHashMap.get(i)).layoutManager
					.scrollToPositionWithOffset(0,
							(int) actualHeader.getTranslationY());
		}

		if (pos == 0) {
			pageSelectedAnimation(
					getResources()
							.getColor(R.color.bnc_shop_by_category_color1),
					getResources().getColor(
							R.color.bnc_shop_by_category_color1_light),
					getResources().getColor(
							R.color.bnc_shop_by_category_color1_lightdiff));
		} else if (pos == 1) {
			pageSelectedAnimation(
					getResources()
							.getColor(R.color.bnc_shop_by_category_color2),
					getResources().getColor(
							R.color.bnc_shop_by_category_color2_light),
					getResources().getColor(
							R.color.bnc_shop_by_category_color2_lightdiff));
		} else {
			pageSelectedAnimation(
					getResources()
							.getColor(R.color.bnc_shop_by_category_color3),
					getResources().getColor(
							R.color.bnc_shop_by_category_color3_light),
					getResources().getColor(
							R.color.bnc_shop_by_category_color3_lightdiff));
		}
	}

}
