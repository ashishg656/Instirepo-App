package com.instirepo.app.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.instirepo.app.R;
import com.instirepo.app.fragments.CreatePostFragment1OtherCategory;

public class ZCreatePostActivity extends BaseActivity {

	CreatePostFragment1OtherCategory createPostFragment1OtherCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_post_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		setFirstFragmentForOthersCategory();
	}

	void setFirstFragmentForOthersCategory() {
		createPostFragment1OtherCategory = CreatePostFragment1OtherCategory
				.newInstance(new Bundle());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmtnholder, createPostFragment1OtherCategory)
				.commit();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to discrad this post?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ZCreatePostActivity.this.finish();
			}
		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
