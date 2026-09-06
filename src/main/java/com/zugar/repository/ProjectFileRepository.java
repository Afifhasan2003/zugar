package com.zugar.repository;

import com.zugar.model.ProjectFile;

import java.util.List;

public interface ProjectFileRepository {
    void save(ProjectFile file);
    ProjectFile findById(String id);
    List<ProjectFile> findByUserId(String userId);
    List<ProjectFile> findLatestByUserId(String userId, int limit);
    void update(ProjectFile file);
    boolean delete(String id);
}
