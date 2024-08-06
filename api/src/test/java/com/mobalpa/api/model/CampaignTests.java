package com.mobalpa.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CampaignTests {

    @Test
    public void testCampaignCreation() {
        Campaign campaign = new Campaign();
        campaign.setId(1);
        campaign.setName("Summer Sale");
        campaign.setType(Campaign.Type.PRODUCT);
        campaign.setDiscountRate(15.0);
        campaign.setTargetUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        LocalDateTime now = LocalDateTime.now();
        campaign.setDateStart(now);
        campaign.setDateEnd(now.plusDays(10));
        campaign.setCreatedAt(now);

        assertNotNull(campaign);
        assertEquals(1, campaign.getId());
        assertEquals("Summer Sale", campaign.getName());
        assertEquals(Campaign.Type.PRODUCT, campaign.getType());
        assertEquals(15.0, campaign.getDiscountRate());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), campaign.getTargetUuid());
        assertEquals(now, campaign.getDateStart());
        assertEquals(now.plusDays(10), campaign.getDateEnd());
        assertEquals(now, campaign.getCreatedAt());
    }
}