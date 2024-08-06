package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryPriceRequestDTO {
    private String effectiveDate;
    private String offerCode;
    private int majorFunctionalVersion;
    private String contract;
    private String customerNumber;
    private String customerMarketingTypeCode;
    private String customerEstablishZoneCode;
    private Criteria criteria;
    private List<Case> cases;

    @Data
    public static class Criteria {
        private String additionalProp1;
        private String additionalProp2;
        private String additionalProp3;
    }

    @Data
    public static class Case {
        private String productCode;
        private String serviceCode;
        private Criteria criteria;
    }
}
