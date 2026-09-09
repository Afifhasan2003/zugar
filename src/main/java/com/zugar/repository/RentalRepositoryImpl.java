package com.zugar.repository;

import com.zugar.db.DatabaseManager;
import com.zugar.model.RentalItem;
import com.zugar.model.RentalRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {

    @Override
    public void saveItem(RentalItem item) {
        String sql = "INSERT INTO rental_items (id, owner_id, title, category, description, price_per_day, deposit, condition, location, access_restriction, available) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getId());
            ps.setString(2, item.getOwnerId());
            ps.setString(3, item.getTitle());
            ps.setString(4, item.getCategory());
            ps.setString(5, item.getDescription());
            ps.setDouble(6, item.getPricePerDay());
            ps.setDouble(7, item.getDeposit());
            ps.setString(8, item.getCondition());
            ps.setString(9, item.getLocation());
            ps.setString(10, item.getAccessRestriction());
            ps.setInt(11, item.isAvailable() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving rental item", e);
        }
    }

    @Override
    public RentalItem findItemById(String id) {
        String sql = "SELECT * FROM rental_items WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding item by id", e);
        }
        return null;
    }

    @Override
    public List<RentalItem> findAllItems() {
        String sql = "SELECT * FROM rental_items ORDER BY rowid DESC";
        List<RentalItem> items = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(mapItem(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all items", e);
        }
        return items;
    }

    @Override
    public List<RentalItem> findItemsByOwner(String ownerId) {
        String sql = "SELECT * FROM rental_items WHERE owner_id = ? ORDER BY rowid DESC";
        List<RentalItem> items = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching owner items", e);
        }
        return items;
    }

    @Override
    public void updateItem(RentalItem item) {
        String sql = "UPDATE rental_items SET title=?, category=?, description=?, price_per_day=?, deposit=?, condition=?, location=?, access_restriction=?, available=? WHERE id=?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getTitle());
            ps.setString(2, item.getCategory());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getPricePerDay());
            ps.setDouble(5, item.getDeposit());
            ps.setString(6, item.getCondition());
            ps.setString(7, item.getLocation());
            ps.setString(8, item.getAccessRestriction());
            ps.setInt(9, item.isAvailable() ? 1 : 0);
            ps.setString(10, item.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating item", e);
        }
    }

    @Override
    public boolean deleteItem(String id) {
        String sql = "DELETE FROM rental_items WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting item", e);
        }
    }

    @Override
    public void saveRequest(RentalRequest request) {
        String sql = "INSERT INTO rental_requests (id, item_id, renter_id, owner_id, start_date, end_date, offered_price, counter_price, status, message) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getId());
            ps.setString(2, request.getItemId());
            ps.setString(3, request.getRenterId());
            ps.setString(4, request.getOwnerId());
            ps.setString(5, request.getStartDate());
            ps.setString(6, request.getEndDate());
            ps.setDouble(7, request.getOfferedPrice());
            if (request.getCounterPrice() == null) ps.setNull(8, java.sql.Types.REAL);
            else ps.setDouble(8, request.getCounterPrice());
            ps.setString(9, request.getStatus());
            ps.setString(10, request.getMessage());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving request", e);
        }
    }

    @Override
    public RentalRequest findRequestById(String id) {
        String sql = "SELECT * FROM rental_requests WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRequest(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding request by id", e);
        }
        return null;
    }

    @Override
    public List<RentalRequest> findRequestsByOwner(String ownerId) {
        String sql = "SELECT * FROM rental_requests WHERE owner_id = ? ORDER BY rowid DESC";
        List<RentalRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRequest(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching owner requests", e);
        }
        return list;
    }

    @Override
    public List<RentalRequest> findRequestsByRenter(String renterId) {
        String sql = "SELECT * FROM rental_requests WHERE renter_id = ? ORDER BY rowid DESC";
        List<RentalRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, renterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRequest(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching renter requests", e);
        }
        return list;
    }

    @Override
    public void updateRequestStatus(String id, String status) {
        String sql = "UPDATE rental_requests SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating request status", e);
        }
    }

    @Override
    public void updateCounterOffer(String id, double counterPrice) {
        String sql = "UPDATE rental_requests SET counter_price = ?, status = 'NEGOTIATING' WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, counterPrice);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving counter offer", e);
        }
    }

    private RentalItem mapItem(ResultSet rs) throws SQLException {
        return new RentalItem(
                rs.getString("id"),
                rs.getString("owner_id"),
                rs.getString("title"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getDouble("price_per_day"),
                rs.getDouble("deposit"),
                rs.getString("condition"),
                rs.getString("location"),
                rs.getString("access_restriction"),
                rs.getInt("available") == 1
        );
    }

    private RentalRequest mapRequest(ResultSet rs) throws SQLException {
        return new RentalRequest(
                rs.getString("id"),
                rs.getString("item_id"),
                rs.getString("renter_id"),
                rs.getString("owner_id"),
                rs.getString("start_date"),
                rs.getString("end_date"),
                rs.getDouble("offered_price"),
                rs.getObject("counter_price", Double.class),
                rs.getString("status"),
                rs.getString("message")
        );
    }
}
