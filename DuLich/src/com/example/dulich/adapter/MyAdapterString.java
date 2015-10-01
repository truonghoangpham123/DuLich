package com.example.dulich.adapter;

import java.io.InputStream;
import java.util.ArrayList;

import com.example.dulich.R;

import android.R.drawable;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapterString extends BaseAdapter implements Filterable {

	public Context context;
	public ArrayList<Dulich> arrayList;

	private ArrayFilter mFilter;
	private ArrayList<Dulich> arrayListSearch;
	private ArrayList<Dulich> arrayListOld;

	private final Object mLock = new Object();

	double latitude, longitude, s;

	public MyAdapterString(Context context, ArrayList<Dulich> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
		this.arrayListOld = arrayList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Dulich item = arrayList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);

		}

		TextView itemName = (TextView) convertView
				.findViewById(R.id.txtName);
		ImageView avatar = (ImageView) convertView
				.findViewById(R.id.imv_ava);
		
		try {
			InputStream img = context.getAssets().open("im_"+item.getId()+".jpg");
			Drawable d = Drawable.createFromStream(img, null);
			avatar.setImageDrawable(d);
			img.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			avatar.setBackgroundResource(R.drawable.ds_img);
		}

		itemName.setText(item.getTen());

		return convertView;

	}

	public void updateData(ArrayList<Dulich> update) {
		arrayList.clear();
		for (int i = 0; i < update.size(); i++) {
			arrayList.add(update.get(i));
		}
		notifyDataSetChanged();
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (arrayListSearch == null) {
				synchronized (mLock) {
					arrayListSearch = new ArrayList<Dulich>();
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					results.values = arrayListOld;
					results.count = arrayListOld.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
				final int count = arrayListOld.size();

				final ArrayList<Dulich> newValues = new ArrayList<Dulich>(count);

				for (int i = 0; i < count; i++) {
					final Dulich value = arrayListOld.get(i);
					if (value.getTen().toLowerCase().trim()
							.contains(prefixString)
							|| value.getDiachi().toLowerCase().trim()
									.contains(prefixString)) {
						newValues.add(value);
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// noinspection unchecked
			arrayList = (ArrayList<Dulich>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}
