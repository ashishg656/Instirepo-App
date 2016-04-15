package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.fragments.AboutUsFragment;
import com.instirepo.app.fragments.CreditsFragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity implements OnClickListener {

	TextView aboutUs, contactUs, credits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_activity_layout);

		aboutUs = (TextView) findViewById(R.id.aboutus);
		contactUs = (TextView) findViewById(R.id.contactus);
		credits = (TextView) findViewById(R.id.credits);
		toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Instirepo");

		aboutUs.setOnClickListener(this);
		contactUs.setOnClickListener(this);
		credits.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aboutus:
			switchToAboutFragment();
			break;
		case R.id.credits:
			switchToCreditsFragment();
			break;
		case R.id.contactus:
			sendEmailIntentUsingToAction(getResources().getString(R.string.instirepo_support_email));
			break;

		default:
			break;
		}
	}

	void switchToAboutFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentcontainer, AboutUsFragment.newInstance(new Bundle()))
				.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack("sd")
				.commit();
		getSupportActionBar().setTitle("About Us");
	}

	void switchToCreditsFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentcontainer, CreditsFragment.newInstance(new Bundle()))
				.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack("sd1")
				.commit();
		getSupportActionBar().setTitle("Credits");
	}

	@Override
	public void onBackPressed() {
		getSupportActionBar().setTitle("Instirepo");
		super.onBackPressed();
	}
}
