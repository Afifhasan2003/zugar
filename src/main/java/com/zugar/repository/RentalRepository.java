package com.zugar.repository;

import com.zugar.model.RentalItem;
import com.zugar.model.RentalRequest;
import java.util.List;

public interface RentalRepository {
    void saveItem(RentalItem item);
    RentalItem findItemById(String id);
    List<RentalItem> findAllItems();
    List<RentalItem> findItemsByOwner(String ownerId);
    void updateItem(RentalItem item);
    boolean deleteItem(String id);

    void saveRequest(RentalRequest request);
    RentalRequest findRequestById(String id);
    List<RentalRequest> findRequestsByOwner(String ownerId);
    List<RentalRequest> findRequestsByRenter(String renterId);
    void updateRequestStatus(String id, String status);
    void updateCounterOffer(String id, double counterPrice);
}
