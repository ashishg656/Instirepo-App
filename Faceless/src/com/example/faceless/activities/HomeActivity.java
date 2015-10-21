package com.example.faceless.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.faceless.R;
import com.example.faceless.fragments.AllChannelsFragment;
import com.example.faceless.fragments.PostsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class HomeActivity extends BaseActivity {

	ViewPager viewPager;
	MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		viewPager = (ViewPager) findViewById(R.id.pager_launch);

		if (!checkPlayServices())
			return;

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 100)
						.show();
			} else {
				Toast.makeText(
						this,
						"This device doesn't support Play services, App will not work normally",
						Toast.LENGTH_LONG).show();
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
