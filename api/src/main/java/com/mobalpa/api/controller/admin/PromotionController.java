package com.mobalpa.api.controller.admin;

import com.mobalpa.api.model.Campaign;
import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
@Tag(name = "Promotion management for admin users", description = "APIs for managing promotions for admin users")
public class PromotionController {

  @Autowired
  private PromotionService promotionService;

  @GetMapping("/campaigns")
  @Operation(summary = "Get all campaigns", description = "Fetches all campaigns.")
  public ResponseEntity<List<Campaign>> getAllCampaigns() {
    List<Campaign> campaigns = promotionService.getAllCampaigns();
    return ResponseEntity.ok(campaigns);
  }

  @GetMapping("/campaigns/{id}")
  @Operation(summary = "Get campaign by ID", description = "Fetches a campaign by its unique identifier.")
  public ResponseEntity<Campaign> getCampaignById(@PathVariable Integer id) {
    Campaign campaign = promotionService.getCampaignById(id)
        .orElseThrow(() -> new RuntimeException("Campaign not found"));
    return ResponseEntity.ok(campaign);
  }

  @PostMapping("/campaigns")
  @Operation(summary = "Create campaign", description = "Creates a new campaign.")
  public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
    Campaign createdCampaign = promotionService.createCampaign(campaign);
    return ResponseEntity.status(201).body(createdCampaign);
  }

  @PutMapping("/campaigns/{id}")
  @Operation(summary = "Update campaign", description = "Updates a campaign.")
  public ResponseEntity<Campaign> updateCampaign(@PathVariable Integer id, @RequestBody Campaign campaignDetails) {
    Campaign updatedCampaign = promotionService.updateCampaign(id, campaignDetails);
    return ResponseEntity.ok(updatedCampaign);
  }

  @DeleteMapping("/campaigns/{id}")
  @Operation(summary = "Delete campaign", description = "Deletes a campaign.")
  public ResponseEntity<Void> deleteCampaign(@PathVariable Integer id) {
    promotionService.deleteCampaign(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/coupons")
  @Operation(summary = "Get all coupons", description = "Fetches all coupons.")
  public ResponseEntity<List<CouponCode>> getAllCoupons() {
    List<CouponCode> coupons = promotionService.getAllCoupons();
    return ResponseEntity.ok(coupons);
  }

  @GetMapping("/coupons/{id}")
  @Operation(summary = "Get coupon by ID", description = "Fetches a coupon by its ID.")
  public ResponseEntity<CouponCode> getCouponById(@PathVariable Integer id) {
    CouponCode coupon = promotionService.getCouponById(id)
        .orElseThrow(() -> new RuntimeException("Coupon not found"));
    return ResponseEntity.ok(coupon);
  }

  @PostMapping("/coupons")
  @Operation(summary = "Create coupon", description = "Creates a new coupon.")
  public ResponseEntity<?> createCoupon(@RequestBody CouponCode coupon) {
    CouponCode createdCoupon = promotionService.createCoupon(coupon);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
  }

  @PutMapping("/coupons/{id}")
  @Operation(summary = "Update coupon", description = "Updates a coupon.")
  public ResponseEntity<CouponCode> updateCoupon(@PathVariable Integer id, @RequestBody CouponCode couponDetails) {
    CouponCode updatedCoupon = promotionService.updateCoupon(id, couponDetails);
    return ResponseEntity.ok(updatedCoupon);
  }

  @DeleteMapping("/coupons/{id}")
  @Operation(summary = "Delete coupon", description = "Deletes a coupon.")
  public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
    promotionService.deleteCoupon(id);
    return ResponseEntity.noContent().build();
  }
}