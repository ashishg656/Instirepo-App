package com.instirepo.app.adapters;

import java.util.List;

import com.instirepo.app.R;
import com.instirepo.app.objects.SeenByPeopleObject;
import com.instirepo.app.objects.SeenByPeopleObject.PeopleSeenPost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SeenByPeopleListAdapter extends BaseAdapter {

	Context context;
	List<SeenByPeopleObject.PeopleSeenPost> mData;

	public SeenByPeopleListAdapter(Context context, List<PeopleSeenPost> mData) {
		super();
		this.context = context;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SeenByHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.seen_by_people_list_item_layout, parent, false);
			holder = new SeenByHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (SeenByHolder) convertView.getTag();

		return convertView;
	}

	class SeenByHolder {

		public SeenByHolder(View v) {

		}
	}

}
