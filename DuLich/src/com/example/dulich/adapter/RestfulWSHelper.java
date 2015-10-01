package com.example.dulich.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RestfulWSHelper {

	private static final boolean SSL_MODE = true;

	private static String getResponseText(InputStream inStream) {
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

	/**
	 * Make a GET request.
	 * 
	 * @param url
	 *            Request link + param.
	 * @return Response content in JSON.
	 */
	public static JSONObject doGet(String url) {
		boolean event = true;
		// ErrorObject error = null;

		JSONObject json = null;

		HttpClient httpclient = RestfulWSHelper.getHttpClient();
		HttpGet request = new HttpGet(url);

		HttpResponse response;
		try {
			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				json = new JSONObject(getResponseText(instream));
				instream.close();
			}
		} catch (ClientProtocolException e) {
			event = false;
			// error = ErrorManager.CLIENT_PROTOCOL_EXCEPTION_ERROR;
		} catch (IOException e) {
			event = false;
			// error = ErrorManager.IO_EXCEPTION_ERROR;
		} catch (JSONException e) {
			event = false;
			// error = ErrorManager.JSON_EXCEPTION_ERROR;
		}
		//
		// if (!event) {
		// JSONObject errorObject = new JSONObject();
		// try {
		// // errorObject.put("event", false);
		// // errorObject.put("error", error.getErrorId());
		// // errorObject.put("message", error.getErrorMessage());
		// } catch (JSONException e) {
		// e.printStackTrace();
		// return null;
		// }
		//
		// return errorObject;
		// }

		return json;
	}

	/**
	 * Make a POST request.
	 * 
	 * @param url
	 *            Request link.
	 * @param nameValuePairs
	 *            From data.
	 * @return Response content in JSON
	 */
	public static JSONObject doPost(String url,
			List<NameValuePair> nameValuePairs) {

		// if (GlobalConfig.VS_DEBUG)
		// Log.d(GlobalConfig.APP_ID, "Request: " + nameValuePairs.toString());

		boolean event = true;
		// ErrorObject error = null;

		JSONObject json = null;

		HttpClient httpclient = RestfulWSHelper.getHttpClient();
		HttpPost request = new HttpPost(url);

		HttpResponse response;
		try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					nameValuePairs);

			formEntity.setContentEncoding("UTF-8");

			request.setEntity(formEntity);

			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				json = new JSONObject(getResponseText(instream));
				instream.close();
			}
		} catch (ClientProtocolException e) {
			event = false;
			// error = ErrorManager.CLIENT_PROTOCOL_EXCEPTION_ERROR;
		} catch (IOException e) {
			event = false;
			// error = ErrorManager.IO_EXCEPTION_ERROR;
		} catch (JSONException e) {
			event = false;
			// error = ErrorManager.JSON_EXCEPTION_ERROR;
		}

		if (!event) {
			JSONObject errorObject = new JSONObject();
			try {
				errorObject.put("event", false);
				// errorObject.put("error", error.getErrorId());
				// errorObject.put("message", error.getErrorMessage());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}

			return errorObject;
		}

		return json;
	}

	public static JSONObject doGet1(String url,
			List<NameValuePair> nameValuePairs) {

		// if (GlobalConfig.VS_DEBUG)
		// Log.d(GlobalConfig.APP_ID, "Request: " + nameValuePairs.toString());

		boolean event = true;
		// ErrorObject error = null;

		JSONObject json = null;

		HttpClient httpclient = RestfulWSHelper.getHttpClient();
		HttpGet request = new HttpGet(url);

		HttpResponse response;
		try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					nameValuePairs);

			formEntity.setContentEncoding("UTF-8");

			// request.setEntity(formEntity);

			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				json = new JSONObject(getResponseText(instream));
				instream.close();
			}
		} catch (ClientProtocolException e) {
			event = false;
			// error = ErrorManager.CLIENT_PROTOCOL_EXCEPTION_ERROR;
		} catch (IOException e) {
			event = false;
			// error = ErrorManager.IO_EXCEPTION_ERROR;
		} catch (JSONException e) {
			event = false;
			// error = ErrorManager.JSON_EXCEPTION_ERROR;
		}

		if (!event) {
			JSONObject errorObject = new JSONObject();
			try {
				errorObject.put("event", false);
				// errorObject.put("error", error.getErrorId());
				// errorObject.put("message", error.getErrorMessage());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}

			return errorObject;
		}

		return json;
	}

	/**
	 * Make a POST request with raw data.
	 * 
	 * @author Phu Dinh
	 * 
	 * @param url
	 *            Request link.
	 * @param data
	 *            From data.
	 * @return Response content in JSON
	 */
	public static JSONObject doRawPost(String url, String data) {

		// if (GlobalConfig.VS_DEBUG)
		// Log.d(GlobalConfig.APP_ID, "Request: " + data);

		boolean event = true;
		// ErrorObject error = null;

		JSONObject json = null;

		HttpClient httpclient = RestfulWSHelper.getHttpClient();
		HttpPost request = new HttpPost(url);

		HttpResponse response;
		try {

			StringEntity s = new StringEntity(data, "UTF-8");
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");

			request.setEntity(s);
			request.setEntity(s);

			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				json = new JSONObject(getResponseText(instream));
				instream.close();
			}
		} catch (ClientProtocolException e) {
			event = false;
			// error = ErrorManager.CLIENT_PROTOCOL_EXCEPTION_ERROR;
		} catch (IOException e) {
			event = false;
			// error = ErrorManager.IO_EXCEPTION_ERROR;
		} catch (JSONException e) {
			event = false;
			// error = ErrorManager.JSON_EXCEPTION_ERROR;
		}

		if (!event) {
			JSONObject errorObject = new JSONObject();
			try {
				errorObject.put("event", false);
				// errorObject.put("error", error.getErrorId());
				// errorObject.put("message", error.getErrorMessage());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}

			return errorObject;
		}

		return json;
	}

	/**
	 * Make a PUT request.
	 * 
	 * @param url
	 *            Request link.
	 * @param c
	 *            Body content.
	 * @param authentication_key
	 *            Authentication key.
	 * @return Response content in JSON
	 */
	public static JSONObject doPut(String url,
			List<NameValuePair> nameValuePairs) {
		boolean event = true;
		// ErrorObject error = null;

		JSONObject json = null;

		HttpClient httpclient = RestfulWSHelper.getHttpClient();
		HttpPut request = new HttpPut(url);

		HttpResponse response;
		try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					nameValuePairs);

			formEntity.setContentEncoding("UTF-8");

			request.setEntity(formEntity);

			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				json = new JSONObject(getResponseText(instream));
				instream.close();
			}
		} catch (ClientProtocolException e) {
			event = false;
			// error = ErrorManager.CLIENT_PROTOCOL_EXCEPTION_ERROR;
		} catch (IOException e) {
			event = false;
			// error = ErrorManager.IO_EXCEPTION_ERROR;
		} catch (JSONException e) {
			event = false;
			// error = ErrorManager.JSON_EXCEPTION_ERROR;
		}

		if (!event) {
			JSONObject errorObject = new JSONObject();
			try {
				errorObject.put("event", false);
				// errorObject.put("error", error.getErrorId());
				// errorObject.put("message", error.getErrorMessage());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}

			return errorObject;
		}

		return json;
	}

	/**
	 * Make a DELETE request.
	 * 
	 * @param url
	 *            Request link + param.
	 * @param authentication_key
	 *            Authentication key.
	 * @return Response content in JSON
	 */
	public static JSONObject doDelete(String url) {
		boolean event = true;
		// ErrorObject error = null;

		JSONObject json = null;

		HttpClient httpclient = RestfulWSHelper.getHttpClient();
		HttpDelete request = new HttpDelete(url);

		HttpResponse response;
		try {
			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				json = new JSONObject(getResponseText(instream));
				instream.close();
			}
		} catch (ClientProtocolException e) {
			event = false;
			// error = ErrorManager.CLIENT_PROTOCOL_EXCEPTION_ERROR;
		} catch (IOException e) {
			event = false;
			// error = ErrorManager.IO_EXCEPTION_ERROR;
		} catch (JSONException e) {
			event = false;
			// error = ErrorManager.JSON_EXCEPTION_ERROR;
		}

		if (!event) {
			JSONObject errorObject = new JSONObject();
			// try {
			// errorObject.put("event", false);
			// // errorObject.put("error", error.getErrorId());
			// // errorObject.put("message", error.getErrorMessage());
			// } catch (JSONException e) {
			// e.printStackTrace();
			// return null;
			// }

			// return errorObject;
			// }
		}
		return json;
	}

	private static DefaultHttpClient getHttpClient() {
		if (!SSL_MODE)
			return new DefaultHttpClient();

		KeyStore trustStore;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		} catch (KeyManagementException e) {
			e.printStackTrace();
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public static JSONObject WSRestclient(String url,
			ArrayList<NameValuePair> params)
			throws UnsupportedEncodingException {
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64)"));
		headers.add(new BasicNameValuePair("Content-Type",
				"application/x-www-form-urlencoded"));
		return WSRestclient(url, headers, params);
	}

	public static JSONObject WSRestclient(String url,
			ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params)
			throws UnsupportedEncodingException {
		HttpPost request = new HttpPost(url);

		for (NameValuePair h : headers) {
			request.addHeader(h.getName(), h.getValue());
		}

		if (!params.isEmpty()) {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}
		return executeRequest(request, url);
	}

	private static JSONObject executeRequest(HttpUriRequest request, String url) {
		JSONObject json = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(request);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						instream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				Log.i("Hoa debug", "total.toString() = " + total.toString());
				json = new JSONObject(total.toString());
				instream.close();
				Log.i("Hoa debug", "code" + json);
				instream.close();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		Log.i("Hoa debug", "end executeRequest:" + url);
		return json;
	}
}
