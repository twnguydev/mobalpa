package com.mobalpa.api.dto;

import lombok.Data;

import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.model.Campaign;

import java.util.List;

@Data
public class ProductWithCampaignDTO {
    private ProductDTO product;
    private List<Campaign> campaigns;
}