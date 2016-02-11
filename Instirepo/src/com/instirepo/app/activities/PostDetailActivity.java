package com.instirepo.app.activities;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.instirepo.app.adapters.PostDetailSeenByListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.fragments.CommentsFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;
import com.instirepo.app.widgets.ObservableScrollViewListener;

public class PostDetailActivity extends BaseActivity implements AppConstants,
		OnClickListener, ZUrls, ObservableScrollViewListener,
		AppRequestListener {

	PostListSinglePostObject mData;
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

	View bookImageDividerView;
	int statusBarHeight, toolbarHeight;
	LinearLayout transparentToolbar;
	FrameLayout appbarContainer;

	PostDetailSeenByListAdapter adapterSeenByList;

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
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		transparentToolbar = (LinearLayout) findViewById(R.id.faketoolbarshopdetail);
		appbarContainer = (FrameLayout) findViewById(R.id.appbarcontainer);
		scrollView = (ObservableScrollView) findViewById(R.id.postdetailscrollview);
		bookImageDividerView = (View) findViewById(R.id.boomimagivideview);
		seenByRecyclerView = (RecyclerView) findViewById(R.id.viewiedbyrecycler);

		scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						try {
							scrollView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} catch (Exception e) {
							scrollView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
						statusBarHeight = getResources().getDisplayMetrics().heightPixels
								- scrollView.getHeight();
					}
				});

		toolbarHeight = getResources().getDimensionPixelSize(
				R.dimen.z_toolbar_height);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");

		appbarContainer.setTranslationY(-toolbarHeight);

		scrollView.setScrollListnerer(this);

		findViewById(R.id.openuserprofilepost).setOnClickListener(this);
		findViewById(R.id.seenbycontainer).setOnClickListener(this);
		findViewById(R.id.commnts).setOnClickListener(this);
		findViewById(R.id.reportpostlayout).setOnClickListener(this);
		findViewById(R.id.followpostlayout).setOnClickListener(this);
		upvoteImage.setOnClickListener(this);
		downvoteImage.setOnClickListener(this);
		savePostImage.setOnClickListener(this);

		if (getIntent().hasExtra("postobj")) {
			mData = getIntent().getExtras().getParcelable("postobj");

			setImagesForPostAndUserImage();

			setInitialDataUsingnIntentObj();

			loadSeensbyList();
		} else if (getIntent().hasExtra("postid")) {
			postId = getIntent().getExtras().getInt("postid");

			loadDataThroughPostID();
		}
	}

	private void setImagesForPostAndUserImage() {
		if (mData.getImage() != null) {
			ImageRequestManager.get(this).requestImage(this, postImage,
					ZApplication.getImageUrl(mData.getImage()), -1);
		}

		ImageRequestManager.get(this).requestImage(this, uploaderImage,
				mData.getUser_image(), -1);
	}

	private void loadDataThroughPostID() {
		final String url = postDescriptionPage + "?post_id=" + postId;
		hideErrorLayout();
		showLoadingLayout();

		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String data) {
						mData = new Gson().fromJson(data,
								PostListSinglePostObject.class);
						setImagesForPostAndUserImage();
						setInitialDataUsingnIntentObj();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						try {
							Entry entry = ZApplication.getInstance()
									.getRequestQueue().getCache().get(url);
							String data = new String(entry.data, "UTF-8");
							mData = new Gson().fromJson(data,
									PostListSinglePostObject.class);
							setImagesForPostAndUserImage();
							setInitialDataUsingnIntentObj();
						} catch (Exception e) {
							showErrorLayout();
							hideLoadingLayout();
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(PostDetailActivity.this));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, url);
	}

	private void loadSeensbyList() {
		final String url = postDescriptionPage + "?post_id=" + mData.getId();

		HashMap<String, String> p = new HashMap<>();
		p.put("user_id", ZPreferences.getUserProfileID(PostDetailActivity.this));

		CustomStringRequest req = new CustomStringRequest(Method.POST, url,
				"seenlistpostdetails", this, p);
		ZApplication.getInstance()
				.addToRequestQueue(req, "seenlistpostdetails");
	}

	private void setInitialDataUsingnIntentObj() {
		hideErrorLayout();
		hideLoadingLayout();

		if (mData != null) {
			getSupportActionBar().setTitle(mData.getHeading());

			postHeading.setText(mData.getHeading());
			postDescription.setText(mData.getDescription());
			category.setText(mData.getCategory());
			uploaderName.setText(mData.getUser_name());
			uploadTime.setText(TimeUtils.getPostTime(mData.getTime()));
			numberOfPeopleViewed.setText(mData.getSeens() + " people viewed");
			votes.setText("" + (mData.getUpvotes() - mData.getDownvotes()));
			comments.setText(mData.getComment() + " comments");
			if (mData.isIs_following()) {
				followPost.setText(getResources().getString(
						R.string.following_post));
				followPostImage.setSelected(true);
			} else {
				followPost.setText(getResources().getString(
						R.string.follow_post));
				followPostImage.setSelected(false);
			}

			if (mData.isIs_reported()) {
				reportPost.setText(getResources().getString(
						R.string.reported_post));
				reportPostImage.setSelected(true);
			} else {
				reportPost.setText(getResources().getString(
						R.string.report_post));
				reportPostImage.setSelected(false);
			}

			savePostImage.setSelected(mData.isIs_saved());
			upvoteImage.setSelected(mData.isHas_upvoted());
			downvoteImage.setSelected(mData.isHas_downvoted());

			GradientDrawable categoryBg = (GradientDrawable) category
					.getBackground();
			int color = Color.parseColor(mData.getCategory_color());
			categoryBg.setStroke(
					getResources().getDimensionPixelSize(R.dimen.z_one_dp),
					color);
			category.setTextColor(color);
		}

		setSeensByInRecyclerView();
	}

	private void setSeensByInRecyclerView() {
		if (mData.getSeens_by_list() != null
				&& mData.getSeens_by_list().size() >= 3) {
			LinearLayoutManager layoutManager = new LinearLayoutManager(this,
					LinearLayoutManager.HORIZONTAL, false);
			seenByRecyclerView.setLayoutManager(layoutManager);

			adapterSeenByList = new PostDetailSeenByListAdapter(
					mData.getSeens_by_list(), this);
			seenByRecyclerView.setAdapter(adapterSeenByList);

			seenByRecyclerView.setVisibility(View.VISIBLE);
		} else {
			seenByRecyclerView.setVisibility(View.GONE);
		}
	}

	public void reportPostButtonClick() {
		mData.setIs_reported(!mData.isIs_reported());
		setInitialDataUsingnIntentObj();

		if (mData.isIs_reported()) {
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
						mData.setIs_reported(!mData.isIs_reported());
						setInitialDataUsingnIntentObj();
						showSnackBar("Unable to report post. Check internet");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(PostDetailActivity.this));
				p.put("post_id", mData.getId() + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, reportPostUrl);
	}

	public void followPostRequest() {
		mData.setIs_following(!mData.isIs_following());
		setInitialDataUsingnIntentObj();

		if (mData.isIs_following()) {
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
						mData.setIs_following(!mData.isIs_following());
						setInitialDataUsingnIntentObj();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(PostDetailActivity.this));
				p.put("post_id", mData.getId() + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, followPostRequest);
	}

	public void upvoteOrDownvotePost(boolean isUpvoteClicked) {
		previousDownvotes = mData.getDownvotes();
		previousUpvotes = mData.getUpvotes();
		previouslyDownvoted = mData.isHas_downvoted();
		previouslyUpvoted = mData.isHas_upvoted();

		if (mData.isHas_upvoted()) {
			// upvoted
			if (isUpvoteClicked) {
				showSnackBar("Already Upvoted");
				return;
			} else {
				showSnackBar("Neither upvoted nor downvoted");
				mData.setHas_upvoted(false);
				mData.setUpvotes(mData.getUpvotes() - 1);
				setInitialDataUsingnIntentObj();
			}
		} else if (mData.isHas_downvoted()) {
			// downvoted
			if (isUpvoteClicked) {
				showSnackBar("Neither upvoted nor downvoted");
				mData.setHas_downvoted(false);
				mData.setDownvotes(mData.getDownvotes() - 1);
				setInitialDataUsingnIntentObj();
			} else {
				showSnackBar("Already Downvoted");
				return;
			}
		} else {
			// neither upvoted nor downvoted
			if (isUpvoteClicked) {
				showSnackBar("Upvoted");
				mData.setHas_upvoted(true);
				mData.setUpvotes(mData.getUpvotes() + 1);
				setInitialDataUsingnIntentObj();
			} else {
				showSnackBar("Downvoted");
				mData.setHas_downvoted(true);
				mData.setDownvotes(mData.getDownvotes() + 1);
				setInitialDataUsingnIntentObj();
			}
		}

		upvotePostRequest(isUpvoteClicked);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.openuserprofilepost:
			switchToUserProfileViewedByOtherFragment(mData.getUser_id(),
					mData.getUser_name(), mData.getUser_image());
			break;
		case R.id.seenbycontainer:
			switchToSeenByPeopleFragment(mData.getId());
			break;
		case R.id.commnts:
			switchToCommentsFragment(mData.getId());
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
		if (mData.isIs_saved()) {
			mData.setIs_saved(false);
			mData.setSaves(mData.getSaves() - 1);
			showSnackBar("Removed Post From Important Posts List");
		} else {
			mData.setIs_saved(true);
			mData.setSaves(mData.getSaves() + 1);
			showSnackBar("Marked Post As important");
		}
		markPostImportantRequestServer();
		setInitialDataUsingnIntentObj();
	}

	private void reverseSavePostAsImportant() {
		if (mData.isIs_saved()) {
			mData.setIs_saved(false);
			mData.setSaves(mData.getSaves() - 1);
		} else {
			mData.setIs_saved(true);
			mData.setSaves(mData.getSaves() + 1);
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
				p.put("post_id", mData.getId() + "");
				p.put("is_upvote_clicked", Boolean.toString(isUpvoteClicked));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, markPostAsImportant);
	}

	protected void reverseUpvotePost() {
		mData.setUpvotes(previousUpvotes);
		mData.setDownvotes(previousDownvotes);
		mData.setHas_downvoted(previouslyDownvoted);
		mData.setHas_upvoted(previouslyUpvoted);

		setInitialDataUsingnIntentObj();
	}

	@Override
	public void onBackPressed() {
		Fragment fragmentUserProfile = getSupportFragmentManager()
				.findFragmentByTag(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG);
		Fragment fragmentComments = getSupportFragmentManager()
				.findFragmentByTag(Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG);
		Fragment fragmentUserProfileOpenedFromCommentsListAdapter = getSupportFragmentManager()
				.findFragmentByTag(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG_FROM_COMMENT_LIST_ADAPTER);

		if (fragmentUserProfileOpenedFromCommentsListAdapter != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentUserProfileOpenedFromCommentsListAdapter != null
				&& ((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter).fragmentDestroyed) {
			super.onBackPressed();
		} else if (fragmentUserProfile != null
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
				p.put("post_id", mData.getId() + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, markPostAsImportant);
	}

	@Override
	public void onScrollStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(int x, int y, int oldx, int oldy) {
		float trans = y / 3;
		postImage.setTranslationY(trans);

		toolbarScrollChanges(y, oldy);
	}

	private void toolbarScrollChanges(int y, int oldy) {
		int dy = -1 * (y - oldy);
		int location[] = new int[5];
		bookImageDividerView.getLocationOnScreen(location);
		int actualLocation = location[1] - statusBarHeight;
		if (actualLocation < 0) {
			float trans = appbarContainer.getTranslationY() + dy;
			if (trans > 0)
				trans = 0;
			else if (trans < -toolbarHeight)
				trans = -toolbarHeight;
			appbarContainer.setTranslationY(trans);
		} else {
			if (actualLocation < toolbarHeight) {
				float trans = transparentToolbar.getTranslationY() + dy;
				if (trans > 0)
					trans = 0;
				else if (trans < -toolbarHeight)
					trans = -toolbarHeight;
				transparentToolbar.setTranslationY(trans);
				if (appbarContainer.getTranslationY() > -toolbarHeight) {
					float transAppbar = appbarContainer.getTranslationY() + dy;
					if (transAppbar > 0)
						transAppbar = 0;
					else if (transAppbar < -toolbarHeight)
						transAppbar = -toolbarHeight;
					appbarContainer.setTranslationY(transAppbar);
				}
			} else {
				transparentToolbar.setTranslationY(0);
				appbarContainer.animate().translationY(-toolbarHeight)
						.setDuration(100).start();
			}
		}
	}

	@Override
	public void onRequestStarted(String requestTag) {

	}

	@Override
	public void onRequestFailed(String requestTag, VolleyError error) {
		setSeensByInRecyclerView();
	}

	@Override
	public void onRequestCompleted(String requestTag, String response) {
		if (requestTag == "seenlistpostdetails") {
			PostListSinglePostObject obj = new Gson().fromJson(response,
					PostListSinglePostObject.class);
			mData.setSeens_by_list(obj.getSeens_by_list());
			setSeensByInRecyclerView();
		}
	}
}
