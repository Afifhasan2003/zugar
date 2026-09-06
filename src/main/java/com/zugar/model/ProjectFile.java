package com.zugar.model;

public class ProjectFile {
    private String id;
    private String userId;
    private String filename;
    private String content;
    private long updatedAt;

    public ProjectFile() {
    }

    public ProjectFile(String id, String userId, String filename, String content, long updatedAt) {
        this.id = id;
        this.userId = userId;
        this.filename = filename;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ProjectFile{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", filename='" + filename + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
