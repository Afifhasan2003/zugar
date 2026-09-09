package com.zugar.state;

public class NegotiatingState implements RentalRequestState {
    public String getDisplayName() { return "Negotiating"; }
    public boolean canAccept() { return true; }
    public boolean canReject() { return true; }
    public boolean canCounter() { return true; }
    public RentalRequestState onAccept() { return new AcceptedState(); }
    public RentalRequestState onReject() { return new RejectedState(); }
    public RentalRequestState onCounter() { return this; }
    public String toStatusString() { return "NEGOTIATING"; }
}