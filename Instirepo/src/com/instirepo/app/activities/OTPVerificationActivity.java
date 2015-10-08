package com.instirepo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.instirepo.app.R;

public class OTPVerificationActivity extends BaseActivity {

	TextView mobileNumber;
	Button verifyButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.otp_verification_activity_layout);

		verifyButton = (Button) findViewById(R.id.verify);
		mobileNumber = (TextView) findViewById(R.id.mobile);

		verifyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mobileNumber.getText().toString().length() != 10) {
					makeToast("Enter valid 10 digit mobile number");
				} else {
//					Intent in = new Intent(OTPVerificationActivity.this,
//							VerifyMobile.class);
//					in.putExtra("app_id", "62eeda7365b54afeb3076bb");
//					in.putExtra("access_token",
//							"30cd92320a71c4407e3651df093113b4fef09daa");
//					String mobile = VerifyMobile
//							.getCountryCode(getApplicationContext())
//							+ mobileNumber.getText().toString();
//					in.putExtra("mobile", mobile);
//					startActivityForResult(in, VerifyMobile.REQUEST_CODE);
				}
			}
		});
	}

	void makeToast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
//		if (arg0 == VerifyMobile.REQUEST_CODE) {
//			String message = arg2.getStringExtra("message");
//			int result = arg2.getIntExtra("result", 0);
//			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
//					.show();
//			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
//					.show();
//		}
	}

}
