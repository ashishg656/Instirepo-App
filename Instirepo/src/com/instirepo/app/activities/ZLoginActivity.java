package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.fragments.LoginScreen1Fragment;
import com.instirepo.app.fragments.LoginScreenStudentDetailsFragment;
import com.instirepo.app.fragments.LoginScreenTeacherDetailsFragment;

import android.os.Bundle;

public class ZLoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);

		setFirstFragment();
	}

	public void setFirstFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						LoginScreen1Fragment.newInstance(new Bundle()))
				.commit();
	}

	public void setSecondFragmentForTeacherDetails() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.fragmentcontainer,
						LoginScreenTeacherDetailsFragment
								.newInstance(new Bundle())).addToBackStack("1")
				.commit();
	}

	public void setSecondFragmentForStudentDetails() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.fragmentcontainer,
						LoginScreenStudentDetailsFragment
								.newInstance(new Bundle())).addToBackStack("1")
				.commit();
	}

}
