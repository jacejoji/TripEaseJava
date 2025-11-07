package com.ust.ui;

import java.util.ArrayList;
import java.util.Scanner;

import com.ust.bean.*;
import com.ust.dao.*;

public class LauncherCLI {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		TripEaseDAO tripEaseDAO = new TripEaseDAO();
		AdministratorDAO adminDAO = new AdministratorDAO();
		CustomerDAO customerDAO = new CustomerDAO();
		CredentialsBean user ;

		// ------------------ LOGIN -------------------
		int loginAttempts = 0;
		while(loginAttempts < 3){
			System.out.println("!!! Welcome to TripEase CLI !!!");
			System.out.println("Automation of Travel Agency (ATA)");
			System.out.println("====================================");
		do {
		System.out.print("Enter UserID: ");
		String userID = scanner.nextLine();

		System.out.print("Enter Password: ");
		String password = scanner.nextLine();

		user = tripEaseDAO.login(userID, password);
		if (user == null) {
			System.out.println("Invalid credentials.");
			System.out.println("Please try again."+(3-loginAttempts)+" attempts remaining.");
			loginAttempts++;
		}
		else {
			break;
		}
		}while(loginAttempts < 3);

		System.out.println("Login Successful!");
		System.out.println("Logged in as: " + (user.getUserType().equalsIgnoreCase("Administrator") ? "Administrator" : "Customer"));

		// ------------------ ROLE BASED MENU -------------------
		if (user.getUserType().equalsIgnoreCase("Administrator")) {
			adminMenu(scanner, adminDAO, customerDAO,user);
		} else {
			customerMenu(scanner, customerDAO,user);
		}
		loginAttempts = 0;
		}

		scanner.close();
	}

	// ==========================================================
	// ADMINISTRATOR MENU  (AD-001 → AD-013)
	// ==========================================================
	private static void adminMenu(Scanner scanner, AdministratorDAO adminDAO, CustomerDAO customerDAO,CredentialsBean user) {
		while (true) {
			System.out.println("\n====== ADMIN REQUIREMENTS ======");
			System.out.println("AD-001 : Add Vehicle");
			System.out.println("AD-002 : Delete Vehicle");
			System.out.println("AD-003 : View Vehicle");
			System.out.println("AD-004 : Modify Vehicle");
			System.out.println("AD-005 : Add Route");
			System.out.println("AD-006 : Delete Route");
			System.out.println("AD-007 : View Route");
			System.out.println("AD-008 : Modify Route");
			System.out.println("AD-009 : Add Driver");
			System.out.println("AD-010 : Delete Driver");
			System.out.println("AD-011 : Modify Driver");
			System.out.println("AD-012 : Allot Driver");
			System.out.println("AD-013 : View Booking Details (Date + Route)");
			System.out.println("0 : Logout");
			System.out.println("\n============================");
			System.out.print("Enter Requirement ID or 0 to exit: ");

			String req = scanner.nextLine().trim();

			switch (req.toUpperCase()) {

				// ---------------- VEHICLE -----------------
				case "AD-001":
					System.out.println("[AD-001] Adding Vehicle");
					VehicleBean v = new VehicleBean();
					System.out.print("VehicleID: "); 
					v.setVehicleID(scanner.nextLine());
					System.out.print("Name: "); 
					v.setName(scanner.nextLine());
					System.out.print("Type: "); 
					v.setType(scanner.nextLine());
					System.out.print("Registration Number: ");
					 v.setRegistrationNumber(scanner.nextLine());
					System.out.print("Seating Capacity: "); 
					v.setSeatingCapacity(Integer.parseInt(scanner.nextLine()));
					System.out.print("Fare Per KM: "); 
					v.setFarePerKM(Double.parseDouble(scanner.nextLine()));
					System.out.println(adminDAO.addVehicle(v));
					break;

				case "AD-002":
					System.out.println("[AD-002] Delete Vehicle");
					ArrayList<String> delV = new ArrayList<>();
					System.out.print("Enter VehicleID: ");
					delV.add(scanner.nextLine());
					System.out.println("Deleted: " + adminDAO.deleteVehicle(delV));
					break;

				case "AD-003":
					System.out.println("[AD-003] View Vehicle");
					System.out.print("Enter VehicleID: ");
					System.out.println(adminDAO.viewVehicle(scanner.nextLine()));
					break;

				case "AD-004":
					System.out.println("[AD-004] Modify Vehicle");
					VehicleBean mv = new VehicleBean();
					System.out.print("VehicleID: "); 
					mv.setVehicleID(scanner.nextLine());
					System.out.print("Name: "); 
					mv.setName(scanner.nextLine());
					System.out.print("Type: "); 
					mv.setType(scanner.nextLine());
					System.out.print("Registration No: "); 
					mv.setRegistrationNumber(scanner.nextLine());
					System.out.print("Seats: "); 
					mv.setSeatingCapacity(Integer.parseInt(scanner.nextLine()));
					System.out.print("Fare/KM: "); 
					mv.setFarePerKM(Double.parseDouble(scanner.nextLine()));
					System.out.println("Updated: " + adminDAO.modifyVehicle(mv));
					break;

				// ---------------- ROUTE -----------------
				case "AD-005":
					System.out.println("[AD-005] Add Route");
					RouteBean r = new RouteBean();
					System.out.print("RouteID: "); 
					r.setRouteID(scanner.nextLine()); 
					System.out.print("Source: "); 
					r.setSource(scanner.nextLine());
					System.out.print("Destination: "); 
					r.setDestination(scanner.nextLine());
					System.out.print("Distance: "); 
					r.setDistance(Integer.parseInt(scanner.nextLine()));
					System.out.print("Travel Duration: ");
					 r.setTravelDuration(Integer.parseInt(scanner.nextLine()));
					System.out.println(adminDAO.addRoute(r));
					break;

				case "AD-006":
					System.out.println("[AD-006] Delete Route");
					ArrayList<String> delR = new ArrayList<>();
					System.out.print("Enter RouteID: ");
					delR.add(scanner.nextLine());
					System.out.println("Deleted: " + adminDAO.deleteRoute(delR));
					break;

				case "AD-007":
					System.out.print("Enter RouteID: ");
					System.out.println(adminDAO.viewRoute(scanner.nextLine()));
					break;

				case "AD-008":
					System.out.println("[AD-008] Modify Route");
					RouteBean mr = new RouteBean();
					System.out.print("RouteID: "); 
					mr.setRouteID(scanner.nextLine());
					System.out.print("Source: "); 
					mr.setSource(scanner.nextLine());
					System.out.print("Destination: ");
					 mr.setDestination(scanner.nextLine());
					System.out.print("Distance: "); 
					mr.setDistance(Integer.parseInt(scanner.nextLine()));
					System.out.print("Duration: "); 
					mr.setTravelDuration(Integer.parseInt(scanner.nextLine()));
					System.out.println("Updated: " + adminDAO.modifyRoute(mr));
					break;

				// ---------------- DRIVER -----------------
				case "AD-009":
					System.out.println("[AD-009] Add Driver");
					DriverBean d = new DriverBean();
					System.out.print("DriverID: "); 
					d.setDriverID(scanner.nextLine());
					System.out.print("Name: "); 
					d.setName(scanner.nextLine());
					System.out.print("Street: "); 
					d.setStreet(scanner.nextLine());
					System.out.print("Location: "); 
					d.setLocation(scanner.nextLine());
					System.out.print("City: "); 
					d.setCity(scanner.nextLine());
					System.out.print("State: "); 
					d.setState(scanner.nextLine());
					System.out.print("Pincode: "); 
					d.setPincode(scanner.nextLine());
					System.out.print("Mobile: "); 
					d.setMobileNo(scanner.nextLine());
					System.out.print("License No: "); 
					d.setLicenseNumber(scanner.nextLine());
					System.out.println(adminDAO.addDriver(d));
					break;

				case "AD-010":
					ArrayList<String> delD = new ArrayList<>();
					System.out.print("Enter DriverID: ");
					delD.add(scanner.nextLine());
					System.out.println("Deleted: " + adminDAO.deleteDriver(delD));
					break;

				case "AD-011":
					System.out.println("[AD-011] Modify Driver");
					DriverBean md = new DriverBean();
					System.out.print("DriverID: "); 
					md.setDriverID(scanner.nextLine());
					System.out.print("Name: "); 
					md.setName(scanner.nextLine());
					System.out.print("Mobile: "); 
					md.setMobileNo(scanner.nextLine());
					System.out.print("License: "); 
					md.setLicenseNumber(scanner.nextLine());
					System.out.println("Updated: " + adminDAO.modifyDriver(md));
					break;

				// ---------------- ALLOT DRIVER -----------------
				case "AD-012":
					System.out.println("[AD-012] Allot Driver");
					System.out.print("ReservationID: ");
					String rs = scanner.nextLine();
					System.out.print("DriverID: ");
					String dr = scanner.nextLine();
					System.out.println("Allotted: " + adminDAO.allotDriver(rs, dr));
					break;

				// ---------------- VIEW BOOKINGS -----------------
				case "AD-013":
					System.out.println("[AD-013] View Bookings");
					System.out.print("JourneyDate: ");
					String jd = scanner.nextLine();
					System.out.print("Source: ");
					String s = scanner.nextLine();
					System.out.print("Destination: ");
					String d2 = scanner.nextLine();
					ArrayList<ReservationBean> list = adminDAO.viewBookingDetails(jd, s, d2);
					list.forEach(System.out::println);
					break;

				case "0":
					System.out.println("Logging out...");
					return;

				default:
					System.out.println("Invalid Requirement ID.");
			}

		}
	}

	// ==========================================================
	// CUSTOMER MENU  (US-001 → US-005)
	// ==========================================================
	private static void customerMenu(Scanner scanner, CustomerDAO customerDAO,CredentialsBean user) {

		while (true) {
			System.out.println("\n====== CUSTOMER REQUIREMENTS ======");
			System.out.println("US-001 : Register Profile");
			System.out.println("US-002 : View Vehicles by Date/Route");
			System.out.println("US-003 : Book Vehicle");
			System.out.println("US-004 : Cancel Booking");
			System.out.println("US-005 : View / Print Ticket");
			System.out.println("0 : Logout");
			System.out.println("\n============================");
			System.out.print("Enter Requirement ID: ");
			String req = scanner.nextLine().trim();

			switch (req.toUpperCase()) {

				case "US-001":
					System.out.println("[US-001] Register Profile");
					System.out.print("Name: ");
					String name = scanner.nextLine();
					System.out.print("Street: ");
					String street = scanner.nextLine();
					System.out.print("Location: ");
					String location = scanner.nextLine();
					System.out.print("City: ");
					String city = scanner.nextLine();
					System.out.print("State: ");
					String state = scanner.nextLine();
					System.out.print("Pincode: ");
					String pincode = scanner.nextLine();
					System.out.print("Mobile: ");
					String mobile = scanner.nextLine();
					System.out.println("Password: ");
					String password = scanner.nextLine();
					System.out.println("Registered: " + customerDAO.registerProfile(user.getUserID(), name, street, location, city, state, pincode, mobile,password, name, name));
					break;

				case "US-002":
					System.out.println("Enter vehicle type:");
					String t = scanner.nextLine();
					customerDAO.viewVehiclesByType(t).forEach(System.out::println);
					break;

				case "US-003":
					ReservationBean rb = new ReservationBean();
					System.out.print("ReservationID: "); 
					rb.setReservationID(scanner.nextLine());
					 rb.setUserID(user.getUserID());
					System.out.print("RouteID: "); 
					rb.setRouteID(scanner.nextLine());
					System.out.print("VehicleID: "); 
					rb.setVehicleID(scanner.nextLine());
					System.out.print("BookingDate: "); 
					rb.setBookingDate(scanner.nextLine());
					System.out.print("JourneyDate: "); 
					rb.setJourneyDate(scanner.nextLine());
					System.out.print("Boarding Point: "); 
					rb.setBoardingPoint(scanner.nextLine());
					System.out.print("Drop Point: "); 
					rb.setDropPoint(scanner.nextLine());
					rb.setBookingStatus("CONFIRMED");
					System.out.println("Booking ID: " + customerDAO.bookVehicle(rb));
					break;

				case "US-004":
					String uid = user.getUserID();
					System.out.print("ReservationID: ");
					String rid = scanner.nextLine();
					System.out.println("Cancelled: " + customerDAO.cancelBooking(uid, rid));
					break;

				case "US-005":
					System.out.print("Enter ReservationID: ");
					String id = scanner.nextLine();
					ReservationBean res = customerDAO.printBookingDetails(id);
					System.out.println(res == null ? "Not found" : res);
					break;

				case "0":
					System.out.println("Logging out...");
					return;

				default:
					System.out.println("Invalid Requirement ID.");
			}
		}
	}
}
