/*
 *   public class for managing our main search algorithm
 * 
 * 
 */
package com.example.enroute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.sun.org.apache.xerces.internal.util.URI;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;


public class SearchBase {

  //-------------------------------------------------------
  // instance variables
	private static final String API_KEY = "AIzaSyBoRZcwm_ad2YpAdLlA6XKn8m8Gjae9B0E";
	private static final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
	private static final String DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?";
	
	List<GeoPoint> routePoints;

  //-------------------------------------------------------
  // constructor
  public SearchBase() {
    super();
  }
  
  /*-------------------------------------------------------
   * main search function
   * @destination - A string entered by the user which is the
   * desired destination. 
   * 
   * @query - The place/type of food searched for. 
   */
  
  public String loadSearchResults( String destination, String query) {
	  
	  //TODO: Get current location from location manager, format properly
	  String directionReq = DIRECTION_URL + "origin=New+Haven,CT&destination=Hartford,CT" + "&sensor=true";    
	  
	  AsyncTask mySearch = new myGoogleSearch();
	  String retVal = "";
	  
	  try{
			//mySearch.execute(directionReq);
			//block until it finishes, then updates the list of lat/lng in order
			String result = searchRequest(directionReq); //mySearch.get().toString();
			retVal = processDirectionResponse(result);
			
		}catch(Exception e){
			Log.v("Exception google search","Exception:"+e.getMessage());
		}
	  
	  return retVal;
	  
  }
  
  public ArrayList<LocationResult> loadLocationResult (String destination, String query, String polyline) {
	  
	  //Google Maps is smart enough to use both location and place description
	  //to find places. So we just concatenate them. 
	  String mapsQuery = "query=" + query; //+ (destination + " " + query).replace(' ','+');
	  
	  List<GeoPoint> routePoints = decodePoly(polyline);
	  ArrayList<LocationResult> locList = new ArrayList<LocationResult>();
	  
	  List<GeoPoint> testPoints = new ArrayList<GeoPoint>();
	  
	  int routeLen = routePoints.size();
	  
	  GeoPoint q1 = routePoints.get(routeLen/4);
	  GeoPoint q2 = routePoints.get(routeLen/2);
	  GeoPoint q3 = routePoints.get(routeLen*3/4);
	  
	  testPoints.add(q1);
	  testPoints.add(q2);
	  testPoints.add(q3);
	  
	  for (GeoPoint p : testPoints) {

		  double lat = ((double)p.getLatitudeE6())/1000000.0;
		  double lng = ((double)p.getLongitudeE6())/1000000.0;

		  //hard code the radius to 5, limit to first result
		  String location = "&location=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&radius=5";

		  //find one place close to our query 1/4th of the way into the map. 
		  String locReq= PLACES_URL + mapsQuery + "&sensor=true&key=" + API_KEY + location;

		  try{
			  String result = searchRequest(locReq); 

			  //block until it finishes, then updates the list of lat/lng in order
			  locList.add(processPlaceResponse(result));

		  }catch(Exception e){
			  Log.v("Exception Location Result","Exception:"+e.getMessage());
		  }	

	  }

	 return locList;
  }
  
  public class LocationResult {
	  
	  	//These are actually microdegrees, so divide by 1,000,000 to get the decimal lat/lng
		public double lat, lng;
		public String name, address;

		public LocationResult(double i, double j, String n, String a) {
		
			lat = i;
			lng = j;
			name = n;
			address = a;
		}
	}
  
  public LocationResult processPlaceResponse(String resp) throws IllegalStateException, 
		IOException, JSONException, NoSuchAlgorithmException {
	  
		Log.v("gsearch","gsearch result:"+resp);
		JSONObject mResponseObject = new JSONObject(resp);
		JSONArray results = mResponseObject.getJSONArray("results");
		JSONObject firstResult = results.getJSONObject(0);
		JSONObject geometry = firstResult.getJSONObject("geometry");
		JSONObject location = geometry.getJSONObject("location");
		
		String address = firstResult.getString("formatted_address");
		String name = firstResult.getString("name");
		double lat = location.getDouble("lat");
		double lng = location.getDouble("lng");
		
	  return new LocationResult(lat,lng,name,address);
  }
  
  public String searchRequest(String searchURL) throws MalformedURLException, IOException  {

		//StringBuilder response = new StringBuilder();
	    String resultList = null;
		Log.v("gsearch","gsearch url:"+searchURL);
		URL url = new URL(searchURL);
		String retVal = "";
	  
         
		HttpGet httpGet = new HttpGet(searchURL);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpClient httpClient = sslClient(new DefaultHttpClient(httpParameters));
		HttpResponse response = httpClient.execute(httpGet);
		
		retVal = inputStreamToString(response.getEntity().getContent());
		
		
	    return retVal; 
	}
  
  private HttpClient sslClient(HttpClient client) {
	    try {
	        X509TrustManager tm = new X509TrustManager() { 
	            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
	            }

	            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
	            }

	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }

	        };
	        SSLContext ctx = SSLContext.getInstance("TLS");
	        ctx.init(null, new TrustManager[]{tm}, null);
	        SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
	        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        ClientConnectionManager ccm = client.getConnectionManager();
	        SchemeRegistry sr = ccm.getSchemeRegistry();
	        sr.register(new Scheme("https", ssf, 443));
	        return new DefaultHttpClient(ccm, client.getParams());
	    } catch (Exception ex) {
	        return null;
	    }
	}
  
  public class MySSLSocketFactory extends SSLSocketFactory {
	     SSLContext sslContext = SSLContext.getInstance("TLS");

	     public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	         super(truststore);

	         TrustManager tm = new X509TrustManager() {
	             public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	             }

	             public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	             }

	             public X509Certificate[] getAcceptedIssuers() {
	                 return null;
	             }
	         };

	         sslContext.init(null, new TrustManager[] { tm }, null);
	     }

	     public MySSLSocketFactory(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
	        super(null);
	        sslContext = context;
	     }

	     @Override
	     public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	         return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	     }

	     @Override
	     public Socket createSocket() throws IOException {
	         return sslContext.getSocketFactory().createSocket();
	     }
	}
  private String inputStreamToString(InputStream is) {
	  String line = "";
	  StringBuilder total = new StringBuilder();

	  // Wrap a BufferedReader around the InputStream
	  BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	  // Read response until the end
	  try {
		while ((line = rd.readLine()) != null) { 
			  total.append(line); 
		  }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	  // Return full string
	  return total.toString();
  }
  
  public String processDirectionResponse(String resp) throws IllegalStateException, 
	IOException, JSONException, NoSuchAlgorithmException {
		
		Log.v("gsearch","gsearch result:"+resp);
		JSONObject mResponseObject = new JSONObject(resp);
		JSONArray routes = mResponseObject.getJSONArray("routes");
		JSONObject routeFirst = routes.getJSONObject(0);
		JSONObject polyline = routeFirst.getJSONObject("overview_polyline");
		String result = polyline.getString("points");
		return result;
		
	}
	
	private class myGoogleSearch extends AsyncTask<String, Integer, String> {
		
		protected String doInBackground(String... searchKey) {
			
			String key = searchKey[0];

			try{
				return searchRequest(key);
			}catch(Exception e){
				Log.v("Exception google search","Exception:"+e.getMessage());
				return "";
						
			}
		}		
		
		/*
		protected void onPostExecute(String result) {
			try{
				processResponse(result);
			}catch(Exception e){
				Log.v("Exception google search","Exception:"+e.getMessage());
						
			}		
		}
		*/
	}
	
	//straight from google. decodes polyline
	private List<GeoPoint> decodePoly(String encoded) {

		  List<GeoPoint> poly = new ArrayList<GeoPoint>();
		  int index = 0, len = encoded.length();
		  int lat = 0, lng = 0;

		  while (index < len) {
		      int b, shift = 0, result = 0;
		      do {
		          b = encoded.charAt(index++) - 63;
		          result |= (b & 0x1f) << shift;
		          shift += 5;
		      } while (b >= 0x20);
		      int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		      lat += dlat;

		      shift = 0;
		      result = 0;
		      do {
		          b = encoded.charAt(index++) - 63;
		          result |= (b & 0x1f) << shift;
		          shift += 5;
		      } while (b >= 0x20);
		      int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		      lng += dlng;

		      GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
		           (int) (((double) lng / 1E5) * 1E6));
		      poly.add(p);
		  }

		  return poly;
		}
	
  
  //-------------------------------------------------------
  // private auxiliary functions
  
  
}