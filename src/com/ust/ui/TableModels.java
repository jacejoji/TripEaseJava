package com.ust.ui;

import com.ust.bean.*;
import com.ust.dao.AdministratorDAO;
import com.ust.dao.CustomerDAO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public final class TableModels {
    private TableModels(){}

    // ---------- Vehicles ----------
    // no-arg: fetch live from DB via CustomerDAO
    public static AbstractTableModel vehicles() {
        ArrayList<VehicleBean> data = new CustomerDAO().viewVehicles();
        return vehicles(data);
    }

    // existing overload that uses provided list
    public static AbstractTableModel vehicles(ArrayList<VehicleBean> data) {
        String[] cols = {"ID","Name","Type","Reg No","Seats","Fare/KM"};
        return new AbstractTableModel() {
            public int getRowCount(){ return data.size(); }
            public int getColumnCount(){ return cols.length; }
            public String getColumnName(int c){ return cols[c]; }
            public Object getValueAt(int r,int c){
                VehicleBean v = data.get(r);
                if (v == null) return "";
                return switch(c){
                    case 0 -> v.getVehicleID();
                    case 1 -> v.getName();
                    case 2 -> v.getType();
                    case 3 -> v.getRegistrationNumber();
                    case 4 -> v.getSeatingCapacity();
                    case 5 -> v.getFarePerKM();
                    default -> "";
                };
            }
        };
    }

    // ---------- Routes ----------
    public static AbstractTableModel routes() {
        ArrayList<RouteBean> data = new CustomerDAO().viewAllRoutes();
        return routes(data);
    }

    public static AbstractTableModel routes(ArrayList<RouteBean> data) {
        String[] cols = {"RouteID","Source","Destination","Distance","Duration"};
        return new AbstractTableModel() {
            public int getRowCount(){ return data.size(); }
            public int getColumnCount(){ return cols.length; }
            public String getColumnName(int c){ return cols[c]; }
            public Object getValueAt(int r,int c){
                RouteBean o = data.get(r);
                if (o == null) return "";
                return switch(c){
                    case 0 -> o.getRouteID();
                    case 1 -> o.getSource();
                    case 2 -> o.getDestination();
                    case 3 -> o.getDistance();
                    case 4 -> o.getTravelDuration();
                    default -> "";
                };
            }
        };
    }

    // ---------- Reservations ----------
    // no-arg: fetch all bookings (admin view) from AdministratorDAO
    public static AbstractTableModel reservations() {
        ArrayList<ReservationBean> data = new AdministratorDAO().getAllBookings();
        return reservations(data);
    }

    public static AbstractTableModel reservations(ArrayList<ReservationBean> data) {
        String[] cols = {"ResID","UserID","VehicleID","RouteID","BookDate","JourneyDate","DriverID","Status","Boarding","Drop","TotalFare"};
        return new AbstractTableModel() {
            public int getRowCount(){ return data.size(); }
            public int getColumnCount(){ return cols.length; }
            public String getColumnName(int c){ return cols[c]; }
            public Object getValueAt(int r,int c){
                ReservationBean x = data.get(r);
                if (x == null) return "";
                return switch(c){
                    case 0 -> x.getReservationID();
                    case 1 -> x.getUserID();
                    case 2 -> x.getVehicleID();
                    case 3 -> x.getRouteID();
                    case 4 -> x.getBookingDate();
                    case 5 -> x.getJourneyDate();
                    case 6 -> x.getDriverID();
                    case 7 -> x.getBookingStatus();
                    case 8 -> x.getBoardingPoint();
                    case 9 -> x.getDropPoint();
                    case 10 -> x.getTotalFare();
                    default -> "";
                };
            }
        };
    }
}
