package com.instirepo.app.fragments;

import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class CreatePostSavePostVisibilitiesFragment extends BaseFragment
		implements OnClickListener {

	TextView cancelButton, saveButton;
	EditText name;

	public static CreatePostSavePostVisibilitiesFragment newInstance(Bundle b) {
		CreatePostSavePostVisibilitiesFragment frg = new CreatePostSavePostVisibilitiesFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(
						R.layout.create_post_select_name_for_visibility_collection_fragment,
						container, false);

		cancelButton = (TextView) v.findViewById(R.id.cancel);
		saveButton = (TextView) v.findViewById(R.id.save);
		name = (EditText) v.findViewById(R.id.enrollmentnumebr);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		cancelButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			getActivity().onBackPressed();
			break;
		case R.id.save:
			if (name.getText().toString().trim().length() == 0) {
				makeToast("Enter Name");
			} else {
				((CreatePostActivity) getActivity())
						.sendRequestForSavingPostVisibilitiesOnServer(name
								.getText().toString().trim());
			}
			break;

		default:
			break;
		}
	}
}
