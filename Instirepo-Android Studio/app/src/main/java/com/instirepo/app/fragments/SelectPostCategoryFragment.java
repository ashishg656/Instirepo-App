package com.instirepo.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.instirepo.app.R;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.adapters.SelectPostCategoryGridAdapter;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.AllPostCategoriesObject;

public class SelectPostCategoryFragment extends BaseFragment implements ZUrls {

	GridView gridView;
	SelectPostCategoryGridAdapter adapter;
	AllPostCategoriesObject allPostCategoriesObject;

	public static SelectPostCategoryFragment newInstance(Bundle b) {
		SelectPostCategoryFragment frg = new SelectPostCategoryFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.select_post_category_fragment_layout,
						container, false);

		gridView = (GridView) v.findViewById(R.id.selectcategorygrd);

		setProgressLayoutVariablesAndErrorVariables(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		allPostCategoriesObject = ((HomeActivity) getActivity()).allPostCategoriesObject;

		hideErrorLayout();
		hideLoadingLayout();

		if (allPostCategoriesObject != null
				&& allPostCategoriesObject.getCategories() != null) {
			adapter = new SelectPostCategoryGridAdapter(
					allPostCategoriesObject.getCategories(), getActivity());
			gridView.setAdapter(adapter);
		}
	}
}
