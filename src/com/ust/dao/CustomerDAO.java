package com.ust.dao;

import com.ust.bean.*;
import com.ust.util.DBUtil;
import com.ust.util.IdGenerator;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

    // ---------------- VIEW VEHICLES BY TYPE ----------------
    public ArrayList<VehicleBean> viewVehiclesByType(String vehicleType) {
        ArrayList<VehicleBean> result = new ArrayList<>();
        String sql = "SELECT VehicleId, Name, Type, RegistrationNumber, SeatingCapacity, FarePerKM FROM ATA_TBL_Vehicle WHERE Type = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicleType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VehicleBean v = mapVehicle(rs);
                    result.add(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---------------- VIEW ALL VEHICLES ----------------
    public ArrayList<VehicleBean> viewVehicles() {
        ArrayList<VehicleBean> result = new ArrayList<>();
        String sql = "SELECT VehicleId, Name, Type, RegistrationNumber, SeatingCapacity, FarePerKM FROM ATA_TBL_Vehicle";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapVehicle(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---------------- VIEW VEHICLES BY SEATS ----------------
    public ArrayList<VehicleBean> viewVehicleBySeats(int noOfSeats) {
        ArrayList<VehicleBean> result = new ArrayList<>();
        String sql = "SELECT VehicleId, Name, Type, RegistrationNumber, SeatingCapacity, FarePerKM FROM ATA_TBL_Vehicle WHERE SeatingCapacity = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, noOfSeats);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapVehicle(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---------------- VIEW ALL ROUTES ----------------
    public ArrayList<RouteBean> viewAllRoutes() {
        ArrayList<RouteBean> result = new ArrayList<>();
        String sql = "SELECT RouteId, Source, Destination, Distance, TravelDuration FROM ATA_TBL_Route";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RouteBean r = new RouteBean();
                r.setRouteID(rs.getString("RouteId"));
                r.setSource(rs.getString("Source"));
                r.setDestination(rs.getString("Destination"));
                r.setDistance(rs.getInt("Distance"));
                r.setTravelDuration(rs.getInt("TravelDuration"));
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---------------- BOOK VEHICLE ----------------
    /**
     * Inserts reservation into DB. Computes TotalFare as (route.distance * vehicle.farePerKM)
     * if possible. Expects reservationBean.bookingDate and journeyDate as yyyy-MM-dd strings.
     * Returns reservation ID on success, otherwise returns "FAIL".
     */
    public String bookVehicle(ReservationBean reservationBean) {
        // Compute fare by querying route and vehicle
        double totalFare = 0.0;
        boolean fareComputed = false;

        try (Connection con = DBUtil.getConnection()) {
            // Get route distance
            if (reservationBean.getRouteID() != null && !reservationBean.getRouteID().trim().isEmpty()) {
                String rSql = "SELECT Distance FROM ATA_TBL_Route WHERE RouteId = ?";
                try (PreparedStatement rps = con.prepareStatement(rSql)) {
                    rps.setString(1, reservationBean.getRouteID());
                    try (ResultSet rs = rps.executeQuery()) {
                        if (rs.next()) {
                            int distance = rs.getInt("Distance");
                            // get vehicle fare/km
                            if (reservationBean.getVehicleID() != null && !reservationBean.getVehicleID().trim().isEmpty()) {
                                String vSql = "SELECT FarePerKM FROM ATA_TBL_Vehicle WHERE VehicleId = ?";
                                try (PreparedStatement vps = con.prepareStatement(vSql)) {
                                    vps.setString(1, reservationBean.getVehicleID());
                                    try (ResultSet vrs = vps.executeQuery()) {
                                        if (vrs.next()) {
                                            double farePerKm = vrs.getDouble("FarePerKM");
                                            totalFare = distance * farePerKm;
                                            fareComputed = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            // If fare not computed, you can set a default or 0. Here we set 0 if unknown.
            if (!fareComputed) totalFare = reservationBean.getTotalFare(); // keep if UI provided

            // Insert reservation
            String insertSql = "INSERT INTO ATA_TBL_Reservation "
                    + "(ReservationId, UserId, VehicleId, RouteId, BookingDate, JourneyDate, DriverId, BookingStatus, TotalFare, BoardingPoint, DropPoint) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ips = con.prepareStatement(insertSql)) {
                ips.setString(1, IdGenerator.generateReservationId(con));
                ips.setString(2, reservationBean.getUserID());
                ips.setString(3, reservationBean.getVehicleID());
                ips.setString(4, reservationBean.getRouteID());

                // bookingDate & journeyDate -> java.sql.Date
                if (reservationBean.getBookingDate() != null && !reservationBean.getBookingDate().trim().isEmpty()) {
                    ips.setDate(5, Date.valueOf(reservationBean.getBookingDate()));
                } else {
                    ips.setNull(5, Types.DATE);
                }
                if (reservationBean.getJourneyDate() != null && !reservationBean.getJourneyDate().trim().isEmpty()) {
                    ips.setDate(6, Date.valueOf(reservationBean.getJourneyDate()));
                } else {
                    ips.setNull(6, Types.DATE);
                }

                ips.setString(7, reservationBean.getDriverID());
                ips.setString(8, reservationBean.getBookingStatus() != null ? reservationBean.getBookingStatus() : "CONFIRMED");
                ips.setDouble(9, totalFare);
                ips.setString(10, reservationBean.getBoardingPoint());
                ips.setString(11, reservationBean.getDropPoint());

                int inserted = ips.executeUpdate();
                if (inserted == 1) {
                    return reservationBean.getReservationID();
                } else {
                    return "FAIL";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    // ---------------- CANCEL BOOKING ----------------
    /**
     * Marks the booking as Canceled if reservation belongs to userId.
     */
    public boolean cancelBooking(String userID, String reservationID) {
        String sql = "UPDATE ATA_TBL_Reservation SET BookingStatus = ? WHERE ReservationId = ? AND UserId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "Canceled");
            ps.setString(2, reservationID);
            ps.setString(3, userID);
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- VIEW BOOKING DETAILS ----------------
    public ReservationBean viewBookingDetails(String reservationID) {
        String sql = "SELECT ReservationId, UserId, VehicleId, RouteId, BookingDate, JourneyDate, DriverId, BookingStatus, TotalFare, BoardingPoint, DropPoint "
                + "FROM ATA_TBL_Reservation WHERE ReservationId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationBean r = mapReservation(rs);
                    return r;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- PRINT BOOKING DETAILS ----------------
    public ReservationBean printBookingDetails(String reservationID) {
        // For DB-backed implementation it's same as viewBookingDetails
        return viewBookingDetails(reservationID);
    }

    // ---------------- REGISTER PROFILE ----------------
    /**
     * Inserts a profile and credentials record.
     * - userID: must be unique and match ATA_TBL_User_Credentials.Userid
     * - dateOfBirth: expected yyyy-MM-dd (converted to DATE)
     * Returns the created ProfileBean on success, null on failure.
     */
    public static ProfileBean registerProfile(ProfileBean pb) {
        if (pb == null) return null;

        // DOB is NOT NULL in your schema; validate
        if (pb.getDateOfBirth() == null || pb.getDateOfBirth().trim().isEmpty()) {
            System.err.println("registerProfile: dateOfBirth is required by schema.");
            return null;
        }

        String userID = null;

        String insertCredSql =
                "INSERT INTO ATA_TBL_User_Credentials (Userid, Password, Usertype, Loginstatus) VALUES (?, ?, ?, ?)";

        String insertProfileSql =
                "INSERT INTO ATA_TBL_User_Profile "
                + "(UserId, Firstname, Lastname, Dateofbirth, Gender, Street, Location, City, State, Pincode, MobileNo, EmailId) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false); // Start transaction

            // generate user id using your IdGenerator (uses the same connection if required)
            userID = IdGenerator.generateUserId(con, pb.getFirstName());
            pb.setUserID(userID);

            // 1) INSERT credentials FIRST (because profile.UserId FK -> credentials.Userid)
            try (PreparedStatement psCred = con.prepareStatement(insertCredSql)) {
                psCred.setString(1, userID);
                psCred.setString(2, pb.getPassword());
                psCred.setString(3, "C"); // Customer
                psCred.setInt(4, 0);      // loginstatus = 0

                int credInserted = psCred.executeUpdate();
                if (credInserted != 1) {
                    con.rollback();
                    System.err.println("registerProfile: failed to insert credentials");
                    return null;
                }
            }

            // 2) INSERT profile
            try (PreparedStatement psProfile = con.prepareStatement(insertProfileSql)) {
                psProfile.setString(1, userID);
                psProfile.setString(2, pb.getFirstName());
                psProfile.setString(3, pb.getLastName());

                // Dateofbirth is required and validated above; convert to java.sql.Date
                psProfile.setDate(4, java.sql.Date.valueOf(pb.getDateOfBirth()));

                psProfile.setString(5, pb.getGender());
                psProfile.setString(6, pb.getStreet());
                psProfile.setString(7, pb.getLocation());
                psProfile.setString(8, pb.getCity());
                psProfile.setString(9, pb.getState());
                psProfile.setString(10, pb.getPincode());
                psProfile.setString(11, pb.getMobileNo());
                psProfile.setString(12, pb.getEmailID());

                int profileInserted = psProfile.executeUpdate();
                if (profileInserted != 1) {
                    con.rollback();
                    System.err.println("registerProfile: failed to insert profile");
                    return null;
                }
            }

            con.commit(); // success
            return pb;

        } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
            // duplicate userID or other constraint failure
            dup.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ---------------- HELPERS ----------------
    private VehicleBean mapVehicle(ResultSet rs) throws SQLException {
        VehicleBean v = new VehicleBean();
        v.setVehicleID(rs.getString("VehicleId"));
        v.setName(rs.getString("Name"));
        v.setType(rs.getString("Type"));
        v.setRegistrationNumber(rs.getString("RegistrationNumber"));
        v.setSeatingCapacity(rs.getInt("SeatingCapacity"));
        v.setFarePerKM(rs.getDouble("FarePerKM"));
        return v;
    }

    private ReservationBean mapReservation(ResultSet rs) throws SQLException {
        ReservationBean r = new ReservationBean();
        r.setReservationID(rs.getString("ReservationId"));
        r.setUserID(rs.getString("UserId"));
        r.setVehicleID(rs.getString("VehicleId"));
        r.setRouteID(rs.getString("RouteId"));

        Date bookingDate = rs.getDate("BookingDate");
        if (bookingDate != null) r.setBookingDate(bookingDate.toString());

        Date journeyDate = rs.getDate("JourneyDate");
        if (journeyDate != null) r.setJourneyDate(journeyDate.toString());

        r.setDriverID(rs.getString("DriverId"));
        r.setBookingStatus(rs.getString("BookingStatus"));
        r.setTotalFare(rs.getDouble("TotalFare"));
        r.setBoardingPoint(rs.getString("BoardingPoint"));
        r.setDropPoint(rs.getString("DropPoint"));
        return r;
    }
}
