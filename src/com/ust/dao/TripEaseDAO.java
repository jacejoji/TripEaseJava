package com.ust.dao;

import java.util.ArrayList;

import com.ust.bean.*;
import com.ust.service.*;
import com.ust.util.Data;  

public class TripEaseDAO {
    // Fetching data from DataUtil
    public static ArrayList<CredentialsBean> userList = Data.getLoginData();
    public static ArrayList<ProfileBean> profileList = Data.getProfileData();
    public static ArrayList<VehicleBean> vehicleList = Data.getVehicleData();
    public static ArrayList<DriverBean> driverList = Data.getDriverData();
    public static ArrayList<RouteBean> routeList = Data.getRouteData();
    public static ArrayList<ReservationBean> reservationList = Data.getReservationData();
    
    //User authentication
	public CredentialsBean login(String userID, String password) {
		for (CredentialsBean user : userList) {
			if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}
	
	//View Profile
	public ProfileBean viewProfile(String userID) {
		for (ProfileBean profile : profileList) {
			if (profile.getUserID().equals(userID)) {
				return profile;
			}
		}
		return null;
	}
}
