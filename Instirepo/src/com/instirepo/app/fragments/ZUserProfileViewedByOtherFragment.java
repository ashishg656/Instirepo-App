package com.instirepo.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.anim.TouchInterceptFrameLayoutUserProfile;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;
import com.instirepo.app.widgets.ObservableScrollViewListener;

public class ZUserProfileViewedByOtherFragment extends BaseFragment {

	int heightOfUserDetailCard;
	int marginTopForUserDetailCard;
	LinearLayout userProfileDetail, scrollViewLinearLayout;
	ObservableScrollView scrollView;
	CircularImageView circularImageView;

	int userProfileImageHeight;
	TouchInterceptFrameLayoutUserProfile touchInterceptFrameLayoutUserProfile;

	public static ZUserProfileViewedByOtherFragment newInstance(Bundle b) {
		ZUserProfileViewedByOtherFragment frg = new ZUserProfileViewedByOtherFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.user_profile_viewed_by_other_fragment_layout,
				container, false);

		userProfileDetail = (LinearLayout) v.findViewById(R.id.userprofilecard);
		scrollView = (ObservableScrollView) v
				.findViewById(R.id.scrollviewuserrofiel);
		circularImageView = (CircularImageView) v
				.findViewById(R.id.userprofilecircularimage);
		scrollViewLinearLayout = (LinearLayout) v
				.findViewById(R.id.scrollviewlinearlayout);
		touchInterceptFrameLayoutUserProfile = (TouchInterceptFrameLayoutUserProfile) v
				.findViewById(R.id.touchinterceptframelayouruserprof);

		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		touchInterceptFrameLayoutUserProfile.scrollView = scrollView;

		userProfileImageHeight = getActivity().getResources()
				.getDimensionPixelSize(R.dimen.z_user_profile_image_height);

		userProfileDetail.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						userProfileDetail.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						heightOfUserDetailCard = userProfileDetail.getHeight();

						performCalculationsAfterViewTreeObserver();
					}
				});
	}

	void performCalculationsAfterViewTreeObserver() {
		marginTopForUserDetailCard = (int) (getActivity().getResources()
				.getDisplayMetrics().heightPixels / 3);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) userProfileDetail
				.getLayoutParams();
		params.topMargin = marginTopForUserDetailCard;
		userProfileDetail.setLayoutParams(params);

		params = (FrameLayout.LayoutParams) circularImageView.getLayoutParams();
		params.topMargin = marginTopForUserDetailCard - userProfileImageHeight
				/ 2;
		circularImageView.setLayoutParams(params);

		scrollView.setPadding(0,
				(int) (marginTopForUserDetailCard + heightOfUserDetailCard), 0,
				0);

		scrollView.setScrollListnerer(new ObservableScrollViewListener() {

			@Override
			public void onScrollStopped() {

			}

			@Override
			public void onScroll(int x, int y, int oldx, int oldy) {
				int location[] = new int[2];
				scrollViewLinearLayout.getLocationInWindow(location);
				Log.w("as", "scr " + scrollView.getScrollY() + " top  "
						+ location[1]);

				// TODO do animation
				userProfileDetail.setTranslationY(-scrollView.getScrollY());
				circularImageView.setTranslationY(-scrollView.getScrollY());
			}
		});

		// scrollView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// Log.w("as", "hello");
		// return false;
		// }
		// });
	}
}
