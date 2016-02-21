package com.instirepo.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SellProductFragment2ProductDetail extends BaseFragment {

	public static SellProductFragment2ProductDetail newInstance(Bundle b) {
		SellProductFragment2ProductDetail frg = new SellProductFragment2ProductDetail();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
