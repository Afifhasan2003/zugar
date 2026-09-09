package com.zugar.db;

import com.zugar.service.PasswordHasher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
                    + "counter_price REAL, "
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
            addCounterPriceColumn(stmt);

            seedDummyData(stmt);
        }
    }

    private static void addCounterPriceColumn(Statement stmt) throws SQLException {
        try {
            stmt.execute("ALTER TABLE rental_requests ADD COLUMN counter_price REAL");
        } catch (SQLException e) {
            if (!e.getMessage().toLowerCase().contains("duplicate column name")) throw e;
        }
    }

    private static void seedDummyData(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users;")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }

        String pwdHash = PasswordHasher.hashPassword("password123");
        stmt.execute("INSERT INTO users (id, username, password_hash) VALUES ('user-alice-1', 'alice', '" + pwdHash + "');");
        stmt.execute("INSERT INTO users (id, username, password_hash) VALUES ('user-bob-2', 'bob', '" + pwdHash + "');");
        stmt.execute("INSERT INTO users (id, username, password_hash) VALUES ('user-charlie-3', 'charlie', '" + pwdHash + "');");

        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-1', 'user-alice-1', 'Sony A7 III Camera Body', 'Photography', 'Professional full-frame mirrorless camera in great condition.', 1200.0, 5000.0, 'Like New', 'Dhaka University Area', 'Anyone', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-2', 'user-bob-2', 'MacBook Pro M2 16-inch', 'Electronics', '32GB RAM, 1TB SSD for heavy video editing & coding.', 2500.0, 15000.0, 'Excellent', 'Dhanmondi', 'University Members Only', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-3', 'user-charlie-3', 'Rode Wireless GO II Microphone', 'Audio/Visual', 'Dual channel wireless microphone system with windshields.', 800.0, 3000.0, 'Good', 'Gulshan', 'Anyone', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-4', 'user-alice-1', 'Trek Mountain Bicycle', 'Sports', '21-speed mountain bike with front suspension.', 500.0, 2000.0, 'Good', 'Uttara', 'Anyone', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-5', 'user-bob-2', 'DJI Mini 3 Pro Drone', 'Photography', '4K 60fps lightweight drone with 3 batteries and fly more combo.', 1500.0, 8000.0, 'Like New', 'Mirpur', 'Anyone', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-6', 'user-alice-1', 'Epson 4K Home Projector', 'Audio/Visual', 'High brightness 3000 lumens 4K projector for events & movies.', 1800.0, 6000.0, 'Excellent', 'Banani', 'Housing Society Only', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-7', 'user-charlie-3', 'Yamaha Acoustic Guitar', 'Other', 'Warm sound acoustic guitar with hard carrying case.', 400.0, 1500.0, 'Good', 'Mohakhali', 'Anyone', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-8', 'user-bob-2', 'Honda Livo 110cc Motorbike', 'Vehicles', 'Fuel efficient city motorbike, includes 2 helmets.', 900.0, 5000.0, 'Good', 'Farmgate', 'Verified Members Only', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-9', 'user-alice-1', 'Canon EF 70-200mm f/2.8L Lens', 'Photography', 'Fast telephoto zoom lens with image stabilization.', 1000.0, 4000.0, 'Excellent', 'Dhaka University Area', 'Anyone', 1);");
        stmt.execute("INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES ('item-10', 'user-charlie-3', '4-Person Camping Tent & Kit', 'Sports', 'Waterproof tent, sleeping bags, and portable gas stove.', 600.0, 2000.0, 'Good', 'Sylhet City', 'Anyone', 1);");
    }
}
