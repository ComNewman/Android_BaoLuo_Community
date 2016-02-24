package com.baoluo.community.entity;

import java.io.Serializable;
	/**
	 * 地区
	 * @author xiangyang.fu
	 *
	 */
public class LocationArea implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Province;
	private String City;
	public LocationArea() {
		super();
		 
	}
	public LocationArea(String province, String city) {
		super();
		Province = province;
		City = city;
	}
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	
	
}
