package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.fragments.PostsByStudentsFragment;
import com.instirepo.app.fragments.PostsByTeachersFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

public class ZHomeActivity extends BaseActivity {

	ViewPager viewPager;
	TabLayout tabLayout;
	MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tabLayout = (TabLayout) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager_launch);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

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
}
