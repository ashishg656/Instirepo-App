package com.instirepo.app.activities;

import java.util.ArrayList;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.fragments.CreatePostFragment1OtherCategory;
import com.instirepo.app.fragments.CreatePostFragment2;
import com.instirepo.app.fragments.CreatePostSelectBranchYearFragment;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.objects.LoginScreenFragment2Object;

@SuppressLint("NewApi")
public class CreatePostActivity extends BaseActivity implements AppConstants {

	CreatePostFragment1OtherCategory createPostFragment1OtherCategory;
	CreatePostFragment2 createPostFragment2;
	int touchX, touchY;
	View circularRevealView;
	int deviceHeight;
	LinearLayout mainLayoutForFragment;
	int toolbarHeight;
	AppBarLayout appBarLayout;

	int categoryId;
	public String categoryName, categoryType;
	public boolean isFirstFragmentVisible;

	public ArrayList<Integer> branchesArray, yearArray, batchArray,
			teacherArray;
	public ArrayList<String> branchesArrayString, yearArrayString,
			batchArrayString, teacherArrayString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_post_activity_layout);

		branchesArray = new ArrayList<>();
		batchArray = new ArrayList<>();
		yearArray = new ArrayList<>();
		teacherArray = new ArrayList<>();
		branchesArrayString = new ArrayList<>();
		batchArrayString = new ArrayList<>();
		yearArrayString = new ArrayList<>();
		teacherArrayString = new ArrayList<>();

		categoryId = getIntent().getExtras().getInt("catid");
		categoryName = getIntent().getExtras().getString("catname");
		categoryType = getIntent().getExtras().getString("cattype");

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		circularRevealView = (View) findViewById(R.id.circularereavelaveiw);
		mainLayoutForFragment = (LinearLayout) findViewById(R.id.linearlayoutcreatepost);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		toolbarHeight = getResources().getDimensionPixelSize(
				R.dimen.z_toolbar_height);
		appBarLayout.setTranslationY(-toolbarHeight);

		touchX = getIntent().getExtras().getInt("touchx");
		touchY = getIntent().getExtras().getInt("touchy");
		deviceHeight = getResources().getDisplayMetrics().heightPixels;

		circularRevealView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						try {
							circularRevealView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} catch (Exception e) {
							circularRevealView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
						showAndHideCircularRevealView();
					}
				});
		if (categoryType
				.equalsIgnoreCase(AllPostCategoriesObject.categoryEvent)) {

		} else if (categoryType
				.equalsIgnoreCase(AllPostCategoriesObject.categoryPoll)) {

		} else
			setFirstFragmentForOthersCategory();
	}

	private void showAndHideCircularRevealView() {
		SupportAnimator anim = ViewAnimationUtils.createCircularReveal(
				circularRevealView, touchX, touchY, 0, deviceHeight);
		anim.setDuration(500);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.addListener(new ZCircularAnimatorListener() {
			@Override
			public void onAnimationEnd() {
				mainLayoutForFragment.setVisibility(View.VISIBLE);
				circularRevealView.animate().alpha(0).setDuration(300)
						.setListener(new ZAnimatorListener() {
							@Override
							public void onAnimationEnd(Animator animation) {
								circularRevealView.setVisibility(View.GONE);
								appBarLayout.animate().translationY(0)
										.setDuration(250).start();
							}
						}).start();
			}
		});
		anim.start();
	}

	void setFirstFragmentForOthersCategory() {
		createPostFragment1OtherCategory = CreatePostFragment1OtherCategory
				.newInstance(new Bundle());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmtnholder, createPostFragment1OtherCategory)
				.commit();
	}

	public void setSecondFragmentForPostVisibility() {
		createPostFragment2 = CreatePostFragment2.newInstance(new Bundle());
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragmtnholder, createPostFragment2)
				.addToBackStack("").commit();
	}

	@Override
	public void onBackPressed() {
		if (isFirstFragmentVisible && isFormsFilled()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to discard this post?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							CreatePostActivity.this.finish();
						}
					});
			builder.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		} else {
			super.onBackPressed();
		}
	}

	boolean isFormsFilled() {
		if (createPostFragment1OtherCategory != null) {
			if (getEdittextLength(createPostFragment1OtherCategory.postHeading)
					|| getEdittextLength(createPostFragment1OtherCategory.postDescription)
					|| getEdittextLength(createPostFragment1OtherCategory.postCompanyName)
					|| createPostFragment1OtherCategory.roundedImageView
							.getDrawable() != null
					|| createPostFragment1OtherCategory.fileUrls.size() != 0) {
				return true;
			}
		}
		return false;
	}

	boolean getEdittextLength(EditText et) {
		if (et.getText().toString().trim().length() == 0)
			return false;
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (createPostFragment1OtherCategory != null
				&& requestCode == CreatePostFragment1OtherCategory.REQUEST_CODE_CREATOR) {
			createPostFragment1OtherCategory.onActivityResult(requestCode,
					resultCode, data);
		}
	}

	public void showFragmentForSelectingBranch(Bundle bundle) {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmtnholder,
						CreatePostSelectBranchYearFragment.newInstance(bundle))
				.addToBackStack("").commit();
	}

	public void updateBranchesList(long[] checkedItemIds,
			LoginScreenFragment2Object mData) {
		branchesArray = new ArrayList<>();
		branchesArrayString = new ArrayList<>();
		for (int i = 0; i < checkedItemIds.length; i++) {
			branchesArray.add(mData.getBranches_list()
					.get((int) checkedItemIds[i]).getBranch_id());
			branchesArrayString.add(mData.getBranches_list()
					.get((int) checkedItemIds[i]).getBranch_name());
		}
		createPostFragment2.updateToTextBoxInFragment2();
		super.onBackPressed();
	}
}
