/*
 *   defines the Place object to store POI result
 * 
 * 
 */
package com.example.enroute;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable{
  
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

  /********************************************************
   * 
   *  Getters and Setters
   * 
   ********************************************************/
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
	
	/********************************************************
	 * 
	 *  Parceling
	 * 
	 ********************************************************/

	  
  public Place(Parcel in){
    
      String[] data = new String[7];
      in.readStringArray(data);
      
      //read in parcelled data as strings
      this.name = data[0];
      this.phoneNumber = data[1];
      this.latitude = Integer.parseInt( data[2] );
      this.longitude = Integer.parseInt( data[3] );
      this.address = data[4];
      this.distance = Double.parseDouble( data[5] );
      this.distanceOffRoute = Double.parseDouble( data[6] );
      
  }


  @Override
  public int describeContents(){
      return 0;
  }
  
  @Override
  public void writeToParcel(Parcel dest, int flags) {
      dest.writeStringArray(new String[] {
          this.name,
          this.phoneNumber,
          Integer.toString( this.latitude ),
          Integer.toString( this.longitude ),
          this.address,
          Double.toString( this.distance ),
          Double.toString( this.distanceOffRoute )
     });
  }
  
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
      public Place createFromParcel(Parcel in) {
          return new Place(in); 
      }
  
      public Place[] newArray(int size) {
          return new Place[size];
      }
  };

}
