package com.ust.dao;

import java.util.ArrayList;

import com.ust.bean.CredentialsBean;
import com.ust.bean.DriverBean;
import com.ust.bean.ProfileBean;
import com.ust.bean.ReservationBean;
import com.ust.bean.RouteBean;
import com.ust.bean.VehicleBean;
import com.ust.service.Customer;
import com.ust.util.Data;

public class CustomerDAO  implements Customer{
	private ArrayList<VehicleBean> vehicleList = Data.getVehicleData();
    private ArrayList<DriverBean> driverList = Data.getDriverData();
    private ArrayList<RouteBean> routeList = Data.getRouteData();
    private ArrayList<ReservationBean> reservationList = Data.getReservationData();
    private ArrayList<CredentialsBean> userList = Data.getLoginData();
    private ArrayList<ProfileBean> profileList = Data.getProfileData();
    
    @Override
    public ArrayList<VehicleBean> viewVehiclesByType(String vehicleType) {
        ArrayList<VehicleBean> result = new ArrayList<>();
        for (VehicleBean vehicle : vehicleList) {
            if (vehicle.getType().equalsIgnoreCase(vehicleType)) {
                result.add(vehicle);
            }
        }
        return result;
    }
    @Override
    public ArrayList<VehicleBean> viewVehicleBySeats(int noOfSeats) {
        ArrayList<VehicleBean> result = new ArrayList<>();
        for (VehicleBean vehicle : vehicleList) {
            if (vehicle.getSeatingCapacity() == noOfSeats) {
                result.add(vehicle);
            }
        }
        return result;
    }
    @Override
    public ArrayList<RouteBean> viewAllRoutes() {
        return new ArrayList<>(routeList);
    }
    @Override
    public String bookVehicle(ReservationBean reservationBean) {
        reservationList.add(reservationBean);
        return reservationBean.getReservationID(); // Return reservation ID
    }
    @Override
    public boolean cancelBooking(String userID, String reservationID) {
        for (ReservationBean reservation : reservationList) {
            if (reservation.getReservationID().equals(reservationID) && reservation.getUserID().equals(userID)) {
                reservation.setBookingStatus("Canceled");  // Mark the booking as canceled
                return true;
            }
        }
        return false;
    }
    @Override
    public ReservationBean viewBookingDetails(String reservationID) {
        for (ReservationBean reservation : reservationList) {
            if (reservation.getReservationID().equals(reservationID)) {
                return reservation;
            }
        }
        return null;
    }

    @Override
    public ReservationBean printBookingDetails(String reservationID) {
        return viewBookingDetails(reservationID); // Return the booking details for printing
    }
}
