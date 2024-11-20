package com.mobalpa.api.repository;

import com.mobalpa.api.model.Campaign;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
  List<Campaign> findByTargetUuid(UUID productUuid);
}