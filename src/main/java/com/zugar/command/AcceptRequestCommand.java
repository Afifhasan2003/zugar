package com.zugar.command;

import com.zugar.model.RentalRequest;
import com.zugar.repository.RentalRepository;
import com.zugar.state.RentalRequestState;
import com.zugar.state.RentalRequestStateFactory;

public class AcceptRequestCommand implements Command {
    private final RentalRepository rentalRepository;
    private final RentalRequest req;

    public AcceptRequestCommand(RentalRepository rentalRepository, RentalRequest req) {
        this.rentalRepository = rentalRepository;
        this.req = req;
    }

    @Override
    public void execute() {
        RentalRequestState state = RentalRequestStateFactory.fromStatus(req.getStatus());
        rentalRepository.updateRequestStatus(req.getId(), state.onAccept().toStatusString());
    }
}
