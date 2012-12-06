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
	private double distance;
	private double distanceOffRoute;
	private String phoneNumber;

	//-------------------------------------------------------
  // constructor
	public Place(String name, double distance, double distanceOffRoute, 
	    String phoneNumber) {
		super();
		
		//set local vars
		this.name = name;
		this.distance = distance;
		this.distanceOffRoute = distanceOffRoute;
		this.phoneNumber = phoneNumber;
	}

	//-------------------------------------------------------
	// Accessors + Mutators
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
	  return name;
	}
	 
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistanceOffRoute(double distanceOffRoute) {
	  this.distanceOffRoute = distanceOffRoute;
	}
	
	public double getDistanceOffRoute() {
		return distanceOffRoute;
	}

}
