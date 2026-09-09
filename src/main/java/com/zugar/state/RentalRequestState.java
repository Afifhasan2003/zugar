package com.zugar.state;

public interface RentalRequestState {
    String getDisplayName();
    boolean canAccept();
    boolean canReject();
    boolean canCounter();
    RentalRequestState onAccept();
    RentalRequestState onReject();
    RentalRequestState onCounter();
    String toStatusString();
}