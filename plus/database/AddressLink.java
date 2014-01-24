package com.mpay.plus.database;

public class AddressLink {

	public AddressLink(String latitude, String longitude, String address,
			String desscription) {
		setLatitude(latitude);
		setLongitude(longitude);
		setAddress(address);
		setDesscription(desscription);
	}

	private String latitude = "";
	private String longitude = "";
	private String address = "";
	private String desscription = "";

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDesscription() {
		return desscription;
	}

	public void setDesscription(String desscription) {
		this.desscription = desscription;
	}
}
