package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.LoginScreenFragment2Object;
import com.instirepo.app.objects.LoginScreenFragment2Object.Batches;
import com.instirepo.app.objects.LoginScreenFragment2Object.Branches;
import com.instirepo.app.objects.LoginScreenFragment2Object.Years;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CustomExpandableListView;

public class CreatePostSelectBatchFragment extends BaseFragment implements
		AppConstants, ZUrls, OnClickListener {

	ExpandableListView expandableListViewParent;
	LoginScreenFragment2Object mData;
	TextView okButton;
	ArrayList<Integer> batches;
	ArrayList<String> batchesString;

	public static CreatePostSelectBatchFragment newInstance(Bundle b) {
		CreatePostSelectBatchFragment frg = new CreatePostSelectBatchFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.create_post_select_batch_fragment_layout, container,
				false);

		expandableListViewParent = (ExpandableListView) v
				.findViewById(R.id.exlvlevel1);
		setProgressLayoutVariablesAndErrorVariables(v);
		okButton = (TextView) v.findViewById(R.id.okbuttonseen);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		okButton.setOnClickListener(this);
		batches = ((CreatePostActivity) getActivity()).batchArray;
		batchesString = ((CreatePostActivity) getActivity()).batchArrayString;

		loadData();
	}

	private void loadData() {
		showLoadingLayout();
		hideErrorLayout();

		StringRequest req = new StringRequest(Method.POST,
				userRegistrationStep1Url, new Listener<String>() {
					@Override
					public void onResponse(String res) {
						mData = new Gson().fromJson(res,
								LoginScreenFragment2Object.class);

						if (getActivity() != null) {
							expandableListViewParent
									.setAdapter(new ParentLevel());
						}

						hideErrorLayout();
						hideLoadingLayout();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						hideLoadingLayout();
						showErrorLayout();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req,
				userRegistrationStep1Url);
	}

	public class ParentLevel extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			ArrayList<Years> years = new ArrayList<>();
			Branches branch = (Branches) getGroup(groupPosition);
			for (int i = 0; i < mData.getYears_list().size(); i++) {
				if (mData.getYears_list().get(i).getBranch_id() == branch
						.getBranch_id()) {
					years.add(mData.getYears_list().get(i));
				}
			}
			return years.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			CustomExpandableListView SecondLevelexplv = new CustomExpandableListView(
					getActivity());

			ArrayList<Years> years = new ArrayList<>();
			Branches branch = (Branches) getGroup(groupPosition);
			for (int i = 0; i < mData.getYears_list().size(); i++) {
				if (mData.getYears_list().get(i).getBranch_id() == branch
						.getBranch_id()) {
					years.add(mData.getYears_list().get(i));
				}
			}

			SecondLevelexplv.setAdapter(new SecondLevelAdapter(years));
			SecondLevelexplv.setGroupIndicator(null);

			return SecondLevelexplv;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mData.getBranches_list().get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return mData.getBranches_list().size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View v = LayoutInflater.from(getActivity()).inflate(
					R.layout.batch_group_level1, parent, false);

			Branches branch = (Branches) getGroup(groupPosition);
			TextView tv = (TextView) v.findViewById(R.id.text);
			ImageView iv = (ImageView) v.findViewById(R.id.batchimage1);

			tv.setText(branch.getBranch_name());
			GradientDrawable gd = (GradientDrawable) iv.getBackground();
			gd.setColor(getActivity().getResources().getColor(
					R.color.z_red_color_primary));

			return v;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	public class SecondLevelAdapter extends BaseExpandableListAdapter {

		List<Years> years;

		public SecondLevelAdapter(List<Years> years) {
			this.years = years;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			ArrayList<Batches> batches = new ArrayList<>();
			Years year = (Years) getGroup(groupPosition);
			for (int i = 0; i < mData.getBatches_list().size(); i++) {
				if (mData.getBatches_list().get(i).getYear_id() == year
						.getYear_id()) {
					batches.add(mData.getBatches_list().get(i));
				}
			}
			return batches.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			View v = LayoutInflater.from(getActivity()).inflate(
					R.layout.batch_group_level3, parent, false);

			Batches batch = (Batches) getChild(groupPosition, childPosition);
			TextView tv = (TextView) v.findViewById(R.id.text);
			tv.setText(batch.getBatch_name());

			CheckBox checkBox = (CheckBox) v.findViewById(R.id.check);

			if (batches.contains(batch.getBatch_id())) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}
			checkBox.setTag(R.integer.z_batch_tag_id, batch);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Batches batch = (Batches) buttonView
							.getTag(R.integer.z_batch_tag_id);
					if (isChecked) {
						if (!batches.contains(batch.getBatch_id())) {
							batches.add(batch.getBatch_id());
							batchesString.add(batch.getBatch_name());
						}
					} else {
						if (batches.contains(batch.getBatch_id())) {
							int id = batches.indexOf(batch.getBatch_id());
							batches.remove(id);
							batchesString.remove(id);
						}
					}

					makeToast("len " + batches.size());
				}
			});

			return v;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			ArrayList<Batches> batches = new ArrayList<>();
			Years year = (Years) getGroup(groupPosition);
			for (int i = 0; i < mData.getBatches_list().size(); i++) {
				if (mData.getBatches_list().get(i).getYear_id() == year
						.getYear_id()) {
					batches.add(mData.getBatches_list().get(i));
				}
			}
			return batches.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return years.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return years.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View v = LayoutInflater.from(getActivity()).inflate(
					R.layout.batch_group_level2, parent, false);

			Years year = (Years) getGroup(groupPosition);
			TextView tv = (TextView) v.findViewById(R.id.text);
			ImageView iv = (ImageView) v.findViewById(R.id.batchimage1);

			tv.setText(year.getAdmission_year() + " - "
					+ year.getPassout_year());
			GradientDrawable gd = (GradientDrawable) iv.getBackground();
			gd.setColor(getActivity().getResources().getColor(
					R.color.purple_post));

			return v;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okbuttonseen:
			((CreatePostActivity) getActivity()).updateBatchesList(
					batchesString, batches);
			break;

		default:
			break;
		}
	}
}
