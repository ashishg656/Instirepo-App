package com.instirepo.app.widgets;

public interface ObservableScrollViewListener {

	public void onScrollStopped();

	public void onScroll(int x, int y, int oldx, int oldy);
}
