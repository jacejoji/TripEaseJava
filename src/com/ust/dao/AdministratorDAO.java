package com.ust.dao;

import java.util.ArrayList;

import com.ust.bean.CredentialsBean;
import com.ust.bean.DriverBean;
import com.ust.bean.ProfileBean;
import com.ust.bean.ReservationBean;
import com.ust.bean.RouteBean;
import com.ust.bean.VehicleBean;
import com.ust.service.Administrator;
import com.ust.util.Data;
import static com.ust.dao.TripEaseDAO.*;

public class AdministratorDAO  implements Administrator{
	    @Override
	    public String addVehicle(VehicleBean vehicleBean) {
	        vehicleList.add(vehicleBean);
	        return "Vehicle added successfully";
	    }
	    @Override
	    public int deleteVehicle(ArrayList<String> vehicleIDs) {
	        int count = 0;
	        for (String vehicleID : vehicleIDs) {
	            for (VehicleBean vehicle : vehicleList) {
	                if (vehicle.getVehicleID().equals(vehicleID)) {
	                    vehicleList.remove(vehicle);
	                    count++;
	                    break;
	                }
	            }
	        }
	        return count;
	    }

	    @Override
	    public VehicleBean viewVehicle(String vehicleID) {
	        for (VehicleBean vehicle : vehicleList) {
	            if (vehicle.getVehicleID().equals(vehicleID)) {
	                return vehicle;
	            }
	        }
	        return null;
	    }

	    @Override
	    public boolean modifyVehicle(VehicleBean vehicleBean) {
	        for (int i = 0; i < vehicleList.size(); i++) {
	            if (vehicleList.get(i).getVehicleID().equals(vehicleBean.getVehicleID())) {
	                vehicleList.set(i, vehicleBean); // Replace old vehicle with modified one
	                return true;
	            }
	        }
	        return false;
	    }

	    @Override
	    public String addDriver(DriverBean driverBean) {
	        driverList.add(driverBean);
	        return "Driver added successfully";
	    }

	    @Override
	    public int deleteDriver(ArrayList<String> driverIDs) {
	        int count = 0;
	        for (String driverID : driverIDs) {
	            for (DriverBean driver : driverList) {
	                if (driver.getDriverID().equals(driverID)) {
	                    driverList.remove(driver);
	                    count++;
	                    break;
	                }
	            }
	        }
	        return count;
	    }

	    @Override
	    public boolean modifyDriver(DriverBean driverBean) {
	        for (int i = 0; i < driverList.size(); i++) {
	            if (driverList.get(i).getDriverID().equals(driverBean.getDriverID())) {
	                driverList.set(i, driverBean);  // Replace old driver with modified one
	                return true;
	            }
	        }
	        return false;
	    }

	    @Override
	    public boolean allotDriver(String reservationID, String driverID) {
	        for (ReservationBean reservation : reservationList) {
	            if (reservation.getReservationID().equals(reservationID)) {
	                reservation.setDriverID(driverID);  // Assign driver to the reservation
	                return true;
	            }
	        }
	        return false;
	    }

	    @Override
	    public String addRoute(RouteBean routeBean) {
	        routeList.add(routeBean);
	        return "Route added successfully";
	    }

	    @Override
	    public int deleteRoute(ArrayList<String> routeIDs) {
	        int count = 0;
	        for (String routeID : routeIDs) {
	            for (RouteBean route : routeList) {
	                if (route.getRouteID().equals(routeID)) {
	                    routeList.remove(route);
	                    count++;
	                    break;
	                }
	            }
	        }
	        return count;
	    }

	    @Override
	    public RouteBean viewRoute(String routeID) {
	        for (RouteBean route : routeList) {
	            if (route.getRouteID().equals(routeID)) {
	                return route;
	            }
	        }
	        return null;
	    }

	    @Override
	    public boolean modifyRoute(RouteBean routeBean) {
	        for (int i = 0; i < routeList.size(); i++) {
	            if (routeList.get(i).getRouteID().equals(routeBean.getRouteID())) {
	                routeList.set(i, routeBean);  // Replace old route with modified one
	                return true;
	            }
	        }
	        return false;
	    }

	    @Override
	    public ArrayList<ReservationBean> viewBookingDetails(String journeyDate, String source, String destination) {
	        ArrayList<ReservationBean> result = new ArrayList<>();
	        for (ReservationBean reservation : reservationList) {
	            if (reservation.getJourneyDate().equalsIgnoreCase(journeyDate) &&
	                reservation.getRouteID().equalsIgnoreCase(source + "-" + destination)) {
	                result.add(reservation);
	            }
	        }
	        return result;
	    }
	    public ArrayList<ReservationBean> getAllBookings() {
	        return new ArrayList<>(reservationList);
	    }
}
