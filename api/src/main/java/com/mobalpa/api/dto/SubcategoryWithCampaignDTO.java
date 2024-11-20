package com.mobalpa.api.dto;

import lombok.Data;
import java.util.List;

import com.mobalpa.api.dto.catalogue.SubcategoryDTO;
import com.mobalpa.api.model.Campaign;

@Data
public class SubcategoryWithCampaignDTO {
    private SubcategoryDTO subcategory;
    private List<Campaign> campaigns;
}
