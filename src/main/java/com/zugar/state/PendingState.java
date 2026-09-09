package com.zugar.state;

public class PendingState implements RentalRequestState {
    public String getDisplayName() { return "Pending"; }
    public boolean canAccept() { return true; }
    public boolean canReject() { return true; }
    public boolean canCounter() { return true; }
    public RentalRequestState onAccept() { return new AcceptedState(); }
    public RentalRequestState onReject() { return new RejectedState(); }
    public RentalRequestState onCounter() { return new NegotiatingState(); }
    public String toStatusString() { return "PENDING"; }
}