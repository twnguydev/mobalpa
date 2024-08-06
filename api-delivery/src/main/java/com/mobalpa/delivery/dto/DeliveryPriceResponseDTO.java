package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryPriceResponseDTO {
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
        private String contract;
        private String customerNumber;
        private String customerMarketingTypeCode;
        private String customerEstablishZoneCode;
        private Criteria criteria;
        private List<Case> cases;
        private Summary simulationSummary;
        private Summary billableSummary;

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
            private List<Accounting> accounting;

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
                    private double taxRate;
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
                    private boolean configuratorHidden;
                    private boolean configuratorIfZeroDisplay;
                    private boolean contractQuoteDisplay;
                    private String contractPriceSimulationDisplay;
                }
            }
        }

        @Data
        public static class Summary {
            private double totalAmountWithoutTaxes;
            private double totalAmountWithTaxes;
            private double totalAmountTaxes;
        }
    }
}