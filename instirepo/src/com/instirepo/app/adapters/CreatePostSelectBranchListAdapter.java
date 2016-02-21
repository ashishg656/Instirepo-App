package com.instirepo.app.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.objects.LoginScreenFragment2Object;
import com.instirepo.app.objects.LoginScreenFragment2Object.Branches;

public class CreatePostSelectBranchListAdapter extends
		ArrayAdapter<LoginScreenFragment2Object.Branches> {

	List<LoginScreenFragment2Object.Branches> mData;
	Context context;

	public CreatePostSelectBranchListAdapter(Context context,
			List<LoginScreenFragment2Object.Branches> objects) {
		super(context, 0, objects);
		this.context = context;
		this.mData = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.textview_select_branch_post_visibility, parent,
					false);
		}

		Branches branch = getItem(position);
		CheckedTextView textView = (CheckedTextView) convertView
				.findViewById(R.id.text1);
		textView.setText(branch.getBranch_name());

		ArrayList<Integer> branchesArray = ((CreatePostActivity) context).branchesArray;
		if (branchesArray != null
				&& branchesArray.contains(branch.getBranch_id())) {
			((ListView) parent).setItemChecked(position, true);
		} else {
			((ListView) parent).setItemChecked(position, false);
		}

		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
