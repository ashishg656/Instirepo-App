package com.example.faceless.activities;

import android.os.Bundle;

import com.example.faceless.R;
import com.example.faceless.fragments.LoginFragment1;
import com.example.faceless.fragments.LoginFragment2;

public class LoginActivity extends BaseActivity {

	public String teamName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_activity_layout);

		setFirstFragment();
	}

	private void setFirstFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentcon, new LoginFragment1()).commit();
	}

	public void setSecondFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentcon, new LoginFragment2())
				.addToBackStack("Ad").commit();
	}

}
