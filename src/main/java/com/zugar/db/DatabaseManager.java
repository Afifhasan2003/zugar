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

            stmt.execute(createUsersTable);
            stmt.execute(createProjectFilesTable);
        }
    }
}
