package com.ust.service;
import java.util.ArrayList;
import com.ust.bean.*;

public interface Administrator {
	// Vehicle operations
    String addVehicle(VehicleBean vehicleBean);
    int deleteVehicle(ArrayList<String> vehicleIDs);
    VehicleBean viewVehicle(String vehicleID);
    boolean modifyVehicle(VehicleBean vehicleBean);

    // Driver operations
    String addDriver(DriverBean driverBean);
    int deleteDriver(ArrayList<String> driverIDs);
    boolean modifyDriver(DriverBean driverBean);
    boolean allotDriver(String reservationID, String driverID);

    // Route operations
    String addRoute(RouteBean routeBean);
    int deleteRoute(ArrayList<String> routeIDs);
    RouteBean viewRoute(String routeID);
    boolean modifyRoute(RouteBean routeBean);

    // Booking operations
    ArrayList<ReservationBean> viewBookingDetails(String journeyDate, String source, String destination);

}
