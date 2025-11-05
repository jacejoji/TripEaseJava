package com.ust.dao;

import java.util.ArrayList;

import com.ust.bean.*;
import com.ust.service.*;
import com.ust.util.Data;  

public class TripEaseDAO {

    // Fetching data from DataUtil
    private ArrayList<VehicleBean> vehicleList = Data.getVehicleData();
    private ArrayList<DriverBean> driverList = Data.getDriverData();
    private ArrayList<RouteBean> routeList = Data.getRouteData();
    private ArrayList<ReservationBean> reservationList = Data.getReservationData();
    private ArrayList<CredentialsBean> userList = Data.getLoginData();
    private ArrayList<ProfileBean> profileList = Data.getProfileData();
    
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
