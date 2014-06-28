package com.hoot.ui.fragment;

import com.hoot.wifiplaza.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class BaseListFragment<Result, Input> extends ListFragment {
	private static final String TAG = "BaseListFragment";
	protected View mLoadingHint;
	protected FrameLayout mListContainer;
	protected LinearLayout mProgressContainer;
	private boolean mListShown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
		mListContainer = (FrameLayout) view.findViewById(R.id.listContainer);
		mProgressContainer = (LinearLayout) view
				.findViewById(R.id.progressContainer);
		return view;
	}

	private void setListShown(boolean shown, boolean animate) {
		if (mListShown == shown) {
			return;
		}
		mListShown = shown;
		if (shown) {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
			}
			mProgressContainer.setVisibility(View.GONE);
			mListContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}

	@Override
	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false);
	}
}
