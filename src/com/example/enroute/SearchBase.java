/*
 *   public class for managing our main search algorithm
 * 
 */
package com.example.enroute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

import android.util.Log;


public class SearchBase {

  //-------------------------------------------------------
  // instance variables
	private static final String API_KEY = Constants.AAYUSH_API_KEY;
	private static final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
	private static final String DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?";
	
	//The list of special "place" points on our route
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
	  String directionReq = DIRECTION_URL + "origin=New+Haven,CT&destination=" + destination + "&sensor=true";    
	  
	  String retVal = "";
	  
	  try{
			String result = searchRequest(directionReq); 
			retVal = processDirectionResponse(result);
			
		}catch(Exception e){
			Log.v("Exception google search","Exception:"+e.getMessage());
		}
	  
	  return retVal;
	  
  }
  
  /*-------------------------------------------------------
   * Auxiliary search function to find places along a route
   * @destination - A string entered by the user which is the
   * desired destination. 
   * 
   * @query - The place/type of food searched for. 
   * 
   * @polyline - A Google API polyline which represents the route, found by loadSearchResults
   */
  
  public ArrayList<Place> loadLocationResult (String destination, String query, String polyline) {
	  
	  //Google Maps is smart enough to use both location and place description
	  //to find places. So we just concatenate them. 
	  String mapsQuery = "query=" + query; 
	  
	  List<GeoPoint> routePoints = decodePoly(polyline);
	  ArrayList<Place> locList = new ArrayList<Place>();
	  
	  //Check at these points along the route for our POI's.
	  List<GeoPoint> testPoints = new ArrayList<GeoPoint>();
	  
	  int routeLen = routePoints.size();
	  
	  GeoPoint q1 = routePoints.get(routeLen/10);
	  GeoPoint q2 = routePoints.get(routeLen/9);
	  GeoPoint q3 = routePoints.get(routeLen/8);
	  GeoPoint q4 = routePoints.get(routeLen/3);
	  GeoPoint q5 = routePoints.get(routeLen/2);
	  GeoPoint q6 = routePoints.get(routeLen*4/5);
	  GeoPoint q7 = routePoints.get(routeLen*5/6);
	  
	  testPoints.add(q1);
	  testPoints.add(q2);
	  testPoints.add(q3);
	  testPoints.add(q4);
	  testPoints.add(q5);
	  testPoints.add(q6);
	  testPoints.add(q7);
	  
	  for (GeoPoint p : testPoints) {

		  double lat = ((double)p.getLatitudeE6())/1000000.0;
		  double lng = ((double)p.getLongitudeE6())/1000000.0;

		  String location = "&location=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&radius=5";

		  //The actual Places API query
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
  
  /*-------------------------------------------------------
   * Given response for a place query, we find the first place returned
   * @resp - The result of an HTTP Get for a specific POI 
   * 
   * @polyline - A Google API polyline which represents the route, found by loadSearchResults
   */
  
  public Place processPlaceResponse(String resp) throws IllegalStateException, 
		IOException, JSONException, NoSuchAlgorithmException {
	  
		JSONObject mResponseObject = new JSONObject(resp);
		JSONArray results = mResponseObject.getJSONArray("results");
		JSONObject firstResult = results.getJSONObject(0);
		JSONObject geometry = firstResult.getJSONObject("geometry");
		JSONObject location = geometry.getJSONObject("location");
		
		String address = firstResult.getString("formatted_address");
		String name = firstResult.getString("name");
		int lat = (int)(location.getDouble("lat")*1000000);
		int lng = (int)(location.getDouble("lng")*1000000);
		
	  return new Place(name, "", lat,lng,address, 2,2);
  }
  
  /*-------------------------------------------------------
   * Given response for a destination query, we return the polyline
   * @resp - The result of an HTTP Get for a specific destination
   * 
   */
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
  
  
  /*-------------------------------------------------------
   * Executes an HTTP request to get information from a given API query
   * @searchURL - The URL with parameters of the maps/places API request. 
   * 
   */
  public String searchRequest(String searchURL) throws MalformedURLException, IOException  {

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

		//we use a SSL Client class to avoid getting Certificate denied errors from Google
		HttpClient httpClient = sslClient(new DefaultHttpClient(httpParameters));
		HttpResponse response = httpClient.execute(httpGet);
		
		//Convert the reply into a parseable string
		retVal = inputStreamToString(response.getEntity().getContent());
		
	    return retVal; 
	}
  
  /*-------------------------------------------------------
   * Converts an HttpClient into a special HttpClient with a blind certificate to avoid SSL errors.
   * Gotten from StackOverflow.  
   * @client - an existing HttpClient
   * 
   */
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

  /*-------------------------------------------------------
   * Reads a whole InputStream and converts it into a String for processing. 
   * Gotten from StackOverflow. 
   * @is - An existing InputStream
   * 
   */
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
  

  /*-------------------------------------------------------
   * Converts a String polyline into a list of GeoPoints. Provided by Google. 
   * @encoded - The polyline itself. 
   */
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
  
 /*-------------------------------------------------------
  * For testing only 
  * 
  */
  public ArrayList<Place> genFakePlaces() {
    ArrayList<Place> results = new ArrayList<Place>();
      Place p1 = new Place("Zoo", "1234567890", 41313133, -72925149, "51 Prospect Street New Haven, CT 06511", 1.5, 3 );
      Place p2 = new Place("Commons", "01234567890", 41311876, -72925669, "500 College Street New Haven, CT 06511", 5, 2 );
      Place p3 = new Place("Grove Cemetary", "1112223333", 41312972, -72928244, "120 High Street New Haven, CT 06511", -3, 4 );
      results.add(p1);
      results.add(p2);
      results.add(p3);
      
      return results;
  }
  //-------------------------------------------------------
  // private auxiliary functions
  
}