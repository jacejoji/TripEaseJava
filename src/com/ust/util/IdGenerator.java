package com.ust.util;

import java.sql.*;

public final class IdGenerator {
    private IdGenerator(){}

    /**
     * Gets next numeric sequence value from ata_sequence table in a concurrency-safe way.
     * Caller must not manipulate auto-commit; this method uses its own transaction.
     */
    public static long nextSeq(Connection con, String seqName) throws SQLException {
        // Expect caller passes a valid connection. We'll manage a small transaction locally.
        boolean origAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);

            String selectSql = "SELECT current_value FROM ata_sequence WHERE name = ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                ps.setString(1, seqName);
                long cur;
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        // Sequence not found -> create row starting at 1000 (or throw)
                        String insert = "INSERT INTO ata_sequence (name, current_value) VALUES (?, 1000)";
                        try (PreparedStatement ips = con.prepareStatement(insert)) {
                            ips.setString(1, seqName);
                            ips.executeUpdate();
                        }
                        cur = 1000L;
                    } else {
                        cur = rs.getLong("current_value");
                    }
                }

                long next = cur + 1L;
                String update = "UPDATE ata_sequence SET current_value = ? WHERE name = ?";
                try (PreparedStatement ups = con.prepareStatement(update)) {
                    ups.setLong(1, next);
                    ups.setString(2, seqName);
                    ups.executeUpdate();
                }

                con.commit();
                return next;
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(origAutoCommit);
        }
    }

    /**
     * sanitize and get first N letters (A-Z). If not enough letters, pad with 'X'.
     */
    private static String prefix(String s, int n) {
        if (s == null) s = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length() && sb.length() < n; ++i) {
            char ch = s.charAt(i);
            if (Character.isLetter(ch)) sb.append(Character.toUpperCase(ch));
        }
        while (sb.length() < n) sb.append('X');
        return sb.toString();
    }

    // ---- convenience generator methods ----

    public static String generateVehicleId(Connection con, String vehicleName) throws SQLException {
        String prefix = prefix(vehicleName, 2);
        long seq = nextSeq(con, "ata_seq_vehicleId");
        return String.format("%s%04d", prefix, seq % 10000); // 4 digits
    }

    public static String generateDriverId(Connection con, String firstName) throws SQLException {
        String prefix = prefix(firstName, 2);
        long seq = nextSeq(con, "ata_seq_driverId");
        return String.format("%s%04d", prefix, seq % 10000);
    }

    public static String generateUserId(Connection con, String firstName) throws SQLException {
        String prefix = prefix(firstName, 2);
        long seq = nextSeq(con, "ata_seq_userId");
        return String.format("%s%04d", prefix, seq % 10000);
    }

    public static String generateRouteId(Connection con, String source, String destination) throws SQLException {
        String p1 = prefix(source, 2);
        String p2 = prefix(destination, 2);
        long seq = nextSeq(con, "ata_seq_routeId");
        return String.format("%s%s%04d", p1, p2, seq % 10000);
    }

    public static String generateReservationId(Connection con) throws SQLException {
        long seq = nextSeq(con, "ata_seq_reservationId");
        return String.format("RS%04d", seq % 10000); // prefix RS or whatever you'd like
    }
}
