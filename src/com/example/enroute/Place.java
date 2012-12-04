package com.example.enroute;

public class Place {
	String name;
	double distance;
	double distanceOffRoute;
	String phoneNumber;

	public Place(String name, double distance, double distanceOffRoute,
			String phoneNumber) {
		super();
		this.name = name;
		this.distance = distance;
		this.distanceOffRoute = distanceOffRoute;
		this.phoneNumber = phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public double getDistance() {
		return distance;
	}

	public double getDistanceOffRoute() {
		return distanceOffRoute;
	}

	public void setDistanceOffRoute(double distanceOffRoute) {
		this.distanceOffRoute = distanceOffRoute;
	}
}
