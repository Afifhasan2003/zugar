package com.zugar.state;

public final class RentalRequestStateFactory {
    private RentalRequestStateFactory() { }

    public static RentalRequestState fromStatus(String status) {
        if ("ACCEPTED".equalsIgnoreCase(status)) return new AcceptedState();
        if ("REJECTED".equalsIgnoreCase(status)) return new RejectedState();
        if ("NEGOTIATING".equalsIgnoreCase(status)) return new NegotiatingState();
        return new PendingState();
    }
}