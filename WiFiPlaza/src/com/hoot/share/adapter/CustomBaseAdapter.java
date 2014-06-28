package com.hoot.share.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class CustomBaseAdapter<T> extends BaseAdapter {
	protected List<T> mData;
	protected Context mContext;
	protected LayoutInflater mInflater;

	public CustomBaseAdapter(Context context, List<T> data) {
		this.mData = data;
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<T> data) {
		this.mData = data;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
