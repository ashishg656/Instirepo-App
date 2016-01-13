package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.instirepo.app.adapters.CommentListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.AddCommentObject;
import com.instirepo.app.objects.CommentsListObject;
import com.instirepo.app.objects.CommentsListObject.CommentObject;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CustomGoogleFloatingActionButton;

@SuppressLint("NewApi")
public class CommentsFragment extends BaseFragment implements OnClickListener,
		ZUrls {

	ListView listView;
	CommentListAdapter adapter;
	CustomGoogleFloatingActionButton addCommentFab;
	public LinearLayout addCommentLayout;
	int dimen16, dimen56;
	private int deviceWidth;
	int location[];

	boolean isShowCommentLayoutAnimRunning;
	boolean shownAddCommentLayoutOnListScroll;

	boolean isRequestRunning, isMoreAllowed = true;
	Integer nextPage = 1;
	int postid;

	View footerView;
	LinearLayout sendComment;
	EditText commentBox;
	ImageView sendCommentImage;
	TextView numberOfComments;

	ProgressDialog progressDialog;

	public static CommentsFragment newInstance(Bundle b) {
		CommentsFragment frg = new CommentsFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.comments_fragment_layout, container,
				false);

		listView = (ListView) v.findViewById(R.id.commentlist);
		addCommentFab = (CustomGoogleFloatingActionButton) v
				.findViewById(R.id.adddocmmentfab);
		addCommentLayout = (LinearLayout) v.findViewById(R.id.commentlayoutadd);
		setProgressLayoutVariablesAndErrorVariables(v);
		sendComment = (LinearLayout) v.findViewById(R.id.sendcomment);
		commentBox = (EditText) v.findViewById(R.id.commentbox);
		numberOfComments = (TextView) v.findViewById(R.id.numberofcomments);
		sendCommentImage = (ImageView) v.findViewById(R.id.commentsendicon);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postid = getArguments().getInt("postid");
		sendCommentImage.setAlpha(0.5f);

		footerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.loading_more, null, false);

		dimen16 = getActivity().getResources().getDimensionPixelSize(
				R.dimen.z_margin_large);
		dimen56 = getActivity().getResources().getDimensionPixelSize(
				R.dimen.z_toolbar_height);
		deviceWidth = getActivity().getResources().getDisplayMetrics().widthPixels;

		addCommentFab.setOnClickListener(this);
		sendComment.setOnClickListener(this);

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount == totalItemCount - 1)
						&& addCommentLayout.getVisibility() == View.GONE
						&& !isShowCommentLayoutAnimRunning
						&& !shownAddCommentLayoutOnListScroll) {
					showCommentLayout();
					shownAddCommentLayoutOnListScroll = true;
				}

				int diff = totalItemCount
						- (firstVisibleItem + visibleItemCount);
				if (diff < 6 && !isRequestRunning && isMoreAllowed)
					loadData();
			}
		});

		commentBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0) {
					sendCommentImage
							.setImageResource(R.drawable.ic_send_disabled_grey);
					sendCommentImage.setAlpha(0.5f);
				} else {
					sendCommentImage.setImageResource(R.drawable.ic_send_white);
					sendCommentImage.setAlpha(1f);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		loadData();
	}

	private void loadData() {
		if (!isRequestRunning) {
			isRequestRunning = true;
			if (adapter == null) {
				showLoadingLayout();
				hideErrorLayout();
			}
			final String url = getCommentsOnPost + "?pagenumber=" + nextPage
					+ "&post_id=" + postid;
			StringRequest req = new StringRequest(Method.POST, url,
					new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							isRequestRunning = false;
							CommentsListObject obj = new Gson().fromJson(arg0,
									CommentsListObject.class);
							setAdapterData(obj);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							isRequestRunning = false;
							try {
								Cache cache = ZApplication.getInstance()
										.getRequestQueue().getCache();
								Entry entry = cache.get(url);
								String data = new String(entry.data, "UTF-8");
								CommentsListObject obj = new Gson().fromJson(
										data, CommentsListObject.class);
								setAdapterData(obj);
							} catch (Exception e) {
								if (adapter == null) {
									hideLoadingLayout();
									showErrorLayout();
								}
							}
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					HashMap<String, String> p = new HashMap<>();
					p.put("user_id",
							ZPreferences.getUserProfileID(getActivity()));
					return p;
				}
			};
			ZApplication.getInstance()
					.addToRequestQueue(req, getCommentsOnPost);
		}
	}

	protected void setAdapterData(CommentsListObject obj) {
		nextPage = obj.getNext_page();
		if (nextPage == null)
			isMoreAllowed = false;
		if (adapter == null) {
			hideLoadingLayout();
			hideErrorLayout();

			adapter = new CommentListAdapter(getActivity(), obj.getComments());
			if (isMoreAllowed)
				listView.addFooterView(footerView);
			listView.setAdapter(adapter);
		} else {
			adapter.addData(obj.getComments());
			if (!isMoreAllowed)
				listView.removeFooterView(footerView);
			adapter.notifyDataSetChanged();
		}

		numberOfComments.setText(obj.getCount() + " comments");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.adddocmmentfab) {
			showCommentLayout();
		} else if (v.getId() == R.id.sendcomment) {
			if (commentBox.getText().toString().length() > 0) {
				sendCommentToServer();
			}
		}
	}

	private void sendCommentToServer() {
		if (progressDialog != null)
			progressDialog.dismiss();
		progressDialog = ProgressDialog.show(getActivity(), "Adding comment",
				"Please wait. Posting your comment");

		StringRequest req = new StringRequest(Method.POST, addCommentOnPost,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						AddCommentObject obj = new Gson().fromJson(arg0,
								AddCommentObject.class);

						numberOfComments.setText(obj.getCount() + " comments");
						CommentObject commentObject = new CommentsListObject().new CommentObject();
						commentObject.setId(obj.getId());
						commentObject.setComment(commentBox.getText()
								.toString());
						commentObject.setTime("now");
						commentObject.setIs_by_user(true);
						commentObject.setUser_image(obj.getImage());
						commentObject.setUser_name(obj.getName());

						commentBox.setText("");
						commentBox.clearFocus();
						InputMethodManager imm = (InputMethodManager) getActivity()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getActivity()
								.getCurrentFocus().getWindowToken(), 0);

						adapter.addSingleComment(commentObject);
						adapter.notifyDataSetChanged();
						listView.scrollTo(0, 0);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						makeToast("Some error occured in posting comment. Please check internet and try again");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
				p.put("post_id", postid + "");
				p.put("comment", commentBox.getText().toString());
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, addCommentOnPost);
	}

	private void showCommentLayout() {
		isShowCommentLayoutAnimRunning = true;
		addCommentFab.animate().translationY(dimen16).translationX(-dimen16)
				.setInterpolator(new AccelerateInterpolator()).setDuration(150)
				.setListener(new ZAnimatorListener() {

					@Override
					public void onAnimationEnd(Animator animation) {
						location = new int[2];
						addCommentFab.getLocationInWindow(location);
						location[0] = location[0];
						addCommentFab.setVisibility(View.GONE);
						addCommentLayout.setVisibility(View.VISIBLE);

						SupportAnimator animator = ViewAnimationUtils
								.createCircularReveal(addCommentLayout,
										location[0] + dimen56 / 3, dimen56 / 2,
										dimen56 / 2, deviceWidth);
						animator.setInterpolator(new AccelerateDecelerateInterpolator());
						animator.setDuration(600);
						animator.addListener(new ZCircularAnimatorListener() {
							@Override
							public void onAnimationEnd() {
								isShowCommentLayoutAnimRunning = false;
							}
						});
						animator.start();
					}
				}).start();
	}

	void hideCommentLayout() {
		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				addCommentLayout, location[0] + dimen56 / 3, dimen56 / 2,
				deviceWidth, dimen56 / 2);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.setDuration(600);
		animator.addListener(new ZCircularAnimatorListener() {

			@Override
			public void onAnimationEnd() {
				addCommentFab.setVisibility(View.VISIBLE);
				addCommentLayout.setVisibility(View.GONE);
				addCommentFab
						.animate()
						.translationX(0)
						.translationY(0)
						.setDuration(80)
						.setInterpolator(new AccelerateDecelerateInterpolator())
						.setListener(new ZAnimatorListener() {
							@Override
							public void onAnimationEnd(Animator animation) {
								addCommentFab.setVisibility(View.VISIBLE);
								addCommentLayout.setVisibility(View.GONE);
							}
						}).start();
			}
		});
		animator.start();
	}

	public boolean shouldGoBackOnBackButtonPress() {
		if (addCommentLayout.getVisibility() == View.VISIBLE) {
			hideCommentLayout();
			return false;
		}
		return true;
	}
}
