package com.instirepo.app.activities;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;

import com.instirepo.app.R;
import com.instirepo.app.fragments.MyPostsFragment;

public class UserProfileActivity extends BaseActivity {

	public int headerHeight, deviceHeight;
	ViewPager viewPager;
	TabLayout tabLayout;
	MyPagerAdapter adapter;
	public static final int TRANSLATION_DURATION = 200;
	boolean isToolbarAnimRunning;
	AppBarLayout appBarLayout;
	HashMap<Integer, Fragment> fragmentHashMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_activity_layout);
		fragmentHashMap = new HashMap<>();

		deviceHeight = getResources().getDisplayMetrics().heightPixels;
		headerHeight = (int) (deviceHeight / 2.1);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tabLayout = (TabLayout) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

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

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
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

}
