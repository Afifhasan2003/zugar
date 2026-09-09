package com.zugar.command;

import com.zugar.model.RentalRequest;
import com.zugar.repository.RentalRepository;

public class CounterOfferCommand implements Command {
    private final RentalRepository rentalRepository;
    private final RentalRequest req;
    private final double counterPrice;

    public CounterOfferCommand(RentalRepository rentalRepository, RentalRequest req, double counterPrice) {
        this.rentalRepository = rentalRepository;
        this.req = req;
        this.counterPrice = counterPrice;
    }

    @Override
    public void execute() {
        rentalRepository.updateCounterOffer(req.getId(), counterPrice);
    }
}
