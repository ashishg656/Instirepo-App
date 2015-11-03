package com.instirepo.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

	Toolbar toolbar;
	int toolbarHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	void showLoadingLayout() {

	}

	void hideLoadingLayout() {

	}

	void showErrorLayout() {

	}

	void hideErrorLayout() {

	}

	void makeToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
