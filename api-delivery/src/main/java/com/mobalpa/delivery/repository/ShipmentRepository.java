package com.mobalpa.delivery.repository;

import com.mobalpa.delivery.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
  Optional<Shipment> findByDeliveryNumber(String deliveryNumber);
  void deleteByDeliveryNumber(String deliveryNumber);
}