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

		while (true) {

			CredentialsBean user = null;
			int attempts = 0;

			System.out.println("====================================");
			System.out.println("!!! Welcome to TripEase CLI !!!");
			System.out.println("Automation of Travel Agency (ATA)");
			System.out.println("====================================");

			// ------------------ LOGIN -------------------
			while (attempts < 3) {
				System.out.print("Enter UserID: ");
				String userID = scanner.nextLine().trim();

				System.out.print("Enter Password: ");
				String password = scanner.nextLine().trim();

				user = tripEaseDAO.login(userID, password);
				if (user == null) {
					attempts++;
					System.out.println("Invalid credentials. Attempts left: " + (3 - attempts));
				} else {
					break;
				}
			}

			if (user == null) {
				System.out.println("Maximum attempts reached. Program exiting...");
				break;
			}

			System.out.println("Login Successful!");
			System.out.println("Logged in as: " + (user.getUserType().equalsIgnoreCase("A") ? "Administrator" : "Customer"));

			// ------------------ ROLE BASED MENU -------------------
			if (user.getUserType().equalsIgnoreCase("Administrator")) {
				adminMenu(scanner, adminDAO, customerDAO, user);
			} else {
				// PASS tripEaseDAO so US-001 can call register(...)
				customerMenu(scanner, customerDAO, tripEaseDAO, user);
			}

			// After logout, loop back to login
		}

		scanner.close();
	}

	// =============================================================================================
	// ADMIN MENU (AD-001 → AD-013)
	// =============================================================================================
	private static void adminMenu(Scanner scanner, AdministratorDAO adminDAO, CustomerDAO customerDAO, CredentialsBean user) {

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
			System.out.println("AD-013 : View Booking Details (Date+Route)");
			System.out.println("0 : Logout");
			System.out.println("====================================");
			System.out.print("Enter Requirement ID: ");

			String req = scanner.nextLine().trim().toUpperCase();

			switch (req) {

				// ---------------- VEHICLE -----------------
				case "AD-001":
					System.out.println("[AD-001] Add Vehicle");
					VehicleBean v = new VehicleBean();
					System.out.print("VehicleID: "); v.setVehicleID(scanner.nextLine());
					System.out.print("Name: "); v.setName(scanner.nextLine());
					System.out.print("Type: "); v.setType(scanner.nextLine());
					System.out.print("Registration No: "); v.setRegistrationNumber(scanner.nextLine());
					System.out.print("Seating Capacity: "); v.setSeatingCapacity(Integer.parseInt(scanner.nextLine()));
					System.out.print("Fare per KM: "); v.setFarePerKM(Double.parseDouble(scanner.nextLine()));
					System.out.println(adminDAO.addVehicle(v));
					break;

				case "AD-002":
					System.out.println("[AD-002] Delete Vehicle");
					ArrayList<String> dv = new ArrayList<>();
					System.out.print("VehicleID: "); dv.add(scanner.nextLine());
					System.out.println("Deleted: " + adminDAO.deleteVehicle(dv));
					break;

				case "AD-003":
					System.out.println("[AD-003] View Vehicle");
					System.out.print("VehicleID: ");
					System.out.println(adminDAO.viewVehicle(scanner.nextLine()));
					break;

				case "AD-004":
					System.out.println("[AD-004] Modify Vehicle");
					VehicleBean mv = new VehicleBean();
					System.out.print("VehicleID: ");
					mv.setVehicleID(scanner.nextLine());
					System.out.print("Name: "); mv.setName(scanner.nextLine());
					System.out.print("Type: "); mv.setType(scanner.nextLine());
					System.out.print("Registration No: "); mv.setRegistrationNumber(scanner.nextLine());
					System.out.print("Seats: "); mv.setSeatingCapacity(Integer.parseInt(scanner.nextLine()));
					System.out.print("Fare/KM: "); mv.setFarePerKM(Double.parseDouble(scanner.nextLine()));
					System.out.println("Updated: " + adminDAO.modifyVehicle(mv));
					break;

				// ---------------- ROUTE -----------------
				case "AD-005":
					System.out.println("[AD-005] Add Route");
					RouteBean r = new RouteBean();
					System.out.print("RouteID: "); r.setRouteID(scanner.nextLine());
					System.out.print("Source: "); r.setSource(scanner.nextLine());
					System.out.print("Destination: "); r.setDestination(scanner.nextLine());
					System.out.print("Distance: "); r.setDistance(Integer.parseInt(scanner.nextLine()));
					System.out.print("Duration: "); r.setTravelDuration(Integer.parseInt(scanner.nextLine()));
					System.out.println(adminDAO.addRoute(r));
					break;

				case "AD-006":
					System.out.println("[AD-006] Delete Route");
					ArrayList<String> drt = new ArrayList<>();
					System.out.print("RouteID: "); drt.add(scanner.nextLine());
					System.out.println("Deleted: " + adminDAO.deleteRoute(drt));
					break;

				case "AD-007":
					System.out.print("RouteID: ");
					System.out.println(adminDAO.viewRoute(scanner.nextLine()));
					break;

				case "AD-008":
					System.out.println("[AD-008] Modify Route");
					RouteBean mr = new RouteBean();
					System.out.print("RouteID: "); mr.setRouteID(scanner.nextLine());
					System.out.print("Source: "); mr.setSource(scanner.nextLine());
					System.out.print("Destination: "); mr.setDestination(scanner.nextLine());
					System.out.print("Distance: "); mr.setDistance(Integer.parseInt(scanner.nextLine()));
					System.out.print("Duration: "); mr.setTravelDuration(Integer.parseInt(scanner.nextLine()));
					System.out.println("Updated: " + adminDAO.modifyRoute(mr));
					break;

				// ---------------- DRIVER -----------------
				case "AD-009":
					System.out.println("[AD-009] Add Driver");
					DriverBean d = new DriverBean();
					System.out.print("DriverID: "); d.setDriverID(scanner.nextLine());
					System.out.print("Name: "); d.setName(scanner.nextLine());
					System.out.print("Street: "); d.setStreet(scanner.nextLine());
					System.out.print("Location: "); d.setLocation(scanner.nextLine());
					System.out.print("City: "); d.setCity(scanner.nextLine());
					System.out.print("State: "); d.setState(scanner.nextLine());
					System.out.print("Pincode: "); d.setPincode(scanner.nextLine());
					System.out.print("Mobile: "); d.setMobileNo(scanner.nextLine());
					System.out.print("License No: "); d.setLicenseNumber(scanner.nextLine());
					System.out.println(adminDAO.addDriver(d));
					break;

				case "AD-010":
					System.out.println("[AD-010] Delete Driver");
					ArrayList<String> dd = new ArrayList<>();
					System.out.print("DriverID: "); dd.add(scanner.nextLine());
					System.out.println("Deleted: " + adminDAO.deleteDriver(dd));
					break;

				case "AD-011":
					System.out.println("[AD-011] Modify Driver");
					DriverBean md = new DriverBean();
					System.out.print("DriverID: "); md.setDriverID(scanner.nextLine());
					System.out.print("Name: "); md.setName(scanner.nextLine());
					System.out.print("Street: "); md.setStreet(scanner.nextLine());
					System.out.print("Location: "); md.setLocation(scanner.nextLine());
					System.out.print("City: "); md.setCity(scanner.nextLine());
					System.out.print("State: "); md.setState(scanner.nextLine());
					System.out.print("Pincode: "); md.setPincode(scanner.nextLine());
					System.out.print("Mobile: "); md.setMobileNo(scanner.nextLine());
					System.out.print("License: "); md.setLicenseNumber(scanner.nextLine());
					System.out.println("Updated: " + adminDAO.modifyDriver(md));
					break;

				// ---------------- ALLOT DRIVER -----------------
				case "AD-012":
					System.out.println("[AD-012] Allot Driver");
					System.out.print("ReservationID: ");
					String rs = scanner.nextLine();
					System.out.print("DriverID: ");
					String dr = scanner.nextLine();
					System.out.println("Success: " + adminDAO.allotDriver(rs, dr));
					break;

				// ---------------- VIEW BOOKINGS -----------------
				case "AD-013":
					System.out.println("[AD-013] View Bookings");
					System.out.print("JourneyDate: "); String jd = scanner.nextLine();
					System.out.print("Source: "); String so = scanner.nextLine();
					System.out.print("Destination: "); String de = scanner.nextLine();
					adminDAO.viewBookingDetails(jd, so, de).forEach(System.out::println);
					break;

				case "0":
					System.out.println("Logging out...");
					return;

				default:
					System.out.println("Invalid Requirement ID.");
			}
		}
	}

	// =============================================================================================
	// CUSTOMER MENU (US-001 → US-005)
	// =============================================================================================
	private static void customerMenu(Scanner scanner, CustomerDAO customerDAO, TripEaseDAO tripEaseDAO, CredentialsBean user) {

		while (true) {

			System.out.println("\n====== CUSTOMER REQUIREMENTS ======");
			System.out.println("US-001 : Register Profile");
			System.out.println("US-002 : View Vehicles by Type");
			System.out.println("US-003 : Book Vehicle");
			System.out.println("US-004 : Cancel Booking");
			System.out.println("US-005 : View / Print Ticket");
			System.out.println("0 : Logout");
			System.out.println("====================================");
			System.out.print("Enter Requirement ID: ");

			String req = scanner.nextLine().trim().toUpperCase();

			switch (req) {

				case "US-001":
					System.out.println("[US-001] Register Profile");
					ProfileBean p = new ProfileBean();

					// Your DAO register(...) will auto-generate userID,
					// but we also allow setting it to the logged-in user's ID if desired.
					// If your DAO ignores p.userID and generates a fresh one, that's fine.
					// If you want to force use of current user, uncomment next line.
					// p.setUserID(user.getUserID());

					// FIRST NAME must be >= 2 characters (DAO uses substring(0,2))
					String fName;
					while (true) {
						System.out.print("First Name (>=2 chars): ");
						fName = scanner.nextLine();
						if (fName != null && fName.trim().length() >= 2) break;
						System.out.println("First name must be at least 2 characters.");
					}
					p.setFirstName(fName);

					System.out.print("Last Name: "); p.setLastName(scanner.nextLine());
					System.out.print("Date of Birth: "); p.setDateOfBirth(scanner.nextLine());
					System.out.print("Gender: "); p.setGender(scanner.nextLine());
					System.out.print("Street: "); p.setStreet(scanner.nextLine());
					System.out.print("Location: "); p.setLocation(scanner.nextLine());
					System.out.print("City: "); p.setCity(scanner.nextLine());
					System.out.print("State: "); p.setState(scanner.nextLine());
					System.out.print("Pincode: "); p.setPincode(scanner.nextLine());
					System.out.print("Mobile: "); p.setMobileNo(scanner.nextLine());
					System.out.print("Email: "); p.setEmailID(scanner.nextLine());

					// IMPORTANT: DAO expects profile.getPassword()
					String pwd;
					while (true) {
						System.out.print("Password: ");
						pwd = scanner.nextLine();
						if (pwd != null && !pwd.trim().isEmpty()) break;
						System.out.println("Password cannot be empty.");
					}
					p.setPassword(pwd);

					String newUserId = tripEaseDAO.register(p);
					System.out.println("Registered. Generated UserID: " + newUserId);
					break;

				case "US-002":
					System.out.print("Vehicle Type: ");
					customerDAO.viewVehiclesByType(scanner.nextLine()).forEach(System.out::println);
					break;

				case "US-003":
					System.out.println("[US-003] Book Vehicle");
					ReservationBean rb = new ReservationBean();

					System.out.print("ReservationID: "); rb.setReservationID(scanner.nextLine());
					rb.setUserID(user.getUserID());
					System.out.print("RouteID: "); rb.setRouteID(scanner.nextLine());
					System.out.print("VehicleID: "); rb.setVehicleID(scanner.nextLine());
					System.out.print("BookingDate: "); rb.setBookingDate(scanner.nextLine());
					System.out.print("JourneyDate: "); rb.setJourneyDate(scanner.nextLine());
					System.out.print("Boarding Point: "); rb.setBoardingPoint(scanner.nextLine());
					System.out.print("Drop Point: "); rb.setDropPoint(scanner.nextLine());
					rb.setBookingStatus("CONFIRMED");

					System.out.println("Booking ID: " + customerDAO.bookVehicle(rb));
					break;

				case "US-004":
					System.out.print("ReservationID: ");
					System.out.println("Cancel Status: " +
							customerDAO.cancelBooking(user.getUserID(), scanner.nextLine()));
					break;

				case "US-005":
					System.out.print("ReservationID: ");
					ReservationBean result = customerDAO.printBookingDetails(scanner.nextLine());
					System.out.println(result == null ? "Not found." : result);
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
