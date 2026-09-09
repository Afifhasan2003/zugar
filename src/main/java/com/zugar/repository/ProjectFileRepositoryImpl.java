package com.zugar.repository;

import com.zugar.db.DatabaseManager;
import com.zugar.model.ProjectFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectFileRepositoryImpl implements ProjectFileRepository {

    @Override
    public void save(ProjectFile file) {
        String sql = "INSERT INTO project_files (id, user_id, filename, content, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, file.getId());
            pstmt.setString(2, file.getUserId());
            pstmt.setString(3, file.getFilename());
            pstmt.setString(4, file.getContent());
            long now = file.getUpdatedAt() > 0 ? file.getUpdatedAt() : System.currentTimeMillis() / 1000L;
            pstmt.setLong(5, now);

            pstmt.executeUpdate();
            file.setUpdatedAt(now);
        } catch (SQLException e) {
            throw new RuntimeException("Error saving project file: " + e.getMessage(), e);
        }
    }

    @Override
    public ProjectFile findById(String id) {
        String sql = "SELECT id, user_id, filename, content, updated_at FROM project_files WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProjectFile(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding project file by id: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<ProjectFile> findByUserId(String userId) {
        String sql = "SELECT id, user_id, filename, content, updated_at FROM project_files WHERE user_id = ? ORDER BY updated_at DESC";
        List<ProjectFile> files = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    files.add(mapRowToProjectFile(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding project files by user_id: " + e.getMessage(), e);
        }
        return files;
    }

    @Override
    public List<ProjectFile> findLatestByUserId(String userId, int limit) {
        String sql = "SELECT id, user_id, filename, content, updated_at FROM project_files WHERE user_id = ? ORDER BY updated_at DESC LIMIT ?";
        List<ProjectFile> files = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setInt(2, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    files.add(mapRowToProjectFile(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding latest project files: " + e.getMessage(), e);
        }
        return files;
    }

    @Override
    public void update(ProjectFile file) {
        String sql = "UPDATE project_files SET filename = ?, content = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            long now = System.currentTimeMillis() / 1000L;
            pstmt.setString(1, file.getFilename());
            pstmt.setString(2, file.getContent());
            pstmt.setLong(3, now);
            pstmt.setString(4, file.getId());

            pstmt.executeUpdate();
            file.setUpdatedAt(now);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating project file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM project_files WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting project file: " + e.getMessage(), e);
        }
    }

    private ProjectFile mapRowToProjectFile(ResultSet rs) throws SQLException {
        return new ProjectFile(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("filename"),
                rs.getString("content"),
                rs.getLong("updated_at")
        );
    }
}
