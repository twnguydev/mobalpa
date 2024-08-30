package com.mobalpa.delivery.controller;

import com.mobalpa.delivery.dto.ParcelDTO;
import com.mobalpa.delivery.dto.StatusUpdateDTO;
import com.mobalpa.delivery.model.Parcel;
import com.mobalpa.delivery.service.ParcelService;
import com.mobalpa.delivery.service.DepotService;
import com.mobalpa.delivery.service.DepotService.DeliveryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/delivery")
@Tag(name = "Delivery", description = "APIs for managing deliveries")
public class DeliveryController {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private DepotService deliveryPriceService;

    @PostMapping
    @Operation(
        summary = "Create parcel",
        description = "Creates a new parcel.",
        security = @SecurityRequirement(name = "apiKey"),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Parcel created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Parcel.class),
                    examples = @ExampleObject(
                        value = "{\n" +
                                "    \"uuid\": \"f2ed0f08-084f-437a-9be0-df95a6d8298e\",\n" +
                                "    \"orderUuid\": \"12345678-1234-1234-1234-123456789abc\",\n" +
                                "    \"name\": \"John Doe\",\n" +
                                "    \"companyName\": \"Mobalpa\",\n" +
                                "    \"email\": \"john.doe@example.com\",\n" +
                                "    \"telephone\": \"+33612345678\",\n" +
                                "    \"address\": \"1, rue de la Paix\",\n" +
                                "    \"houseNumber\": \"1\",\n" +
                                "    \"address2\": \"Bâtiment B\",\n" +
                                "    \"city\": \"Marseille\",\n" +
                                "    \"country\": \"FR\",\n" +
                                "    \"postalCode\": \"13001\",\n" +
                                "    \"weight\": 13.0,\n" +
                                "    \"width\": 60.0,\n" +
                                "    \"height\": 50.0,\n" +
                                "    \"totalOrderValue\": 149.98,\n" +
                                "    \"totalOrderValueCurrency\": \"EUR\",\n" +
                                "    \"shippingMethodCheckoutName\": \"Mobalpa Centrale\",\n" +
                                "    \"senderAddress\": \"Warehouse 1\",\n" +
                                "    \"recipientAddress\": \"1 Place Jean-Jaurès 13001 Marseille\",\n" +
                                "    \"recipientPhoneNumber\": \"0676543425\",\n" +
                                "    \"recipientEmail\": \"john.doe@example.com\",\n" +
                                "    \"recipientName\": \"John Doe\",\n" +
                                "    \"quantity\": 1,\n" +
                                "    \"totalInsuredValue\": 149.98,\n" +
                                "    \"isReturn\": false,\n" +
                                "    \"status\": \"CREATED\",\n" +
                                "    \"shipment\": {\n" +
                                "        \"deliveryNumber\": \"MOB_hgEaqkwEceUs\",\n" +
                                "        \"orderUuid\": \"12345678-1234-1234-1234-123456789abc\",\n" +
                                "        \"name\": \"Mobalpa Centrale\",\n" +
                                "        \"address\": \"Warehouse 1\"\n" +
                                "    },\n" +
                                "    \"parcelItems\": [\n" +
                                "        {\n" +
                                "            \"uuid\": \"f81a52b8-9c15-4f78-9d2d-9cae6d8c9476\",\n" +
                                "            \"description\": \"Blender\",\n" +
                                "            \"productUuid\": null,\n" +
                                "            \"quantity\": 1,\n" +
                                "            \"value\": 49.99,\n" +
                                "            \"weight\": 2.5,\n" +
                                "            \"width\": 10.0,\n" +
                                "            \"height\": 20.0,\n" +
                                "            \"properties\": {\n" +
                                "                \"color\": \"Red\"\n" +
                                "            }\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"uuid\": \"cd81de5a-ae46-4b04-94dc-b69c42483945\",\n" +
                                "            \"description\": \"Microwave Oven\",\n" +
                                "            \"productUuid\": null,\n" +
                                "            \"quantity\": 1,\n" +
                                "            \"value\": 99.99,\n" +
                                "            \"weight\": 10.5,\n" +
                                "            \"width\": 50.0,\n" +
                                "            \"height\": 30.0,\n" +
                                "            \"properties\": {\n" +
                                "                \"color\": \"White\"\n" +
                                "            }\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input or error occurred",
                content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(
                        value = "Invalid parcel data provided"
                    )
                )
            )
        }
    )
    public ResponseEntity<?> createParcel(
            @RequestBody
            @Schema(description = "The parcel data to create", 
                    implementation = ParcelDTO.class)
            ParcelDTO parcelDTO
    ) {
        try {
            Parcel createdParcel = parcelService.createParcel(parcelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParcel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{deliveryNumber}")
    @Operation(
        summary = "Get parcel by delivery number",
        description = "Fetches a parcel by its delivery number.",
        security = @SecurityRequirement(name = "apiKey"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Parcel found successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Parcel.class),
                    examples = @ExampleObject(
                        value = "{\n" +
                                "    \"uuid\": \"f2ed0f08-084f-437a-9be0-df95a6d8298e\",\n" +
                                "    \"orderUuid\": \"12345678-1234-1234-1234-123456789abc\",\n" +
                                "    \"name\": \"John Doe\",\n" +
                                "    \"companyName\": \"Mobalpa\",\n" +
                                "    \"email\": \"john.doe@example.com\",\n" +
                                "    \"telephone\": \"+33612345678\",\n" +
                                "    \"address\": \"1, rue de la Paix\",\n" +
                                "    \"houseNumber\": \"1\",\n" +
                                "    \"address2\": \"Bâtiment B\",\n" +
                                "    \"city\": \"Marseille\",\n" +
                                "    \"country\": \"FR\",\n" +
                                "    \"postalCode\": \"13001\",\n" +
                                "    \"weight\": 13.0,\n" +
                                "    \"width\": 60.0,\n" +
                                "    \"height\": 50.0,\n" +
                                "    \"totalOrderValue\": 149.98,\n" +
                                "    \"totalOrderValueCurrency\": \"EUR\",\n" +
                                "    \"shippingMethodCheckoutName\": \"Mobalpa Centrale\",\n" +
                                "    \"senderAddress\": \"Warehouse 1\",\n" +
                                "    \"recipientAddress\": \"1 Place Jean-Jaurès 13001 Marseille\",\n" +
                                "    \"recipientPhoneNumber\": \"0676543425\",\n" +
                                "    \"recipientEmail\": \"john.doe@example.com\",\n" +
                                "    \"recipientName\": \"John Doe\",\n" +
                                "    \"quantity\": 1,\n" +
                                "    \"totalInsuredValue\": 149.98,\n" +
                                "    \"isReturn\": false,\n" +
                                "    \"status\": \"CREATED\",\n" +
                                "    \"shipment\": {\n" +
                                "        \"deliveryNumber\": \"MOB_hgEaqkwEceUs\",\n" +
                                "        \"orderUuid\": \"12345678-1234-1234-1234-123456789abc\",\n" +
                                "        \"name\": \"Mobalpa Centrale\",\n" +
                                "        \"address\": \"Warehouse 1\"\n" +
                                "    },\n" +
                                "    \"parcelItems\": [\n" +
                                "        {\n" +
                                "            \"uuid\": \"cd81de5a-ae46-4b04-94dc-b69c42483945\",\n" +
                                "            \"description\": \"Microwave Oven\",\n" +
                                "            \"productUuid\": null,\n" +
                                "            \"quantity\": 1,\n" +
                                "            \"value\": 99.99,\n" +
                                "            \"weight\": 10.5,\n" +
                                "            \"width\": 50.0,\n" +
                                "            \"height\": 30.0,\n" +
                                "            \"properties\": {\n" +
                                "                \"color\": \"White\"\n" +
                                "            }\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"uuid\": \"f81a52b8-9c15-4f78-9d2d-9cae6d8c9476\",\n" +
                                "            \"description\": \"Blender\",\n" +
                                "            \"productUuid\": null,\n" +
                                "            \"quantity\": 1,\n" +
                                "            \"value\": 49.99,\n" +
                                "            \"weight\": 2.5,\n" +
                                "            \"width\": 10.0,\n" +
                                "            \"height\": 20.0,\n" +
                                "            \"properties\": {\n" +
                                "                \"color\": \"Red\"\n" +
                                "            }\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Parcel not found",
                content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(
                        value = "Parcel with delivery number MOB_hgEaqkwEceUs not found"
                    )
                )
            )
        }
    )
    public ResponseEntity<Parcel> getParcelByDeliveryNumber(
            @PathVariable
            @Schema(description = "The delivery number of the parcel to fetch", example = "MOB_hgEaqkwEceUs")
            String deliveryNumber
    ) {
        Parcel parcel = parcelService.getParcelByDeliveryNumber(deliveryNumber);
        return ResponseEntity.ok(parcel);
    }

    @PutMapping("/{deliveryNumber}")
    @Operation(summary = "Update parcel status", 
               description = "Updates the status of a parcel.", 
               security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parcel status updated successfully", 
                     content = @Content(schema = @Schema(implementation = Parcel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", 
                     content = @Content),
        @ApiResponse(responseCode = "404", description = "Parcel not found", 
                     content = @Content)
    })
    public ResponseEntity<Parcel> updateParcelStatus(
            @PathVariable
            @Schema(description = "The delivery number of the parcel to update", 
                    example = "MOB_hgEaqkwEceUs")
            String deliveryNumber,
            @RequestBody
            @Schema(description = "The status update information", 
                    implementation = StatusUpdateDTO.class
    )
            StatusUpdateDTO status) {
        Parcel updatedParcel = parcelService.updateParcelStatus(deliveryNumber, status);
        return ResponseEntity.ok(updatedParcel);
    }

    @DeleteMapping("/{deliveryNumber}")
    @Operation(summary = "Delete parcel", 
               description = "Deletes a parcel.", 
               security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Parcel deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Parcel not found")
    })
    public ResponseEntity<Void> deleteParcel(
            @PathVariable 
            @Schema(description = "The delivery number of the parcel to delete", 
                    example = "MOB_hgEaqkwEceUs") 
            String deliveryNumber) {
        parcelService.deleteParcel(deliveryNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/depot")
    @Operation(summary = "Get all delivery prices", 
               description = "Fetches all delivery prices.", 
               security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of delivery prices", 
                     content = @Content(mediaType = "application/json", 
                     examples = @ExampleObject(value = 
                        "{\n" +
                        "  \"Chronopost\": {\n" +
                        "    \"price\": 5.6,\n" +
                        "    \"address\": \"Chronopost Depot, 15 Rue de l'Industrie, 75012 Paris\"\n" +
                        "  },\n" +
                        "  \"La Poste\": {\n" +
                        "    \"price\": 4.2,\n" +
                        "    \"address\": \"La Poste Depot, 25 Avenue de la République, 75011 Paris\"\n" +
                        "  },\n" +
                        "  \"Mobalpa Centrale\": {\n" +
                        "    \"price\": 0.0,\n" +
                        "    \"address\": \"Mobalpa Centrale, 10 Rue du Commerce, 69002 Lyon\"\n" +
                        "  }\n" +
                        "}"))
        )
    })
    public ResponseEntity<?> getDeliveryPrices() {
        Map<String, DeliveryInfo> depot = deliveryPriceService.getDeliveryPrices();
        return ResponseEntity.ok(depot);
    }

    @GetMapping("/depot/{method}")
    @Operation(summary = "Get delivery price by method", 
               description = "Fetches the delivery price by method.", 
               security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of delivery price", 
                     content = @Content(mediaType = "application/json", 
                     examples = @ExampleObject(value = 
                        "{\n" +
                        "  \"price\": 5.6,\n" +
                        "  \"address\": \"Chronopost Depot, 15 Rue de l'Industrie, 75012 Paris\"\n" +
                        "}"
                        ))
        )
    })
    public ResponseEntity<DeliveryInfo> getDeliveryPrice(
            @PathVariable 
            @Schema(description = "The delivery method name to fetch the price for", 
                    example = "Chronopost") 
            String method) {
        DeliveryInfo depot = deliveryPriceService.getDeliveryPrice(method);
        return ResponseEntity.ok(depot);
    }
}