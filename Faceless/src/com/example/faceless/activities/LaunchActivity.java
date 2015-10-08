package com.example.faceless.activities;

import com.example.faceless.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LaunchActivity extends BaseActivity implements OnClickListener {

	Button joinTeam, createTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_activity_layout);

		joinTeam = (Button) findViewById(R.id.join);
		createTeam = (Button) findViewById(R.id.creeate);

		joinTeam.setOnClickListener(this);
		createTeam.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.join:
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			break;
		case R.id.creeate:
			i = new Intent(this, SignUpActivity.class);
			startActivity(i);

		default:
			break;
		}
	}

}
