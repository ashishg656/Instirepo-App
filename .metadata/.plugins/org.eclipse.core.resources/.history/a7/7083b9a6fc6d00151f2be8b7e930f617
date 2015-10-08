package com.example.faceless.activities;

import com.example.faceless.R;
import com.example.faceless.fragments.AllChannelsFragment;
import com.example.faceless.fragments.PostsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class HomeActivity extends BaseActivity {

	ViewPager viewPager;
	MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			if (pos == 0) {
				return new PostsFragment();
			} else {
				return new AllChannelsFragment();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

}
