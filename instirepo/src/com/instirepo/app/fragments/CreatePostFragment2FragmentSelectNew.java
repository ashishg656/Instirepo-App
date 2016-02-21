package com.instirepo.app.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.widgets.CustomFlowLayout;
import com.instirepo.app.widgets.CustomGoogleFloatingActionButton;

public class CreatePostFragment2FragmentSelectNew extends BaseFragment
		implements OnClickListener, ZUrls, AppConstants {

	ImageView batchImage, batchImage1, batchImage2, batchImage3, batchImage4;
	LinearLayout branchLayout, yearLayout, batchLayout, teacherLayout,
			publicLayout;
	CustomFlowLayout customFlowLayout;
	TextView saveVisibility;
	public CustomGoogleFloatingActionButton uploadButton;

	public static CreatePostFragment2FragmentSelectNew newInstance(Bundle b) {
		CreatePostFragment2FragmentSelectNew frg = new CreatePostFragment2FragmentSelectNew();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.create_post_fragment_2_fragment_select, container,
				false);

		batchImage = (ImageView) v.findViewById(R.id.batchimage);
		batchImage1 = (ImageView) v.findViewById(R.id.batchimage1);
		batchImage2 = (ImageView) v.findViewById(R.id.batchimage2);
		batchImage3 = (ImageView) v.findViewById(R.id.batchimage3);
		batchImage4 = (ImageView) v.findViewById(R.id.batchimage4);
		branchLayout = (LinearLayout) v.findViewById(R.id.branchlayout);
		customFlowLayout = (CustomFlowLayout) v.findViewById(R.id.customeflow);
		yearLayout = (LinearLayout) v.findViewById(R.id.yearlayout);
		batchLayout = (LinearLayout) v.findViewById(R.id.batchlayout);
		teacherLayout = (LinearLayout) v.findViewById(R.id.teacherseelc);
		saveVisibility = (TextView) v.findViewById(R.id.savevis);
		uploadButton = (CustomGoogleFloatingActionButton) v
				.findViewById(R.id.createpostfinalsetep);
		publicLayout = (LinearLayout) v
				.findViewById(R.id.publiclayoutcretaepostbutton);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		branchLayout.setOnClickListener(this);
		yearLayout.setOnClickListener(this);
		batchLayout.setOnClickListener(this);
		teacherLayout.setOnClickListener(this);
		saveVisibility.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		publicLayout.setOnClickListener(this);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) batchImage
				.getLayoutParams();
		int deviceWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		int widthImage = deviceWidth
				/ 3
				- 2
				* getActivity().getResources().getDimensionPixelSize(
						R.dimen.z_margin_large);
		params.height = widthImage;
		batchImage.setLayoutParams(params);
		batchImage2.setLayoutParams(params);
		batchImage1.setLayoutParams(params);
		batchImage3.setLayoutParams(params);
		batchImage4.setLayoutParams(params);

		GradientDrawable categoryBg = (GradientDrawable) batchImage
				.getBackground();
		categoryBg.setColor(getActivity().getResources().getColor(
				R.color.z_green_color_primary));

		categoryBg = (GradientDrawable) batchImage1.getBackground();
		categoryBg.setColor(getActivity().getResources().getColor(
				R.color.z_red_color_primary));

		categoryBg = (GradientDrawable) batchImage2.getBackground();
		categoryBg.setColor(getActivity().getResources().getColor(
				R.color.purple_post));

		categoryBg = (GradientDrawable) batchImage3.getBackground();
		categoryBg.setColor(getActivity().getResources().getColor(
				R.color.orange_post));

		categoryBg = (GradientDrawable) batchImage4.getBackground();
		categoryBg.setColor(getActivity().getResources().getColor(
				R.color.PrimaryDarkColor));

		((CreatePostActivity) getActivity()).callFragmentUpdateCustomFlowBox();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.branchlayout:
			Bundle bundle = new Bundle();
			bundle.putInt("option", Z_VISIBILIY_BRANCH);
			((CreatePostActivity) getActivity())
					.showFragmentForSelectingBranch(bundle);
			break;
		case R.id.yearlayout:
			bundle = new Bundle();
			bundle.putInt("option", Z_VISIBILIY_YEAR);
			((CreatePostActivity) getActivity())
					.showFragmentForSelectingYearOrBatch(bundle);
			break;
		case R.id.batchlayout:
			bundle = new Bundle();
			bundle.putInt("option", Z_VISIBILIY_BATCH);
			((CreatePostActivity) getActivity())
					.showFragmentForSelectingYearOrBatch(bundle);
			break;
		case R.id.teacherseelc:
			bundle = new Bundle();
			bundle.putInt("option", Z_VISIBILIY_BATCH);
			((CreatePostActivity) getActivity())
					.showFragmentForSelectingTeacher(bundle);
			break;
		case R.id.savevis:
			((CreatePostActivity) getActivity())
					.showSavePostVisibilitiesFragment();
			break;
		case R.id.createpostfinalsetep:
			((CreatePostActivity) getActivity())
					.showDialogConfirmationBeforeSendingPost(
							Z_CREATE_POST_SELECTED_PEOPLE, -1);
			break;
		case R.id.publiclayoutcretaepostbutton:
			((CreatePostActivity) getActivity())
					.showDialogConfirmationBeforeSendingPost(
							Z_CREATE_POST_PUBLIC, -1);
			break;

		default:
			break;
		}
	}

}
