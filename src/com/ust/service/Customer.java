package com.ust.service;

import java.util.ArrayList;

import com.ust.bean.*;

public interface Customer {
	 ArrayList<VehicleBean> viewVehiclesByType(String vehicleType);
	    ArrayList<VehicleBean> viewVehicleBySeats(int noOfSeats);
	    // Route viewing
	    ArrayList<RouteBean> viewAllRoutes();

	    // Booking operations
	    String bookVehicle(ReservationBean reservationBean);
	    boolean cancelBooking(String userID, String reservationID);
	    ReservationBean viewBookingDetails(String reservationID);
	    ReservationBean printBookingDetails(String reservationID);

}
