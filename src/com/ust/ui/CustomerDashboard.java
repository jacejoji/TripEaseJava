package com.ust.ui;

import com.ust.bean.*;
import com.ust.dao.CustomerDAO;
import com.ust.dao.TripEaseDAO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class CustomerDashboard extends JFrame {

    private final String currentUserId;
    private final CustomerDAO customerDAO = new CustomerDAO();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardHost = new JPanel(cardLayout);
    private final JTextArea status = new JTextArea(3, 80);

    public CustomerDashboard(String currentUserId) {
        this.currentUserId = currentUserId;
        setTitle("TripEase - Customer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(topBar(), BorderLayout.NORTH);
        JPanel left = new JPanel(new BorderLayout());
        left.add(reqDropdown(), BorderLayout.NORTH);
        left.add(sidebar(), BorderLayout.CENTER);
        add(left, BorderLayout.WEST);
        add(cards(), BorderLayout.CENTER);
        add(statusBar(), BorderLayout.SOUTH);
    }

    private JComponent topBar() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Customer Console", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new Launcher().show());
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(new JLabel("Logged in: " + currentUserId + "  "));
        right.add(logoutBtn);

        p.add(title, BorderLayout.CENTER);
        p.add(right, BorderLayout.EAST);
        return p;
    }


    private JComponent sidebar() {
        JPanel bar = new JPanel(new GridLayout(0,1,6,6));
        bar.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        bar.add(button("Register", () -> cardLayout.show(cardHost, "register")));
        bar.add(button("View Routes", () -> {
            refreshRoutes();
            cardLayout.show(cardHost, "routes");
        }));
        bar.add(button("View Vehicles by Type", () -> cardLayout.show(cardHost, "vehType")));
        bar.add(button("View Vehicles by Seats", () -> cardLayout.show(cardHost, "vehSeats")));
        bar.add(button("Book Vehicle", () -> cardLayout.show(cardHost, "book")));
        bar.add(button("Cancel Booking", () -> cardLayout.show(cardHost, "cancel")));
        bar.add(button("View Booking", () -> cardLayout.show(cardHost, "viewBook")));
        bar.add(button("Print Booking", () -> cardLayout.show(cardHost, "printBook")));
        return bar;
    }

    private JButton button(String text, Runnable onClick) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(180, 35));
        b.setMaximumSize(new Dimension(180, 35));
        b.setFont(new Font("SansSerif", Font.PLAIN, 14));
        b.addActionListener(e -> onClick.run());
        return b;
    }


    private JComponent statusBar() {
        status.setEditable(false);
        return new JScrollPane(status);
    }

    private JComponent cards() {
        cardHost.add(registerPanel(), "register");
        cardHost.add(routesPanel(), "routes");
        cardHost.add(vehiclesByTypePanel(), "vehType");
        cardHost.add(vehiclesBySeatsPanel(), "vehSeats");
        cardHost.add(bookPanel(), "book");
        cardHost.add(cancelPanel(), "cancel");
        cardHost.add(viewBookingPanel(), "viewBook");
        cardHost.add(printBookingPanel(), "printBook");
        return cardHost;
    }

    // ---------------------- REGISTER ----------------------

    private JComponent registerPanel() {
        JTextField firstName = new JTextField(12);
        JTextField lastName = new JTextField(12);
        JTextField dob = new JTextField(10); // yyyy-MM-dd
        JTextField gender = new JTextField(6);
        JTextField street = new JTextField(16);
        JTextField location = new JTextField(12);
        JTextField city = new JTextField(12);
        JTextField state = new JTextField(12);
        JTextField pincode = new JTextField(8);
        JTextField mobile = new JTextField(12);
        JTextField email = new JTextField(16);
        JPasswordField password = new JPasswordField(12);

        JButton register = new JButton("Register");
        register.addActionListener(e -> {
            ProfileBean pb = new ProfileBean();
            pb.setFirstName(firstName.getText().trim());
            pb.setLastName(lastName.getText().trim());
            pb.setDateOfBirth(dob.getText().trim());
            pb.setGender(gender.getText().trim());
            pb.setStreet(street.getText().trim());
            pb.setLocation(location.getText().trim());
            pb.setCity(city.getText().trim());
            pb.setState(state.getText().trim());
            pb.setPincode(pincode.getText().trim());
            pb.setMobileNo(mobile.getText().trim());
            pb.setEmailID(email.getText().trim());
            pb.setPassword(new String(password.getPassword()));

			String result = TripEaseDAO.register(pb);
			status.setText(result);
        });

        JPanel f = FormUtils.form(
                "First Name", firstName, "Last Name", lastName,
                "DOB (yyyy-MM-dd)", dob, "Gender", gender,
                "Street", street, "Location", location, "City", city, "State", state,
                "Pincode", pincode, "Mobile", mobile, "Email", email, "Password", password
        );
        return FormUtils.section("Register", f, register);
    }

    // ---------------------- VIEW ROUTES ----------------------

    private JTable routesTable;
    private JComponent routesPanel() {
        routesTable = new JTable(TableModels.routes());
        return FormUtils.section("All Routes", new JPanel(), new JScrollPane(routesTable));
    }
    private void refreshRoutes() { routesTable.setModel(TableModels.routes()); }

    // ---------------------- VIEW VEHICLES ----------------------

    private JComponent vehiclesByTypePanel() {
        JTextField type = new JTextField(10);
        JTable table = new JTable(TableModels.vehicles(new ArrayList<>()));

        JButton search = new JButton("Search");
        search.addActionListener(e -> {
            if (!FormUtils.require(type, "Vehicle Type", status)) return;
            ArrayList<VehicleBean> list = customerDAO.viewVehiclesByType(type.getText().trim());
            table.setModel(TableModels.vehicles(list));
            status.setText("Found " + list.size() + " vehicle(s) of type " + type.getText().trim());
        });

        JPanel f = FormUtils.form("Type (AC/NonAC/SUV/...)", type);
        return FormUtils.section("Vehicles by Type", f, search, new JScrollPane(table));
    }

    private JComponent vehiclesBySeatsPanel() {
        JSpinner seats = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
        JTable table = new JTable(TableModels.vehicles(new ArrayList<>()));

        JButton search = new JButton("Search");
        search.addActionListener(e -> {
            ArrayList<VehicleBean> list = customerDAO.viewVehicleBySeats((Integer)seats.getValue());
            table.setModel(TableModels.vehicles(list));
            status.setText("Found " + list.size() + " vehicle(s) with seats = " + seats.getValue());
        });

        JPanel f = FormUtils.form("Seats", seats);
        return FormUtils.section("Vehicles by Seats", f, search, new JScrollPane(table));
    }

    // ---------------------- BOOK VEHICLE ----------------------

    private JComponent bookPanel() {
        JTextField reservationId = new JTextField(10);
        JTextField routeId = new JTextField(10);
        JTextField vehicleId = new JTextField(10);
        JTextField boarding = new JTextField(12);
        JTextField drop = new JTextField(12);
        JTextField bookingDate = new JTextField(10); // yyyy-MM-dd
        JTextField journeyDate = new JTextField(10); // yyyy-MM-dd
        JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JButton book = new JButton("Book Vehicle");
        book.addActionListener(e -> {
            if (!FormUtils.require(reservationId, "Reservation ID", status)) return;
            if (!FormUtils.require(routeId, "Route ID", status)) return;
            if (!FormUtils.require(vehicleId, "Vehicle ID", status)) return;
            if (!FormUtils.require(boarding, "Boarding Point", status)) return;
            if (!FormUtils.require(drop, "Drop Point", status)) return;
            if (!FormUtils.require(bookingDate, "Booking Date", status)) return;
            if (!FormUtils.require(journeyDate, "Journey Date", status)) return;
            ReservationBean rb = new ReservationBean();
            rb.setReservationID(reservationId.getText().trim());
            rb.setUserID(currentUserId);
            rb.setRouteID(routeId.getText().trim());
            rb.setVehicleID(vehicleId.getText().trim());
            rb.setBoardingPoint(boarding.getText().trim());
            rb.setDropPoint(drop.getText().trim());
            rb.setBookingDate(bookingDate.getText().trim());
            rb.setJourneyDate(journeyDate.getText().trim());
            rb.setBookingStatus("CONFIRMED");
            // Note: per FS, fare = Distance * FarePerKm (you can compute in DAO if desired)
            String id = customerDAO.bookVehicle(rb);
            status.setText("bookVehicle → " + id);
        });

        JPanel f = FormUtils.form(
                "Reservation ID", reservationId,
                "Route ID", routeId,
                "Vehicle ID", vehicleId,
                "Boarding Point", boarding,
                "Drop Point", drop,
                "Booking Date (yyyy-MM-dd)", bookingDate,
                "Journey Date (yyyy-MM-dd)", journeyDate,
                "Passengers", passengers
        );
        return FormUtils.section("Book Vehicle", f, book);
    }

    // ---------------------- CANCEL BOOKING ----------------------

    private JComponent cancelPanel() {
        JTextField reservationId = new JTextField(12);
        JButton cancel = new JButton("Cancel Booking");
        cancel.addActionListener(e -> {
            if (!FormUtils.require(reservationId, "Reservation ID", status)) return;
            boolean ok = customerDAO.cancelBooking(currentUserId, reservationId.getText().trim());
            status.setText("cancelBooking → " + ok);
        });

        JPanel f = FormUtils.form("Reservation ID", reservationId);
        return FormUtils.section("Cancel Booking", f, cancel);
    }

    // ---------------------- VIEW / PRINT BOOKING ----------------------

    private JComponent viewBookingPanel() {
        JTextField reservationId = new JTextField(12);
        JTextArea out = new JTextArea(10, 50);
        out.setEditable(false);
        JButton view = new JButton("View");
        view.addActionListener(e -> {
            if (!FormUtils.require(reservationId, "Reservation ID", status)) return;
            ReservationBean rb = customerDAO.viewBookingDetails(reservationId.getText().trim());
            out.setText(rb == null ? "Not found" : rb.toString());
        });

        JPanel f = FormUtils.form("Reservation ID", reservationId);
        return FormUtils.section("View Booking", f, view, new JScrollPane(out));
    }

    private JComponent printBookingPanel() {
        JTextField reservationId = new JTextField(12);
        JTextArea out = new JTextArea(10, 50);
        out.setEditable(false);
        JButton print = new JButton("Print");
        print.addActionListener(e -> {
            if (!FormUtils.require(reservationId, "Reservation ID", status)) return;
            ReservationBean rb = customerDAO.printBookingDetails(reservationId.getText().trim());
            out.setText(rb == null ? "Not found" : "=== TICKET ===\n" + rb.toString());
        });

        JPanel f = FormUtils.form("Reservation ID", reservationId);
        return FormUtils.section("Print Booking", f, print, new JScrollPane(out));
    }
    private JComponent reqDropdown() {

        String[] ids = {
                "US-001  Register Profile",
                "US-002  View Vehicles by Type",
                "US-003  Book Vehicle",
                "US-004  Cancel Booking",
                "US-005  View/Print Booking"
        };

        JComboBox<String> combo = new JComboBox<>(ids);
        combo.setSelectedIndex(-1);

        combo.addActionListener(e -> {
            String s = (String) combo.getSelectedItem();
            if (s == null) return;

            String id = s.substring(0, 6);

            switch (id) {
                case "US-001": cardLayout.show(cardHost, "register"); break;
                case "US-002": cardLayout.show(cardHost, "vehType"); break;
                case "US-003": cardLayout.show(cardHost, "book"); break;
                case "US-004": cardLayout.show(cardHost, "cancel"); break;
                case "US-005": cardLayout.show(cardHost, "printBook"); break;
            }
        });

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Requirement ID Jump"), BorderLayout.NORTH);
        p.add(combo, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        return p;
    }

}
