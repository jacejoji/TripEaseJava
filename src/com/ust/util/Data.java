package com.ust.util;

import com.ust.bean.*;
import java.util.ArrayList;

public class Data {
	// Create fake data for users
	public static ArrayList<CredentialsBean> getLoginData() {
		ArrayList<CredentialsBean> userList = new ArrayList<>();
	CredentialsBean user1 = new CredentialsBean();
	user1.setUserID("customer");
	user1.setPassword("password1");
	user1.setUserType("Customer");
	user1.setLoginStatus(1);
	userList.add(user1);
	CredentialsBean user2 = new CredentialsBean();
	user2.setUserID("admin");
	user2.setPassword("password2");
	user2.setUserType("Administrator");
	user2.setLoginStatus(1);
	userList.add(user2);
	return userList;
	}
	
	//Create fake profile data for user
	public static ArrayList<ProfileBean> getProfileData() {
		ArrayList<ProfileBean> profileList = new ArrayList<>();
		ProfileBean profile1 = new ProfileBean();
		profile1.setUserID("customer");
		profile1.setFirstName("John Doe");
		profile1.setLastName("Doe");
		profile1.setDateOfBirth("1990-01-01");
		profile1.setGender("Male");
		profile1.setStreet("123 Main St");
		profile1.setLocation("New York");
		profile1.setCity("New York");
		profile1.setState("NY");
		profile1.setPincode("10001");
		profile1.setMobileNo("1234567890");
		profile1.setEmailID("jdoe@ust");
		profile1.setPassword("password1");
		profileList.add(profile1);
		
		ProfileBean profile2 = new ProfileBean();
		profile2.setUserID("admin");
		profile2.setFirstName("Jane Doe");
		profile2.setLastName("Doe");
		profile2.setDateOfBirth("1990-01-01");
		profile2.setGender("Female");
		profile2.setStreet("456 Main St");
		profile2.setLocation("Los Angeles");
		profile2.setCity("Los Angeles");
		profile2.setState("CA");
		profile2.setPincode("90001");
		profile2.setMobileNo("9876543210");
		profile2.setEmailID("jdoe@ust");
		profile2.setPassword("password2");
		profileList.add(profile2);
		return profileList;
	}

    // Create fake data for vehicles
    public static ArrayList<VehicleBean> getVehicleData() {
        ArrayList<VehicleBean> vehicleList = new ArrayList<>();

        VehicleBean vehicle1 = new VehicleBean();
        vehicle1.setVehicleID("VH001");
        vehicle1.setName("Toyota Corolla");
        vehicle1.setType("Sedan");
        vehicle1.setSeatingCapacity(4);
        vehicle1.setFarePerKM(10.5);
        vehicleList.add(vehicle1);

        VehicleBean vehicle2 = new VehicleBean();
        vehicle2.setVehicleID("VH002");
        vehicle2.setName("Honda CR-V");
        vehicle2.setType("SUV");
        vehicle2.setSeatingCapacity(6);
        vehicle2.setFarePerKM(12.5);
        vehicleList.add(vehicle2);

        VehicleBean vehicle3 = new VehicleBean();
        vehicle3.setVehicleID("VH003");
        vehicle3.setName("Mercedes-Benz S-Class");
        vehicle3.setType("Luxury");
        vehicle3.setSeatingCapacity(4);
        vehicle3.setFarePerKM(25.0);
        vehicleList.add(vehicle3);

        VehicleBean vehicle4 = new VehicleBean();
        vehicle4.setVehicleID("VH004");
        vehicle4.setName("Tesla Model X");
        vehicle4.setType("Electric SUV");
        vehicle4.setSeatingCapacity(7);
        vehicle4.setFarePerKM(15.0);
        vehicleList.add(vehicle4);

        VehicleBean vehicle5 = new VehicleBean();
        vehicle5.setVehicleID("VH005");
        vehicle5.setName("Ford Mustang");
        vehicle5.setType("Sports Car");
        vehicle5.setSeatingCapacity(2);
        vehicle5.setFarePerKM(20.0);
        vehicleList.add(vehicle5);

        return vehicleList;
    }

    // Create fake data for drivers
    public static ArrayList<DriverBean> getDriverData() {
        ArrayList<DriverBean> driverList = new ArrayList<>();

        DriverBean driver1 = new DriverBean();
        driver1.setDriverID("DR001");
        driver1.setName("John Doe");
        driver1.setStreet("123 Elm St");
        driver1.setLocation("New York");
        driver1.setCity("New York");
        driver1.setState("NY");
        driver1.setPincode("10001");
        driver1.setMobileNo("1234567890");
        driver1.setLicenseNumber("ABC123456");
        driverList.add(driver1);

        DriverBean driver2 = new DriverBean();
        driver2.setDriverID("DR002");
        driver2.setName("Jane Smith");
        driver2.setStreet("456 Maple Ave");
        driver2.setLocation("Los Angeles");
        driver2.setCity("Los Angeles");
        driver2.setState("CA");
        driver2.setPincode("90001");
        driver2.setMobileNo("9876543210");
        driver2.setLicenseNumber("XYZ654321");
        driverList.add(driver2);

        DriverBean driver3 = new DriverBean();
        driver3.setDriverID("DR003");
        driver3.setName("Sam Wilson");
        driver3.setStreet("789 Oak Rd");
        driver3.setLocation("Chicago");
        driver3.setCity("Chicago");
        driver3.setState("IL");
        driver3.setPincode("60007");
        driver3.setMobileNo("1122334455");
        driver3.setLicenseNumber("LMN987654");
        driverList.add(driver3);

        DriverBean driver4 = new DriverBean();
        driver4.setDriverID("DR004");
        driver4.setName("Emily Davis");
        driver4.setStreet("101 Pine Ln");
        driver4.setLocation("San Francisco");
        driver4.setCity("San Francisco");
        driver4.setState("CA");
        driver4.setPincode("94101");
        driver4.setMobileNo("5566778899");
        driver4.setLicenseNumber("OPQ456789");
        driverList.add(driver4);

        return driverList;
    }

    // Create fake data for routes
    public static ArrayList<RouteBean> getRouteData() {
        ArrayList<RouteBean> routeList = new ArrayList<>();

        RouteBean route1 = new RouteBean();
        route1.setRouteID("RT001");
        route1.setSource("New York");
        route1.setDestination("Washington D.C.");
        route1.setDistance(225);
        route1.setTravelDuration(4); // Hours
        routeList.add(route1);

        RouteBean route2 = new RouteBean();
        route2.setRouteID("RT002");
        route2.setSource("Los Angeles");
        route2.setDestination("San Francisco");
        route2.setDistance(380);
        route2.setTravelDuration(6); // Hours
        routeList.add(route2);

        RouteBean route3 = new RouteBean();
        route3.setRouteID("RT003");
        route3.setSource("Chicago");
        route3.setDestination("Detroit");
        route3.setDistance(280);
        route3.setTravelDuration(5); // Hours
        routeList.add(route3);

        RouteBean route4 = new RouteBean();
        route4.setRouteID("RT004");
        route4.setSource("Miami");
        route4.setDestination("Orlando");
        route4.setDistance(230);
        route4.setTravelDuration(4); // Hours
        routeList.add(route4);

        return routeList;
    }

    // Create fake data for reservations
    public static ArrayList<ReservationBean> getReservationData() {
        ArrayList<ReservationBean> reservationList = new ArrayList<>();

        ReservationBean reservation1 = new ReservationBean();
        reservation1.setReservationID("RS001");
        reservation1.setUserID("U001");
        reservation1.setRouteID("RT001");
        reservation1.setBookingDate("2025-12-01");
        reservation1.setJourneyDate("2025-12-10");
        reservation1.setVehicleID("VH001");
        reservation1.setDriverID("DR001");
        reservation1.setBookingStatus("Confirmed");
        reservation1.setTotalFare(100.0);
        reservation1.setBoardingPoint("Times Square");
        reservation1.setDropPoint("National Mall");
        reservationList.add(reservation1);

        ReservationBean reservation2 = new ReservationBean();
        reservation2.setReservationID("RS002");
        reservation2.setUserID("U002");
        reservation2.setRouteID("RT002");
        reservation2.setBookingDate("2025-12-02");
        reservation2.setJourneyDate("2025-12-12");
        reservation2.setVehicleID("VH002");
        reservation2.setDriverID("DR002");
        reservation2.setBookingStatus("Pending");
        reservation2.setTotalFare(120.0);
        reservation2.setBoardingPoint("Los Angeles Airport");
        reservation2.setDropPoint("Golden Gate Bridge");
        reservationList.add(reservation2);

        ReservationBean reservation3 = new ReservationBean();
        reservation3.setReservationID("RS003");
        reservation3.setUserID("U003");
        reservation3.setRouteID("RT003");
        reservation3.setBookingDate("2025-12-03");
        reservation3.setJourneyDate("2025-12-15");
        reservation3.setVehicleID("VH003");
        reservation3.setDriverID("DR003");
        reservation3.setBookingStatus("Confirmed");
        reservation3.setTotalFare(150.0);
        reservation3.setBoardingPoint("Chicago O'Hare");
        reservation3.setDropPoint("Detroit Airport");
        reservationList.add(reservation3);

        ReservationBean reservation4 = new ReservationBean();
        reservation4.setReservationID("RS004");
        reservation4.setUserID("U004");
        reservation4.setRouteID("RT004");
        reservation4.setBookingDate("2025-12-04");
        reservation4.setJourneyDate("2025-12-20");
        reservation4.setVehicleID("VH004");
        reservation4.setDriverID("DR004");
        reservation4.setBookingStatus("Confirmed");
        reservation4.setTotalFare(80.0);
        reservation4.setBoardingPoint("Miami Airport");
        reservation4.setDropPoint("Orlando Downtown");
        reservationList.add(reservation4);

        return reservationList;
    }
}
