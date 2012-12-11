package com.example.enroute;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.enroute.Constants;

public class MapResultsActivity extends MapActivity implements LocationListener, android.content.DialogInterface.OnClickListener  {

  //----------------------------------------------------------------
  //Instance Variables

  //misc
  private Context mainContext;
  ArrayList<Place> results;
  
  //map configs
  private MapView mapView;
  private MyLocationOverlay myLocation;
  private MapController mapController;
  private ArrayList<OverlayItem> mapPoints;
  
  //gps hooks
  private LocationManager locManager;
  private LocationListener locListener;

  //current location
  private Double curLat;
  private Double curLong;
 
  
  //----------------------------------------------------------------
  //Main onCreate initializer function
  //setup the view, instance vars, etc 
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    //initialize our layout
    setContentView(R.layout.activity_map);
    
    //initialize instance vars
    mainContext = this;
    curLat = 0.0;
    curLong = 0.0;
    
    //setup location listener
    locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    locListener = new MyLocationListener();
    
    //ensure GPS is enabled
    checkGPS();
    
    //load results from data passed to intent
    loadPassedData();
    
    //setup map
    setupMapView();
    
    //load results overlays
    addPins();
    
  }
  
  //load results from intent bundle
  protected void loadPassedData(){
    
    //TODO:  right now this is just dummy data!
    
    // initialize dummy data
    results = new ArrayList<Place>();
    Place p1 = new Place("Zoo", "1234567890", 41313133, -72925149, " 51 Prospect Street New Haven, CT 06511", 1.5, 3 );
    Place p2 = new Place("Commons", "01234567890", 41311876, -72925669, "500 College Street New Haven, CT 06511", 5, 2 );
    Place p3 = new Place("Grove Cemetary", "1112223333", 41312972, -72928244, "  120 High Street New Haven, CT 06511", -3, 4 );
    results.add(p1);
    results.add(p2);
    results.add(p3);
  }
  
  
  //add pin overlays to map 
  public void addPins(){
    
    //setup itemized overlay list to hold all pins
    List<Overlay> mapOverlays = mapView.getOverlays();
    Drawable pinIcon = this.getResources().getDrawable(R.drawable.redpin);
    PlaceItemizedOverlay pins = new PlaceItemizedOverlay(pinIcon, this);
    
    //iterate over results
    for (Place p : results){
      
      //get location
      GeoPoint gp = new GeoPoint(p.getLatitude(), p.getLongitude());
      
      //create description text
      String desc = p.getAddress() + "\n\n" + "Distance: ";
      desc += Double.toString( p.getDistance() );
      desc += "\n" + "Offset: ";
      desc += Double.toString( p.getDistanceOffRoute() );
      
      //create overlay
      OverlayItem oi = new OverlayItem(gp, p.getName(), desc);
      pins.addOverlay(oi);
    }
        
    //add pins to map
    mapOverlays.add(pins);
  }

  //----------------------------------------------------------------
  //handle onClick of list view toggle button
  public void switchToListView(View btn){
    
    Intent intent = new Intent(mainContext, ListResultsActivity.class);
    startActivityForResult(intent, 1);
    
  }
  
  
  //----------------------------------------------------------------
  //Initialize our view and layout 
  //for displaying the map
  private void setupMapView() {

    //initialize map object and variables
    mapView = (MapView) findViewById(R.id.m_wbMap);
    mapController = mapView.getController();
    mapPoints = new ArrayList<OverlayItem>();

    //setup map defaults
    mapView.setBuiltInZoomControls(true);
    mapController.setCenter(Constants.COMMONS);
    mapController.setZoom(Constants.DEFAULT_ZOOM);
    
    //display current location
    setupSelf();

  }
  
  //add an overlay for current location
  //and compass direction to the map
  private void setupSelf(){
    
    //add self to map
    myLocation = new MyLocationOverlay(this, mapView);
    myLocation.enableCompass();
    myLocation.enableMyLocation();
    mapView.getOverlays().add(myLocation);
    
    //center on self
    if (curLat != 0.0){
      GeoPoint gp = new GeoPoint(curLat.intValue(), curLong.intValue());
      mapController.setCenter(gp);
    }
    
  }  

  
  //----------------------------------------------------------------  
  //ensure that GPS settings are enabled before proceeding 
  //if not, fire a dialogue to notify user
  private boolean checkGPS(){

    //check to see if GPS is enabled
    boolean gpsEnabled = false;
    try {
      gpsEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } catch (Exception ex) {
    }

    // don't start listeners if no provider is enabled
    if (!gpsEnabled) {
      AlertDialog.Builder builder = new Builder(this);
      builder.setTitle("Must enable GPS!");
      builder.setMessage("Sorry, but you must enable GPS to continue!");
      builder.setPositiveButton("OK", this);
      builder.setNeutralButton("Cancel", this);
      builder.create().show();
      return false;
    } 

    return true;

  }


  //handles onClick for GPS warning dialog
  //fires intent to change GPS settings if approved
  @Override
  public void onClick(DialogInterface dialog, int which) {
    if(which == DialogInterface.BUTTON_NEUTRAL){
      //nothing, they clicked cancel
    }else if (which == DialogInterface.BUTTON_POSITIVE) {
      startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1001);
    }
  }
  
  
  //listens for GPS movement
  class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

      if (location == null)
        return;

      //grab current location
      curLat = location.getLatitude();
      curLong = location.getLongitude();

    }

    @Override
    public void onProviderDisabled(String provider) {
      // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String provider) {
      // TODO Auto-generated method stub
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      // TODO Auto-generated method stub
    }
  }
  
  
  //closing shop
  //called whenever activity ends
  protected void onStop() {
    super.onStop();

    //stop tracking
    locManager.removeUpdates(locListener);

  }

  
  //----------------------------------------------------------------
  //Default override functions
  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    }
  
  @Override
  public void onLocationChanged(Location location) {
    Log.d("EnRoute", "onLocationChanged with location " + location.toString());
  }

  @Override
  public void onProviderDisabled(String provider) {
  }

  @Override
  public void onProviderEnabled(String provider) {
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }
 
}
