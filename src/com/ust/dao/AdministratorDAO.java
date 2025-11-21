package com.ust.dao;

import com.ust.bean.*;
import com.ust.util.DBUtil;
import com.ust.util.IdGenerator;

import java.sql.*;
import java.util.ArrayList;

public class AdministratorDAO {

    // ---------------- VEHICLES ----------------
    public String addVehicle(VehicleBean vehicleBean) {
        String sql = "INSERT INTO ATA_TBL_Vehicle (VehicleId, Name, Type, RegistrationNumber, SeatingCapacity, FarePerKM) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
        	
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, IdGenerator.generateVehicleId(con,vehicleBean.getName()));
            ps.setString(2, vehicleBean.getName());
            ps.setString(3, vehicleBean.getType());
            ps.setString(4, vehicleBean.getRegistrationNumber());
            ps.setInt(5, vehicleBean.getSeatingCapacity());
            ps.setDouble(6, vehicleBean.getFarePerKM());
            int inserted = ps.executeUpdate();
            return inserted == 1 ? "Vehicle added successfully" : "FAIL";
        } catch (SQLIntegrityConstraintViolationException dup) {
            // duplicate primary key or constraint
            dup.printStackTrace();
            return "DUPLICATE";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public int deleteVehicle(ArrayList<String> vehicleIDs) {
        String sql = "DELETE FROM ATA_TBL_Vehicle WHERE VehicleId = ?";
        int count = 0;
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (String id : vehicleIDs) {
                ps.setString(1, id);
                count += ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public VehicleBean viewVehicle(String vehicleID) {
        String sql = "SELECT VehicleId, Name, Type, RegistrationNumber, SeatingCapacity, FarePerKM FROM ATA_TBL_Vehicle WHERE VehicleId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicleID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapVehicle(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean modifyVehicle(VehicleBean vehicleBean) {
        String sql = "UPDATE ATA_TBL_Vehicle SET Name = ?, Type = ?, RegistrationNumber = ?, SeatingCapacity = ?, FarePerKM = ? WHERE VehicleId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicleBean.getName());
            ps.setString(2, vehicleBean.getType());
            ps.setString(3, vehicleBean.getRegistrationNumber());
            ps.setInt(4, vehicleBean.getSeatingCapacity());
            ps.setDouble(5, vehicleBean.getFarePerKM());
            ps.setString(6, vehicleBean.getVehicleID());
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- DRIVERS ----------------
    public String addDriver(DriverBean driverBean) {
        String sql = "INSERT INTO ATA_TBL_Driver (DriverId, Name, Street, Location, City, State, Pincode, MobileNo, LicenseNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, IdGenerator.generateDriverId(con,driverBean.getName()));
            ps.setString(2, driverBean.getName());
            ps.setString(3, driverBean.getStreet());
            ps.setString(4, driverBean.getLocation());
            ps.setString(5, driverBean.getCity());
            ps.setString(6, driverBean.getState());
            ps.setString(7, driverBean.getPincode());
            ps.setString(8, driverBean.getMobileNo());
            ps.setString(9, driverBean.getLicenseNumber());
            int inserted = ps.executeUpdate();
            return inserted == 1 ? "Driver added successfully" : "FAIL";
        } catch (SQLIntegrityConstraintViolationException dup) {
            dup.printStackTrace();
            return "DUPLICATE";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public int deleteDriver(ArrayList<String> driverIDs) {
        String sql = "DELETE FROM ATA_TBL_Driver WHERE DriverId = ?";
        int count = 0;
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (String id : driverIDs) {
                ps.setString(1, id);
                count += ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean modifyDriver(DriverBean driverBean) {
        String sql = "UPDATE ATA_TBL_Driver SET Name = ?, Street = ?, Location = ?, City = ?, State = ?, Pincode = ?, MobileNo = ?, LicenseNumber = ? WHERE DriverId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, driverBean.getName());
            ps.setString(2, driverBean.getStreet());
            ps.setString(3, driverBean.getLocation());
            ps.setString(4, driverBean.getCity());
            ps.setString(5, driverBean.getState());
            ps.setString(6, driverBean.getPincode());
            ps.setString(7, driverBean.getMobileNo());
            ps.setString(8, driverBean.getLicenseNumber());
            ps.setString(9, driverBean.getDriverID());
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public DriverBean viewDriver(String driverID) {
        String sql = "SELECT DriverId, Name, Street, Location, City, State, Pincode, MobileNo, LicenseNumber FROM ATA_TBL_Driver WHERE DriverId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, driverID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapDriver(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- ALLOT DRIVER ----------------
    public boolean allotDriver(String reservationID, String driverID) {
        String sql = "UPDATE ATA_TBL_Reservation SET DriverId = ? WHERE ReservationId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, driverID);
            ps.setString(2, reservationID);
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- ROUTES ----------------
    public String addRoute(RouteBean routeBean) {
        String sql = "INSERT INTO ATA_TBL_Route (RouteId, Source, Destination, Distance, TravelDuration) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, IdGenerator.generateRouteId(con,routeBean.getSource(),routeBean.getDestination()));
            ps.setString(2, routeBean.getSource());
            ps.setString(3, routeBean.getDestination());
            ps.setInt(4, routeBean.getDistance());
            ps.setInt(5, routeBean.getTravelDuration());
            int inserted = ps.executeUpdate();
            return inserted == 1 ? "Route added successfully" : "FAIL";
        } catch (SQLIntegrityConstraintViolationException dup) {
            dup.printStackTrace();
            return "DUPLICATE";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public int deleteRoute(ArrayList<String> routeIDs) {
        String sql = "DELETE FROM ATA_TBL_Route WHERE RouteId = ?";
        int count = 0;
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (String id : routeIDs) {
                ps.setString(1, id);
                count += ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public RouteBean viewRoute(String routeID) {
        String sql = "SELECT RouteId, Source, Destination, Distance, TravelDuration FROM ATA_TBL_Route WHERE RouteId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, routeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRoute(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean modifyRoute(RouteBean routeBean) {
        String sql = "UPDATE ATA_TBL_Route SET Source = ?, Destination = ?, Distance = ?, TravelDuration = ? WHERE RouteId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, routeBean.getSource());
            ps.setString(2, routeBean.getDestination());
            ps.setInt(3, routeBean.getDistance());
            ps.setInt(4, routeBean.getTravelDuration());
            ps.setString(5, routeBean.getRouteID());
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- VIEW BOOKINGS ----------------
    public ArrayList<ReservationBean> viewBookingDetails(String journeyDate, String source, String destination) {
        ArrayList<ReservationBean> list = new ArrayList<>();
        String sql = "SELECT r.ReservationId, r.UserId, r.VehicleId, r.RouteId, r.BookingDate, r.JourneyDate, r.DriverId, r.BookingStatus, r.TotalFare, r.BoardingPoint, r.DropPoint "
                + "FROM ATA_TBL_Reservation r JOIN ATA_TBL_Route ro ON r.RouteId = ro.RouteId "
                + "WHERE r.JourneyDate = ? AND ro.Source = ? AND ro.Destination = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(journeyDate));
            ps.setString(2, source);
            ps.setString(3, destination);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReservation(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<ReservationBean> getAllBookings() {
        ArrayList<ReservationBean> list = new ArrayList<>();
        String sql = "SELECT ReservationId, UserId, VehicleId, RouteId, BookingDate, JourneyDate, DriverId, BookingStatus, TotalFare, BoardingPoint, DropPoint FROM ATA_TBL_Reservation";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapReservation(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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

    private DriverBean mapDriver(ResultSet rs) throws SQLException {
        DriverBean d = new DriverBean();
        d.setDriverID(rs.getString("DriverId"));
        d.setName(rs.getString("Name"));
        d.setStreet(rs.getString("Street"));
        d.setLocation(rs.getString("Location"));
        d.setCity(rs.getString("City"));
        d.setState(rs.getString("State"));
        d.setPincode(rs.getString("Pincode"));
        d.setMobileNo(rs.getString("MobileNo"));
        d.setLicenseNumber(rs.getString("LicenseNumber"));
        return d;
    }

    private RouteBean mapRoute(ResultSet rs) throws SQLException {
        RouteBean r = new RouteBean();
        r.setRouteID(rs.getString("RouteId"));
        r.setSource(rs.getString("Source"));
        r.setDestination(rs.getString("Destination"));
        r.setDistance(rs.getInt("Distance"));
        r.setTravelDuration(rs.getInt("TravelDuration"));
        return r;
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
