package com.instirepo.app.adapters;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instirepo.app.R;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.MessageListObject;
import com.instirepo.app.objects.MessageListObject.SingleMessage;

public class MessageListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	List<MessageListObject.SingleMessage> mData;
	boolean isMoreAllowed;
	Context context;

	public MessageListAdapter(List<SingleMessage> mData, boolean isMoreAllowed,
			Context context) {
		super();
		this.mData = mData;
		this.isMoreAllowed = isMoreAllowed;
		this.context = context;
	}

	public void addData(boolean isMore, List<SingleMessage> data) {
		mData.addAll(data);
		this.isMoreAllowed = isMore;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		if (isMoreAllowed)
			return mData.size() + 1;
		return mData.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == mData.size()) {
			return Z_MESSAGE_LIST_TYPE_LAODING;
		} else {
			if (mData.get(position).isIs_by_user())
				return Z_MESSAGE_LIST_TYPE_CHAT_BY_USER;
			else
				return Z_MESSAGE_LIST_TYPE_CHAT_BY_PERSON;
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holdercom, int pos) {
		if (getItemViewType(pos) == Z_MESSAGE_LIST_TYPE_CHAT_BY_PERSON
				|| getItemViewType(pos) == Z_MESSAGE_LIST_TYPE_CHAT_BY_USER) {
			MessageHolder holder = (MessageHolder) holdercom;
			
		} else {

		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int type) {
		if (type == Z_MESSAGE_LIST_TYPE_LAODING) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.loading_more, vg, false);
			PostHolderLoading holder = new PostHolderLoading(v);
			return holder;
		} else if (type == Z_MESSAGE_LIST_TYPE_CHAT_BY_USER) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.message_list_list_item_by_same_user, vg, false);
			MessageHolder holder = new MessageHolder(v);
			return holder;
		} else {
			View v = LayoutInflater.from(context).inflate(
					R.layout.message_list_list_item_by_different_user, vg,
					false);
			MessageHolder holder = new MessageHolder(v);
			return holder;
		}
	}

	class MessageHolder extends RecyclerView.ViewHolder {

		public MessageHolder(View v) {
			super(v);
		}
	}

	class PostHolderLoading extends RecyclerView.ViewHolder {

		public PostHolderLoading(View v) {
			super(v);
		}
	}

}
