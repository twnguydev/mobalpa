package com.mobalpa.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class WishlistDTO {
    private Integer id;
    private UUID userUuid;
    private List<CatalogueProductDTO> items;
}
