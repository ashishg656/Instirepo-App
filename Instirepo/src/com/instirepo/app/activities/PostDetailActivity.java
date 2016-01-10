package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.objects.PostListSinglePostObject;

import android.os.Bundle;

public class PostDetailActivity extends BaseActivity {

	PostListSinglePostObject postListSinglePostObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_detail_activity_layout);

		if (getIntent().hasExtra("postobj")) {
			postListSinglePostObject = getIntent().getExtras().getParcelable(
					"postobj");
			setInitialDataUsingnIntentObj();
		}
	}

	private void setInitialDataUsingnIntentObj() {
		
	}

}
