package com.zugar;

import com.zugar.db.DatabaseManager;
import com.zugar.model.ProjectFile;
import com.zugar.model.User;
import com.zugar.repository.ProjectFileRepository;
import com.zugar.repository.ProjectFileRepositoryImpl;
import com.zugar.repository.UserRepository;
import com.zugar.repository.UserRepositoryImpl;
import com.zugar.service.AuthService;
import com.zugar.service.JwtService;
import com.zugar.service.ProjectFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ZugarAppTest {

    @TempDir
    Path tempDir;

    private UserRepository userRepository;
    private ProjectFileRepository projectFileRepository;
    private JwtService jwtService;
    private AuthService authService;
    private ProjectFileService projectFileService;

    @BeforeEach
    public void setUp() throws SQLException {
        File dbFile = tempDir.resolve("test_zugar.db").toFile();
        DatabaseManager.getInstance().setDbUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        DatabaseManager.getInstance().initializeDatabase();

        userRepository = new UserRepositoryImpl();
        projectFileRepository = new ProjectFileRepositoryImpl();
        jwtService = new JwtService();
        authService = new AuthService(userRepository, jwtService);
        projectFileService = new ProjectFileService(projectFileRepository);
    }

    @Test
    public void testDatabaseTablesCreated() throws SQLException {
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'")) {

            boolean usersTableFound = false;
            boolean projectFilesTableFound = false;

            while (rs.next()) {
                String tableName = rs.getString("name");
                if ("users".equalsIgnoreCase(tableName)) {
                    usersTableFound = true;
                } else if ("project_files".equalsIgnoreCase(tableName)) {
                    projectFilesTableFound = true;
                }
            }

            assertTrue(usersTableFound, "users table should be created");
            assertTrue(projectFilesTableFound, "project_files table should be created");
        }
    }

    @Test
    public void testUserRegistrationAndLogin() {
        User user = authService.register("afif", "secret123");
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("afif", user.getUsername());
        assertTrue(user.getCreatedAt() > 0);

        String token = authService.login("afif", "secret123");
        assertNotNull(token);
        assertFalse(token.isEmpty());

        User validatedUser = authService.validateAndGetUser(token);
        assertNotNull(validatedUser);
        assertEquals(user.getId(), validatedUser.getId());
        assertEquals("afif", validatedUser.getUsername());
    }

    @Test
    public void testInvalidLoginThrowsException() {
        authService.register("testuser", "password123");
        try {
            authService.login("testuser", "wrongpassword");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid username or password"));
        }
    }

    @Test
    public void testProjectFileRepositoryAndService() {
        User user = authService.register("developer", "pass");
        ProjectFile file1 = projectFileService.createProjectFile(user.getId(), "main.zg", "print('hello')");
        ProjectFile file2 = projectFileService.createProjectFile(user.getId(), "utils.zg", "fn help() {}");

        assertNotNull(file1.getId());
        assertNotNull(file2.getId());

        List<ProjectFile> files = projectFileService.getFilesForUser(user.getId());
        assertEquals(2, files.size());

        List<ProjectFile> latestFiles = projectFileService.getLatestFilesForUser(user.getId(), 10);
        assertEquals(2, latestFiles.size());

        ProjectFile updated = projectFileService.updateProjectFile(file1.getId(), "main_updated.zg", "print('hello world')");
        assertEquals("main_updated.zg", updated.getFilename());
        assertEquals("print('hello world')", updated.getContent());

        boolean deleted = projectFileService.deleteProjectFile(file2.getId());
        assertTrue(deleted);

        List<ProjectFile> remainingFiles = projectFileService.getFilesForUser(user.getId());
        assertEquals(1, remainingFiles.size());
        assertEquals(file1.getId(), remainingFiles.get(0).getId());
    }

    @Test
    public void testJwtTokenParsingAndValidation() {
        String token = jwtService.generateToken("user-123", "alice");
        assertTrue(jwtService.validateToken(token));
        assertEquals("user-123", jwtService.extractUserId(token));
        assertEquals("alice", jwtService.extractUsername(token));
    }
}
