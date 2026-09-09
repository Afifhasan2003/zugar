package com.zugar.state;

public class AcceptedState implements RentalRequestState {
    public String getDisplayName() { return "Accepted"; }
    public boolean canAccept() { return false; }
    public boolean canReject() { return false; }
    public boolean canCounter() { return false; }
    public RentalRequestState onAccept() { return this; }
    public RentalRequestState onReject() { return this; }
    public RentalRequestState onCounter() { return this; }
    public String toStatusString() { return "ACCEPTED"; }
}