package com.ust.ui;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class FormUtils {
    private static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");

    private FormUtils(){}

    public static JPanel form(Object... pairs) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        int r = 0;
        for (int i = 0; i < pairs.length; i += 2) {
            JLabel lbl = new JLabel(String.valueOf(pairs[i]));
            JComponent comp = (JComponent) pairs[i+1];
            gc.gridx = 0; gc.gridy = r; gc.weightx = 0;
            p.add(lbl, gc);
            gc.gridx = 1; gc.gridy = r++; gc.weightx = 1;
            p.add(comp, gc);
        }
        return p;
    }

    public static JComponent section(String title, JComponent... parts) {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 18));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        outer.add(t);
        outer.add(Box.createVerticalStrut(10));

        for (JComponent c : parts) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
            outer.add(c);
            outer.add(Box.createVerticalStrut(10));
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(outer, BorderLayout.NORTH); // ✅ prevents stretching
        return new JScrollPane(wrapper);        // ✅ scrollable & compact
    }


    public static JPanel row(JComponent... children) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        for (JComponent c : children) p.add(c);
        return p;
    }

    public static JComponent wrap(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    public static Date parseYmd(String s) {
        try {
            if (s == null || s.isEmpty()) return null;
            return YMD.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    public static ArrayList<String> splitIds(String csv) {
        ArrayList<String> list = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return list;
        for (String part : csv.split(",")) {
            String id = part.trim();
            if (!id.isEmpty()) list.add(id);
        }
        return list;
    }
    public static boolean require(JTextField field, String fieldName, JTextArea status) {
        if (field.getText().trim().isEmpty()) {
            status.setText("ERROR: " + fieldName + " cannot be empty.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean requirePassword(JPasswordField field, String fieldName, JTextArea status) {
        if (new String(field.getPassword()).trim().isEmpty()) {
            status.setText("ERROR: " + fieldName + " cannot be empty.");
            field.requestFocus();
            return false;
        }
        return true;
    }

}
