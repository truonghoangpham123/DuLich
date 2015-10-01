package com.example.dulich;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.example.dulich.adapter.Dulich;
import com.example.dulich.db.Database;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Map_Activity extends FragmentActivity implements OnClickListener,
		LocationListener {
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private String addressA;
	private double latitude;
	private double longitude;

	AutoCompleteTextView atvPlaces1;
	PlacesTask placesTask;
	ParserTask parserTask;

	Geocoder geocoder;
	List<Address> locationList;
	List<String> locationNameList;
	private String DestinationAddress;
	private Button btnFind, btnOk;
	private Spinner spin;
	String arr[] = { "2km", "4km", "6km", "8km", "10km" };

	private String sps, bankinh;
	private Double r;
	
	private ArrayList<Dulich>listLanglat = null;
	private Database dbManager;
	
	Dulich mDulich = new Dulich();
	
	private double latitude_FIND;
	private double longitude_FIND;
	
	private double latitude_RADIUS;
	private double longitude_RADIUS;
	
	private LatLng LOCATION_RADIUS;
	private LatLng LOCATION_FIND;
	private String addressFIND;
	private String addressRADIUS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		if(dbManager == null)
			dbManager = new Database(this);
		
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);
			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				onLocationChanged(location);
			}
			locationManager.requestLocationUpdates(provider, 20000, 0, this);

			// Creating a LatLng object for the current location
			LatLng latLng = new LatLng(latitude, longitude);

			// Showing the current location in Google Map
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			// Zoom in the Google Map
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

//			try {
//
//				Geocoder geo = new Geocoder(getApplicationContext(),
//						Locale.getDefault());
//				List<Address> addresses = geo.getFromLocation(latitude,
//						longitude, 1);
//				if (addresses.isEmpty()) {
//				} else {
//					if (addresses.size() > 0) {
//
//						addressA = addresses.get(0).getAddressLine(0) + ", "
//								+ addresses.get(0).getAdminArea() + ", "
//								+ addresses.get(0).getCountryName();
//
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace(); // getFromLocation() may sometimes fail
//			}
//			addMarkertoMap_curent(new LatLng(latitude, longitude), googleMap,
//					"" + addressA);

		}

		geocoder = new Geocoder(this, Locale.ENGLISH);

		atvPlaces1 = (AutoCompleteTextView) findViewById(R.id.atv_places1);
		atvPlaces1.setThreshold(1);

		atvPlaces1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				placesTask = new PlacesTask();
				placesTask.execute(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		btnFind = (Button) findViewById(R.id.btnFind);
		btnFind.setOnClickListener(this);

	}

	// add icon
	private void addMarkertoMap_curent(LatLng point, GoogleMap mapGoogle,
			String title) {

		Marker markerCustom = mapGoogle.addMarker(new MarkerOptions()
				.position(point)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icmarker_current_1))
				.title(title));

	}
	
	
//	 add icon
		private void addMarkertoMap_curentA(LatLng point, GoogleMap mapGoogle,
				String title) {

			Marker markerCustom = mapGoogle.addMarker(new MarkerOptions()
					.position(point)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icmarker_current))
					.title(title));

		}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btnFind:
			dialog();
			break;

		default:
			break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// Getting latitude of the current location
		latitude = location.getLatitude();

		// Getting longitude of the current location
		longitude = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	// /////////////////////////////////////////////////////////
	public void ShowAddress(View v)

	{

		Geocoder gc = new Geocoder(this, Locale.getDefault());
		List<Address> locations = null;

		try {

			locations = gc.getFromLocationName(atvPlaces1.getText().toString(),
					1);

		} catch (IOException e) {
		}

		if (locations.isEmpty()) {

			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
					Map_Activity.this);
			dlgAlert.setMessage("Location not found!!! Please change address and try again.");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
		} else {

			latitude_FIND = locations.get(0).getLatitude();
			longitude_FIND = locations.get(0).getLongitude();

			LOCATION_FIND = new LatLng(latitude_FIND, longitude_FIND);
			
			try {

				Geocoder geo = new Geocoder(
						getApplicationContext(),
						Locale.getDefault());
				List<Address> addresses = geo.getFromLocation(
						latitude_FIND, longitude_FIND, 1);
				if (addresses.isEmpty()) {
				} else {
					if (addresses.size() > 0) {

						addressFIND = addresses.get(0)
								.getAddressLine(0)
								+ ", "
								+ addresses.get(0)
										.getAdminArea()
								+ ", "
								+ addresses.get(0)
										.getCountryName();

					}
				}
			} catch (Exception e) {
				e.printStackTrace(); // getFromLocation() may
										// sometimes fail
			}
			
			addMarkertoMap_curent(LOCATION_FIND,
					googleMap, addressFIND);
			
			listLanglat = dbManager.getLatn();
			Log.i("Huong debug", ">>>>>>>>>"+listLanglat.size());
			for(int i=0; i<listLanglat.size(); i++){
				
				latitude_RADIUS = listLanglat.get(i).getLati();
				longitude_RADIUS = listLanglat.get(i).getLoti();
				
				LOCATION_RADIUS = new LatLng(latitude_RADIUS, longitude_RADIUS);
//				addMarkertoMap_curent(LOCATION_RADIUS,
//						googleMap, addressRADIUS);
				try {

					Geocoder geo = new Geocoder(
							getApplicationContext(),
							Locale.getDefault());
					List<Address> addresses = geo.getFromLocation(
							latitude_RADIUS, longitude_RADIUS, 1);
					if (addresses.isEmpty()) {
					} else {
						if (addresses.size() > 0) {

							addressRADIUS = addresses.get(0)
									.getAddressLine(0)
									+ ", "
									+ addresses.get(0)
											.getAdminArea()
									+ ", "
									+ addresses.get(0)
											.getCountryName();

						}
					}
				} catch (Exception e) {
					e.printStackTrace(); // getFromLocation() may
											// sometimes fail
				}
				
				double kc = getDistance(LOCATION_FIND, LOCATION_RADIUS);
				Log.i("Huong debug", "Khoảng cách---------------------->: " + kc+ "\n"+r);
				if (kc <= r) {
					addMarkertoMap_curentA(LOCATION_RADIUS,
							googleMap, ""+listLanglat.get(i).getTen()+"\n"+addressRADIUS);
				}

				googleMap.addCircle(new CircleOptions()
						.center(LOCATION_FIND)
						.radius(r)
						.fillColor(Color.parseColor("#B2A9F6")));
				
				googleMap.moveCamera(CameraUpdateFactory.newLatLng(LOCATION_FIND));
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
			}
			

		}
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyCPIb8M9bPrGObgC7mEHQexdJg5kDv15AA";
			// String key = "key=AIzaSyBJQ6QGGyUhRIk0tTaAm42FSEV_0KKvyDU";

			String input = "";

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=false";

			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&" + key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			try {
				// Fetching the data from web service in background
				data = downloadUrl(url);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { android.R.id.text1 };

			// Creating a SimpleAdapter for the AutoCompleteTextView
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			atvPlaces1.setAdapter(adapter);
		}
	}

	public void dialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(Map_Activity.this);

		dialog.setContentView(R.layout.dialogfind);
		dialog.setTitle("Chọn bán kính");
		dialog.setCancelable(false);

		spin = (Spinner) dialog.findViewById(R.id.spFind);
		// Gán Data source (arr) vào Adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		// phải gọi lệnh này để hiển thị danh sách cho Spinner
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		// Thiết lập adapter cho Spinner
		spin.setAdapter(adapter);
		// thiết lập sự kiện chọn phần tử cho Spinner
		spin.setOnItemSelectedListener(new MyProcessEvent());

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		// phải gọi lệnh này để hiển thị danh sách cho Spinner
		adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		// Thiết lập adapter cho Spinner
		spin.setAdapter(adapter1);
		// thiết lập sự kiện chọn phần tử cho Spinner
		spin.setOnItemSelectedListener(new MyProcessEvent());
		btnOk = (Button) dialog.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowAddress(v);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	// Class tạo sự kiện khi click chọn spinner
	private class MyProcessEvent implements OnItemSelectedListener {

		// Khi có chọn lựa thì vào hàm này
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String arr[] = { "2km", "4km", "6km", "8km", "10km" };
			sps = (arr[arg2]);
			if (String.valueOf(sps) == "2km") {
				bankinh = "2000";
			} else if (String.valueOf(sps) == "4km") {
				bankinh = "4000";
			} else if (String.valueOf(sps) == "6km") {
				bankinh = "6000";
			} else if (String.valueOf(sps) == "8km") {
				bankinh = "8000";
			} else if (String.valueOf(sps) == "10km") {
				bankinh = "10000";
			}

			r = Double.valueOf(bankinh);

		}

		// Nếu không chọn gì cả
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	// tính khoảng cách
	public double getDistance(LatLng LatLng1, LatLng LatLng2) {
		double distance = 0;
		Location locationA = new Location("A");
		locationA.setLatitude(LatLng1.latitude);
		locationA.setLongitude(LatLng1.longitude);

		Location locationB = new Location("B");
		locationB.setLatitude(LatLng2.latitude);
		locationB.setLongitude(LatLng2.longitude);
		distance = locationA.distanceTo(locationB);
		return distance;
	}
}