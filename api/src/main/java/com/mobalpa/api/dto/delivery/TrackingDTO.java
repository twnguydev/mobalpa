package com.mobalpa.api.dto.delivery;

import lombok.Data;
import java.util.List;

@Data
public class TrackingDTO {
    private String deliveryId;
    private String orderId;
    private String deliveryMethod;
    private String status;
    private TrackingDetailsDTO trackingDetails;

    @Data
    public static class TrackingDetailsDTO {
        private String currentLocation;
        private String estimatedDeliveryDate;
        private List<HistoryDTO> history;

        @Data
        public static class HistoryDTO {
            private String location;
            private String status;
            private String timestamp;
        }
    }
}
