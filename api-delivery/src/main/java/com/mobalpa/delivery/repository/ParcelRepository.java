package com.mobalpa.delivery.repository;

import com.mobalpa.delivery.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, UUID> {
}
