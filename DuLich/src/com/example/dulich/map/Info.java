package com.example.dulich.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.w3c.dom.Document;
import com.example.dulich.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") public class Info extends FragmentActivity implements  LocationListener, LocationSource {
	
	private TextView txtTen, txtDiadiem, txtGioithieu;
	private ImageView imv_ava;
	private LatLng CURRENT_LOCATION;
	private LatLng DULICH_LOCATION;
	GoogleMap mMap;
	GMapV2Direction md;
	private OnLocationChangedListener mChangedListener;
	private LocationManager manager;
	String provider;
	String addressA, addressB;
	double lat,lon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		

		initializeUI();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi")
	public void initializeUI() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		String ten = i.getStringExtra("ten");
		String diachi = i.getStringExtra("diachi");
		String gioithieu = i.getStringExtra("gioithieu");
		String id = i.getStringExtra("id");
		 lat = i.getDoubleExtra("lat", 0);
		 lon = i.getDoubleExtra("lon", 0);
		DULICH_LOCATION = new LatLng(lat, lon);
		
		
		
		
		Log.e("Huong debug", ">>>>>>>"+id);
		Log.e("Huong debug", ">>>>>>>"+lat);
		Log.e("Huong debug", ">>>>>>>"+DULICH_LOCATION);
		
		
		txtTen = (TextView)findViewById(R.id.txtTen);
		txtDiadiem = (TextView)findViewById(R.id.txtDiadiem);
		txtGioithieu = (TextView)findViewById(R.id.txtGioithieu);
		imv_ava =(ImageView)findViewById(R.id.imageView1);
		
		
		txtTen.setText(""+ten);
		txtDiadiem.setText("Địa điểm: " +diachi);
		txtGioithieu.setText("Giới thiệu: "+gioithieu);
		
		
		try {
			InputStream imv = this.getAssets().open("im_"+(Integer.valueOf(id)+1)+".jpg");
			Drawable d = Drawable.createFromStream(imv, null);
			imv_ava.setImageDrawable(d);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		
		md = new GMapV2Direction();
	
		mMap = ((SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		//update
		mMap.setMyLocationEnabled(true);
		mMap.setLocationSource(this);

		manager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		provider = manager.getBestProvider(criteria, true);
		Location location = manager.getLastKnownLocation(provider);
		if(location != null){
			onLocationChanged(location);
		}

		manager.requestLocationUpdates(provider, 6000, 0, this);
		
		
		
	}
	private void DrawDirection()
	{	

	
		
		mMap.addMarker(new MarkerOptions().position(CURRENT_LOCATION).title(""+addressA).icon(BitmapDescriptorFactory.fromResource(R.drawable.icmarker_current)));
		mMap.addMarker(new MarkerOptions().position(DULICH_LOCATION).title(""+addressB).icon(BitmapDescriptorFactory.fromResource(R.drawable.icmarker_current_1)));

		Log.e("Log ManhHien ", ">>>>"+addressB);
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION,11));
		Document doc = md.getDocument(CURRENT_LOCATION, DULICH_LOCATION, GMapV2Direction.MODE_DRIVING);
		int duration = md.getDurationValue(doc);
//		String distance = md.getDistanceText(doc);
//		txtdistance.setText("Khoảng cách: "+distance);
		
		String start_address = md.getStartAddress(doc);
		String copy_right = md.getCopyRights(doc);

		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

		for(int i = 0 ; i < directionPoint.size() ; i++) {   
			rectLine.add(directionPoint.get(i));
		}

		mMap.addPolyline(rectLine);
		// Setting latitude and longitude in the TextView tv_location
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (manager!=null)
		{
			manager.removeUpdates(this);
		}
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (manager != null)
		{
			mMap.setMyLocationEnabled(true);
		}
	}



	@Override
	public void onLocationChanged(Location location) {

		mChangedListener.onLocationChanged( location );

		// Getting latitude of the current location
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude(); 
		
		try {

			Geocoder geo = new Geocoder(getApplicationContext(),
					Locale.getDefault());
			List<Address> addresses = geo.getFromLocation(latitude, longitude,
					1);
			if (addresses.isEmpty()) {
			} else {
				if (addresses.size() > 0) {

					addressA = addresses.get(0).getAddressLine(0) + ", "
							+ addresses.get(0).getAdminArea() + ", "
							+ addresses.get(0).getCountryName();
					Log.e("Lay dia chi current", ""+addressA);

					


				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // getFromLocation() may sometimes fail
		}

		// dia chi quan
		try {

			Geocoder geo = new Geocoder(getApplicationContext(),
					Locale.getDefault());
			List<Address> addresses = geo.getFromLocation(lat,
					lon, 1);
			if (addresses.isEmpty()) {
			} else {
				if (addresses.size() > 0) {

					addressB = addresses.get(0).getAddressLine(0) + ", "
							+ addresses.get(0).getAdminArea() + ", "
							+ addresses.get(0).getCountryName();
					Log.e("Lay dia chi du lich", ""+addressB);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // getFromLocation() may sometimes fail
		}


		// Creating a LatLng object for the current location
		CURRENT_LOCATION = new LatLng(latitude, longitude);
		Log.e("main", "CURRENT_LOCATION lat= "+CURRENT_LOCATION.latitude);
		Log.e("main", "CURRENT_LOCATION LNG= "+CURRENT_LOCATION.longitude);
		// Showing the current location in Google Map
		mMap.moveCamera(CameraUpdateFactory.newLatLng(CURRENT_LOCATION));

		// Zoom in the Google Map
		mMap.clear();
		try
		{
			DrawDirection();
		}
		catch(Exception e)
		{
		}

	}


	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mChangedListener = listener;
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mChangedListener = null;
		
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
}
