package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetailActivity extends BaseActivity {

	PostListSinglePostObject postListSinglePostObject;
	ImageView postImage;
	TextView postHeading, postDescription, category, uploaderName, uploadTime,
			numberOfPeopleViewed, votes, comments, viewSeensButton, followPost,
			reportPost;
	CircularImageView uploaderImage;
	RecyclerView seenByRecyclerView;
	ObservableScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_detail_activity_layout);

		postImage = (ImageView) findViewById(R.id.postimage);

		if (getIntent().hasExtra("postobj")) {
			postListSinglePostObject = getIntent().getExtras().getParcelable(
					"postobj");
			setInitialDataUsingnIntentObj();
		}
	}

	private void setInitialDataUsingnIntentObj() {

	}

}
