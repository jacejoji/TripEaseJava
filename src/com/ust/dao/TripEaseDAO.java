package com.ust.dao;

import com.ust.bean.CredentialsBean;
import com.ust.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * TripEaseDAO: handles authentication and credential operations against
 * ATA_TBL_User_Credentials table in MySQL.
 *
 * Methods:
 *  - login(userID, password)         : returns CredentialsBean or null
 *  - changePassword(credentials, newPwd) : returns "SUCCESS" / "INVALID" / "FAIL"
 *  - logout(userID)                  : returns true if logout succeeded
 *  - addCredentials(CredentialsBean) : inserts credentials (used during registration)
 *  - userExists(userID)              : checks if a user exists
 */
public class TripEaseDAO {

    /**
     * Attempt login. If successful, returns a populated CredentialsBean and
     * sets Loginstatus=1 in DB.
     */
    public CredentialsBean login(String userID, String password) {
        String sql = "SELECT Userid, Password, Usertype, Loginstatus FROM ATA_TBL_User_Credentials WHERE Userid = ? AND Password = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userID);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CredentialsBean cb = new CredentialsBean();
                    cb.setUserID(rs.getString("Userid"));
                    cb.setPassword(rs.getString("Password"));
                    // Map DB 'A'/'C' to your previous UI strings if needed.
                    // We'll keep whatever Usertype string is stored ('A' or 'C').
                    cb.setUserType(rs.getString("Usertype"));
                    cb.setLoginStatus(rs.getInt("Loginstatus"));

                    // Update loginstatus = 1 (logged in)
                    setLoginStatus(userID, 1);

                    return cb;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Change password: validates old password from credentials bean, updates to newPwd.
     * Returns:
     *   "SUCCESS" if updated,
     *   "INVALID" if credentials don't match,
     *   "FAIL" on other errors.
     */
    public String changePassword(CredentialsBean c, String newPwd) {
        String verifySql = "SELECT Password FROM ATA_TBL_User_Credentials WHERE Userid = ?";
        String updateSql = "UPDATE ATA_TBL_User_Credentials SET Password = ? WHERE Userid = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement verifyPs = con.prepareStatement(verifySql)) {

            verifyPs.setString(1, c.getUserID());
            try (ResultSet rs = verifyPs.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("Password");
                    if (!stored.equals(c.getPassword())) {
                        return "INVALID";
                    }
                } else {
                    return "INVALID";
                }
            }

            try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                updatePs.setString(1, newPwd);
                updatePs.setString(2, c.getUserID());
                int updated = updatePs.executeUpdate();
                return updated == 1 ? "SUCCESS" : "FAIL";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    /**
     * Logout: sets Loginstatus = 0.
     * Returns true if update affected a row.
     */
    public boolean logout(String userID) {
        return setLoginStatus(userID, 0);
    }

    /**
     * Internal helper: set loginstatus value.
     */
    private boolean setLoginStatus(String userID, int status) {
        String sql = "UPDATE ATA_TBL_User_Credentials SET Loginstatus = ? WHERE Userid = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setString(2, userID);
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Insert a credentials record. Returns true on success.
     * Useful when creating a new profile (register).
     */
    public boolean addCredentials(CredentialsBean cred) {
        String sql = "INSERT INTO ATA_TBL_User_Credentials (Userid, Password, Usertype, Loginstatus) VALUES (?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cred.getUserID());
            ps.setString(2, cred.getPassword());
            ps.setString(3, cred.getUserType()); // expect 'A' or 'C'
            ps.setInt(4, cred.getLoginStatus());

            int inserted = ps.executeUpdate();
            return inserted == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a user exists in credentials table.
     */
    public boolean userExists(String userID) {
        String sql = "SELECT 1 FROM ATA_TBL_User_Credentials WHERE Userid = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Utility: load all credentials (if needed)
     */
    public ArrayList<CredentialsBean> loadAllCredentials() {
        ArrayList<CredentialsBean> list = new ArrayList<>();
        String sql = "SELECT Userid, Password, Usertype, Loginstatus FROM ATA_TBL_User_Credentials";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CredentialsBean cb = new CredentialsBean();
                cb.setUserID(rs.getString("Userid"));
                cb.setPassword(rs.getString("Password"));
                cb.setUserType(rs.getString("Usertype"));
                cb.setLoginStatus(rs.getInt("Loginstatus"));
                list.add(cb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
