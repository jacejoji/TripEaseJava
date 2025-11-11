package com.ust.ui;
import com.ust.dao.TripEaseDAO;
import com.ust.util.DBUtil;
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
        UIManager.put("Button.focus", UIManager.getColor("Button.background"));
        UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Monospaced", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Table.rowHeight", 26);
        UIManager.put("TableHeader.font", new Font("SansSerif", Font.BOLD, 14));
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
        //JButton logoutBtn = new JButton("Logout");

        int r = 0;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("User ID"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(userIDField, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Password"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(passwordField, gc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.add(loginBtn);
        actions.add(changePwdBtn);
        //actions.add(logoutBtn);
        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        form.add(actions, gc);

        frame.add(form, BorderLayout.CENTER);

        // Output / status area
        outputArea = new JTextArea(6, 40);
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea), BorderLayout.SOUTH);
       outputArea.setText("Database Connection Established with id:"+(String)DBUtil.getDBConnection().toString());

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
            String uid = userIDField.getText().trim();
            String oldPwd = new String(passwordField.getPassword()).trim();

            if (uid.isEmpty() || oldPwd.isEmpty()) {
                outputArea.setText("Enter UserID and current password first.");
                return;
            }

            // Ask for new password using an input field dialog
            String newPwd = JOptionPane.showInputDialog(frame, "Enter new password:");

            if (newPwd == null || newPwd.trim().isEmpty()) {
                outputArea.setText("Password change cancelled.");
                return;
            }

            CredentialsBean cred = new CredentialsBean();
            cred.setUserID(uid);
            cred.setPassword(oldPwd);

            String result = tripEaseDAO.changePassword(cred, newPwd.trim());

            switch (result) {
                case "SUCCESS":
                    outputArea.setText("Password changed successfully.");
                    break;

                case "INVALID":
                    outputArea.setText("Invalid current password. Please try again.");
                    break;

                case "FAIL":
                default:
                    outputArea.setText("Failed to change password.");
                    break;
            }
        });
//        logoutBtn.addActionListener(e -> {
//
//            String uid = userIDField.getText().trim();
//
//            if (uid.isEmpty()) {
//                outputArea.setText("Enter UserID first.");
//                return;
//            }
//
//            boolean ok = tripEaseDAO.logout(uid);
//
//            if (ok) {
//                outputArea.setText("Logout successful.");
//                userIDField.setText("");
//                passwordField.setText("");
//            } else {
//                outputArea.setText("Logout failed. UserID not found or already logged out.");
//            }
//        });


        frame.setVisible(true);
    }
}
