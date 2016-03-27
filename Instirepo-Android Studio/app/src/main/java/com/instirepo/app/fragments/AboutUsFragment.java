package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUsFragment extends BaseFragment {

	TextView text;

	public static AboutUsFragment newInstance(Bundle b) {
		AboutUsFragment frg = new AboutUsFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.about_us_fragment_layout, container, false);

		text = (TextView) v.findViewById(R.id.text);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		text.setText(getActivity().getResources().getString(R.string.about_us_string));
	}
}
