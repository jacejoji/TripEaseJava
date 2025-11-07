package com.ust.ui;

import com.ust.bean.*;
import com.ust.dao.AdministratorDAO;
import com.ust.dao.CustomerDAO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class AdminDashboard extends JFrame {

    private final String currentUserId;
    private final AdministratorDAO adminDAO = new AdministratorDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardHost = new JPanel(cardLayout);

    private final JTextArea status = new JTextArea(3, 80);

    public AdminDashboard(String currentUserId) {
        this.currentUserId = currentUserId;
        setTitle("TripEase - Administrator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(topBar(), BorderLayout.NORTH);
        add(sidebar(), BorderLayout.WEST);
        add(cards(), BorderLayout.CENTER);
        add(statusBar(), BorderLayout.SOUTH);
    }

    private JComponent topBar() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Administrator Console", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        p.add(title, BorderLayout.CENTER);
        p.add(new JLabel(" Logged in: " + currentUserId + "  "), BorderLayout.EAST);
        return p;
    }

    private JComponent sidebar() {
        JPanel bar = new JPanel();
        bar.setLayout(new GridLayout(0,1,6,6));
        bar.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        bar.add(button("Vehicles - Add", () -> cardLayout.show(cardHost, "vehAdd")));
        bar.add(button("Vehicles - View All", () -> {
            refreshVehicleTable();
            cardLayout.show(cardHost, "vehView");
        }));
        bar.add(button("Vehicles - Modify", () -> cardLayout.show(cardHost, "vehMod")));
        bar.add(button("Vehicles - Delete", () -> cardLayout.show(cardHost, "vehDel")));
        bar.add(button("Vehicles - View by ID", () -> cardLayout.show(cardHost, "vehById")));

        bar.add(new JSeparator());
        bar.add(button("Routes - Add", () -> cardLayout.show(cardHost, "routeAdd")));
        bar.add(button("Routes - View All", () -> {
            refreshRouteTable();
            cardLayout.show(cardHost, "routeView");
        }));
        bar.add(button("Routes - Modify", () -> cardLayout.show(cardHost, "routeMod")));
        bar.add(button("Routes - Delete", () -> cardLayout.show(cardHost, "routeDel")));
        bar.add(button("Routes - View by ID", () -> cardLayout.show(cardHost, "routeById")));

        bar.add(new JSeparator());
        bar.add(button("Drivers - Add", () -> cardLayout.show(cardHost, "driverAdd")));
        bar.add(button("Drivers - Modify", () -> cardLayout.show(cardHost, "driverMod")));
        bar.add(button("Drivers - Delete", () -> cardLayout.show(cardHost, "driverDel")));

        bar.add(new JSeparator());
        bar.add(button("Allot Driver → Reservation", () -> cardLayout.show(cardHost, "allot")));

        bar.add(new JSeparator());
        bar.add(button("Bookings by Date/Route", () -> cardLayout.show(cardHost, "bookings")));

        return bar;
    }

    private JButton button(String text, Runnable onClick) {
        JButton b = new JButton(text);
        b.addActionListener(e -> onClick.run());
        return b;
    }

    private JComponent statusBar() {
        status.setEditable(false);
        return new JScrollPane(status);
    }

    private JComponent cards() {
        cardHost.add(vehicleAddPanel(), "vehAdd");
        cardHost.add(vehicleViewPanel(), "vehView");
        cardHost.add(vehicleModifyPanel(), "vehMod");
        cardHost.add(vehicleDeletePanel(), "vehDel");
        cardHost.add(vehicleByIdPanel(), "vehById");

        cardHost.add(routeAddPanel(), "routeAdd");
        cardHost.add(routeViewPanel(), "routeView");
        cardHost.add(routeModifyPanel(), "routeMod");
        cardHost.add(routeDeletePanel(), "routeDel");
        cardHost.add(routeByIdPanel(), "routeById");

        cardHost.add(driverAddPanel(), "driverAdd");
        cardHost.add(driverModifyPanel(), "driverMod");
        cardHost.add(driverDeletePanel(), "driverDel");

        cardHost.add(allotPanel(), "allot");
        cardHost.add(bookingsPanel(), "bookings");

        return cardHost;
    }

    // ---------------------- VEHICLE PANELS ----------------------

    private JTable vehicleTable;
    private JComponent vehicleViewPanel() {
        vehicleTable = new JTable(TableModels.vehicles());
        return wrapTable("All Vehicles", vehicleTable);
    }
    private void refreshVehicleTable() { vehicleTable.setModel(TableModels.vehicles()); }

    private JComponent vehicleAddPanel() {
        JTextField id = new JTextField(12);
        JTextField name = new JTextField(16);
        JTextField number = new JTextField(12);
        JTextField type = new JTextField(10);
        JSpinner seats = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
        JSpinner fareKm = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 1000.0, 0.5));

        JPanel f = FormUtils.form(
                "Vehicle ID", id,
                "Name", name,
                "Registration Number", number,
                "Type (AC/NonAC/SUV/...)", type,
                "Seating Capacity", seats,
                "Fare / KM", fareKm
        );
        JButton save = new JButton("Add Vehicle");
        save.addActionListener(e -> {
            VehicleBean v = new VehicleBean();
            v.setVehicleID(id.getText().trim());
            v.setName(name.getText().trim());
            v.setRegistrationNumber(number.getText().trim());
            v.setType(type.getText().trim());
            v.setSeatingCapacity((Integer)seats.getValue());
            v.setFarePerKM(((Double)fareKm.getValue()));
            String result = adminDAO.addVehicle(v);
            status.setText("addVehicle → " + result);
            refreshVehicleTable();
        });
        return FormUtils.section("Add Vehicle", f, save);
    }

    private JComponent vehicleByIdPanel() {
        JTextField id = new JTextField(12);
        JTextArea result = new JTextArea(8, 50);
        result.setEditable(false);
        JButton find = new JButton("View Vehicle");
        find.addActionListener(e -> {
            VehicleBean v = adminDAO.viewVehicle(id.getText().trim());
            result.setText(v == null ? "Not found" : v.toString());
        });
        JPanel f = FormUtils.form("Vehicle ID", id);
        return FormUtils.section("View Vehicle by ID", f, find, new JScrollPane(result));
    }

    private JComponent vehicleModifyPanel() {
        JTextField id = new JTextField(12);
        JTextField name = new JTextField(16);
        JTextField number = new JTextField(12);
        JTextField type = new JTextField(10);
        JSpinner seats = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
        JSpinner fareKm = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 1000.0, 0.5));

        JButton load = new JButton("Load");
        JButton update = new JButton("Update");

        load.addActionListener(e -> {
            VehicleBean v = adminDAO.viewVehicle(id.getText().trim());
            if (v == null) { status.setText("Vehicle not found"); return; }
            name.setText(v.getName());
            number.setText(v.getRegistrationNumber());
            type.setText(v.getType());
            seats.setValue(v.getSeatingCapacity());
            fareKm.setValue(v.getFarePerKM());
            status.setText("Vehicle loaded");
        });

        update.addActionListener(e -> {
            VehicleBean v = new VehicleBean();
            v.setVehicleID(id.getText().trim());
            v.setName(name.getText().trim());
            v.setRegistrationNumber(number.getText().trim());
            v.setType(type.getText().trim());
            v.setSeatingCapacity((Integer)seats.getValue());
            v.setFarePerKM(((Double)fareKm.getValue()));
            boolean ok = adminDAO.modifyVehicle(v);
            status.setText("modifyVehicle → " + ok);
            refreshVehicleTable();
        });

        JPanel f = FormUtils.form(
                "Vehicle ID", id,
                "Name", name,
                "Registration Number", number,
                "Type", type,
                "Seating Capacity", seats,
                "Fare / KM", fareKm
        );
        return FormUtils.section("Modify Vehicle", f, FormUtils.row(load, update));
    }

    private JComponent vehicleDeletePanel() {
        JTextField ids = new JTextField(40);
        JButton del = new JButton("Delete Vehicles");
        del.addActionListener(e -> {
            ArrayList<String> idList = FormUtils.splitIds(ids.getText());
            int count = adminDAO.deleteVehicle(idList);
            status.setText("deleteVehicle → " + count + " removed");
            refreshVehicleTable();
        });
        JPanel f = FormUtils.form("Vehicle IDs (comma-separated)", ids);
        return FormUtils.section("Delete Vehicles", f, del);
    }

    // ---------------------- ROUTE PANELS ----------------------

    private JTable routeTable;
    private void refreshRouteTable() { routeTable.setModel(TableModels.routes()); }
    private JComponent routeViewPanel() {
        routeTable = new JTable(TableModels.routes());
        return wrapTable("All Routes", routeTable);
    }

    private JComponent routeAddPanel() {
        JTextField id = new JTextField(12);
        JTextField src = new JTextField(12);
        JTextField dst = new JTextField(12);
        JSpinner dist = new JSpinner(new SpinnerNumberModel(10, 1, 100000, 1));
        JSpinner dur = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        JButton save = new JButton("Add Route");
        save.addActionListener(e -> {
            RouteBean rb = new RouteBean();
            rb.setRouteID(id.getText().trim());
            rb.setSource(src.getText().trim());
            rb.setDestination(dst.getText().trim());
            rb.setDistance((Integer)dist.getValue());
            rb.setTravelDuration((Integer)dur.getValue());
            String res = adminDAO.addRoute(rb);
            status.setText("addRoute → " + res);
            refreshRouteTable();
        });

        JPanel f = FormUtils.form(
                "Route ID", id,
                "Source", src,
                "Destination", dst,
                "Distance (KM)", dist,
                "Travel Duration (hrs)", dur
        );
        return FormUtils.section("Add Route", f, save);
    }

    private JComponent routeByIdPanel() {
        JTextField id = new JTextField(12);
        JTextArea result = new JTextArea(8, 50);
        result.setEditable(false);
        JButton find = new JButton("View Route");
        find.addActionListener(e -> {
            RouteBean r = adminDAO.viewRoute(id.getText().trim());
            result.setText(r == null ? "Not found" : r.toString());
        });
        JPanel f = FormUtils.form("Route ID", id);
        return FormUtils.section("View Route by ID", f, find, new JScrollPane(result));
    }

    private JComponent routeModifyPanel() {
        JTextField id = new JTextField(12);
        JTextField src = new JTextField(12);
        JTextField dst = new JTextField(12);
        JSpinner dist = new JSpinner(new SpinnerNumberModel(10, 1, 100000, 1));
        JSpinner dur = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        JButton load = new JButton("Load");
        JButton update = new JButton("Update");

        load.addActionListener(e -> {
            RouteBean r = adminDAO.viewRoute(id.getText().trim());
            if (r == null) { status.setText("Route not found"); return; }
            src.setText(r.getSource());
            dst.setText(r.getDestination());
            dist.setValue(r.getDistance());
            dur.setValue(r.getTravelDuration());
            status.setText("Route loaded");
        });

        update.addActionListener(e -> {
            RouteBean rb = new RouteBean();
            rb.setRouteID(id.getText().trim());
            rb.setSource(src.getText().trim());
            rb.setDestination(dst.getText().trim());
            rb.setDistance((Integer)dist.getValue());
            rb.setTravelDuration((Integer)dur.getValue());
            boolean ok = adminDAO.modifyRoute(rb);
            status.setText("modifyRoute → " + ok);
            refreshRouteTable();
        });

        JPanel f = FormUtils.form(
                "Route ID", id,
                "Source", src,
                "Destination", dst,
                "Distance (KM)", dist,
                "Travel Duration (hrs)", dur
        );
        return FormUtils.section("Modify Route", f, FormUtils.row(load, update));
    }

    private JComponent routeDeletePanel() {
        JTextField ids = new JTextField(40);
        JButton del = new JButton("Delete Routes");
        del.addActionListener(e -> {
            ArrayList<String> idList = FormUtils.splitIds(ids.getText());
            int count = adminDAO.deleteRoute(idList);
            status.setText("deleteRoute → " + count + " removed");
            refreshRouteTable();
        });
        JPanel f = FormUtils.form("Route IDs (comma-separated)", ids);
        return FormUtils.section("Delete Routes", f, del);
    }

    // ---------------------- DRIVER PANELS ----------------------

    private JComponent driverAddPanel() {
        JTextField id = new JTextField(12);
        JTextField name = new JTextField(16);
        JTextField street = new JTextField(16);
        JTextField location = new JTextField(12);
        JTextField city = new JTextField(12);
        JTextField state = new JTextField(12);
        JTextField pincode = new JTextField(8);
        JTextField mobile = new JTextField(12);
        JTextField license = new JTextField(16);

        JButton save = new JButton("Add Driver");
        save.addActionListener(e -> {
            DriverBean d = new DriverBean();
            d.setDriverID(id.getText().trim());
            d.setName(name.getText().trim());
            d.setStreet(street.getText().trim());
            d.setLocation(location.getText().trim());
            d.setCity(city.getText().trim());
            d.setState(state.getText().trim());
            d.setPincode(pincode.getText().trim());
            d.setMobileNo(mobile.getText().trim());
            d.setLicenseNumber(license.getText().trim());
            String res = adminDAO.addDriver(d);
            status.setText("addDriver → " + res);
        });

        JPanel f = FormUtils.form(
                "Driver ID", id,
                "Name", name,
                "Street", street,
                "Location", location,
                "City", city,
                "State", state,
                "Pincode", pincode,
                "Mobile", mobile,
                "License No.", license
        );
        return FormUtils.section("Add Driver", f, save);
    }

    private JComponent driverModifyPanel() {
        JTextField id = new JTextField(12);
        JTextField name = new JTextField(16);
        JTextField mobile = new JTextField(12);
        JTextField license = new JTextField(16);

        JButton load = new JButton("Load");
        JButton update = new JButton("Update");

        load.addActionListener(e -> {
            // We don’t have viewDriver(id) in DAO; simple scan via view in UI is omitted.
            status.setText("Load driver → (optional) add a viewDriver method if needed.");
        });

        update.addActionListener(e -> {
            DriverBean d = new DriverBean();
            d.setDriverID(id.getText().trim());
            d.setName(name.getText().trim());
            d.setMobileNo(mobile.getText().trim());
            d.setLicenseNumber(license.getText().trim());
            boolean ok = adminDAO.modifyDriver(d);
            status.setText("modifyDriver → " + ok);
        });

        JPanel f = FormUtils.form(
                "Driver ID", id,
                "Name", name,
                "Mobile", mobile,
                "License No.", license
        );
        return FormUtils.section("Modify Driver", f, FormUtils.row(load, update));
    }

    private JComponent driverDeletePanel() {
        JTextField ids = new JTextField(40);
        JButton del = new JButton("Delete Drivers");
        del.addActionListener(e -> {
            ArrayList<String> idList = FormUtils.splitIds(ids.getText());
            int count = adminDAO.deleteDriver(idList);
            status.setText("deleteDriver → " + count + " removed");
        });
        JPanel f = FormUtils.form("Driver IDs (comma-separated)", ids);
        return FormUtils.section("Delete Drivers", f, del);
    }

    // ---------------------- ALLOT DRIVER ----------------------

    private JComponent allotPanel() {
        JTextField reservationId = new JTextField(12);
        JTextField driverId = new JTextField(12);
        JButton allot = new JButton("Allot Driver");

        allot.addActionListener(e -> {
            boolean ok = adminDAO.allotDriver(reservationId.getText().trim(), driverId.getText().trim());
            status.setText("allotDriver → " + ok);
        });

        JPanel f = FormUtils.form("Reservation ID", reservationId, "Driver ID", driverId);
        return FormUtils.section("Allot Driver to Reservation", f, allot);
    }

    // ---------------------- VIEW BOOKINGS BY DATE/ROUTE ----------------------

    private JTable bookingsTable;

    private JComponent bookingsPanel() {
        JTextField date = new JTextField(12); // yyyy-MM-dd
        JTextField src = new JTextField(12);
        JTextField dst = new JTextField(12);
        JButton search = new JButton("Search Bookings");
        bookingsTable = new JTable(TableModels.reservations(new ArrayList<>()));

        search.addActionListener(e -> {
            String d = date.getText().trim();
            if (d == null) { status.setText("Invalid date. Use yyyy-MM-dd"); return; }
            ArrayList<ReservationBean> list =
                    adminDAO.viewBookingDetails(d, src.getText().trim(), dst.getText().trim());
            bookingsTable.setModel(TableModels.reservations(list));
            status.setText("Found " + list.size() + " booking(s).");
        });

        JPanel f = FormUtils.form(
                "Journey Date (yyyy-MM-dd)", date,
                "Source", src,
                "Destination", dst
        );
        return FormUtils.section("View Bookings by Date & Route", f, search, new JScrollPane(bookingsTable));
    }
    // ---------------------- UTIL ----------------------

    private JComponent wrapTable(String title, JTable table) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("  " + title), BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }
}
