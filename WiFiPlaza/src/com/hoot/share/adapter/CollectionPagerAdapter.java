package com.hoot.share.adapter;

import com.hoot.ui.fragment.AppFragment;
import com.hoot.ui.fragment.DemoObjectFragment;
import com.hoot.ui.fragment.HotAppFragment;
import com.hoot.ui.fragment.HotFragment;
import com.hoot.ui.fragment.MainFragment;
import com.hoot.ui.fragment.PlaceHolderFragment;
import com.hoot.util.XLog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	private static final String TAG = "CollectionPagerAdapter";

	public CollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;
		Bundle args = new Bundle();
		switch (i) {
		case 0: {
			XLog.v(TAG, "getItem");
			fragment = new MainFragment();
			args.putInt(PlaceHolderFragment.ARG_OBJECT, i + 1);
			fragment.setArguments(args);
			return fragment;
		}
		 case 1: {
		 fragment = new HotFragment();
		 args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
		 fragment.setArguments(args);
		 return fragment;
		 }
		 case 2: {
			 fragment = new HotAppFragment();
			 args.putInt(AppFragment.ARG_OBJECT, i + 1);
			 fragment.setArguments(args);
			 return fragment;
			 }
		default:
			fragment = new PlaceHolderFragment();

			args.putInt(PlaceHolderFragment.ARG_OBJECT, i + 1);
			fragment.setArguments(args);
			return fragment;
		}

	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "OBJECT " + (position + 1);
	}
}
