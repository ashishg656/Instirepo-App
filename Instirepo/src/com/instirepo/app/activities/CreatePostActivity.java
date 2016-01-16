package com.instirepo.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.fragments.CreatePostFragment1OtherCategory;
import com.instirepo.app.fragments.CreatePostFragment2;
import com.instirepo.app.fragments.CreatePostSavePostVisibilitiesFragment;
import com.instirepo.app.fragments.CreatePostSelectBranchFragment;
import com.instirepo.app.fragments.CreatePostSelectTeacherFragment;
import com.instirepo.app.fragments.CreatePostSelectYearOrBatchFragment;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.objects.CreatePostDataToSendToServer;
import com.instirepo.app.objects.DropboxFilesObject;
import com.instirepo.app.objects.LoginScreenFragment2Object;
import com.instirepo.app.preferences.ZPreferences;

@SuppressLint("NewApi")
public class CreatePostActivity extends BaseActivity implements AppConstants,
		ZUrls {

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

	boolean isSavePostVisibilityCollectionRequestRunning;
	ProgressDialog progressDialog;

	public LoginScreenFragment2Object loginScreenFragment2Object;

	CreatePostDataToSendToServer createPostDataToSendToServer;

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
		getSupportActionBar().setTitle("Create Post");

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

	public void setSecondFragmentForPostVisibility(
			CreatePostDataToSendToServer data) {
		createPostDataToSendToServer = data;

		createPostDataToSendToServer.setCategoryId(categoryId);

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
					|| createPostFragment1OtherCategory.dropboxFilesList.size() != 0) {
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
	}

	public void showFragmentForSelectingBranch(Bundle bundle) {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmtnholder,
						CreatePostSelectBranchFragment.newInstance(bundle))
				.addToBackStack("").commit();
	}

	public void showFragmentForSelectingYearOrBatch(Bundle bundle) {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmtnholder,
						CreatePostSelectYearOrBatchFragment.newInstance(bundle))
				.addToBackStack("").commit();
	}

	public void showFragmentForSelectingTeacher(Bundle bundle) {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmtnholder,
						CreatePostSelectTeacherFragment.newInstance(bundle))
				.addToBackStack("").commit();
	}

	public void showSavePostVisibilitiesFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmtnholder,
						CreatePostSavePostVisibilitiesFragment
								.newInstance(new Bundle())).addToBackStack("")
				.commit();
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

	public void updateYearsList(ArrayList<String> batchesString,
			ArrayList<Integer> batches) {
		this.batchArray = batches;
		this.batchArrayString = batchesString;
		createPostFragment2.updateToTextBoxInFragment2();
		super.onBackPressed();
	}

	public void updateTeachersList(ArrayList<Integer> teacherIds,
			ArrayList<String> teacherName) {
		this.teacherArray = teacherIds;
		this.teacherArrayString = teacherName;
		createPostFragment2.updateToTextBoxInFragment2();
		super.onBackPressed();
	}

	public void updateBatchesList(ArrayList<String> names,
			ArrayList<Integer> ids) {
		batchArray = ids;
		batchArrayString = names;
		createPostFragment2.updateToTextBoxInFragment2();
		super.onBackPressed();
	}

	public void callFragmentUpdateCustomFlowBox() {
		createPostFragment2.updateToTextBoxInFragment2();
	}

	public int getCountForSelectedVisibilities() {
		return branchesArray.size() + yearArray.size() + batchArray.size()
				+ teacherArray.size();
	}

	public void sendRequestForSavingPostVisibilitiesOnServer(final String name) {
		if (!isSavePostVisibilityCollectionRequestRunning) {
			isSavePostVisibilityCollectionRequestRunning = true;
			progressDialog = ProgressDialog.show(this, null, "Saving data");

			StringRequest req = new StringRequest(Method.POST,
					savePostVisibilities, new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							isSavePostVisibilityCollectionRequestRunning = false;
							((BaseActivity) CreatePostActivity.this)
									.showSnackBar("Saved Collection");

							if (progressDialog != null)
								progressDialog.dismiss();

							CreatePostActivity.this.onBackPressed();

							createPostFragment2.callHideSaveButtonFunction();

							changeToSecondViewPagerTabAfterSavingACollection();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							isSavePostVisibilityCollectionRequestRunning = false;
							makeToast("Unable to save data. Please check internet connection");

							if (progressDialog != null)
								progressDialog.dismiss();
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					HashMap<String, String> p = new HashMap<>();
					p.put("user_id", ZPreferences
							.getUserProfileID(CreatePostActivity.this));
					p.put("name", name);

					JSONArray arrayBatches = new JSONArray();
					for (int id : batchArray) {
						arrayBatches.put(id);
					}
					JSONArray arrayBranches = new JSONArray();
					for (int id : branchesArray) {
						arrayBranches.put(id);
					}
					JSONArray arrayYears = new JSONArray();
					for (int id : yearArray) {
						arrayYears.put(id);
					}
					JSONArray arrayTeachers = new JSONArray();
					for (int id : teacherArray) {
						arrayTeachers.put(id);
					}

					p.put("batches_id", arrayBatches.toString());
					p.put("branches_id", arrayBranches.toString());
					p.put("years_id", arrayYears.toString());
					p.put("teachers_id", arrayTeachers.toString());

					return p;
				}
			};
			ZApplication.getInstance().addToRequestQueue(req,
					savePostVisibilities);
		}
	}

	protected void changeToSecondViewPagerTabAfterSavingACollection() {
		createPostFragment2.viewPager.setCurrentItem(1, true);
		((BaseActivity) this)
				.showSnackBar("Now select one option from your collections/shortcuts to publish post");
	}

	public void clickOnCreateNewCollectionButtonFromAllCollectionsFragment() {
		createPostFragment2.viewPager.setCurrentItem(0, true);
		((BaseActivity) this)
				.showSnackBar("Select more than one options using the checkboxes and then click on save button here.");
	}

	public void showDialogConfirmationBeforeSendingPost(
			final int SELECTED_MODE_TO_CREATE_POST, int savedCollectionId) {
		createPostDataToSendToServer
				.setTypeOfPostVisibilty(SELECTED_MODE_TO_CREATE_POST);
		createPostDataToSendToServer.setSavedCollectionId(savedCollectionId);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (SELECTED_MODE_TO_CREATE_POST == Z_CREATE_POST_SELECTED_PEOPLE) {
			builder.setMessage("Are you sure you want to upload this post, which will be visible to the people you selected above?");
		} else if (SELECTED_MODE_TO_CREATE_POST == Z_CREATE_POST_PUBLIC) {
			builder.setMessage("Are you sure you want to upload this post, which will be visible to all (Public)?");
		} else if (SELECTED_MODE_TO_CREATE_POST == Z_CREATE_POST_SAVED_COLLECTION) {
			builder.setMessage("Are you sure you want to upload this post, which will be visible to the selected people/groups in the saved collection?");
		}

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				makeRequestToUploadPoostToServer();
			}
		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void makeRequestToUploadPoostToServer() {
		if (progressDialog != null)
			progressDialog.dismiss();

		progressDialog = ProgressDialog.show(this, "Creating Post",
				"Publishing post on server", false, false);

		StringRequest req = new StringRequest(Method.POST, uploadPostUrl,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}

						makeToast("Success");
						Intent i = new Intent(CreatePostActivity.this,
								HomeActivity.class);
						startActivity(i);
						CreatePostActivity.this.finish();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}

						makeToast("Error..Please Try again");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id",
						ZPreferences.getUserProfileID(CreatePostActivity.this));
				p.put("heading", createPostDataToSendToServer.getHeading());
				p.put("description",
						createPostDataToSendToServer.getDescription());
				p.put("company_name",
						createPostDataToSendToServer.getCompanyName());
				p.put("category_id",
						createPostDataToSendToServer.getCategoryId() + "");
				if (createPostDataToSendToServer.getCoverPicImage() != null) {
					p.put("cover_image",
							createPostDataToSendToServer.getCoverPicImage());
				}
				p.put("type_of_visibility",
						createPostDataToSendToServer.getTypeOfPostVisibilty()
								+ "");
				if (createPostDataToSendToServer.getTypeOfPostVisibilty() == Z_CREATE_POST_SAVED_COLLECTION) {
					p.put("saved_collection_id",
							createPostDataToSendToServer.getSavedCollectionId()
									+ "");
				} else if (createPostDataToSendToServer
						.getTypeOfPostVisibilty() == Z_CREATE_POST_SELECTED_PEOPLE) {
					JSONArray arrayBatches = new JSONArray();
					for (int id : batchArray) {
						arrayBatches.put(id);
					}
					JSONArray arrayBranches = new JSONArray();
					for (int id : branchesArray) {
						arrayBranches.put(id);
					}
					JSONArray arrayYears = new JSONArray();
					for (int id : yearArray) {
						arrayYears.put(id);
					}
					JSONArray arrayTeachers = new JSONArray();
					for (int id : teacherArray) {
						arrayTeachers.put(id);
					}

					p.put("batches_id", arrayBatches.toString());
					p.put("branches_id", arrayBranches.toString());
					p.put("years_id", arrayYears.toString());
					p.put("teachers_id", arrayTeachers.toString());
				}

				if (createPostDataToSendToServer.getDropboxFilesObject() != null
						&& createPostDataToSendToServer.getDropboxFilesObject()
								.size() > 0) {
					JSONArray dropboxFiles = new JSONArray();

					for (DropboxFilesObject dropfile : createPostDataToSendToServer
							.getDropboxFilesObject()) {
						String jsonString = new Gson().toJson(dropfile);
						try {
							JSONObject request = new JSONObject(jsonString);
							dropboxFiles.put(request);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					p.put("dropbox_files", dropboxFiles.toString());
				}

				if (categoryType.equals(AllPostCategoriesObject.categoryEvent)) {
					p.put("event_location",
							createPostDataToSendToServer.eventLocation);
					p.put("event_fee", createPostDataToSendToServer.eventFee);
					p.put("event_contact",
							createPostDataToSendToServer.eventContact);
					p.put("start_date", createPostDataToSendToServer.startDate);
					p.put("start_time", createPostDataToSendToServer.startTime);
					p.put("end_date", createPostDataToSendToServer.endDate);
					p.put("end_time", createPostDataToSendToServer.endTime);
				} else if (categoryType
						.equals(AllPostCategoriesObject.categoryPoll)) {
					JSONArray arrayChoices = new JSONArray();
					for (String ch : createPostDataToSendToServer.pollChoices)
						arrayChoices.put(ch);
					p.put("choices_poll", arrayChoices.toString());
				}
				return p;
			}
		};
		req.setShouldCache(false);
		req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		ZApplication.getInstance().addToRequestQueue(req, uploadPostUrl);
	}
}
