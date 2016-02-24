package com.baoluo.community.entity;

import java.io.Serializable;

public class ModifyInfoModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private LocationArea LocationArea;
	public ModifyInfoModel(com.baoluo.community.entity.LocationArea locationArea) {
		super();
		LocationArea = locationArea;
	}
	public ModifyInfoModel() {
		super();
	}
	public LocationArea getLocationArea() {
		return LocationArea;
	}
	public void setLocationArea(LocationArea locationArea) {
		LocationArea = locationArea;
	}
	

}
