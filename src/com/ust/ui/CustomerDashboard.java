package com.ust.ui;

import com.ust.bean.*;
import com.ust.dao.CustomerDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
        setLayout(new BorderLayout(8, 8));
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
        addHoverEffect(logoutBtn);
        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new Launcher().show());
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(new JLabel("Logged in: " + currentUserId + "  "));
        right.add(logoutBtn);

        p.add(title, BorderLayout.CENTER);
        p.add(right, BorderLayout.EAST);
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return p;
    }

    private JComponent sidebar() {
        JPanel bar = new JPanel(new GridLayout(0,1,6,6));
        bar.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

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
        b.setPreferredSize(new Dimension(180, 36));
        b.setMaximumSize(new Dimension(220, 36));
        b.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addHoverEffect(b);
        b.addActionListener(e -> onClick.run());
        return b;
    }

    private JComponent statusBar() {
        status.setEditable(false);
        status.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(status);
        sp.setBorder(BorderFactory.createTitledBorder("Status"));
        return sp;
    }

    private JComponent cards() {
        // register panel intentionally removed (registration is done via login screen dialog)
        cardHost.add(routesPanel(), "routes");
        cardHost.add(vehiclesByTypePanel(), "vehType");
        cardHost.add(vehiclesBySeatsPanel(), "vehSeats");
        cardHost.add(bookPanel(), "book");
        cardHost.add(cancelPanel(), "cancel");
        cardHost.add(viewBookingPanel(), "viewBook");
        cardHost.add(printBookingPanel(), "printBook");
        return cardHost;
    }

    // ---------------------- VIEW ROUTES ----------------------

    private JTable routesTable;
    private JComponent routesPanel() {
        routesTable = new JTable(TableModels.routes());
        styleTable(routesTable);
        return FormUtils.section("All Routes", new JPanel(), new JScrollPane(routesTable));
    }
    private void refreshRoutes() { routesTable.setModel(TableModels.routes()); styleTable(routesTable); }

    // ---------------------- VIEW VEHICLES ----------------------

    private JComponent vehiclesByTypePanel() {
        JTextField type = new JTextField(10);
        JTable table = new JTable(TableModels.vehicles(new ArrayList<>()));
        styleTable(table);

        JButton search = new JButton("Search");
        addHoverEffect(search);
        search.addActionListener(e -> {
            if (!FormUtils.require(type, "Vehicle Type", status)) return;
            ArrayList<VehicleBean> list = customerDAO.viewVehiclesByType(type.getText().trim());
            table.setModel(TableModels.vehicles(list));
            styleTable(table);
            status.setText("Found " + list.size() + " vehicle(s) of type " + type.getText().trim());
        });

        JPanel f = FormUtils.form("Type (AC/NonAC/SUV/...)", type);
        return FormUtils.section("Vehicles by Type", f, search, new JScrollPane(table));
    }

    private JComponent vehiclesBySeatsPanel() {
        JSpinner seats = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
        JTable table = new JTable(TableModels.vehicles(new ArrayList<>()));
        styleTable(table);

        JButton search = new JButton("Search");
        addHoverEffect(search);
        search.addActionListener(e -> {
            ArrayList<VehicleBean> list = customerDAO.viewVehicleBySeats((Integer)seats.getValue());
            table.setModel(TableModels.vehicles(list));
            styleTable(table);
            status.setText("Found " + list.size() + " vehicle(s) with seats = " + seats.getValue());
        });

        JPanel f = FormUtils.form("Seats", seats);
        return FormUtils.section("Vehicles by Seats", f, search, new JScrollPane(table));
    }

    // ---------------------- BOOK VEHICLE ----------------------

    private JComponent bookPanel() {
        // ReservationID removed (auto-generated by DAO)
        JTextField routeId = new JTextField(10);
        JTextField vehicleId = new JTextField(10);
        JTextField boarding = new JTextField(12);
        JTextField drop = new JTextField(12);
        JTextField bookingDate = new JTextField(10); // yyyy-MM-dd
        JTextField journeyDate = new JTextField(10); // yyyy-MM-dd
        JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JButton book = new JButton("Book Vehicle");
        addHoverEffect(book);
        book.addActionListener(e -> {
            if (!FormUtils.require(routeId, "Route ID", status)) return;
            if (!FormUtils.require(vehicleId, "Vehicle ID", status)) return;
            if (!FormUtils.require(boarding, "Boarding Point", status)) return;
            if (!FormUtils.require(drop, "Drop Point", status)) return;
            if (!FormUtils.require(bookingDate, "Booking Date", status)) return;
            if (!FormUtils.require(journeyDate, "Journey Date", status)) return;

            ReservationBean rb = new ReservationBean();
            // Do NOT set reservationID - DAO will generate the ID
            rb.setUserID(currentUserId);
            rb.setRouteID(routeId.getText().trim());
            rb.setVehicleID(vehicleId.getText().trim());
            rb.setBoardingPoint(boarding.getText().trim());
            rb.setDropPoint(drop.getText().trim());
            rb.setBookingDate(bookingDate.getText().trim());
            rb.setJourneyDate(journeyDate.getText().trim());
            rb.setBookingStatus("CONFIRMED");

            String id = customerDAO.bookVehicle(rb);
            status.setText("bookVehicle → " + id);
        });

        JPanel f = FormUtils.form(
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
        addHoverEffect(cancel);
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
        addHoverEffect(view);
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
        addHoverEffect(print);
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

    /* ------------------ UI helpers ------------------ */

    // Apply simple hover color fade to buttons (same behavior as Admin/Launcher)
    private void addHoverEffect(JButton btn) {
        Color base = btn.getBackground();
        Color hover = base.brighter();
        btn.setOpaque(true);

        final int steps = 8;
        final int delay = 12;

        btn.addMouseListener(new MouseAdapter() {
            Timer fadeTimer = null;
            int step = 0;
            boolean entering = false;

            private void start(boolean toHover) {
                entering = toHover;
                if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();
                step = 0;
                fadeTimer = new Timer(delay, ev -> {
                    step++;
                    float frac = Math.min(1f, step / (float) steps);
                    if (!entering) frac = 1f - frac;
                    btn.setBackground(interpolateColor(base, hover, frac));
                    if (step >= steps) fadeTimer.stop();
                });
                fadeTimer.start();
            }

            @Override public void mouseEntered(MouseEvent e) { start(true); }
            @Override public void mouseExited(MouseEvent e) { start(false); }
        });
    }

    private Color interpolateColor(Color a, Color b, float f) {
        f = Math.max(0f, Math.min(1f, f));
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * f);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * f);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * f);
        return new Color(r, g, bl);
    }

    // Basic table styling used across customer panels
    private void styleTable(JTable table) {
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JComponent) ((JComponent) c).setOpaque(true);

                if (!isSelected) {
                    c.setBackground((row % 2 == 0) ? Color.WHITE : new Color(190,213,250));
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        });
    }
}
