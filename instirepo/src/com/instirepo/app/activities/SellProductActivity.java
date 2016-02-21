package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SellProductActivity extends BaseActivity {

	View circularRevealView;
	int deviceHeight, deviceWidth;
	LinearLayout mainLayoutForFragment;
	int toolbarHeight;
	AppBarLayout appBarLayout;

	@SuppressLint("NewApi")
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
		getSupportActionBar().setTitle("Sell Product");

		toolbarHeight = getResources().getDimensionPixelSize(R.dimen.z_toolbar_height);
		appBarLayout.setTranslationY(-toolbarHeight);
		deviceHeight = getResources().getDisplayMetrics().heightPixels;
		deviceWidth = getResources().getDisplayMetrics().widthPixels;

		circularRevealView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				try {
					circularRevealView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} catch (Exception e) {
					circularRevealView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
				showAndHideCircularRevealView();
			}
		});

		setFirstFragmentForSelectingCategory();
	}

	private void setFirstFragmentForSelectingCategory() {
		// getSupportFragmentManager().beginTransaction().replace(R.id.fragmtnholder,
		// createPostFragment1OtherCategory)
		// .commit();
	}

	boolean getEdittextLength(EditText et) {
		if (et.getText().toString().trim().length() == 0)
			return false;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return true;
	}

	private void showAndHideCircularRevealView() {
		float finalRadius = (float) Math.sqrt((Math.pow(deviceWidth, 2)) + (Math.pow(deviceHeight, 2)));
		SupportAnimator anim = ViewAnimationUtils.createCircularReveal(circularRevealView, deviceWidth, deviceHeight, 0,
				finalRadius);
		anim.setDuration(500);
		anim.setInterpolator(new DecelerateInterpolator());
		anim.addListener(new ZCircularAnimatorListener() {
			@Override
			public void onAnimationEnd() {
				mainLayoutForFragment.setVisibility(View.VISIBLE);
				circularRevealView.animate().alpha(0).setDuration(200).setListener(new ZAnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						circularRevealView.setVisibility(View.GONE);
						appBarLayout.animate().translationY(0).setDuration(150).start();
					}
				}).start();
			}
		});
		anim.start();
	}
}
