package com.hoot.share.adapter;

import java.util.List;

import com.hoot.pojo.GridItemInfo;
import com.hoot.util.XLog;
import com.hoot.wifiplaza.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends CustomBaseAdapter<GridItemInfo> {
	private static final String TAG = "MainAdapter";

	public MainAdapter(Context context, List<GridItemInfo> data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_main_grid_item,
					null);

			holder = new Holder();
			holder.item = (TextView) convertView.findViewById(R.id.content);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.item.setText(getItem(position).itemName);
		holder.image.setImageResource(getItem(position).imageName);
		return convertView;
	}

	
	static final class Holder {
		ImageView image;
		TextView item;
	}

}
