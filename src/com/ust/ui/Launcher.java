package com.ust.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.ust.bean.CredentialsBean;
import com.ust.bean.ProfileBean;
import com.ust.dao.CustomerDAO;
import com.ust.dao.TripEaseDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Launcher {
    private JFrame frame;
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JTextArea outputArea;

    private final TripEaseDAO tripEaseDAO = new TripEaseDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public static void main(String[] args) {
        // setup FlatLaf (modern LAF) before creating UI
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("FlatLaf not available, falling back to system LAF.");
        }

        // general UI defaults
        UIManager.put("Button.focus", UIManager.getColor("Button.background"));
        UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Monospaced", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Table.rowHeight", 26);
        UIManager.put("TableHeader.font", new Font("SansSerif", Font.BOLD, 14));

        SwingUtilities.invokeLater(() -> new Launcher().show());
    }

    public void show() {
        frame = new JFrame("TripEase - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(12, 12));

        // Top banner
        JLabel title = new JLabel("TripEase - Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        frame.add(title, BorderLayout.NORTH);

        // Center: login form
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 12, 8, 12);
        gc.fill = GridBagConstraints.HORIZONTAL;

        userIDField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginBtn = new JButton("Login");
        JButton changePwdBtn = new JButton("Change Password");
        JButton registerBtn = new JButton("Register");

        // Apply button hover effect
        addHoverEffect(loginBtn);
        addHoverEffect(changePwdBtn);
        addHoverEffect(registerBtn);

        int r = 0;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("User ID"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(userIDField, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Password"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(passwordField, gc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.add(loginBtn);
        actions.add(changePwdBtn);
        actions.add(registerBtn);
        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        form.add(actions, gc);

        frame.add(form, BorderLayout.CENTER);

        // Output / status area
        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outScroll = new JScrollPane(outputArea);
        outScroll.setBorder(BorderFactory.createTitledBorder("Status"));
        frame.add(outScroll, BorderLayout.SOUTH);

        // Wire buttons
        loginBtn.addActionListener(e -> doLogin());
        changePwdBtn.addActionListener(e -> showChangePasswordDialog());
        registerBtn.addActionListener(e -> showRegisterDialog());

        frame.setVisible(true);
    }

    // --- login action
    private void doLogin() {
        String uid = userIDField.getText().trim();
        String pwd = new String(passwordField.getPassword()).trim();

        if (uid.isEmpty() || pwd.isEmpty()) {
            outputArea.setText("Enter UserID and Password.");
            return;
        }

        try {
            CredentialsBean user = tripEaseDAO.login(uid, pwd);
            if (user == null) {
                outputArea.setText("Invalid credentials.");
                return;
            }
            outputArea.setText("Login success. UserType = " + user.getUserType());

            frame.dispose();
            if ("A".equalsIgnoreCase(user.getUserType())) {
                SwingUtilities.invokeLater(() -> new AdminDashboard(uid).setVisible(true));
            } else {
                SwingUtilities.invokeLater(() -> new CustomerDashboard(uid).setVisible(true));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("Login failed: " + ex.getMessage());
        }
    }

    // --- Register dialog (modal)
    private void showRegisterDialog() {
        JDialog dlg = new JDialog(frame, "Register - Create Account", true);
        dlg.setSize(700, 560);
        dlg.setLocationRelativeTo(frame);
        dlg.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        JTextField firstName = new JTextField(14);
        JTextField lastName = new JTextField(14);
        JTextField dob = new JTextField(10); // yyyy-MM-dd
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField street = new JTextField(16);
        JTextField location = new JTextField(12);
        JTextField city = new JTextField(12);
        JTextField state = new JTextField(12);
        JTextField pincode = new JTextField(8);
        JTextField mobile = new JTextField(12);
        JTextField email = new JTextField(16);
        JPasswordField password = new JPasswordField(12);
        JPasswordField confirm = new JPasswordField(12);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("First Name *"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(firstName, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Last Name *"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(lastName, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("DOB (yyyy-MM-dd) *"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(dob, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Gender"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(gender, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Street"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(street, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Location"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(location, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("City"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(city, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("State"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(state, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Pincode"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(pincode, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Mobile *"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(mobile, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Email"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(email, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Password *"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(password, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Confirm Password *"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(confirm, gc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submit = new JButton("Register");
        JButton cancel = new JButton("Cancel");
        addHoverEffect(submit);
        addHoverEffect(cancel);
        buttons.add(cancel);
        buttons.add(submit);

        dlg.add(new JScrollPane(form), BorderLayout.CENTER);
        dlg.add(buttons, BorderLayout.SOUTH);

        submit.addActionListener(ae -> {
            // Basic validation
            if (firstName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "First name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (lastName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Last name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dob.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "DOB is required (yyyy-MM-dd).", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String pwd = new String(password.getPassword()).trim();
            String cfpwd = new String(confirm.getPassword()).trim();
            if (pwd.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Password is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!pwd.equals(cfpwd)) {
                JOptionPane.showMessageDialog(dlg, "Passwords do not match.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (mobile.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Mobile number is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Build ProfileBean (userID will be generated by DAO)
            ProfileBean pb = new ProfileBean(
                    null,
                    firstName.getText().trim(),
                    lastName.getText().trim(),
                    dob.getText().trim(),
                    (String) gender.getSelectedItem(),
                    street.getText().trim(),
                    location.getText().trim(),
                    city.getText().trim(),
                    state.getText().trim(),
                    pincode.getText().trim(),
                    mobile.getText().trim(),
                    email.getText().trim(),
                    pwd
            );

            // Call DAO registerProfile (returns ProfileBean with generated userID)
            try {
                ProfileBean created = customerDAO.registerProfile(pb);
                if (created != null && created.getUserID() != null) {
                    JOptionPane.showMessageDialog(dlg,
                            "Registration successful. Your User ID: " + created.getUserID(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                } else {
                    JOptionPane.showMessageDialog(dlg,
                            "Registration failed. Please check input or try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dlg,
                        "Registration failed due to an internal error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancel.addActionListener(ae -> dlg.dispose());
        dlg.setVisible(true);
    }

    // --- Change password dialog (modal)
    private void showChangePasswordDialog() {
        JDialog dlg = new JDialog(frame, "Change Password", true);
        dlg.setSize(440, 260);
        dlg.setLocationRelativeTo(frame);
        dlg.setLayout(new BorderLayout(8, 8));

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        JTextField uidField = new JTextField(20);
        uidField.setText(userIDField.getText().trim()); // prefill from login field

        JPasswordField current = new JPasswordField(20);
        JPasswordField nw = new JPasswordField(20);
        JPasswordField confirm = new JPasswordField(20);

        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("User ID"), gc);
        gc.gridx = 1; gc.gridy = r++; p.add(uidField, gc);

        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Current Password"), gc);
        gc.gridx = 1; gc.gridy = r++; p.add(current, gc);

        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("New Password"), gc);
        gc.gridx = 1; gc.gridy = r++; p.add(nw, gc);

        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Confirm New Password"), gc);
        gc.gridx = 1; gc.gridy = r++; p.add(confirm, gc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submit = new JButton("Change");
        JButton cancel = new JButton("Cancel");
        addHoverEffect(submit);
        addHoverEffect(cancel);
        buttons.add(cancel);
        buttons.add(submit);

        dlg.add(p, BorderLayout.CENTER);
        dlg.add(buttons, BorderLayout.SOUTH);

        submit.addActionListener(ae -> {
            String uid = uidField.getText().trim();
            String oldPwd = new String(current.getPassword()).trim();
            String newPwd = new String(nw.getPassword()).trim();
            String conf = new String(confirm.getPassword()).trim();

            if (uid.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "User ID is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (oldPwd.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Current password is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (newPwd.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "New password is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!newPwd.equals(conf)) {
                JOptionPane.showMessageDialog(dlg, "New passwords do not match.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            CredentialsBean cred = new CredentialsBean();
            cred.setUserID(uid);
            cred.setPassword(oldPwd);

            String result;
            try {
                result = tripEaseDAO.changePassword(cred, newPwd);
            } catch (Exception ex) {
                ex.printStackTrace();
                result = "FAIL";
            }

            switch (result) {
                case "SUCCESS":
                    outputArea.setText("Password changed successfully for user: " + uid);
                    JOptionPane.showMessageDialog(dlg, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                    break;
                case "INVALID":
                    outputArea.setText("Invalid current password. Please try again.");
                    JOptionPane.showMessageDialog(dlg, "Invalid current password.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    outputArea.setText("Failed to change password.");
                    JOptionPane.showMessageDialog(dlg, "Password change failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });

        cancel.addActionListener(ae -> dlg.dispose());
        dlg.setVisible(true);
    }

    // --- Simple hover color fade for buttons (micro-animation) ---
    private void addHoverEffect(JButton btn) {
        // fallback safe defaults
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

            @Override
            public void mouseEntered(MouseEvent e) { start(true); }
            @Override
            public void mouseExited(MouseEvent e) { start(false); }
        });
    }

    private Color interpolateColor(Color a, Color b, float f) {
        f = Math.max(0f, Math.min(1f, f));
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * f);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * f);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * f);
        return new Color(r, g, bl);
    }
}
