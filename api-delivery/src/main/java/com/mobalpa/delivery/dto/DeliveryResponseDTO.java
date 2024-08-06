package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryResponseDTO {
    private Response response;
    private Result result;

    @Data
    public static class Response {
        private String status;
        private String errorMessage;
    }

    @Data
    public static class Result {
        private String effectiveDate;
        private String offerCode;
        private int majorFunctionalVersion;
        private String customerNumber;
        private String customerMarketingTypeCode;
        private String customerEstablishZoneCode;
        private Criteria criteria;
        private List<Case> cases;
        private SimulationSummary simulationSummary;

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
            private List<Accounting> accounting;

            @Data
            public static class CaseCriteria {
                private String envelope_itemQuantity;
                private String declaredWeight;
                private String weightCategory;
            }

            @Data
            public static class Accounting {
                private double amountWithoutTax;
                private double amountWithTax;
                private double amountTax;
                private List<Charge> chargeList;

                @Data
                public static class Charge {
                    private String accountingCode;
                    private String accountingLevel;
                    private String taxCode;
                    private String taxRate;
                    private int quantity;
                    private String chargeDescription;
                    private double unitAmountWithoutTax;
                    private double unitAmountWithTax;
                    private double unitAmountTax;
                    private double amountWithoutTax;
                    private double amountWithTax;
                    private double amountTax;
                    private String chargeType;
                    private String type;
                    private boolean configuratorDisplay;
                    private boolean configuratorIfZeroDisplay;
                    private boolean contractQuoteDisplay;
                    private String contractPriceSimulationDisplay;
                }
            }
        }

        @Data
        public static class SimulationSummary {
            private double totalAmountWithoutTaxes;
            private double totalAmountWithTaxes;
            private double totalAmountTaxes;
        }
    }
}
