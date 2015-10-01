package com.example.dulich.db;

import java.io.IOException;
import java.util.ArrayList;

import com.example.dulich.adapter.Dulich;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {

	private static String DB_PATH = "/data/data/com.example.dulich/databases/";
	private static String DB_NAME = "data.sqlite";
	private static final int DB_VERSION = 1;
	private DatabaseHelper db;
	private final Context myContext;
	private int a;

	public Database(Context context) {
		myContext = context;
		db = new DatabaseHelper(context);
		try {
			db.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
	}

	private void openDb() {
		try {
			db.openDataBase();
		} catch (SQLException sqle) {
			db.close();
			throw sqle;
		}
	}

	public ArrayList<Dulich> getnamelist(int id_tinh_thanh) {
		ArrayList<Dulich> list = new ArrayList<Dulich>();

		openDb();
		SQLiteDatabase data = db.getReadableDatabase();
		Cursor contro = data.rawQuery(
				"select * from diem_dl where id_tinh_thanh = '" + id_tinh_thanh
						+ "'", null);
		contro.moveToFirst();
		while (!contro.isAfterLast()) {
			Dulich mShop = new Dulich();
			String id = contro.getString(0);
			String ten = contro.getString(2);
			String diachi = contro.getString(3);
			String gioithieu = contro.getString(4);
			double lati = contro.getDouble(5);
			double loti = contro.getDouble(6);

			mShop.setId(id);
			mShop.setTen(ten);
			mShop.setDiachi(diachi);
			mShop.setGioithieu(gioithieu);
			mShop.setLati(lati);
			mShop.setLoti(loti);

			list.add(mShop);
			contro.moveToNext();
		}

		data.close();
		db.close();
		return list;
	}
	


	public ArrayList<Dulich> getlistTP() {
		ArrayList<Dulich> listTP = new ArrayList<Dulich>();

		openDb();
		SQLiteDatabase data = db.getReadableDatabase();
		Cursor contro = data.rawQuery("select * from tinh_thanh", null);
		contro.moveToFirst();
		while (!contro.isAfterLast()) {
			Dulich mShop = new Dulich();
			String id = contro.getString(0);
			String ten_tinh = contro.getString(1);

			mShop.setId(id);
			mShop.setTen_tinh(ten_tinh);

			listTP.add(mShop);
			contro.moveToNext();
		}

		data.close();
		db.close();
		return listTP;
	}

	public ArrayList<Dulich> getLatn() {
		ArrayList<Dulich> listLant = new ArrayList<Dulich>();

		openDb();
		SQLiteDatabase data = db.getReadableDatabase();
		Cursor contro = data.rawQuery("select * from diem_dl ", null);
		contro.moveToFirst();
		while (!contro.isAfterLast()) {
			Dulich mShop = new Dulich();
			String ten = contro.getString(2);
			String id = contro.getString(0);
			double lati = contro.getDouble(5);
			double loti = contro.getDouble(6);

			mShop.setTen(ten);
			mShop.setLati(lati);
			mShop.setLoti(loti);
			mShop.setId(id);

			listLant.add(mShop);
			contro.moveToNext();
		}

		data.close();
		db.close();
		return listLant;
	}
	
	public int get_max(){
		openDb();
		SQLiteDatabase data = db.getWritableDatabase();
		String sql = "select max(id) from diem_dl";
		Cursor contro = data.rawQuery(sql, null);
		contro.moveToFirst();
		int max = contro.getInt(0);
		data.close();
		return max;
	}
	
	public void delete(String name){
		openDb();
		SQLiteDatabase data = db.getWritableDatabase();
		String sql = "delete from diem_dl where ten = '"+name+"'";
		Cursor contro = data.rawQuery(sql, null);
		
		data.execSQL(sql);
		data.close();db.close();
		
	}
	
	
	public void update_dl(Integer id,Integer id_tinh, String ten, String diachi, String gioithieu, String lat, String lon ) {
		openDb();
		SQLiteDatabase data = db.getWritableDatabase();
	
		String sql = "insert into diem_dl values ('"+id+"','"+id_tinh+"','"+ten+"','"+diachi+"','"+gioithieu+"','"+lat+"','"+lon+"')  ";
		Cursor contro = data.rawQuery(sql, null);
		

		data.execSQL(sql);
		data.close();
		db.close();
	
	}
}
