package com.ust.dao;

import java.util.ArrayList;

import com.ust.bean.ReservationBean;
import com.ust.bean.RouteBean;
import com.ust.bean.VehicleBean;
import com.ust.service.Customer;

public class CustDAO implements Customer{

	@Override
	public ArrayList<VehicleBean> viewVehiclesByType(String vehicleType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<VehicleBean> viewVehicleBySeats(int noOfSeats) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<RouteBean> viewAllRoutes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String bookVehicle(ReservationBean reservationBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean cancelBooking(String userID, String reservationID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ReservationBean viewBookingDetails(String reservationID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReservationBean printBookingDetails(String reservationID) {
		// TODO Auto-generated method stub
		return null;
	}

}
