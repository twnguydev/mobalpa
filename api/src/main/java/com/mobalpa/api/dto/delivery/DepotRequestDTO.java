package com.mobalpa.api.dto.delivery;

import java.util.Map;
import lombok.Data;

@Data
public class DepotRequestDTO {
  private Map<String, DepotRequest> depots;
}
