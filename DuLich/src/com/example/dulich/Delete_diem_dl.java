package com.example.dulich;

import java.util.ArrayList;

import com.example.dulich.adapter.Dulich;
import com.example.dulich.adapter.MyAdapterString;
import com.example.dulich.db.Database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Delete_diem_dl extends Activity {
	private Database dbManager;
	private MyAdapterString adapter;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		main();
	}

	private void main(){
		if (dbManager==null) {
			dbManager = new Database(this);	
		}
		final ArrayList<Dulich> list = get_diemdl();
		
		adapter = new MyAdapterString(this, list);
		final GridView grv = (GridView)findViewById(R.id.gridView1);
		grv.setAdapter(adapter);
		grv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				name = list.get(position).getTen().toString();
				
				AlertDialog art = new AlertDialog.Builder(view.getContext()).setMessage("Bạn muốn xóa " +name+ " ra khỏi danh sách điểm du lịch ?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dbManager.delete(name);
								Toast.makeText(getApplicationContext(), "Bạn đã xóa " +name+ " ra khỏi danh sách điểm du lịch", 3000).show();
								ArrayList<Dulich> list = get_diemdl();
								adapter.updateData(list);
								
								
							}
						}).setNegativeButton("No", null).show();

			}
			
		});
		
		
		
	}

	private ArrayList<Dulich> get_diemdl() {
		ArrayList<Dulich> list = dbManager.getLatn();
		return list;

	}

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// dbManager = new Database(this);
	// ArrayList<Dulich> list = get_diemdl();
	// adapter.updateData(list);
	// dbManager.delete(name);
	//
	//
	//
	//
	// }

}
