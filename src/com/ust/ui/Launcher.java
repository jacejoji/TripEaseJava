package com.ust.ui;
import com.ust.dao.*;
import com.ust.bean.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Launcher {
	
    private JFrame frame;
    private JTextField userIDField, passwordField;
    private JTextArea outputArea;
    private TripEaseDAO tripEaseDAO = new TripEaseDAO();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Launcher window = new Launcher();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Launcher() {
        initialize();
    }

    private void initialize() {
        // Initialize the frame
        frame = new JFrame("Login - TripEase");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // Panel for login form
        JPanel loginPanel = new JPanel();
        frame.getContentPane().add(loginPanel, BorderLayout.NORTH);

        JLabel lblUserID = new JLabel("UserID:");
        loginPanel.add(lblUserID);

        userIDField = new JTextField();
        loginPanel.add(userIDField);
        userIDField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        loginPanel.add(lblPassword);

        passwordField = new JTextField();
        loginPanel.add(passwordField);
        passwordField.setColumns(10);

        JButton btnLogin = new JButton("Login");
        loginPanel.add(btnLogin);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Login action
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText().trim();
                String password = passwordField.getText().trim();
                CredentialsBean user = tripEaseDAO.login(userID, password);
                if (user == null) {
                    outputArea.setText("Invalid userID or password. Please try again.");
                } else {
                    outputArea.setText("Login successful!\n" + tripEaseDAO.viewProfile(userID));
                    // Proceed to the main screen after successful login
                    showAdminMenu();
                }
            }
        });
    }

    private void showAdminMenu() {
        // Close login screen and launch admin menu
        frame.dispose();  // Close the login screen

        // Create and display the main application screen (Administrator actions)
        SwingUtilities.invokeLater(() -> {
            try {
                AdminDashboard adminScreen = new AdminDashboard();
                adminScreen.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
