package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.adapters.SeenByPeopleListAdapter;
import com.instirepo.app.objects.SeenByPeopleObject;
import com.instirepo.app.objects.SeenByPeopleObject.PeopleSeenPost;

public class SeenByPeopleFragment extends BaseFragment implements
		OnClickListener {

	ListView listView;
	SeenByPeopleListAdapter adapter;
	TextView okButton;

	public static SeenByPeopleFragment newInstance(Bundle b) {
		SeenByPeopleFragment frg = new SeenByPeopleFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.seen_by_poeple_dialog_fragment_layout, container,
				false);

		listView = (ListView) v.findViewById(R.id.postsbyreachersrecyclef);
		okButton = (TextView) v.findViewById(R.id.okbuttonseen);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		okButton.setOnClickListener(this);

		addData();
	}

	void addData() {
		List<PeopleSeenPost> mData = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			mData.add(new SeenByPeopleObject().new PeopleSeenPost());
		}
		adapter = new SeenByPeopleListAdapter(getActivity(), mData);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okbuttonseen:
			getActivity().onBackPressed();
			break;

		default:
			break;
		}
	}
}
