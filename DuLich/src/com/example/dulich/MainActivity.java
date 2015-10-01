package com.example.dulich;

import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		LocationListener {

	private Button btn_diadiem, btn_map, btn_exit;
	private TextView txt_vtct;
	private double latitude;
	private double longitude;

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	String provider;
	private String address;
	private String mau = "#feffbf";
	// protected boolean gps_enabled,network_enabled;
	private Button btn_update;
	private Button btn_delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initializeUI();

	}

	public void initializeUI() {
		// TODO Auto-generated method stub
		btn_diadiem = (Button) findViewById(R.id.btn_diadiem);
		btn_map = (Button) findViewById(R.id.btn_map);
		btn_update = (Button) findViewById(R.id.btn_update);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(this);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		txt_vtct = (TextView) findViewById(R.id.txt_vtct);

		btn_diadiem.setOnClickListener(this);
		btn_map.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		btn_update.setOnClickListener(this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_diadiem:

			Intent dintent = new Intent(MainActivity.this, List_Activity.class);
			startActivity(dintent);

			break;

		case R.id.btn_map:

			Intent mintent = new Intent(MainActivity.this, Map_Activity.class);
			startActivity(mintent);

			break;

		case R.id.btn_update:

			Intent update_intent = new Intent(MainActivity.this,
					Update_Activity.class);
			startActivity(update_intent);

			break;
		case R.id.btn_delete:

			Intent delete_intent = new Intent(MainActivity.this,
					Delete_diem_dl.class);
			startActivity(delete_intent);

			break;

		case R.id.btn_exit:

			System.exit(0);

			break;

		default:
			break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLongitude();

		try {

			Geocoder geo = new Geocoder(getApplicationContext(),
					Locale.getDefault());
			List<Address> addresses = geo.getFromLocation(latitude, longitude,
					1);
			if (addresses.isEmpty()) {
			} else {
				if (addresses.size() > 0) {

					address = addresses.get(0).getAddressLine(0) + ", "
							+ addresses.get(0).getAdminArea() + ", "
							+ addresses.get(0).getCountryName();

				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // getFromLocation() may
									// sometimes fail
		}
		txt_vtct.setText("Vị trí của tôi: " + address);
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