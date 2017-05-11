package com.asiainfo.simulation.test.model;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = 6360278651218111661L;
	private String userid;
	private String address;
	
	public Address(String userid, String address) {
		this.userid = userid;
		this.address = address;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
