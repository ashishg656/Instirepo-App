package com.instirepo.app.activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;

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
		postHeading = (TextView) findViewById(R.id.postHeading);
		postDescription = (TextView) findViewById(R.id.postdesciption);
		category = (TextView) findViewById(R.id.postcategoy);
		uploaderName = (TextView) findViewById(R.id.uploadrname);
		uploadTime = (TextView) findViewById(R.id.utime);
		numberOfPeopleViewed = (TextView) findViewById(R.id.peopleviewednumebr);
		votes = (TextView) findViewById(R.id.numberofupvotes);
		comments = (TextView) findViewById(R.id.numberofcomments);
		viewSeensButton = (TextView) findViewById(R.id.vieewseens);
		followPost = (TextView) findViewById(R.id.followposttext);
		reportPost = (TextView) findViewById(R.id.reportposttext);
		uploaderImage = (CircularImageView) findViewById(R.id.circularimage);

		if (getIntent().hasExtra("postobj")) {
			postListSinglePostObject = getIntent().getExtras().getParcelable(
					"postobj");
			setInitialDataUsingnIntentObj();
		}
	}

	private void setInitialDataUsingnIntentObj() {
		if (postListSinglePostObject != null) {
			if (postListSinglePostObject.getImage() != null) {
				ImageRequestManager.get(this).requestImage(
						this,
						postImage,
						ZApplication.getImageUrl(postListSinglePostObject
								.getImage()), -1);
			}

			postHeading.setText(postListSinglePostObject.getHeading());
			postDescription.setText(postListSinglePostObject.getDescription());
			category.setText(postListSinglePostObject.getCategory());
			uploaderName.setText(postListSinglePostObject.getUser_name());
			uploadTime.setText(TimeUtils.getPostTime(postListSinglePostObject
					.getTime()));
			numberOfPeopleViewed.setText(postListSinglePostObject.getSeens()
					+ " people viewed");
			votes.setText(""
					+ (postListSinglePostObject.getUpvotes() - postListSinglePostObject
							.getDownvotes()));
			comments.setText(postListSinglePostObject.getComment()
					+ " comments");
			if (postListSinglePostObject.isIs_following()) {
				followPost.setText(getResources().getString(
						R.string.following_post));
			} else
				followPost.setText(getResources().getString(
						R.string.follow_post));
			
			if (postListSinglePostObject.isIs_reported())
				reportPost.setText(getResources().getString(
						R.string.reported_post));
			else
				reportPost.setText(getResources().getString(
						R.string.report_post));

			GradientDrawable categoryBg = (GradientDrawable) category
					.getBackground();
			int color = Color.parseColor(postListSinglePostObject
					.getCategory_color());
			categoryBg.setStroke(
					getResources().getDimensionPixelSize(R.dimen.z_one_dp),
					color);
			category.setTextColor(color);

			ImageRequestManager.get(this).requestImage(this, uploaderImage,
					postListSinglePostObject.getUser_image(), -1);
		}
	}

}
