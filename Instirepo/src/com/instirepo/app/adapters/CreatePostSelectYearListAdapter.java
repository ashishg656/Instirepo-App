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
import com.instirepo.app.objects.LoginScreenFragment2Object.Years;

public class CreatePostSelectYearListAdapter extends
		ArrayAdapter<LoginScreenFragment2Object.Years> {

	List<LoginScreenFragment2Object.Years> mData;
	Context context;

	public CreatePostSelectYearListAdapter(Context context,
			List<LoginScreenFragment2Object.Years> objects) {
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

		Years year = getItem(position);
		CheckedTextView textView = (CheckedTextView) convertView
				.findViewById(R.id.text1);
		textView.setText(year.getAdmission_year() + " - "
				+ year.getPassout_year());

		ArrayList<Integer> yearArray = ((CreatePostActivity) context).yearArray;
		if (yearArray != null && yearArray.contains(year.getYear_id())) {
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
