package com.placement.servlet;

import com.google.gson.Gson;
import com.placement.dao.AdminDAO;
import com.placement.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class AdminServlet extends HttpServlet {

    private AdminDAO adminDAO = new AdminDAO();
    private Gson gson = new Gson();

    // Helper: set response as JSON
    private void setJson(HttpServletResponse res) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
    }

    // Helper: get ID from URL → /api/admins/3 gives 3, /api/admins gives -1
    private long getId(HttpServletRequest req) {
        String path = req.getPathInfo(); // e.g. "/3" or null
        if (path == null || path.equals("/")) return -1;
        try {
            return Long.parseLong(path.substring(1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ── GET ──────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setJson(res);
        PrintWriter out = res.getWriter();
        long id = getId(req);

        try {
            if (id == -1) {
                // GET /api/admins → return all
                List<Admin> admins = adminDAO.getAllAdmins();
                out.print(gson.toJson(admins));
            } else {
                // GET /api/admins/1 → return one
                Admin admin = adminDAO.getAdminById(id);
                if (admin == null) {
                    res.setStatus(404);
                    out.print("{\"error\":\"Admin not found\"}");
                } else {
                    out.print(gson.toJson(admin));
                }
            }
        } catch (SQLException e) {
            res.setStatus(500);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }

    // ── POST ─────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setJson(res);
        PrintWriter out = res.getWriter();

        try {
            Admin admin = gson.fromJson(req.getReader(), Admin.class);

            // Validate
            if (admin.getName() == null || admin.getName().isEmpty() ||
                admin.getPassword() == null || admin.getPassword().isEmpty()) {
                res.setStatus(400);
                out.print("{\"error\":\"Name and password are required\"}");
                out.flush();
                return;
            }

            Admin created = adminDAO.createAdmin(admin);
            res.setStatus(201);
            out.print(gson.toJson(created));

        } catch (SQLException e) {
            res.setStatus(500);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }

    // ── PUT ──────────────────────────────────────────────────
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setJson(res);
        PrintWriter out = res.getWriter();
        long id = getId(req);

        if (id == -1) {
            res.setStatus(400);
            out.print("{\"error\":\"Provide ID in URL: /api/admins/1\"}");
            out.flush();
            return;
        }

        try {
            Admin admin = gson.fromJson(req.getReader(), Admin.class);
            boolean updated = adminDAO.updateAdmin(id, admin);

            if (!updated) {
                res.setStatus(404);
                out.print("{\"error\":\"Admin not found\"}");
            } else {
                admin.setId(id);
                out.print(gson.toJson(admin));
            }
        } catch (SQLException e) {
            res.setStatus(500);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }

    // ── DELETE ───────────────────────────────────────────────
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setJson(res);
        PrintWriter out = res.getWriter();
        long id = getId(req);

        if (id == -1) {
            res.setStatus(400);
            out.print("{\"error\":\"Provide ID in URL: /api/admins/1\"}");
            out.flush();
            return;
        }

        try {
            boolean deleted = adminDAO.deleteAdmin(id);
            if (!deleted) {
                res.setStatus(404);
                out.print("{\"error\":\"Admin not found\"}");
            } else {
                out.print("{\"message\":\"Admin deleted successfully\"}");
            }
        } catch (SQLException e) {
            res.setStatus(500);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }
}