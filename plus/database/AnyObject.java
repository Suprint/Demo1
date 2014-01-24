package com.mpay.plus.database;

public class AnyObject {

	private String sID = "";
	private String sContent = "";
	private String sDescription = "";
	private String sLink = "";
	private String sType = "";
	private String sIsActive = "";
	private String sPromo = "";
	private String sPrice = "";

	public AnyObject() {
		sID = "";
		sContent = "";
		sDescription = "";
		sLink = "";
		sType = "";
		sIsActive = "";
	}

	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsContent() {
		return sContent;
	}

	public void setsContent(String sContent) {
		this.sContent = sContent;
	}

	public String getsDescription() {
		return sDescription;
	}

	public void setsDescription(String sDescription) {
		this.sDescription = sDescription;
	}

	public String getsLink() {
		return sLink;
	}

	public void setsLink(String sLink) {
		this.sLink = sLink;
	}

	public String getsType() {
		return sType;
	}

	public void setsType(String sType) {
		this.sType = sType;
	}

	public String getsIsActive() {
		return sIsActive;
	}

	public void setsIsActive(String sIsActive) {
		this.sIsActive = sIsActive;
	}

	public String getsPromo() {
		return sPromo;
	}

	public void setsPromo(String sPromo) {
		this.sPromo = sPromo;
	}

	public String getsPrice() {
		return sPrice;
	}

	public void setsPrice(String sPrice) {
		this.sPrice = sPrice;
	}
}
