package com.example.dulich;

import java.util.ArrayList;

import com.example.dulich.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.dulich.adapter.Dulich;
import com.example.dulich.adapter.MyAdapterString;
import com.example.dulich.db.Database;
import com.example.dulich.map.Info;

public class List_Activity extends Activity implements OnClickListener {

	private GridView gvData;
	public MyAdapterString arrayAdapter;
	public ArrayList<Dulich> arrayList;
	private Database dbManager;
	private LinearLayout app_bg;

	ArrayList<Dulich> list = null;
	ArrayList<Dulich> listTP = null;

	private Button btnOK;
	private Spinner spin;

	private int index;

	private ArrayList<String> listSTP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		initializeUI();

	}

	public void initializeUI() {
		// TODO Auto-generated method stub

		if (dbManager == null)
			dbManager = new Database(this);
//đổ lên spinner
		listTP = dbManager.getlistTP();
		listSTP = new ArrayList<String>();

		for (int i = 0; i < listTP.size(); i++) {
			String sp = listTP.get(i).getTen_tinh();
			listSTP.add(sp);
		}

		dialog();
		app_bg = (LinearLayout) findViewById(R.id.app_bg);
		app_bg.setVisibility(View.GONE);

		gvData = (GridView) findViewById(R.id.gridView1);

		gvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String ten = list.get(arg2).getTen();
				String diachi = list.get(arg2).getDiachi();
				String gioithieu = list.get(arg2).getGioithieu();
				double lat = list.get(arg2).getLati();
				double lon = list.get(arg2).getLoti();
				
				Intent minIntent = new Intent(getApplicationContext(), Info.class);
				minIntent.putExtra("ten", ten);
				minIntent.putExtra("diachi", diachi);
				minIntent.putExtra("gioithieu", gioithieu);
				minIntent.putExtra("lat", lat);
				minIntent.putExtra("lon", lon);
				minIntent.putExtra("id", ""+arg2);
				
				startActivity(minIntent);
			}
		});

	}

	public ArrayList<Dulich> getAllMedia() {
		ArrayList<Dulich> list = dbManager.getnamelist(index);
		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public void dialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(List_Activity.this);

		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("Chọn tỉnh thành");
		dialog.setCancelable(false);

		spin = (Spinner) dialog.findViewById(R.id.spinner1);
		// Gán Data source (arr) vào Adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listSTP);
		// phải gọi lệnh này để hiển thị danh sách cho Spinner
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		// Thiết lập adapter cho Spinner
		spin.setAdapter(adapter);
		// thiết lập sự kiện chọn phần tử cho Spinner
		spin.setOnItemSelectedListener(new MyProcessEvent());

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listSTP);
		// phải gọi lệnh này để hiển thị danh sách cho Spinner
		adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		// Thiết lập adapter cho Spinner
		spin.setAdapter(adapter1);
		// thiết lập sự kiện chọn phần tử cho Spinner
		spin.setOnItemSelectedListener(new MyProcessEvent());
		btnOK = (Button) dialog.findViewById(R.id.button);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				app_bg.setVisibility(View.VISIBLE);

				list = getAllMedia();
				arrayAdapter = new MyAdapterString(List_Activity.this, list);
				gvData.setAdapter(arrayAdapter);

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

			index = arg2 + 1;
			Log.i("Huong debug", "---->" + index);

		}

		// Nếu không chọn gì cả
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

}