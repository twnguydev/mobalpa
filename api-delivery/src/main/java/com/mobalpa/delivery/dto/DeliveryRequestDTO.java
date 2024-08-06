package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryRequestDTO {
    private String effectiveDate;
    private String offerCode;
    private int majorFunctionalVersion;
    private String contractNumber;
    private String customerNumber;
    private String customerMarketingTypeCode;
    private String customerEstablishZoneCode;
    private Criteria criteria;
    private List<Case> cases;

    @Data
    public static class Criteria {
        private String DT_APPLI_TAR;
        private String ZON_DPAR;
        private String ZON_DESTN;
        private String contract_requestedType;
        private String contract_complexTypeManagement;
        private String ZON_INFO;
        private String offerType;
        private String offerSubType;
    }

    @Data
    public static class Case {
        private String productCode;
        private String serviceCode;
        private CaseCriteria criteria;

        @Data
        public static class CaseCriteria {
            private String envelope_itemQuantity;
            private String declaredWeight;
            private String weightCategory;
        }
    }
}
