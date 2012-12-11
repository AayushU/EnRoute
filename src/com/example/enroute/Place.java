/*
 *   defines the Place object to store POI result
 * 
 * 
 */
package com.example.enroute;

public class Place {
  
  //-------------------------------------------------------
  // instance variables
	private String name;
	private String phoneNumber;
	private int latitude;
	private int longitude;
	private String address;
	private double distance;
	private double distanceOffRoute;


	//-------------------------------------------------------
  // constructor 
	public Place(String name, String phoneNumber, int lat, int lon,
	    String address, double distance, double distanceOffRoute) {
		super();
		
		//set local vars
		this.name = name;
		this.phoneNumber = phoneNumber;
	  this.latitude = lat;
	  this.longitude = lon;
	  this.address = address;
		this.distance = distance;
		this.distanceOffRoute = distanceOffRoute;
		
	}

	//-------------------------------------------------------
	// Accessors + Mutators
  public void setName(String new_name) {
    this.name = new_name;
  }

  public String getName() {
    return name;
  }
  
	public void setPhoneNumber(String new_phoneNumber) {
		this.phoneNumber = new_phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setLatitude(int new_latitude) {
	  this.latitude = new_latitude;
	}
	
	public int getLatitude() {
	  return latitude;
	}
	
	public void setLongitude(int new_longitude) {
	  this.longitude = new_longitude;
	}
	
	public int getLongitude() {
	  return longitude;
	}
	
	public void setAddress(String new_address) {
	  this.address = new_address;
	}
	
	public String getAddress() {
	  return address;
	}
	 
	public void setDistance(double new_distance) {
		this.distance = new_distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistanceOffRoute(double new_distanceOffRoute) {
	  this.distanceOffRoute = new_distanceOffRoute;
	}
	
	public double getDistanceOffRoute() {
		return distanceOffRoute;
	}

}
