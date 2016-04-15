package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.SupportAnimator.AnimatorListener;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.UpvotePostObject;
import com.instirepo.app.objects.UserProfileViewedByOtherObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.serverApi.ImageRequestManager.RequestBitmap;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;
import com.instirepo.app.widgets.ObservableScrollViewListener;

@SuppressLint("NewApi")
public class UserProfileViewedByOtherFragment extends BaseFragment implements ZUrls, OnClickListener {

	int heightOfUserDetailCard;
	int marginTopForUserDetailCard;
	LinearLayout userProfileDetail, scrollViewLinearLayout, mainContentLayout;
	ObservableScrollView scrollView;
	CircularImageView circularImageView;
	FrameLayout progressLayoutContainer;

	int userProfileImageHeight;
	FrameLayout touchInterceptFrameLayoutUserProfile;

	float initialY, initialTranslation;
	VelocityTracker mVelocityTracker;
	private float minFlingVelocity;
	float scrollViewCheckTranslationUp;
	private float deviceHeight, deviceWidth;

	View circularRevealView;
	public boolean fragmentDestroyed = false;

	int userId;
	String name, image;

	UserProfileViewedByOtherObject mData;

	TextView userName, designation, about, branch, batch, year, numberOfPosts, numberOfUpvotes, numberOfDownvotes,
			editProfile, blockUser, messageOnInstirepo, emailUser, callUser, upvotDownVoteIndicatorText, downloadResume;
	View messageDivider, emailDivider;
	LinearLayout voteUserLayout, contactUserLayout, resumeLayout;
	FrameLayout upvoteButton, downVoteButton;

	boolean isViewerSameAsUser;
	private boolean upvoteClickedOrDownvote;

	ProgressBar upvoteProgressBar, downvoteProgressBar;
	ImageView upvotePostImage, downvotePostImage;

	public static UserProfileViewedByOtherFragment newInstance(Bundle b) {
		UserProfileViewedByOtherFragment frg = new UserProfileViewedByOtherFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.user_profile_viewed_by_other_fragment_layout, container, false);

		userProfileDetail = (LinearLayout) v.findViewById(R.id.userprofilecard);
		scrollView = (ObservableScrollView) v.findViewById(R.id.scrollviewuserrofiel);
		circularImageView = (CircularImageView) v.findViewById(R.id.userprofilecircularimage);
		scrollViewLinearLayout = (LinearLayout) v.findViewById(R.id.scrollviewlinearlayout);
		touchInterceptFrameLayoutUserProfile = (FrameLayout) v.findViewById(R.id.touchinterceptframelayouruserprof);
		circularRevealView = (View) v.findViewById(R.id.revealviewuserprofiel);
		userName = (TextView) v.findViewById(R.id.username);
		setProgressLayoutVariablesAndErrorVariables(v);
		mainContentLayout = (LinearLayout) v.findViewById(R.id.maincontentlayout);
		designation = (TextView) v.findViewById(R.id.designation);
		progressLayoutContainer = (FrameLayout) v.findViewById(R.id.progresslayoutcontainer);
		about = (TextView) v.findViewById(R.id.about);
		branch = (TextView) v.findViewById(R.id.branch);
		batch = (TextView) v.findViewById(R.id.batch);
		year = (TextView) v.findViewById(R.id.year);
		numberOfPosts = (TextView) v.findViewById(R.id.posts);
		numberOfUpvotes = (TextView) v.findViewById(R.id.upvotes);
		numberOfDownvotes = (TextView) v.findViewById(R.id.downvotes);
		voteUserLayout = (LinearLayout) v.findViewById(R.id.voteuserlayout);
		contactUserLayout = (LinearLayout) v.findViewById(R.id.contactlayout);
		blockUser = (TextView) v.findViewById(R.id.blockuser);
		editProfile = (TextView) v.findViewById(R.id.viewfullprofile);
		resumeLayout = (LinearLayout) v.findViewById(R.id.resumelayout);
		messageOnInstirepo = (TextView) v.findViewById(R.id.messageoninstiepo);
		emailUser = (TextView) v.findViewById(R.id.emailuser);
		callUser = (TextView) v.findViewById(R.id.calluser);
		messageDivider = (View) v.findViewById(R.id.messagedownview);
		emailDivider = (View) v.findViewById(R.id.emaildownviwe);
		upvotDownVoteIndicatorText = (TextView) v.findViewById(R.id.uppvotedownvotes);
		upvoteButton = (FrameLayout) v.findViewById(R.id.upvotebutton);
		downVoteButton = (FrameLayout) v.findViewById(R.id.downvotebutton);
		upvoteProgressBar = (ProgressBar) v.findViewById(R.id.upvotepostprogress);
		downvoteProgressBar = (ProgressBar) v.findViewById(R.id.downupvotepostprogress);
		downvotePostImage = (ImageView) v.findViewById(R.id.downvotepostimage);
		upvotePostImage = (ImageView) v.findViewById(R.id.upvotepostimage);
		downloadResume = (TextView) v.findViewById(R.id.downloadresuem);

		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		userId = getArguments().getInt("userid");
		name = getArguments().getString("name");
		image = getArguments().getString("image");

		upvoteButton.setOnClickListener(this);
		downVoteButton.setOnClickListener(this);

		if (ZPreferences.getUserProfileID(getActivity()).equalsIgnoreCase(userId + "")) {
			isViewerSameAsUser = true;
		}

		userName.setText(name);
		ImageRequestManager.get(getActivity()).requestImage1(getActivity(), circularImageView, image,
				new RequestBitmap() {

					@Override
					public void onRequestCompleted(Bitmap bitmap) {
						circularImageView.setImageBitmap(bitmap);

						Palette.from(bitmap).generate(new PaletteAsyncListener() {

							@Override
							public void onGenerated(Palette p) {
								int colorLight = p.getDarkMutedColor(0x000000);
								int colorDark = p.getDarkVibrantColor(0x000000);

								userProfileDetail.setBackgroundColor(colorDark);
							}
						});
					}
				}, -1);

		deviceHeight = getActivity().getResources().getDisplayMetrics().heightPixels;
		deviceWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		touchInterceptFrameLayoutUserProfile.setTranslationY(deviceHeight);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progressLayoutContainer.getLayoutParams();
		params.height = (int) deviceHeight;
		progressLayoutContainer.setLayoutParams(params);

		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(circularRevealView, (int) (deviceWidth / 2),
				(int) (0.9 * deviceHeight), 0, deviceHeight);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(500);
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart() {
			}

			@Override
			public void onAnimationRepeat() {
			}

			@Override
			public void onAnimationEnd() {
				circularRevealView.animate().alpha(0).setDuration(300).setListener(new ZAnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						circularRevealView.setVisibility(View.GONE);
					}
				}).start();
			}

			@Override
			public void onAnimationCancel() {
			}
		});
		animator.start();

		ViewConfiguration viewConfiguration = ViewConfiguration.get(getActivity());
		minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 2;

		userProfileImageHeight = getActivity().getResources()
				.getDimensionPixelSize(R.dimen.z_user_profile_image_height);

		userProfileDetail.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				userProfileDetail.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				heightOfUserDetailCard = userProfileDetail.getHeight();

				performCalculationsAfterViewTreeObserver();
			}
		});

		setTouchListenersOnScrollView();

		loadData();
	}

	private void loadData() {
		hideErrorLayout();
		showLoadingLayout();
		mainContentLayout.setVisibility(View.GONE);

		String url = userProfileViewedByOther + "?profile_viewing_id=" + userId;
		StringRequest req = new StringRequest(Method.POST, url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				UserProfileViewedByOtherObject obj = new Gson().fromJson(arg0, UserProfileViewedByOtherObject.class);
				setScrollViewData(obj);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				try {
					Cache cache = ZApplication.getInstance().getRequestQueue().getCache();
					Entry entry = cache.get(userProfileViewedByOther + "?profile_viewing_id=" + userId);
					String data = new String(entry.data, "UTF-8");

					UserProfileViewedByOtherObject obj = new Gson().fromJson(data,
							UserProfileViewedByOtherObject.class);
					setScrollViewData(obj);
				} catch (Exception e) {
					hideLoadingLayout();
					showErrorLayout();

					setTouchListenersOnScrollView();
				}
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
				p.put("profile_viewing_id", userId + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, userProfileViewedByOther);
	}

	protected void setScrollViewData(UserProfileViewedByOtherObject obj) {
		hideErrorLayout();
		hideLoadingLayout();

		mData = obj;
		mainContentLayout.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progressLayoutContainer.getLayoutParams();
		params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
		progressLayoutContainer.setLayoutParams(params);

		designation.setText(mData.getDesignation());
		branch.setText("Branch : " + mData.getBranch());
		batch.setText("Batch : " + mData.getBatch());
		year.setText("Year : " + mData.getYear());
		numberOfPosts.setText("Number of Posts Posted : " + mData.getNumber_of_posts());
		numberOfUpvotes.setText("Number of Upvotes Received : " + mData.getUpvotes());
		numberOfDownvotes.setText("Number of Downvotes Received : " + mData.getDownvotes());

		if (isViewerSameAsUser) {
			voteUserLayout.setVisibility(View.GONE);
			contactUserLayout.setVisibility(View.GONE);
			blockUser.setVisibility(View.GONE);
			messageOnInstirepo.setVisibility(View.GONE);
			messageDivider.setVisibility(View.GONE);
		} else {
			editProfile.setVisibility(View.GONE);
		}

		editProfile.setOnClickListener(this);
		blockUser.setOnClickListener(this);
		downloadResume.setOnClickListener(this);

		setTextInBlockUserTextViewBasedOnBoolean();

		if (obj.getResume() == null) {
			resumeLayout.setVisibility(View.GONE);
		}

		about.setText(obj.getAbout());

		if (mData.getEmail() == null || mData.getEmail().trim().length() == 0) {
			emailDivider.setVisibility(View.GONE);
			emailUser.setVisibility(View.GONE);
		} else {
			emailUser.setText("Send email " + mData.getEmail());
			emailUser.setOnClickListener(this);
		}

		if (mData.getPhone() == null || mData.getPhone().trim().length() == 0) {
			callUser.setVisibility(View.GONE);
		} else {
			callUser.setText("Call : " + mData.getPhone());
			callUser.setOnClickListener(this);
		}

		if (callUser.getVisibility() == View.GONE && emailUser.getVisibility() == View.GONE
				&& messageOnInstirepo.getVisibility() == View.GONE) {
			contactUserLayout.setVisibility(View.GONE);
		}

		messageOnInstirepo.setText("Message " + mData.getName() + " on INSTIREPO");
		messageOnInstirepo.setOnClickListener(this);

		if (mData.isHas_downvoted()) {
			downVoteButton.setSelected(true);
		} else if (mData.isHas_upvoted()) {
			upvoteButton.setSelected(true);
		}

		upvotDownVoteIndicatorText.setText((mData.getUpvotes() - mData.getDownvotes()) + "");

		if (mData.isIs_professor() || mData.isIs_senior_professor()) {
			downVoteButton.setVisibility(View.GONE);
		}
	}

	void setTouchListenersOnScrollView() {
		scrollView.setScrollListnerer(new ObservableScrollViewListener() {

			@Override
			public void onScrollStopped() {

			}

			@Override
			public void onScroll(int x, int y, int oldx, int oldy) {
				try {
					int location[] = new int[2];
					scrollViewLinearLayout.getLocationInWindow(location);
					Log.w("as", "scr " + scrollView.getScrollY() + " top  " + location[1]);
				} catch (Exception e) {
				}

				// TODO do animation
				userProfileDetail.setTranslationY(-scrollView.getScrollY());
				circularImageView.setTranslationY(-scrollView.getScrollY());
			}
		});

		scrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int index = event.getActionIndex();
				int pointerId = event.getPointerId(index);

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					initialTranslation = touchInterceptFrameLayoutUserProfile.getTranslationY();
					initialY = event.getRawY();

					if (mVelocityTracker == null)
						mVelocityTracker = VelocityTracker.obtain();
					else
						mVelocityTracker.clear();
					mVelocityTracker.addMovement(event);

					return true;
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					try {
						mVelocityTracker.addMovement(event);

						float dy = initialY - event.getRawY();
						if ((dy < 0 && scrollView.getScrollY() == 0)
								|| touchInterceptFrameLayoutUserProfile.getTranslationY() != 0) {
							float trans = initialTranslation - dy;
							if (trans < 0) {
								scrollView.setTranslationY(0);
								return false;
							}
							touchInterceptFrameLayoutUserProfile.setTranslationY(trans);
							return true;
						}
					} catch (Exception e) {

					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					try {
						mVelocityTracker.addMovement(event);
						mVelocityTracker.computeCurrentVelocity(1000);
						float yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);

						if (scrollView.getScrollY() == 0) {
							if (Math.abs(yVelocity) < minFlingVelocity) {
								if (touchInterceptFrameLayoutUserProfile
										.getTranslationY() < scrollViewCheckTranslationUp / 2) {
									setScrollViewTranslation0();
								} else {
									dismissScrollViewDown();
								}
							} else if (yVelocity > 0) {
								dismissScrollViewDown();
							}
						}
					} catch (Exception e) {

					}
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
					if (mVelocityTracker != null)
						mVelocityTracker.recycle();
				}
				return false;
			}
		});
	}

	void performCalculationsAfterViewTreeObserver() {
		marginTopForUserDetailCard = (int) (deviceHeight / 3);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) userProfileDetail.getLayoutParams();
		params.topMargin = marginTopForUserDetailCard;
		userProfileDetail.setLayoutParams(params);

		params = (FrameLayout.LayoutParams) circularImageView.getLayoutParams();
		params.topMargin = marginTopForUserDetailCard - userProfileImageHeight / 2;
		circularImageView.setLayoutParams(params);

		scrollView.setPadding(0, (int) (marginTopForUserDetailCard + heightOfUserDetailCard), 0, 0);
		scrollViewCheckTranslationUp = deviceHeight - (marginTopForUserDetailCard);

		touchInterceptFrameLayoutUserProfile.animate().translationY(0).setDuration(500)
				.setInterpolator(new AccelerateDecelerateInterpolator()).start();
	}

	protected void setScrollViewTranslation0() {
		touchInterceptFrameLayoutUserProfile.animate().translationY(0).setDuration(300)
				.setInterpolator(new AccelerateDecelerateInterpolator()).start();
	}

	public void dismissScrollViewDown() {
		touchInterceptFrameLayoutUserProfile.animate().translationY(deviceHeight).setDuration(400)
				.setInterpolator(new AccelerateInterpolator()).setListener(new ZAnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						fragmentDestroyed = true;
						if (getActivity() != null)
							getActivity().onBackPressed();
					}
				}).start();
	}

	public void dismissScrollViewDownCalledFromActivityBackPressed() {
		circularRevealView.setVisibility(View.VISIBLE);
		circularRevealView.setAlpha(1);
		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(circularRevealView, (int) (deviceWidth / 2),
				(int) (0.9 * deviceHeight), deviceHeight, 0);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.setDuration(400);
		animator.start();

		touchInterceptFrameLayoutUserProfile.animate().translationY(deviceHeight).setDuration(400)
				.setInterpolator(new AccelerateInterpolator()).setListener(new ZAnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						fragmentDestroyed = true;
						if (getActivity() != null)
							getActivity().onBackPressed();
					}
				}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.upvotebutton:
			makeUpvoteDownvoteRequest(true);
			break;
		case R.id.downvotebutton:
			makeUpvoteDownvoteRequest(false);
			break;
		case R.id.viewfullprofile:
			((BaseActivity) getActivity()).openUserProfileActivity();
			break;
		case R.id.blockuser:
			makeRequestToBlockUser();
			break;
		case R.id.messageoninstiepo:
			((BaseActivity) getActivity()).openUserChatWithPersonUserActivity(userId, mData.getName(),
					mData.getImage());
			break;
		case R.id.downloadresuem:
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(mData.getResume()));
			startActivity(i);
			break;
		case R.id.emailuser:
			((BaseActivity) getActivity()).sendEmailIntentUsingToAction(mData.getEmail());
			break;
		case R.id.calluser:
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + mData.getPhone()));
			if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	private void makeRequestToBlockUser() {
		if (mData == null)
			return;

		mData.setIs_blocked(!mData.isIs_blocked());
		setTextInBlockUserTextViewBasedOnBoolean();

		StringRequest req = new StringRequest(Method.POST, blockUserRequestUrl, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				Log.w("As", "blocked user");
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				mData.setIs_blocked(!mData.isIs_blocked());
				setTextInBlockUserTextViewBasedOnBoolean();
				makeToast("Unable to send request to block user. Check internet");
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
				p.put("person_id", userId + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, blockUserRequestUrl);
	}

	void setTextInBlockUserTextViewBasedOnBoolean() {
		if (mData.isIs_blocked()) {
			blockUser.setText("UNBLOCK USER");
		} else {
			blockUser.setText("BLOCK USER");
		}
	}

	void makeUpvoteDownvoteRequest(final boolean upvoteClicked) {
		this.upvoteClickedOrDownvote = upvoteClicked;
		if (upvoteClicked) {
			upvotePostImage.setVisibility(View.GONE);
			upvoteProgressBar.setVisibility(View.VISIBLE);
		} else {
			downvotePostImage.setVisibility(View.GONE);
			downvoteProgressBar.setVisibility(View.VISIBLE);
		}
		StringRequest req = new StringRequest(Method.POST, upvoteUser, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				UpvotePostObject obj = new Gson().fromJson(arg0, UpvotePostObject.class);

				String snackBar = "";
				if (obj.isHas_downvoted()) {
					downVoteButton.setSelected(true);
					upvoteButton.setSelected(false);
					snackBar = "Downvoted User";
				} else if (obj.isHas_upvoted()) {
					upvoteButton.setSelected(true);
					downVoteButton.setSelected(false);
					snackBar = "Upvoted User";
				} else {
					upvoteButton.setSelected(false);
					downVoteButton.setSelected(false);
					snackBar = "Neither upvoted nor downvoted";
				}

				upvotDownVoteIndicatorText.setText((obj.getUpvotes() - obj.getDownvotes()) + "");

				numberOfUpvotes.setText("Number of Upvotes Received : " + obj.getUpvotes());
				numberOfDownvotes.setText("Number of Downvotes Received : " + obj.getDownvotes());

				((BaseActivity) getActivity()).showSnackBar(snackBar);

				upvotePostImage.setVisibility(View.VISIBLE);
				upvoteProgressBar.setVisibility(View.GONE);
				downvotePostImage.setVisibility(View.VISIBLE);
				downvoteProgressBar.setVisibility(View.GONE);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				((BaseActivity) getActivity()).showSnackBar("Some error occured. Check internet");

				upvotePostImage.setVisibility(View.VISIBLE);
				upvoteProgressBar.setVisibility(View.GONE);
				downvotePostImage.setVisibility(View.VISIBLE);
				downvoteProgressBar.setVisibility(View.GONE);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("upvoter_id", ZPreferences.getUserProfileID(getActivity()));
				p.put("user_to_be_voted", userId + "");
				if (downVoteButton.getVisibility() == View.GONE && upvoteButton.isSelected())
					upvoteClickedOrDownvote = false;
				p.put("is_upvote_clicked", Boolean.toString(upvoteClickedOrDownvote));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, upvoteUser);
	}
}
