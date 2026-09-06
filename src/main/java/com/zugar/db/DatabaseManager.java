package com.zugar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static String dbUrl = "jdbc:sqlite:zugar.db";

    public static void setDbUrl(String newDbUrl) {
        dbUrl = newDbUrl;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(dbUrl);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id TEXT PRIMARY KEY, "
                    + "username TEXT UNIQUE NOT NULL, "
                    + "password_hash TEXT NOT NULL, "
                    + "created_at INTEGER DEFAULT (UNIXEPOCH())"
                    + ");";

            String createProjectFilesTable = "CREATE TABLE IF NOT EXISTS project_files ("
                    + "id TEXT PRIMARY KEY, "
                    + "user_id TEXT NOT NULL, "
                    + "filename TEXT NOT NULL, "
                    + "content TEXT NOT NULL, "
                    + "updated_at INTEGER DEFAULT (UNIXEPOCH()), "
                    + "FOREIGN KEY(user_id) REFERENCES users(id)"
                    + ");";

            String createRentalItemsTable = "CREATE TABLE IF NOT EXISTS rental_items ("
                    + "id TEXT PRIMARY KEY, "
                    + "owner_id TEXT NOT NULL, "
                    + "title TEXT NOT NULL, "
                    + "category TEXT NOT NULL, "
                    + "description TEXT, "
                    + "price_per_day REAL NOT NULL, "
                    + "deposit REAL NOT NULL, "
                    + "condition TEXT, "
                    + "location TEXT, "
                    + "access_restriction TEXT, "
                    + "available INTEGER DEFAULT 1, "
                    + "FOREIGN KEY(owner_id) REFERENCES users(id)"
                    + ");";

            String createRentalRequestsTable = "CREATE TABLE IF NOT EXISTS rental_requests ("
                    + "id TEXT PRIMARY KEY, "
                    + "item_id TEXT NOT NULL, "
                    + "renter_id TEXT NOT NULL, "
                    + "owner_id TEXT NOT NULL, "
                    + "start_date TEXT NOT NULL, "
                    + "end_date TEXT NOT NULL, "
                    + "offered_price REAL NOT NULL, "
                    + "status TEXT DEFAULT 'PENDING', "
                    + "message TEXT, "
                    + "FOREIGN KEY(item_id) REFERENCES rental_items(id), "
                    + "FOREIGN KEY(renter_id) REFERENCES users(id), "
                    + "FOREIGN KEY(owner_id) REFERENCES users(id)"
                    + ");";

            stmt.execute(createUsersTable);
            stmt.execute(createProjectFilesTable);
            stmt.execute(createRentalItemsTable);
            stmt.execute(createRentalRequestsTable);
        }
    }
}
