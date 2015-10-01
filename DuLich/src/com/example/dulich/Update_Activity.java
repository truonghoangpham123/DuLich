package com.example.dulich;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.dulich.adapter.Dulich;
import com.example.dulich.adapter.PlacesAdapter;
import com.example.dulich.db.Database;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

import android.R.integer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Update_Activity extends Activity implements OnClickListener {

	private EditText edt_ten;
	private AutoCompleteTextView edt_diachi;
	private EditText edt_gioithieu;
	private Button btn_update;
	private String txt_ten;
	private String txt_diachi;
	private String txt_gioithieu;
	private String txt_lat;
	private String txt_lon;
	ArrayList<Dulich> list = null;
	ArrayList<Dulich> listTP = null;
	private ArrayList<String> listSTP;
	private Database dbManager;
	private int index;
	private double latitude = 0;
	private double longitude = 0;
	private String mau = "#feffbf";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_update);

		main();

	}

	public void main() {
		Spinner spn = (Spinner) findViewById(R.id.spiner);
		if (dbManager == null)
			dbManager = new Database(this);
		// đổ lên spinner
		listTP = dbManager.getlistTP();
		listSTP = new ArrayList<String>();

		for (int i = 0; i < listTP.size(); i++) {
			String sp = listTP.get(i).getTen_tinh();
			listSTP.add(sp);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listSTP);
		// phải gọi lệnh này để hiển thị danh sách cho Spinner
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		// Thiết lập adapter cho Spinner
		spn.setAdapter(adapter);
		spn.setOnItemSelectedListener(new MyProcessEvent());

		edt_ten = (EditText) findViewById(R.id.tv_ten);
		edt_diachi = (AutoCompleteTextView) findViewById(R.id.tv_diachi);
		edt_gioithieu = (EditText) findViewById(R.id.tv_gioithieu);

		btn_update = (Button) findViewById(R.id.btn_update);
		btn_update.setOnClickListener(this);

		edt_diachi.setAdapter(new PlacesAdapter(this, edt_diachi.getText()
				.toString()));
	}

	private LatLng getLatLngFromAdress(String adress) {
		Geocoder coder = new Geocoder(this);
		List<Address> add;

		try {
			add = coder.getFromLocationName(adress, 5);
			if (add == null) {
				return null;
			}
			Address location = add.get(0);
			location.getLatitude();
			location.getLongitude();

			LatLng latlng = new LatLng(location.getLatitude(),
					location.getLongitude());
			return latlng;
		} catch (Exception e) {
			return null;
		}
	}

	private class MyProcessEvent implements OnItemSelectedListener {

		// Khi có chọn lựa thì vào hàm này
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			index = arg2 + 1;
		}

		// Nếu không chọn gì cả
		public void onNothingSelected(AdapterView<?> arg0) {
			index = 1;
		}
	}

	public int max() {
		Database db = new Database(this);
		int max = db.get_max();
		return max;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_update:

			txt_ten = edt_ten.getText().toString();
			txt_diachi = edt_diachi.getText().toString();
			txt_gioithieu = edt_gioithieu.getText().toString();

			if (txt_diachi.trim().equals("")) {

			} else {
				LatLng latlng = getLatLngFromAdress(txt_diachi);
				if (latlng == null)
					return;
				else {
					txt_lat = String.valueOf(latlng.latitude);
					txt_lon = String.valueOf(latlng.longitude);
				}
			}
			Database db = new Database(this);
			int max = max();

			if (txt_ten.trim().equals("") || txt_diachi.trim().equals("")
					|| txt_gioithieu.trim().equals("")
					|| txt_lat.trim().equals("") || txt_lon.trim().equals("")) {

				Toast.makeText(this, "Bạn cần điền đầy đủ thông tin", 3000)
						.show();

			} else {
				db.update_dl(max + 1, index, txt_ten, txt_diachi,
						txt_gioithieu, txt_lat, txt_lon);
				Toast.makeText(this, "Cập nhật hoàn tất", 3000).show();
				edt_ten.setText("");
				edt_diachi.setText("");
				edt_gioithieu.setText("");

				Intent inten = new Intent(this, MainActivity.class);
				startActivity(inten);

			}

			break;

		default:
			break;
		}

	}

}
