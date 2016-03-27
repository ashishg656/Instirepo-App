package com.instirepo.app.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.objects.TeachersListObject.TeacherListSingleTeacher;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;

public class CreatePostSelectTeacherListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	boolean isMoreAllowed;
	List<TeacherListSingleTeacher> mData;

	public ArrayList<Integer> teacherIds;
	public ArrayList<String> teacherName;

	public CreatePostSelectTeacherListAdapter(Context context,
			List<TeacherListSingleTeacher> list, boolean isMoreAllowed) {
		super();
		this.context = context;
		this.mData = list;
		this.isMoreAllowed = isMoreAllowed;

		teacherIds = ((CreatePostActivity) context).teacherArray;
		teacherName = ((CreatePostActivity) context).teacherArrayString;
	}

	public void addData(List<TeacherListSingleTeacher> list,
			boolean isMoreAllowed2) {
		mData.addAll(list);
		this.isMoreAllowed = isMoreAllowed2;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (position > mData.size() - 1)
			return Z_RECYCLER_VIEW_ITEM_LOADING;
		return Z_RECYCLER_VIEW_ITEM_NORMAL;
	}

	@Override
	public int getItemCount() {
		if (isMoreAllowed)
			return mData.size() + 1;
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommom, int pos) {
		pos = holderCommom.getAdapterPosition();
		if (getItemViewType(pos) == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			SeenByHolder holder = (SeenByHolder) holderCommom;

			TeacherListSingleTeacher obj = mData.get(pos);

			holder.name.setText(obj.getName());
			ImageRequestManager.get(context).requestImage(context,
					holder.image, obj.getImage(), -1);
			holder.branch.setText(obj.getBranch());

			if (teacherIds.contains(obj.getId())) {
				holder.checkBox.setChecked(true);
			} else {
				holder.checkBox.setChecked(false);
			}

			holder.checkBox.setTag(R.integer.z_batch_tag_id, obj);
			holder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							TeacherListSingleTeacher teacher = (TeacherListSingleTeacher) buttonView
									.getTag(R.integer.z_batch_tag_id);
							if (isChecked) {
								if (!teacherIds.contains(teacher.getId())) {
									teacherIds.add(teacher.getId());
									teacherName.add(teacher.getName());
								}
							} else {
								if (teacherIds.contains(teacher.getId())) {
									int id = teacherIds.indexOf(teacher.getId());
									teacherIds.remove(id);
									teacherName.remove(id);
								}
							}
						}
					});
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.create_post_select_teacher_list_item_layout, arg0,
					false);
			SeenByHolder holder = new SeenByHolder(v);
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

	class SeenByHolder extends RecyclerView.ViewHolder {
		TextView name, branch;
		CircularImageView image;
		CheckBox checkBox;

		public SeenByHolder(View v) {
			super(v);
			name = (TextView) v.findViewById(R.id.seenbyname);
			image = (CircularImageView) v.findViewById(R.id.seenbyimage);
			branch = (TextView) v.findViewById(R.id.seenbytime);
			checkBox = (CheckBox) v.findViewById(R.id.check);
		}
	}

}
