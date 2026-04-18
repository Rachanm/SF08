package com.placement.dao;

import com.placement.config.DBConnection;
import com.placement.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // CREATE - Add new admin
    public Admin createAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO admin (name, password) VALUES (?, ?) RETURNING id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getPassword());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                admin.setId(rs.getLong("id"));
            }
            return admin;
        }
    }

    // READ ALL - Get all admins
    public List<Admin> getAllAdmins() throws SQLException {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM admin ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Admin(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password")
                ));
            }
        }
        return list;
    }

    // READ ONE - Get admin by ID
    public Admin getAdminById(long id) throws SQLException {
        String sql = "SELECT * FROM admin WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password")
                );
            }
            return null; // Not found
        }
    }

    // UPDATE - Update admin by ID
    public boolean updateAdmin(long id, Admin admin) throws SQLException {
        String sql = "UPDATE admin SET name = ?, password = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getPassword());
            stmt.setLong(3, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE - Delete admin by ID
    public boolean deleteAdmin(long id) throws SQLException {
        String sql = "DELETE FROM admin WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}