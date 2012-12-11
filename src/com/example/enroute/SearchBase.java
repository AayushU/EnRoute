/*
 *   public class for managing our main search algorithm
 * 
 * 
 */
package com.example.enroute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;


public class SearchBase {

  //-------------------------------------------------------
  // instance variables
	private static final String API_KEY = "AIzaSyBoRZcwm_ad2YpAdLlA6XKn8m8Gjae9B0E";
	private static final String PLACES_URL = "http://maps.googleapis.com/maps/api/place/search/json?";
	private static final String DIRECTION_URL = "http://maps.googleapis.com/maps/api/directions/json?";
	
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
  
	  //Google Maps is smart enough to use both location and place description
	  //to find places. So we just concatenate them. 
	  String mapsQuery = (destination + " " + query).replace(' ','+');
	  
	  //TODO: Get current location from location manager, format properly
	  String directionReq = DIRECTION_URL + "origin=New+Haven,CT&destination=New+York,NY" + "&sensor=true";    
	  
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
	  
	  //Commented out for now
	  /*
	  String locQuery = PLACES_URL + mapsQuery + "&sensor=true&key=" + API_KEY;
	  
	  try{
			mySearch.execute(locQuery);
			//block until it finishes, then updates the list of lat/lng in order
			processPlaceResponse(mySearch.get().toString());
			
		}catch(Exception e){
			Log.v("Exception google search","Exception:"+e.getMessage());
		}
		*/
  
  }
  
  public String processPlaceResponse(String resp) throws IllegalStateException, 
		IOException, JSONException, NoSuchAlgorithmException {
			

	  return "";
  }
  
  public String searchRequest(String searchURL) throws MalformedURLException, IOException  {

		StringBuilder response = new StringBuilder();
		Log.v("gsearch","gsearch url:"+searchURL);
		URL url = new URL(searchURL);
		
		HttpURLConnection httpconn =(HttpURLConnection) url.openConnection();
		
		if(httpconn.getResponseCode()==HttpURLConnection.HTTP_OK){
			BufferedReader input = new BufferedReader(
					new InputStreamReader(httpconn.getInputStream()), 
					8192);
			String strLine = null;
			while ((strLine = input.readLine()) != null) {
				response.append(strLine);
			}
			input.close();
		}
	    return response.toString();
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