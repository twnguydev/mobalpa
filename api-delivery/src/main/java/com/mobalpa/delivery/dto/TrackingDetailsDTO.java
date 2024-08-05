package com.mobalpa.delivery.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrackingDetailsDTO {
    private String location;
    private String status;
    private LocalDateTime timestamp;
}