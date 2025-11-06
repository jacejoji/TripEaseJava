package com.ust.ui;
import com.ust.bean.*;
import com.ust.dao.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AdminDashboard extends JFrame {
    private JTextArea outputArea;
    private AdministratorDAO administratorDAO = new AdministratorDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public AdminDashboard() {
        setTitle("Administrator Menu - TripEase");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        // Panel for admin options
        JPanel adminPanel = new JPanel();
        getContentPane().add(adminPanel, BorderLayout.NORTH);

        JButton btnAddVehicle = new JButton("Add Vehicle");
        adminPanel.add(btnAddVehicle);

        JButton btnViewVehicles = new JButton("View Vehicles");
        adminPanel.add(btnViewVehicles);

        JButton btnAddRoute = new JButton("Add Route");
        adminPanel.add(btnAddRoute);

        JButton btnViewBookings = new JButton("View Bookings");
        adminPanel.add(btnViewBookings);

        // Output area for displaying results
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Action listeners for buttons
        btnAddVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show dialog to enter vehicle details
                String vehicleID = JOptionPane.showInputDialog("Enter Vehicle ID:");
                String name = JOptionPane.showInputDialog("Enter Vehicle Name:");
                String type = JOptionPane.showInputDialog("Enter Vehicle Type:");
                int seatingCapacity = Integer.parseInt(JOptionPane.showInputDialog("Enter Seating Capacity:"));
                double farePerKM = Double.parseDouble(JOptionPane.showInputDialog("Enter Fare per KM:"));
                
                VehicleBean newVehicle = new VehicleBean();
                newVehicle.setVehicleID(vehicleID);
                newVehicle.setName(name);
                newVehicle.setType(type);
                newVehicle.setSeatingCapacity(seatingCapacity);
                newVehicle.setFarePerKM(farePerKM);

                String result = administratorDAO.addVehicle(newVehicle);
                customerDAO.vehicleList = administratorDAO.vehicleList;
                outputArea.setText("Vehicle added: " + result);
            }
        });

        btnViewVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<VehicleBean> allVehicles = customerDAO.viewVehiclesByType("SUV");
                StringBuilder sb = new StringBuilder();
                if (allVehicles.isEmpty()) {
                    sb.append("No vehicles found.");
                } else {
                    for (VehicleBean vehicle : allVehicles) {
                        sb.append(vehicle.getName()).append(" | Type: ").append(vehicle.getType())
                          .append(" | Seating: ").append(vehicle.getSeatingCapacity()).append("\n");
                    }
                }
                outputArea.setText(sb.toString());
            }
        });

        btnAddRoute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show dialog to enter route details
                String routeID = JOptionPane.showInputDialog("Enter Route ID:");
                String source = JOptionPane.showInputDialog("Enter Source City:");
                String destination = JOptionPane.showInputDialog("Enter Destination City:");
                int distance = Integer.parseInt(JOptionPane.showInputDialog("Enter Distance (in KM):"));
                int travelDuration = Integer.parseInt(JOptionPane.showInputDialog("Enter Travel Duration (in hours):"));

                RouteBean newRoute = new RouteBean();
                newRoute.setRouteID(routeID);
                newRoute.setSource(source);
                newRoute.setDestination(destination);
                newRoute.setDistance(distance);
                newRoute.setTravelDuration(travelDuration);
                String result = administratorDAO.addRoute(newRoute);
                outputArea.setText("Route added: " + result);
            }
        });

        btnViewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ReservationBean> allBookings = administratorDAO.getAllBookings();
                StringBuilder sb = new StringBuilder();
                if (allBookings.isEmpty()) {
                    sb.append("No bookings found.");
                } else {
                    for (ReservationBean booking : allBookings) {
                        sb.append("Reservation ID: ").append(booking.getReservationID())
                          .append(" | Status: ").append(booking.getBookingStatus()).append("\n");
                    }
                }
                outputArea.setText(sb.toString());
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }
}
