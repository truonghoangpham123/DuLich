package com.example.dulich;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

public class HttpUtil {
	private static int timeoutMillis = 1000;	
	public static  int respCode;
	public static int  respPostCode = -1;
	private static final String TAG = "HttpUtil";
	
	public static byte[] httpGetByte(String url, boolean isAlertTask, int Timeout , int retry ) throws Exception
	{		
        int size;
        byte[] byteBuffer               = new byte[1024];
        HttpURLConnection conn          = null;
        InputStream in                  = null;
        ByteArrayOutputStream resultOut = null;
        
        for( int i = 0; i < retry; i++ )
        {        	
        	URL urlObj  = new URL(url);
        	conn = (HttpURLConnection)urlObj.openConnection();           
            conn.setRequestMethod("GET");
            conn.setReadTimeout(timeoutMillis*Timeout);
            conn.setConnectTimeout(timeoutMillis*Timeout);
            
            if ( VERSION.RELEASE.equals("1.6"))
            {
				String xxx = Build.MODEL;
				String yyy = Build.ID;
				conn.setRequestProperty("User-Agent","Dalvik/1.2.0 (Linux; U; Android 1.6; ja-jp; " + xxx + " Build/" + yyy + ")");	
			}
            
            try
            {
            	Log.i(TAG, "loop lan thu " + i + " - bat dau getResponseCode");
                respCode = conn.getResponseCode();
                break;
            }
            catch(UnknownHostException ue)
            {
            	if(i != retry - 1)
            	{	
            		Thread.sleep(3000);
            		Log.i(TAG, "loop lan thu " + i + " - retry getResponseCode");
            		conn.disconnect();
                    conn = null;
                    continue;
            	}
            	else
            	{
            		Log.i(TAG, "chet roi, retry roi ma ko dc, nghi??????????");
            		throw ue;
            	}
            }
            catch(SocketException se)
            {
            	if(i != retry - 1)
            	{	
            		Thread.sleep(3000);
            		Log.i(TAG, "loop lan thu " + i + " - retry getResponseCode");
            		conn.disconnect();
                    conn = null;
                    continue;
            	}
            	else
            	{
            		Log.i(TAG, "chet roi, retry roi ma ko dc, nghi??????????");
            		throw se;
            	}
            }
            catch(Exception e)
            { 
            	Log.i(TAG, "loop lan thu " + i + " - time out");
            	throw e;
            }
        }
        
        
        if (respCode == HttpURLConnection.HTTP_OK)
        {
        	Log.i(TAG, "Ket noi thanh cong");
        	Log.i(TAG, "chay xong doan code kiem tra timeout, chua biet la da get dc du lieu chua");
           
            in = conn.getInputStream();
          
            resultOut = new ByteArrayOutputStream();
            
            while((size = in.read(byteBuffer)) != -1)
            {
                resultOut.write(byteBuffer, 0, size);
            }
            
            resultOut.close();
            in.close();
            conn.disconnect();
            return resultOut.toByteArray();
        }
        else
        {
            return null;        
        }
	}
	
	public static byte[] httpGetByte(String url, int Timeout , int retry ) throws Exception
	{
		int size;
        byte[] byteBuffer               = new byte[1024];
        HttpURLConnection conn          = null;
        InputStream in                  = null;
        ByteArrayOutputStream resultOut = null;
        
        for( int i = 0; i < retry; i++ )
        {        	
        	URL urlObj  = new URL(url);
        	conn = (HttpURLConnection)urlObj.openConnection();           
            conn.setRequestMethod("GET");
            conn.setReadTimeout(timeoutMillis*Timeout);
            conn.setConnectTimeout(timeoutMillis*Timeout);
            
            try
            {
            	Log.i(TAG, "loop lan thu " + i + " - bat dau getResponseCode");
                respCode = conn.getResponseCode();
                break;
            }
            catch(UnknownHostException ue)
            {
            	if(i != retry - 1)
            	{	
            		Thread.sleep(3000);
            		Log.i(TAG, "loop lan thu " + i + " - retry getResponseCode");
            		conn.disconnect();
                    conn = null;
                    continue;
            	}
            	else
            	{
            		Log.i(TAG, "chet roi, retry roi ma ko dc, nghi??????????");
            		throw ue;
            	}
            }
            catch(SocketException se)
            {
            	if(i != retry - 1)
            	{	
            		Thread.sleep(3000);
            		Log.i(TAG, "loop lan thu " + i + " - retry getResponseCode");
            		conn.disconnect();
                    conn = null;
                    continue;
            	}
            	else
            	{
            		Log.i(TAG, "chet roi, retry roi ma ko dc, nghi??????????");
            		throw se;
            	}
            }
            catch(Exception e)
            { 
            	Log.i(TAG, "loop lan thu " + i + " - time out");
            	throw e;
            }
        }
        
        Log.i(TAG, "co ra dc day khi error ko");
        
        
        if (respCode == HttpURLConnection.HTTP_OK)
        {
        	Log.i(TAG, "Ket noi thanh cong");
        	Log.i(TAG, "chay xong doan code kiem tra timeout, chua biet la da get dc du lieu chua");
          
            in = conn.getInputStream();
        
            resultOut = new ByteArrayOutputStream();
            
            while((size = in.read(byteBuffer)) != -1)
            {
                resultOut.write(byteBuffer, 0, size);
            }
            Log.e(TAG, "Ket noi thanh cong111");
            resultOut.close();
            in.close();
            conn.disconnect();
            return resultOut.toByteArray();
        }
        else
        {
            return null;        
        }
	}
	
	public static byte[] httpGetString(String url, int timeout ,int retry)throws Exception{
		byte[] httpData = httpGetByte(url,timeout,retry);
		return httpData;
	}	
	
	public static byte[] httpGetString(String url, boolean isAlertTask, int timeout, int retry )throws Exception{
		byte[] httpData = httpGetByte(url,true, timeout, retry);
		return httpData;
	}
	
	public static String httpPostString(String url, byte[] data, int timeout , int retry)throws Exception{
		byte[] httpData = httpPostByte(url, data, timeout, retry);
		return new String (httpData);
	}

	public static byte[] httpPostByte(String url, byte[] bodyData, int Timeout , int retry) throws Exception{
	    int size;
        byte[] byteBuffer       = new byte[1024];
        HttpURLConnection conn  = null;
        InputStream in          = null;
        OutputStream out        = null;
        ByteArrayOutputStream resultOut = null;        
        
        for( int i = 0; i < retry; i++ )
        {        	
    		Log.v(TAG, "loop count i = " + i);
            URL urlObj = new URL(url);
            conn = (HttpURLConnection)urlObj.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(timeoutMillis*Timeout);
            conn.setConnectTimeout(timeoutMillis*Timeout);
            conn.setDoOutput(true);
           
            try{
                out = conn.getOutputStream();
                out.write(bodyData);
                out.flush();
                out.close();
                respCode            = conn.getResponseCode();
                break;
            }
            catch(UnknownHostException ue)
            {
            	if(i != retry - 1)
            	{	
            		Thread.sleep(3000);
            		Log.i(TAG, "loop lan thu " + i + " - retry getResponseCode");
            		conn.disconnect();
                    conn = null;
                    continue;
            	}
            	else
            	{
            		Log.i(TAG, "chet roi, retry roi ma ko dc, nghi??????????");
            		throw ue;
            	}
            }
            catch(SocketException se)
            {
            	if(i != retry - 1)
            	{	
            		Thread.sleep(3000);
            		Log.i(TAG, "loop lan thu " + i + " - retry getResponseCode");
            		conn.disconnect();
                    conn = null;
                    continue;
            	}
            	else
            	{
            		Log.i(TAG, "chet roi, retry roi ma ko dc, nghi??????????");
            		throw se;
            	}
            }
            catch(Exception e)
            { 
            	Log.i(TAG, "loop lan thu " + i + " - time out");
            	e.printStackTrace();
            	throw e;
            }
        }
        
        Log.i(TAG, "co ra dc day khi error ko");
        
        
        if (respCode == HttpURLConnection.HTTP_OK)
        {
            in = conn.getInputStream();
            resultOut = new ByteArrayOutputStream();
            while((size = in.read(byteBuffer)) != -1)
            {
                resultOut.write(byteBuffer, 0, size);
            }
            
            resultOut.close();
            in.close();
            conn.disconnect();
            
            return resultOut.toByteArray();
        } 
        else
        {
            return null;        
        }       
	}
	
	public static Bitmap getImageBitmap(String url)
	{
		HttpURLConnection conn = null;
		try
		{
			URL urlObj = new URL(url);  
			conn = (HttpURLConnection)urlObj.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(timeoutMillis);
			conn.connect();
			InputStream input = urlObj.openStream();      
			return BitmapFactory.decodeStream(input);
		}
		catch (Exception e) { e.printStackTrace(); }
		finally
		{
			conn.disconnect();
		}
		return null;
	}
	
	public static String getInternetConnection(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifi.isAvailable())
		{
			return "WI-FI";
		} 
		else if (mobile.isAvailable())
		{
			return "3G";
		}
		return "";
	}
}