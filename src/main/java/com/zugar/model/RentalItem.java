package com.zugar.model;

public class RentalItem {
    private String id;
    private String ownerId;
    private String title;
    private String category;
    private String description;
    private double pricePerDay;
    private double deposit;
    private String condition;
    private String location;
    private String accessRestriction;
    private boolean available;

    public RentalItem() {
    }

    public RentalItem(String id, String ownerId, String title, String category, String description, double pricePerDay, double deposit, String condition, String location, String accessRestriction, boolean available) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.category = category;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.deposit = deposit;
        this.condition = condition;
        this.location = location;
        this.accessRestriction = accessRestriction;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAccessRestriction() {
        return accessRestriction;
    }

    public void setAccessRestriction(String accessRestriction) {
        this.accessRestriction = accessRestriction;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
