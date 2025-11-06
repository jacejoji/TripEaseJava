package com.ust.ui;

import com.ust.dao.TripEaseDAO;
import com.ust.bean.CredentialsBean;

import javax.swing.*;
import java.awt.*;

public class Launcher {
    private JFrame frame;
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JTextArea outputArea;
    private final TripEaseDAO tripEaseDAO = new TripEaseDAO();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Launcher().show());
    }

    public void show() {
        frame = new JFrame("TripEase - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Top banner
        JLabel title = new JLabel("TripEase - Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        frame.add(title, BorderLayout.NORTH);

        // Center: login form
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        userIDField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginBtn = new JButton("Login");
        JButton changePwdBtn = new JButton("Change Password");
        JButton logoutBtn = new JButton("Logout");

        int r = 0;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("User ID"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(userIDField, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Password"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(passwordField, gc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.add(loginBtn);
        actions.add(changePwdBtn);
        actions.add(logoutBtn);
        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        form.add(actions, gc);

        frame.add(form, BorderLayout.CENTER);

        // Output / status area
        outputArea = new JTextArea(6, 40);
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // Wire buttons
        loginBtn.addActionListener(e -> {
            String uid = userIDField.getText().trim();
            String pwd = new String(passwordField.getPassword());
            CredentialsBean user = tripEaseDAO.login(uid, pwd); // uses your current method
            if (user == null) {
                outputArea.setText("Invalid credentials.");
                return;
            }
            outputArea.setText("Login success. UserType = " + user.getUserType());

            frame.dispose();
            if ("Administrator".equalsIgnoreCase(user.getUserType())) {
                SwingUtilities.invokeLater(() -> new AdminDashboard(uid).setVisible(true));
            } else {
                SwingUtilities.invokeLater(() -> new CustomerDashboard(uid).setVisible(true));
            }
        });

        changePwdBtn.addActionListener(e -> {
            outputArea.setText("Change password will use TripEaseDAO.changePassword(..). Implement in DAO and wire here.");
            // Example once added:
            // var cred = new CredentialsBean(); cred.setUserID(userIDField.getText()); cred.setPassword(currentPwd);
            // String result = tripEaseDAO.changePassword(cred, newPwd);
        });

        logoutBtn.addActionListener(e -> {
            outputArea.setText("Logout will use TripEaseDAO.logout(..). Implement in DAO and wire here.");
            // Example once added:
            // boolean ok = tripEaseDAO.logout(currentUserId);
        });

        frame.setVisible(true);
    }
}
