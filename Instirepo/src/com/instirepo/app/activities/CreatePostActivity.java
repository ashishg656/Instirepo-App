package com.instirepo.app.activities;

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
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.fragments.CreatePostFragment1OtherCategory;

@SuppressLint("NewApi")
public class CreatePostActivity extends BaseActivity implements AppConstants {

	CreatePostFragment1OtherCategory createPostFragment1OtherCategory;
	int touchX, touchY;
	View circularRevealView;
	int deviceHeight;
	LinearLayout mainLayoutForFragment;
	int toolbarHeight;
	AppBarLayout appBarLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_post_activity_layout);

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

		setFirstFragmentForOthersCategory();

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

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to discard this post?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				CreatePostActivity.this.finish();
			}
		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
