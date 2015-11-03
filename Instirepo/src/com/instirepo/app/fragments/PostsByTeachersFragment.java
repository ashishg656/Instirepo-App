package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instirepo.app.R;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.adapters.PostsByTeachersListAdapter;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.objects.PostsListObject;

public class PostsByTeachersFragment extends BaseFragment {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	PostsByTeachersListAdapter adapter;

	public static PostsByTeachersFragment newInstance(Bundle b) {
		PostsByTeachersFragment frg = new PostsByTeachersFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.posts_by_teacher_fragment_layout,
				container, false);

		recyclerView = (RecyclerView) v
				.findViewById(R.id.postsbyreachersrecyclef);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					int pos = layoutManager.findFirstVisibleItemPosition();
					if (pos == 0) {
						((HomeActivity) getActivity())
								.setToolbarTranslation(recyclerView
										.getChildAt(0));
					} else
						((HomeActivity) getActivity())
								.scrollToolbarAfterTouchEnds();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				((HomeActivity) getActivity()).scrollToolbarBy(-dy);
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		addData();
	}

	private void addData() {
		PostsListObject mData = new PostsListObject();
		List<PostListSinglePostObject> posts = new ArrayList<>();
		for (int i = 0; i < 20; i++)
			posts.add(new PostListSinglePostObject());
		mData.setPosts(posts);
		adapter = new PostsByTeachersListAdapter(getActivity(), mData);

		recyclerView.setAdapter(adapter);
	}
}
