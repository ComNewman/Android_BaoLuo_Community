package com.baoluo.community.entity;

import java.io.Serializable;

public class LocationModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocationInfo Location;

	public LocationInfo getLocation() {
		return Location;
	}

	public void setLocation(LocationInfo location) {
		Location = location;
	}

}
