package com.baoluo.community.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 地理位置
 * 
 * @author Ryan_Fu 2015-5-18
 */
public class LocationInfo implements Serializable {

	private static final long serialVersionUID = 1046578047599225267L;
	private Date  LastTime;
	private String Name;
	private double Lat;// 经度
	private double Lon;// 纬度
	private String Address;// 街道
	private String GeoHash;

	public LocationInfo() {
		super();
	}
	
	public LocationInfo(Date lastTime, String name, double lat, double lon,
			String address, String geoHash) {
		super();
		LastTime = lastTime;
		Name = name;
		Lat = lat;
		Lon = lon;
		Address = address;
		GeoHash = geoHash;
	}
	
	public Date getLastTime() {
		return LastTime;
	}

	public void setLastTime(Date lastTime) {
		LastTime = lastTime;
	}

	public String getGeoHash() {
		return GeoHash;
	}

	public void setGeoHash(String geoHash) {
		GeoHash = geoHash;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public double getLon() {
		return Lon;
	}

	public void setLon(double lon) {
		Lon = lon;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

}