package com.instirepo.app.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.instirepo.app.activities.UserProfileActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.bottomsheet.BottomSheet;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.MarkPostImportantObject;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.objects.UpvotePostObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.PEWImageView;

public class MyPostsStudentsListAdapter extends
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

	boolean isUpotePostRequestRunning, isMarkImpPostRequestRunning;
	private boolean isUpvoteClickedButton;

	public MyPostsStudentsListAdapter(Context context,
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
			return mData.size() + 1 + 1;
		return mData.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return Z_USER_PROFILE_ITEM_HEADER;
		} else {
			if (position > mData.size() - 1 + 1)
				return Z_RECYCLER_VIEW_ITEM_LOADING;
			return Z_RECYCLER_VIEW_ITEM_NORMAL;
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCom, int pos) {
		pos = holderCom.getAdapterPosition();
		if (getItemViewType(pos) == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			pos = pos - 1;
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
			holder.downvotePostLayout.setTag(holder);
			holder.downvotePostLayout.setOnClickListener(clickListener);

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
			holder.numberOfUpvotes.setText(""
					+ (obj.getUpvotes() - obj.getDownvotes()));

			if (obj.isHas_upvoted()) {
				holder.upvotePostLayout.setSelected(true);
				holder.downvotePostLayout.setSelected(false);
			} else if (obj.isHas_downvoted()) {
				holder.upvotePostLayout.setSelected(false);
				holder.downvotePostLayout.setSelected(true);
			} else {
				holder.upvotePostLayout.setSelected(false);
				holder.downvotePostLayout.setSelected(false);
			}

			holder.category.setText(obj.getCategory());
			GradientDrawable categoryBg = (GradientDrawable) holder.category
					.getBackground();
			int color = Color.parseColor(obj.getCategory_color());
			categoryBg.setStroke(
					context.getResources().getDimensionPixelSize(
							R.dimen.z_one_dp), color);
			holder.category.setTextColor(color);

			holder.upvotePostProgress.setVisibility(View.GONE);
			holder.downvotePostProgress.setVisibility(View.GONE);
			holder.upvotePostLayout.setVisibility(View.VISIBLE);
			holder.downvotePostLayout.setVisibility(View.VISIBLE);
		} else if (getItemViewType(pos) == Z_USER_PROFILE_ITEM_HEADER) {
			FakeHeaderHolder holder = (FakeHeaderHolder) holderCom;
			ViewGroup.LayoutParams params = holder.header.getLayoutParams();
			params.height = ((UserProfileActivity) context).headerHeight;
			holder.header.setLayoutParams(params);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.post_by_student_list_item_layout, arg0, false);
			PostsHolderNormal holder = new PostsHolderNormal(v);
			return holder;
		} else if (type == Z_RECYCLER_VIEW_ITEM_LOADING) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.loading_more, arg0, false);
			PostHolderLoading holder = new PostHolderLoading(v);
			return holder;
		} else if (type == Z_USER_PROFILE_ITEM_HEADER) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.fake_header_user_profile, arg0, false);
			FakeHeaderHolder holder = new FakeHeaderHolder(v);
			return holder;
		}
		return null;
	}

	class FakeHeaderHolder extends RecyclerView.ViewHolder {

		LinearLayout header;

		public FakeHeaderHolder(View v) {
			super(v);
			header = (LinearLayout) v.findViewById(R.id.fakehader);
		}
	}

	class PostHolderLoading extends RecyclerView.ViewHolder {

		public PostHolderLoading(View v) {
			super(v);
		}
	}

	class PostsHolderNormal extends RecyclerView.ViewHolder {

		LinearLayout overflowIcon;
		FrameLayout seenByContainerLayout, openUserProfile;
		ImageView commentsLayout, upvotePostLayout, downvotePostLayout;
		CircularImageView userImage;
		TextView userName, time, heading, description, numberOfPeopleViewed,
				numberOfUpvotes, numberOfComments;
		PEWImageView imagePost;
		TextView category;
		ProgressBar upvotePostProgress, downvotePostProgress;

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
			numberOfUpvotes = (TextView) v.findViewById(R.id.numberofupvotes);
			upvotePostLayout = (ImageView) v.findViewById(R.id.upvotepostimage);
			category = (TextView) v.findViewById(R.id.postcategoy);
			downvotePostLayout = (ImageView) v
					.findViewById(R.id.downvotepostimage);
			upvotePostProgress = (ProgressBar) v
					.findViewById(R.id.upvotepostprogress);
			downvotePostProgress = (ProgressBar) v
					.findViewById(R.id.downvotepostprogress);
		}
	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.overflowiconpost:
				PostsHolderNormal holder = (PostsHolderNormal) v.getTag();
				int pos = holder.getAdapterPosition() - 1;
				showOverflowIconContent(mData.get(pos).getHeading(),
						mData.get(pos).getId(), mData.get(pos).isIs_saved(),
						pos, holder);
				break;
			case R.id.seenbycontainer:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition() - 1;
				showSeenByPeople(mData.get(pos).getId());
				break;
			case R.id.commentsviewconatiner:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition() - 1;
				showCommentsFragment(mData.get(pos).getId());
				break;
			case R.id.openuserprofilepost:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition() - 1;
				openUserProfileFragment(mData.get(pos).getUser_id(),
						mData.get(pos).getUser_name(), mData.get(pos)
								.getUser_image());
				break;
			case R.id.upvotepostimage:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition() - 1;
				upvotePost(mData.get(pos).getId(), pos, holder, true);
				break;
			case R.id.savepostimage:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition() - 1;
				markPostImportant(mData.get(pos).getId(), pos, holder);
				break;
			case R.id.downvotepostimage:
				holder = (PostsHolderNormal) v.getTag();
				pos = holder.getAdapterPosition() - 1;
				upvotePost(mData.get(pos).getId(), pos, holder, false);
				break;
			default:
				break;
			}
		}

	}

	public void showOverflowIconContent(String message, final int postid,
			boolean isSaved, final int pos, final PostsHolderNormal holder) {
		BottomSheet sheet = new BottomSheet.Builder((BaseActivity) context)
				.title(message).sheet(R.menu.posts_sliding_panel_menu)
				.listener(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case R.id.markimportant:
							markPostImportant(postid, pos, holder);
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
	}

	public void upvotePost(final int id, int pos, PostsHolderNormal holder,
			final boolean isUpvoteClicked) {
		if (!isUpotePostRequestRunning) {
			isUpotePostRequestRunning = true;
			upvotePostPostition = pos;
			upvotePostHolder = holder;
			isUpvoteClickedButton = isUpvoteClicked;

			if (holder != null && isUpvoteClicked) {
				holder.upvotePostLayout.setVisibility(View.GONE);
				holder.upvotePostProgress.setVisibility(View.VISIBLE);
			} else if (holder != null) {
				holder.downvotePostLayout.setVisibility(View.GONE);
				holder.downvotePostProgress.setVisibility(View.VISIBLE);
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
										+ (obj.getUpvotes() - obj
												.getDownvotes()));

								if (obj.isHas_upvoted()) {
									upvotePostHolder.upvotePostLayout
											.setSelected(true);
									upvotePostHolder.downvotePostLayout
											.setSelected(false);
								} else if (obj.isHas_downvoted()) {
									upvotePostHolder.upvotePostLayout
											.setSelected(false);
									upvotePostHolder.downvotePostLayout
											.setSelected(true);
								} else {
									upvotePostHolder.upvotePostLayout
											.setSelected(false);
									upvotePostHolder.downvotePostLayout
											.setSelected(false);
								}

								upvotePostHolder.upvotePostLayout
										.setVisibility(View.VISIBLE);
								upvotePostHolder.upvotePostProgress
										.setVisibility(View.GONE);
								upvotePostHolder.downvotePostLayout
										.setVisibility(View.VISIBLE);
								upvotePostHolder.downvotePostProgress
										.setVisibility(View.GONE);
							}

							if (obj.isHas_upvoted()) {
								((BaseActivity) context)
										.showSnackBar("Successfully upvoted post");
							} else if (obj.isHas_downvoted()) {
								((BaseActivity) context)
										.showSnackBar("Successfully downvoted post");
							} else {
								((BaseActivity) context)
										.showSnackBar("Removed vote from post");
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							isUpotePostRequestRunning = false;
							((BaseActivity) context)
									.showSnackBar("Some error occured. Check internet connection");

							if (upvotePostHolder != null) {
								upvotePostHolder.upvotePostLayout
										.setVisibility(View.VISIBLE);
								upvotePostHolder.upvotePostProgress
										.setVisibility(View.GONE);
								upvotePostHolder.downvotePostLayout
										.setVisibility(View.VISIBLE);
								upvotePostHolder.downvotePostProgress
										.setVisibility(View.GONE);
							}
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					HashMap<String, String> p = new HashMap<>();
					p.put("user_id", ZPreferences.getUserProfileID(context));
					p.put("post_id", id + "");
					p.put("is_upvote_clicked",
							Boolean.toString(isUpvoteClicked));
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
								} else {
									((BaseActivity) context)
											.showSnackBar("Removed Post From Important Posts List");
								}
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							isMarkImpPostRequestRunning = false;
							((BaseActivity) context)
									.showSnackBar("Some error occured. Check internet connection");
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
		((UserProfileActivity) context).switchToSeenByPeopleFragment(postid);
	}

	public void showCommentsFragment(int postid) {
		((UserProfileActivity) context).switchToCommentsFragment(postid);
	}

	void openUserProfileFragment(int id, String name, String image) {
		((UserProfileActivity) context)
				.switchToUserProfileViewedByOtherFragment(id, name, image);
	}
}
