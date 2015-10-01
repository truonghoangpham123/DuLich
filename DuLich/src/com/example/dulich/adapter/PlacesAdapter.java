package com.example.dulich.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dulich.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;

public class PlacesAdapter extends ArrayAdapter<String> {

	private List<String> places;

	public PlacesAdapter(Context context, String nameFilter) {
		super(context, R.layout.dropdown_item);
		places = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		return places.size();
	}

	@Override
	public String getItem(int position) {
		return places.get(position);
	}

	@Override
	public Filter getFilter() {
		Filter myFilter = new Filter() {

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					String input = "";
					try {
						input = URLEncoder.encode(constraint.toString()
								.replace(" ", "+"), "utf-8");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					;
					String url_fm = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&types=geocode&sensor=false&language=vn&key=%s";
					String url = String.format(url_fm, input,
							"AIzaSyBTooWouMriM2BWBfc7GhWbS5K6M5MvkzQ");
					JSONObject json = RestfulWSHelper.doGet(url);
					Log.i("location", url);
					Log.i("Hoa debug", "--->" + json);
					JSONArray data = null;
					try {
						JSONArray predictions = json
								.getJSONArray("predictions");
						places.clear();
						for (int i = 0; i < predictions.length(); i++) {
							JSONObject prediction = predictions
									.getJSONObject(i);
							places.add(prediction.getString("description"));
						}
						filterResults.values = places;
						filterResults.count = places.size();

					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				return filterResults;
			}
		};
		return myFilter;
	}

}
