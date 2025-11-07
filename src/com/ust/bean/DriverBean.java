package com.ust.bean;

public class DriverBean {
	 @Override
	public String toString() {
		return "driverID=" + driverID + ", name=" + name + ", street=" + street + ", location=" + location
				+ ", city=" + city + ", state=" + state + ", pincode=" + pincode + ", mobileNo=" + mobileNo
				+ ", licenseNumber=" + licenseNumber;
	}
	 private String driverID;
	    private String name;
	    private String street;
	    private String location;
	    private String city;
	    private String state;
	    private String pincode;
	    public String getDriverID() {
			return driverID;
		}
		public void setDriverID(String driverID) {
			this.driverID = driverID;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getPincode() {
			return pincode;
		}
		public void setPincode(String pincode) {
			this.pincode = pincode;
		}
		public String getMobileNo() {
			return mobileNo;
		}
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}
		public String getLicenseNumber() {
			return licenseNumber;
		}
		public void setLicenseNumber(String licenseNumber) {
			this.licenseNumber = licenseNumber;
		}
		private String mobileNo;
	    private String licenseNumber;
	    

}
