package com.mobalpa.api.dto;

import lombok.Data;
import java.util.List;

import com.mobalpa.api.dto.catalogue.CategoryDTO;
import com.mobalpa.api.model.Campaign;

@Data
public class CategoryWithCampaignDTO {
    private CategoryDTO category;
    private List<Campaign> campaigns;
}
