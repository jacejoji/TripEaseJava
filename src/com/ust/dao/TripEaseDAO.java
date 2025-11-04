package com.ust.dao;

import java.util.ArrayList;

import com.ust.bean.DriverBean;
import com.ust.bean.ReservationBean;
import com.ust.bean.RouteBean;
import com.ust.bean.VehicleBean;
import com.ust.service.Administrator;
import com.ust.service.Customer;

public class TripEaseDAO implements Administrator,Customer{

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

	@Override
	public String addVehicle(VehicleBean vehicleBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteVehicle(ArrayList<String> vehicleIDs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public VehicleBean viewVehicle(String vehicleID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean modifyVehicle(VehicleBean vehicleBean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String addDriver(DriverBean driverBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteDriver(ArrayList<String> driverIDs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean modifyDriver(DriverBean driverBean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allotDriver(String reservationID, String driverID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String addRoute(RouteBean routeBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteRoute(ArrayList<String> routeIDs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RouteBean viewRoute(String routeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean modifyRoute(RouteBean routeBean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<ReservationBean> viewBookingDetails(String journeyDate, String source, String destination) {
		// TODO Auto-generated method stub
		return null;
	}

}
