package com.mobalpa.delivery.repository;

import com.mobalpa.delivery.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, UUID> {
  Optional<Parcel> findByUuid(UUID uuid);
  Optional<Parcel> findByShipmentDeliveryNumber(String shipmentDeliveryNumber);
}
