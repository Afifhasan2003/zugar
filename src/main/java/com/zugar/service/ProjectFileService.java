package com.zugar.service;

import com.zugar.model.ProjectFile;
import com.zugar.repository.ProjectFileRepository;

import java.util.List;
import java.util.UUID;

public class ProjectFileService {
    private final ProjectFileRepository projectFileRepository;

    public ProjectFileService(ProjectFileRepository projectFileRepository) {
        this.projectFileRepository = projectFileRepository;
    }

    public ProjectFile createProjectFile(String userId, String filename, String content) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        String id = UUID.randomUUID().toString();
        long now = System.currentTimeMillis() / 1000L;
        ProjectFile file = new ProjectFile(id, userId, filename.trim(), content != null ? content : "", now);

        projectFileRepository.save(file);
        return file;
    }

    public ProjectFile getFileById(String id) {
        return projectFileRepository.findById(id);
    }

    public List<ProjectFile> getFilesForUser(String userId) {
        return projectFileRepository.findByUserId(userId);
    }

    public List<ProjectFile> getLatestFilesForUser(String userId, int limit) {
        return projectFileRepository.findLatestByUserId(userId, limit);
    }

    public ProjectFile updateProjectFile(String id, String filename, String content) {
        ProjectFile existing = projectFileRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Project file not found with id: " + id);
        }

        if (filename != null && !filename.trim().isEmpty()) {
            existing.setFilename(filename.trim());
        }
        if (content != null) {
            existing.setContent(content);
        }

        projectFileRepository.update(existing);
        return existing;
    }

    public boolean deleteProjectFile(String id) {
        return projectFileRepository.delete(id);
    }
}
