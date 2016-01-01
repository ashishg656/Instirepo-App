package com.instirepo.app.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import serverApi.ImageRequestManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.bottomsheet.BottomSheet;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.MarkPostImportantObject;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.objects.UpvotePostObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.PEWImageView;

public class PostsByTeachersListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants,
		ZUrls {

	Context context;
	List<PostListSinglePostObject> mData;
	MyClickListener clickListener;
	boolean isMoreAllowed;

	int markImportantPosition;
	PostsHolderNormal markImportantHolder;

	int upvotePostPostition;
	PostsHolderNormal upvotePostHolder;

	int followPostPosition;
	int reportPostPosition;

	boolean isUpotePostRequestRunning, isMarkImpPostRequestRunning;

	public PostsByTeachersListAdapter(Context context,
			List<PostListSinglePostObject> mData, boolean isMoreAllowed) {
		super();
		this.context = context;
		this.mData = mData;
		this.isMoreAllowed = isMoreAllowed;
		clickListener = new MyClickListener();
	}

	public void addData(List<PostListSinglePostObject> data, boolean isMore) {
		mData.addAll(data);
		this.isMoreAllowed = isMore;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		if (isMoreAllowed == true)
			return mData.size() + 1;
		return mData.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position > mData.size() - 1)
			return Z_RECYCLER_VIEW_ITEM_LOADING;
		return Z_RECYCLER_VIEW_ITEM_NORMAL;
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCom, int pos) {
		pos = holderCom.getAdapterPosition();
		if (getItemViewType(pos) == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			final PostsHolderNormal holder = (PostsHolderNormal) holderCom;

			holder.overflowIcon.setTag(holder);
			holder.overflowIcon.setOnClickListener(clickListener);
			holder.seenByContainerLayout.setTag(holder);
			holder.seenByContainerLayout.setOnClickListener(clickListener);
			holder.commentsLayout.setTag(holder);
			holder.commentsLayout.setOnClickListener(clickListener);
			holder.openUserProfile.setTag(holder);
			holder.openUserProfile.setOnClickListener(clickListener);
			holder.upvotePostLayout.setTag(holder);
			holder.upvotePostLayout.setOnClickListener(clickListener);
			holder.savePostLayout.setTag(holder);
			holder.savePostLayout.setOnClickListener(clickListener);

			PostListSinglePostObject obj = mData.get(pos);

			holder.userName.setText(obj.getUser_name());
			holder.heading.setText(obj.getHeading());
			holder.description.setText(obj.getDescription());
			if (obj.getImage() == null) {
				holder.imagePost.setVisibility(View.GONE);
			} else {
				holder.imagePost.setVisibility(View.VISIBLE);
				ImageRequestManager.get(context).requestImage(context,
						holder.imagePost,
						ZApplication.getImageUrl(obj.getImage()), -1);
			}
			ImageRequestManager.get(context).requestImage(context,
					holder.userImage, obj.getUser_image(), -1);

			holder.time.setText(TimeUtils.getPostTime(obj.getTime()));
			holder.numberOfPeopleViewed.setText(obj.getSeens()
					+ " people viewed");
			holder.numberOfComments.setText("" + obj.getComment());
			holder.numberOfSaves.setText("" + obj.getSaves());
			holder.numberOfUpvotes.setText("" + obj.getUpvotes());

			if (obj.isHas_upvoted())
				holder.upvotePostLayout.setSelected(true);
			else
				holder.upvotePostLayout.setSelected(false);

			if (obj.isIs_saved())
				holder.savePostLayout.setSelected(true);
			else
				holder.savePostLayout.setSelected(false);

			holder.category.setText(obj.getCategory());
			GradientDrawable categoryBg = (GradientDrawable) holder.category
					.getBackground();
			int color = Color.parseColor(obj.getCategory_color());
			categoryBg.setStroke(
					context.getResources().getDimensionPixelSize(
							R.dimen.z_one_dp), color);
			holder.category.setTextColor(color);

			holder.upvotePostProgress.setVisibility(View.GONE);
			holder.savePostProgress.setVisibility(View.GONE);
			holder.upvotePostLayout.setVisibility(View.VISIBLE);
			holder.savePostLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.post_by_teacher_list_item_layout, arg0, false);
			PostsHolderNormal holder = new PostsHolderNormal(v);
			return holder;
		} else if (type == Z_RECYCLER_VIEW_ITEM_LOADING) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.loading_more, arg0, false);
			PostHolderLoading holder = new PostHolderLoading(v);
			return holder;
		}
		return null;
	}

	class PostHolderLoading extends RecyclerView.ViewHolder {

		public PostHolderLoading(View v) {
			super(v);
		}
	}

	class PostsHolderNormal extends RecyclerView.ViewHolder {

		LinearLayout overflowIcon;
		FrameLayout seenByContainerLayout, openUserProfile;
		ImageView commentsLayout, savePostLayout, upvotePostLayout;
		CircularImageView userImage;
		TextView userName, time, heading, description, numberOfPeopleViewed,
				numberOfUpvotes, numberOfSaves, numberOfComments;
		PEWImageView imagePost;
		TextView category;
		ProgressBar upvotePostProgress, savePostProgress;

		public PostsHolderNormal(View v) {
			super(v);
			overflowIcon = (LinearLayout) v.findViewById(R.id.overflowiconpost);
			seenByContainerLayout = (FrameLayout) v
					.findViewById(R.id.seenbycontainer);
			commentsLayout = (ImageView) v
					.findViewById(R.id.commentsviewconatiner);
			openUserProfile = (FrameLayout) v
					.findViewById(R.id.openuserprofilepost);
			userImage = (CircularImageView) v.findViewById(R.id.circularimage);
			userName = (TextView) v.findViewById(R.id.uploadrname);
			time = (TextView) v.findViewById(R.id.utime);
			heading = (TextView) v.findViewById(R.id.uptheading);
			description = (TextView) v.findViewById(R.id.uptdesc);
			imagePost = (PEWImageView) v.findViewById(R.id.pewimage);
			numberOfPeopleViewed = (TextView) v
					.findViewById(R.id.peopleviewednumebr);
			numberOfComments = (TextView) v.findViewById(R.id.numberofcomments);
			numberOfSaves = (TextView) v.findViewById(R.id.numberofsavs);
			numberOfUpvotes = (TextView) v.findViewById(R.id.numberofupvotes);
			savePostLayout = (ImageView) v.findViewById(R.id.savepostimage);
			upvotePostLayout = (ImageView) v.findViewById(R.id.upvotepostimage);
			category = (TextView) v.findViewById(R.id.postcategoy);
			upvotePostProgress = (ProgressBar) v
					.findViewById(R.id.upvotepostprogress);
			savePostProgress = (ProgressBar) v
					.findViewById(R.id.savepostprogress);
		}
	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.overflowiconpost:
				PostsHolderNormal holder = (PostsHolderNormal) v.getTag();
				int pos = holder.getAdapterPosition();
				showOverflowIconContent(mData.get(pos).getHeading(),
						mData.get(pos).getId(), mData.get(pos).isIs_saved(),
						pos, holder, mData.get(pos).isIs_following(), mData
								.get(pos).isIs_reported());
				break;
			case R.id.seenbycontainer:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition();
				showSeenByPeople(mData.get(pos).getId());
				break;
			case R.id.commentsviewconatiner:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition();
				showCommentsFragment(mData.get(pos).getId());
				break;
			case R.id.openuserprofilepost:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition();
				openUserProfileFragment(mData.get(pos).getUser_id(),
						mData.get(pos).getUser_name(), mData.get(pos)
								.getUser_image());
				break;
			case R.id.upvotepostimage:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition();
				upvotePost(mData.get(pos).getId(), pos, holder);
				break;
			case R.id.savepostimage:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition();
				markPostImportant(mData.get(pos).getId(), pos, holder);
				break;
			default:
				break;
			}
		}

	}

	public void showOverflowIconContent(String message, final int postid,
			boolean isSaved, final int pos, final PostsHolderNormal holder,
			boolean isFollowing, boolean isReported) {
		BottomSheet sheet = new BottomSheet.Builder((HomeActivity) context)
				.title(message).sheet(R.menu.posts_sliding_panel_menu)
				.listener(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case R.id.markimportant:
							markPostImportant(postid, pos, holder);
							break;
						case R.id.followpostmenusliding:
							followPostRequest(postid, pos);
							break;
						case R.id.erportpostslidingmeny:
							reportPostRequest(postid, pos);
							break;
						}
					}
				}).show();
		final Menu menu = sheet.getMenu();
		if (!isSaved) {
			menu.getItem(1).setIcon(R.drawable.ic_star_normal);
			menu.getItem(1).setTitle(
					context.getResources().getString(
							R.string.menu_mark_important));
		} else {
			menu.getItem(1).setIcon(R.drawable.ic_star_pressed);
			menu.getItem(1).setTitle(
					context.getResources().getString(
							R.string.menu_marked_important));
		}

		if (!isFollowing) {
			menu.getItem(2).setIcon(R.drawable.ic_social_notifications_grey);
			menu.getItem(2).setTitle(
					context.getResources().getString(R.string.follow_post));
		} else {
			menu.getItem(2).setIcon(R.drawable.ic_social_notifications_blue);
			menu.getItem(2).setTitle(
					context.getResources().getString(R.string.following_post));
		}

		if (!isReported) {
			menu.getItem(0).setIcon(R.drawable.ic_flag_normal);
			menu.getItem(0).setTitle(
					context.getResources().getString(R.string.report_post));
		} else {
			menu.getItem(0).setIcon(R.drawable.ic_flag_selected);
			menu.getItem(0).setTitle(
					context.getResources().getString(R.string.reported_post));
		}
	}

	protected void followPostRequest(final int postid, int pos) {
		followPostPosition = pos;
		mData.get(pos).setIs_following(!mData.get(pos).isIs_following());

		if (mData.get(pos).isIs_following()) {
			((BaseActivity) context).makeToast("Following Post");
		} else {
			((BaseActivity) context).makeToast("Unfollowed Post");
		}

		StringRequest req = new StringRequest(Method.POST, followPostRequest,
				new Listener<String>() {
					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						((BaseActivity) context)
								.makeToast("Unable to follow/unfollow post. Check internet and try agin");

						mData.get(followPostPosition)
								.setIs_following(
										!mData.get(followPostPosition)
												.isIs_following());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("post_id", postid + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, followPostRequest);
	}

	protected void reportPostRequest(final int postid, int pos) {
		reportPostPosition = pos;
		mData.get(pos).setIs_reported(!mData.get(pos).isIs_reported());

		if (mData.get(pos).isIs_reported()) {
			((BaseActivity) context).makeToast("Reported Post");
		} else {
			((BaseActivity) context).makeToast("Undo report Post");
		}

		StringRequest req = new StringRequest(Method.POST, reportPostUrl,
				new Listener<String>() {
					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						((BaseActivity) context)
								.makeToast("Unable to report/unreport post. Check internet and try agin");

						mData.get(reportPostPosition).setIs_reported(
								!mData.get(reportPostPosition).isIs_reported());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("post_id", postid + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, reportPostUrl);
	}

	public void upvotePost(final int id, int pos, PostsHolderNormal holder) {
		if (!isUpotePostRequestRunning) {
			isUpotePostRequestRunning = true;
			upvotePostPostition = pos;
			upvotePostHolder = holder;

			if (holder != null) {
				holder.upvotePostProgress.setVisibility(View.VISIBLE);
				holder.upvotePostLayout.setVisibility(View.GONE);
			}

			StringRequest req = new StringRequest(Method.POST, upvotePost,
					new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							isUpotePostRequestRunning = false;
							UpvotePostObject obj = new Gson().fromJson(arg0,
									UpvotePostObject.class);

							Log.w("as", "msg " + obj.getMessage());

							mData.get(upvotePostPostition).setUpvotes(
									obj.getUpvotes());
							mData.get(upvotePostPostition).setDownvotes(
									obj.getDownvotes());
							mData.get(upvotePostPostition).setHas_downvoted(
									obj.isHas_downvoted());
							mData.get(upvotePostPostition).setHas_upvoted(
									obj.isHas_upvoted());

							if (upvotePostHolder != null) {
								upvotePostHolder.numberOfUpvotes.setText(""
										+ obj.getUpvotes());
								if (obj.isHas_upvoted())
									upvotePostHolder.upvotePostLayout
											.setSelected(true);
								else
									upvotePostHolder.upvotePostLayout
											.setSelected(false);

								upvotePostHolder.upvotePostProgress
										.setVisibility(View.GONE);
								upvotePostHolder.upvotePostLayout
										.setVisibility(View.VISIBLE);
							}

							if (obj.isHas_upvoted()) {
								((BaseActivity) context)
										.showSnackBar("Successfully upvoted post");
							} else {
								((BaseActivity) context)
										.showSnackBar("Removed upvote from post");
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							isUpotePostRequestRunning = false;
							((BaseActivity) context)
									.showSnackBar("Some error occured. Check internet connection");

							if (upvotePostHolder != null) {
								upvotePostHolder.upvotePostProgress
										.setVisibility(View.GONE);
								upvotePostHolder.upvotePostLayout
										.setVisibility(View.VISIBLE);
							}
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					HashMap<String, String> p = new HashMap<>();
					p.put("user_id", ZPreferences.getUserProfileID(context));
					p.put("post_id", id + "");
					p.put("is_upvote_clicked", Boolean.toString(!mData.get(
							upvotePostPostition).isHas_upvoted()));
					return p;
				}
			};
			ZApplication.getInstance().addToRequestQueue(req,
					markPostAsImportant);
		}
	}

	public void markPostImportant(final int id, int pos,
			PostsHolderNormal holder) {
		if (!isMarkImpPostRequestRunning) {
			isMarkImpPostRequestRunning = true;
			markImportantPosition = pos;
			markImportantHolder = holder;

			if (holder != null) {
				holder.savePostProgress.setVisibility(View.VISIBLE);
				holder.savePostLayout.setVisibility(View.GONE);
			}

			StringRequest req = new StringRequest(Method.POST,
					markPostAsImportant, new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							isMarkImpPostRequestRunning = false;
							MarkPostImportantObject obj = new Gson().fromJson(
									arg0, MarkPostImportantObject.class);

							mData.get(markImportantPosition).setSaves(
									obj.getCount());
							mData.get(markImportantPosition).setIs_saved(
									obj.isIs_saved());

							if (markImportantHolder != null) {
								if (obj.isIs_saved()) {
									((BaseActivity) context)
											.showSnackBar("Marked Post As Important");
									markImportantHolder.savePostLayout
											.setSelected(true);
								} else {
									((BaseActivity) context)
											.showSnackBar("Removed Post From Important Posts List");
									markImportantHolder.savePostLayout
											.setSelected(false);
								}
								markImportantHolder.numberOfSaves.setText(obj
										.getCount() + "");

								markImportantHolder.savePostProgress
										.setVisibility(View.GONE);
								markImportantHolder.savePostLayout
										.setVisibility(View.VISIBLE);
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							isMarkImpPostRequestRunning = false;
							((BaseActivity) context)
									.showSnackBar("Some error occured. Check internet connection");

							if (markImportantHolder != null) {
								markImportantHolder.savePostProgress
										.setVisibility(View.GONE);
								markImportantHolder.savePostLayout
										.setVisibility(View.VISIBLE);
							}
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					HashMap<String, String> p = new HashMap<>();
					p.put("user_id", ZPreferences.getUserProfileID(context));
					p.put("post_id", id + "");
					return p;
				}
			};
			ZApplication.getInstance().addToRequestQueue(req,
					markPostAsImportant);
		}
	}

	public void showSeenByPeople(int postid) {
		((HomeActivity) context).switchToSeenByPeopleFragment(postid);
	}

	public void showCommentsFragment(int postid) {
		((HomeActivity) context).switchToCommentsFragment(postid);
	}

	void openUserProfileFragment(int id, String name, String image) {
		((HomeActivity) context).switchToUserProfileViewedByOtherFragment(id,
				name, image);
	}
}
