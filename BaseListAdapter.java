package com.mpay.ui.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpay.agent.R;
import com.mpay.plus.database.Item;
import com.mpay.plus.util.Util;

/**
 * @author quyenlm.vn@gmail.com
 * */
public class BaseListAdapter extends ArrayAdapter<Item> {
	protected final LayoutInflater mInflater;

	public BaseListAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<Item> data) {
		clear();
		if (data != null) {
			for (Item appEntry : data) {
				add(appEntry);
			}
		}
	}

	/**
	 * Populate new items in the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.old_list_item_log, parent, false);
		} else {
			view = convertView;
		}

		Item item = getItem(position);
		((ImageView) view.findViewById(R.id.image)).setImageDrawable(Util.getImage(getContext(), item.getImage()));
		((TextView) view.findViewById(R.id.title)).setText(item.getTitle());
		((TextView) view.findViewById(R.id.description)).setText(item.getDescription());

		return view;
	}	
}