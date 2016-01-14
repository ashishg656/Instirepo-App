package com.instirepo.app.activities;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.fragments.CommentsFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;

public class PostDetailActivity extends BaseActivity implements AppConstants,
		OnClickListener, ZUrls {

	PostListSinglePostObject postListSinglePostObject;
	ImageView postImage, reportPostImage, followPostImage, savePostImage,
			upvoteImage, downvoteImage;
	TextView postHeading, postDescription, category, uploaderName, uploadTime,
			numberOfPeopleViewed, votes, comments, viewSeensButton, followPost,
			reportPost;
	CircularImageView uploaderImage;
	RecyclerView seenByRecyclerView;
	ObservableScrollView scrollView;

	boolean previouslyUpvoted, previouslyDownvoted;
	int previousUpvotes, previousDownvotes;

	int postId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_detail_activity_layout);

		setProgressLayoutVariablesAndErrorVariables();

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
		reportPostImage = (ImageView) findViewById(R.id.rportposticoon);
		followPostImage = (ImageView) findViewById(R.id.followbuttonimage);
		savePostImage = (ImageView) findViewById(R.id.savepostbutton);
		upvoteImage = (ImageView) findViewById(R.id.upvotepostimage);
		downvoteImage = (ImageView) findViewById(R.id.downvotepostimage);

		findViewById(R.id.openuserprofilepost).setOnClickListener(this);
		findViewById(R.id.seenbycontainer).setOnClickListener(this);
		findViewById(R.id.commnts).setOnClickListener(this);
		findViewById(R.id.reportpostlayout).setOnClickListener(this);
		findViewById(R.id.followpostlayout).setOnClickListener(this);
		upvoteImage.setOnClickListener(this);
		downvoteImage.setOnClickListener(this);
		savePostImage.setOnClickListener(this);

		if (getIntent().hasExtra("postobj")) {
			postListSinglePostObject = getIntent().getExtras().getParcelable(
					"postobj");

			setImagesForPostAndUserImage();

			setInitialDataUsingnIntentObj();
		} else if (getIntent().hasExtra("postid")) {
			postId = getIntent().getExtras().getInt("postid");

			loadDataThroughPostID();
		}
	}

	private void setImagesForPostAndUserImage() {
		if (postListSinglePostObject.getImage() != null) {
			ImageRequestManager.get(this).requestImage(
					this,
					postImage,
					ZApplication.getImageUrl(postListSinglePostObject
							.getImage()), -1);
		}

		ImageRequestManager.get(this).requestImage(this, uploaderImage,
				postListSinglePostObject.getUser_image(), -1);
	}

	private void loadDataThroughPostID() {
		final String url = postDescriptionPage + "?post_id=" + postId;
		hideErrorLayout();
		showLoadingLayout();

		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						try {
							Entry entry = ZApplication.getInstance()
									.getRequestQueue().getCache().get(url);
							String data = new String(entry.data, "UTF-8");
							postListSinglePostObject = new Gson().fromJson(
									data, PostListSinglePostObject.class);
							setImagesForPostAndUserImage();
							setInitialDataUsingnIntentObj();
						} catch (Exception e) {
							showErrorLayout();
							hideLoadingLayout();
						}
					}
				});
		ZApplication.getInstance().addToRequestQueue(req, url);
	}

	private void setInitialDataUsingnIntentObj() {
		hideErrorLayout();
		hideLoadingLayout();

		if (postListSinglePostObject != null) {
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
				followPostImage.setSelected(true);
			} else {
				followPost.setText(getResources().getString(
						R.string.follow_post));
				followPostImage.setSelected(false);
			}

			if (postListSinglePostObject.isIs_reported()) {
				reportPost.setText(getResources().getString(
						R.string.reported_post));
				reportPostImage.setSelected(true);
			} else {
				reportPost.setText(getResources().getString(
						R.string.report_post));
				reportPostImage.setSelected(false);
			}

			savePostImage.setSelected(postListSinglePostObject.isIs_saved());
			upvoteImage.setSelected(postListSinglePostObject.isHas_upvoted());
			downvoteImage.setSelected(postListSinglePostObject
					.isHas_downvoted());

			GradientDrawable categoryBg = (GradientDrawable) category
					.getBackground();
			int color = Color.parseColor(postListSinglePostObject
					.getCategory_color());
			categoryBg.setStroke(
					getResources().getDimensionPixelSize(R.dimen.z_one_dp),
					color);
			category.setTextColor(color);
		}
	}

	public void reportPostButtonClick() {
		postListSinglePostObject.setIs_reported(!postListSinglePostObject
				.isIs_reported());
		setInitialDataUsingnIntentObj();

		if (postListSinglePostObject.isIs_reported()) {
			showSnackBar("Reported Post");
		} else
			showSnackBar("Undo Report Post");

		StringRequest req = new StringRequest(Method.POST, reportPostUrl,
				new Listener<String>() {
					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						postListSinglePostObject
								.setIs_reported(!postListSinglePostObject
										.isIs_reported());
						setInitialDataUsingnIntentObj();
						showSnackBar("Unable to report post. Check internet");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(PostDetailActivity.this));
				p.put("post_id", postListSinglePostObject.getId() + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, reportPostUrl);
	}

	public void followPostRequest() {
		postListSinglePostObject.setIs_following(!postListSinglePostObject
				.isIs_following());
		setInitialDataUsingnIntentObj();

		if (postListSinglePostObject.isIs_following()) {
			showSnackBar("Following Post");
		} else
			showSnackBar("Unfollowed post");

		StringRequest req = new StringRequest(Method.POST, followPostRequest,
				new Listener<String>() {
					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showSnackBar("Unable to follow/unfollow post. Check internet and try agin");
						postListSinglePostObject
								.setIs_following(!postListSinglePostObject
										.isIs_following());
						setInitialDataUsingnIntentObj();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(PostDetailActivity.this));
				p.put("post_id", postListSinglePostObject.getId() + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, followPostRequest);
	}

	public void upvoteOrDownvotePost(boolean isUpvoteClicked) {
		previousDownvotes = postListSinglePostObject.getDownvotes();
		previousUpvotes = postListSinglePostObject.getUpvotes();
		previouslyDownvoted = postListSinglePostObject.isHas_downvoted();
		previouslyUpvoted = postListSinglePostObject.isHas_upvoted();

		if (postListSinglePostObject.isHas_upvoted()) {
			// upvoted
			if (isUpvoteClicked) {
				showSnackBar("Already Upvoted");
				return;
			} else {
				showSnackBar("Neither upvoted nor downvoted");
				postListSinglePostObject.setHas_upvoted(false);
				postListSinglePostObject.setUpvotes(postListSinglePostObject
						.getUpvotes() - 1);
				setInitialDataUsingnIntentObj();
			}
		} else if (postListSinglePostObject.isHas_downvoted()) {
			// downvoted
			if (isUpvoteClicked) {
				showSnackBar("Neither upvoted nor downvoted");
				postListSinglePostObject.setHas_downvoted(false);
				postListSinglePostObject.setDownvotes(postListSinglePostObject
						.getDownvotes() - 1);
				setInitialDataUsingnIntentObj();
			} else {
				showSnackBar("Already Downvoted");
				return;
			}
		} else {
			// neither upvoted nor downvoted
			if (isUpvoteClicked) {
				showSnackBar("Upvoted");
				postListSinglePostObject.setHas_upvoted(true);
				postListSinglePostObject.setUpvotes(postListSinglePostObject
						.getUpvotes() + 1);
				setInitialDataUsingnIntentObj();
			} else {
				showSnackBar("Downvoted");
				postListSinglePostObject.setHas_downvoted(true);
				postListSinglePostObject.setDownvotes(postListSinglePostObject
						.getDownvotes() + 1);
				setInitialDataUsingnIntentObj();
			}
		}

		upvotePostRequest(isUpvoteClicked);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.openuserprofilepost:
			switchToUserProfileViewedByOtherFragment(
					postListSinglePostObject.getUser_id(),
					postListSinglePostObject.getUser_name(),
					postListSinglePostObject.getUser_image());
			break;
		case R.id.seenbycontainer:
			switchToSeenByPeopleFragment(postListSinglePostObject.getId());
			break;
		case R.id.commnts:
			switchToCommentsFragment(postListSinglePostObject.getId());
			break;
		case R.id.savepostbutton:
			savePostAsImportant();
			break;
		case R.id.upvotepostimage:
			upvoteOrDownvotePost(true);
			break;
		case R.id.downvotepostimage:
			upvoteOrDownvotePost(false);
			break;
		case R.id.reportpostlayout:
			reportPostButtonClick();
			break;
		case R.id.followpostlayout:
			followPostRequest();
			break;

		default:
			break;
		}
	}

	private void savePostAsImportant() {
		if (postListSinglePostObject.isIs_saved()) {
			postListSinglePostObject.setIs_saved(false);
			postListSinglePostObject.setSaves(postListSinglePostObject
					.getSaves() - 1);
			showSnackBar("Removed Post From Important Posts List");
		} else {
			postListSinglePostObject.setIs_saved(true);
			postListSinglePostObject.setSaves(postListSinglePostObject
					.getSaves() + 1);
			showSnackBar("Marked Post As important");
		}
		markPostImportantRequestServer();
		setInitialDataUsingnIntentObj();
	}

	private void reverseSavePostAsImportant() {
		if (postListSinglePostObject.isIs_saved()) {
			postListSinglePostObject.setIs_saved(false);
			postListSinglePostObject.setSaves(postListSinglePostObject
					.getSaves() - 1);
		} else {
			postListSinglePostObject.setIs_saved(true);
			postListSinglePostObject.setSaves(postListSinglePostObject
					.getSaves() + 1);
		}
		setInitialDataUsingnIntentObj();
	}

	public void upvotePostRequest(final boolean isUpvoteClicked) {
		StringRequest req = new StringRequest(Method.POST, upvotePost,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showSnackBar("Some error occured. Check internet connection");
						reverseUpvotePost();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(PostDetailActivity.this));
				p.put("post_id", postListSinglePostObject.getId() + "");
				p.put("is_upvote_clicked", Boolean.toString(isUpvoteClicked));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, markPostAsImportant);
	}

	protected void reverseUpvotePost() {
		postListSinglePostObject.setUpvotes(previousUpvotes);
		postListSinglePostObject.setDownvotes(previousDownvotes);
		postListSinglePostObject.setHas_downvoted(previouslyDownvoted);
		postListSinglePostObject.setHas_upvoted(previouslyUpvoted);

		setInitialDataUsingnIntentObj();
	}

	@Override
	public void onBackPressed() {
		Fragment fragmentUserProfile = getSupportFragmentManager()
				.findFragmentByTag(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG);
		Fragment fragmentComments = getSupportFragmentManager()
				.findFragmentByTag(Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG);

		if (fragmentUserProfile != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfile).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfile)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentComments != null) {
			CommentsFragment frg = (CommentsFragment) fragmentComments;
			if (frg.shouldGoBackOnBackButtonPress())
				super.onBackPressed();
		} else
			super.onBackPressed();
	}

	public void switchToSeenByPeopleFragment(int postid) {
		Bundle bundle = new Bundle();
		bundle.putInt("postid", postid);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						SeenByPeopleFragment.newInstance(bundle))
				.addToBackStack("tag").commit();
	}

	public void switchToCommentsFragment(int postid) {
		Bundle bundle = new Bundle();
		bundle.putInt("postid", postid);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						CommentsFragment.newInstance(bundle),
						Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG)
				.addToBackStack("Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG")
				.commit();
	}

	public void switchToUserProfileViewedByOtherFragment(int userid,
			String name, String image) {
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putInt("userid", userid);
		bundle.putString("image", image);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						UserProfileViewedByOtherFragment.newInstance(bundle),
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
				.addToBackStack(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
				.commit();
	}

	public void markPostImportantRequestServer() {
		StringRequest req = new StringRequest(Method.POST, markPostAsImportant,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showSnackBar("Some error occured. Check internet connection");
						reverseSavePostAsImportant();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(PostDetailActivity.this));
				p.put("post_id", postListSinglePostObject.getId() + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, markPostAsImportant);
	}
}
