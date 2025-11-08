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

			// Check user id
			if (user.getUserID().equals(userID)) {

				// Check password
				if (user.getPassword().equals(password)) {

					// Mark logged in
					user.setLoginStatus(1);

					// VERY IMPORTANT: return the user with correct userType
					return user;
				} else {
					return null; // password mismatch
				}
			}
		}
		return null; // user not found
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
	public static String register(ProfileBean profile) {
		String generatedID = profile.getFirstName().substring(0, 2).toUpperCase() +
				String.format("%04d", userList.size() + 1);

		profile.setUserID(generatedID);

		profileList.add(profile);

		CredentialsBean cred = new CredentialsBean();
		cred.setUserID(generatedID);
		cred.setPassword(profile.getPassword());
		cred.setUserType("C");
		cred.setLoginStatus(0);

		userList.add(cred);

		return generatedID;
	}
	public String changePassword(CredentialsBean credentials, String newPassword) {
		for (CredentialsBean user : userList) {
			if (user.getUserID().equals(credentials.getUserID())) {
				if (!user.getPassword().equals(credentials.getPassword())) {
					return "INVALID";
				}
				user.setPassword(newPassword);
				return "SUCCESS";
			}
		}
		return "FAIL";
	}
	public boolean logout(String userID) {
		for (CredentialsBean user : userList) {
			if (user.getUserID().equals(userID)) {
				user.setLoginStatus(0);
				return true;
			}
		}
		return false;
	}



}
