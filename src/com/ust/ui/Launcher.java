package com.ust.ui;
import com.ust.bean.DriverBean;
import com.ust.bean.ReservationBean;
import com.ust.bean.RouteBean;
import com.ust.bean.VehicleBean;
import com.ust.dao.TripEaseDAO;
import java.util.ArrayList;
import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TripEaseDAO tripEaseDAO = new TripEaseDAO();

        // Display the main menu
        System.out.println("Welcome to the Automation of Travel Agency (ATA) System");

        // Example: Administrator actions
        System.out.println("\nAdministrator: Adding a new vehicle");
        VehicleBean newVehicle = new VehicleBean();
        newVehicle.setVehicleID("VH006");
        newVehicle.setName("BMW X5");
        newVehicle.setType("SUV");
        newVehicle.setSeatingCapacity(5);
        newVehicle.setFarePerKM(18.0);
        System.out.println(tripEaseDAO.addVehicle(newVehicle));

        // Admin views all vehicles
        System.out.println("\nAdministrator: Viewing all vehicles by type SUV");
        ArrayList<VehicleBean> allVehicles = tripEaseDAO.viewVehiclesByType("SUV");
        for (VehicleBean vehicle : allVehicles) {
            System.out.println(vehicle.getName() + " | Type: " + vehicle.getType() + " | Seating: " + vehicle.getSeatingCapacity());
        }

        // Admin deletes a vehicle
        System.out.println("\nAdministrator: Deleting a vehicle");
        ArrayList<String> vehicleIDsToDelete = new ArrayList<>();
        vehicleIDsToDelete.add("VH001");
        System.out.println("Deleted vehicles: " + tripEaseDAO.deleteVehicle(vehicleIDsToDelete));

        // Admin adds a new route
        System.out.println("\nAdministrator: Adding a new route");
        RouteBean newRoute = new RouteBean();
        newRoute.setRouteID("RT005");
        newRoute.setSource("New York");
        newRoute.setDestination("Chicago");
        newRoute.setDistance(790);
        newRoute.setTravelDuration(12);
        System.out.println(tripEaseDAO.addRoute(newRoute));

        // Admin views routes
        System.out.println("\nAdministrator: Viewing all routes");
        ArrayList<RouteBean> allRoutes = tripEaseDAO.viewAllRoutes();
        for (RouteBean route : allRoutes) {
            System.out.println("Route: " + route.getSource() + " to " + route.getDestination() + " | Distance: " + route.getDistance() + " KM");
        }

        // Admin adds a driver
        System.out.println("\nAdministrator: Adding a new driver");
        DriverBean newDriver = new DriverBean();
        newDriver.setDriverID("DR005");
        newDriver.setName("George Martin");
        newDriver.setStreet("123 Park St");
        newDriver.setLocation("Miami");
        newDriver.setCity("Miami");
        newDriver.setState("FL");
        newDriver.setPincode("33101");
        newDriver.setMobileNo("5555555555");
        newDriver.setLicenseNumber("LIC987654");
        System.out.println(tripEaseDAO.addDriver(newDriver));

        // Customer makes a booking
        System.out.println("\nCustomer: Booking a vehicle");
        ReservationBean reservation = new ReservationBean();
        reservation.setReservationID("RS005");
        reservation.setUserID("U001");
        reservation.setRouteID("RT001");
        reservation.setBookingDate("2025-12-01");
        reservation.setJourneyDate("2025-12-10");
        reservation.setVehicleID("VH002");
        reservation.setDriverID("DR001");
        reservation.setBookingStatus("Confirmed");
        reservation.setTotalFare(250.0);
        reservation.setBoardingPoint("Times Square");
        reservation.setDropPoint("National Mall");
        System.out.println("Booking ID: " + tripEaseDAO.bookVehicle(reservation));

        // Customer views booking details
        System.out.println("\nCustomer: Viewing booking details");
        ReservationBean bookingDetails = tripEaseDAO.viewBookingDetails("RS005");
        System.out.println("Reservation ID: " + bookingDetails.getReservationID());
        System.out.println("Booking Status: " + bookingDetails.getBookingStatus());

        // Customer cancels a booking
        System.out.println("\nCustomer: Canceling a booking");
        System.out.println("Canceling booking RS005: " + tripEaseDAO.cancelBooking("U001", "RS005"));

        // Admin assigns a driver to the booking
        System.out.println("\nAdministrator: Allotting a driver to a booking");
        System.out.println("Assigning driver to booking RS005: " + tripEaseDAO.allotDriver("RS005", "DR005"));

        // Final list of vehicles (This will display all current vehicles in the system)
        System.out.println("\nFinal List of Vehicles:");
        allVehicles = tripEaseDAO.viewVehiclesByType("SUV");
        for (VehicleBean vehicle : allVehicles) {
            System.out.println(vehicle.getName() + " | Type: " + vehicle.getType() + " | Seating: " + vehicle.getSeatingCapacity());
        }

     // Final list of bookings (Including canceled bookings)
        System.out.println("\nFinal List of Bookings (including canceled):");
        ArrayList<ReservationBean> allBookings = tripEaseDAO.getAllBookings();
        for (ReservationBean booking : allBookings) {
            System.out.println("Reservation ID: " + booking.getReservationID() + " | Status: " + booking.getBookingStatus());
        }

        scanner.close();  // Close the scanner object after use
    }
}
