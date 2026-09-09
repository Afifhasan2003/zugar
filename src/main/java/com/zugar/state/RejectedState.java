package com.zugar.state;

public class RejectedState implements RentalRequestState {
    public String getDisplayName() { return "Rejected"; }
    public boolean canAccept() { return false; }
    public boolean canReject() { return false; }
    public boolean canCounter() { return false; }
    public RentalRequestState onAccept() { return this; }
    public RentalRequestState onReject() { return this; }
    public RentalRequestState onCounter() { return this; }
    public String toStatusString() { return "REJECTED"; }
}