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

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
public class PromotionController {

  @Autowired
  private PromotionService promotionService;

  @GetMapping("/campaigns")
  public ResponseEntity<List<Campaign>> getAllCampaigns() {
    List<Campaign> campaigns = promotionService.getAllCampaigns();
    return ResponseEntity.ok(campaigns);
  }

  @GetMapping("/campaigns/{id}")
  public ResponseEntity<Campaign> getCampaignById(@PathVariable Integer id) {
    Campaign campaign = promotionService.getCampaignById(id)
        .orElseThrow(() -> new RuntimeException("Campaign not found"));
    return ResponseEntity.ok(campaign);
  }

  @PostMapping("/campaigns")
  public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
    Campaign createdCampaign = promotionService.createCampaign(campaign);
    return ResponseEntity.status(201).body(createdCampaign);
  }

  @PutMapping("/campaigns/{id}")
  public ResponseEntity<Campaign> updateCampaign(@PathVariable Integer id, @RequestBody Campaign campaignDetails) {
    Campaign updatedCampaign = promotionService.updateCampaign(id, campaignDetails);
    return ResponseEntity.ok(updatedCampaign);
  }

  @DeleteMapping("/campaigns/{id}")
  public ResponseEntity<Void> deleteCampaign(@PathVariable Integer id) {
    promotionService.deleteCampaign(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/coupons")
  public ResponseEntity<List<CouponCode>> getAllCoupons() {
    List<CouponCode> coupons = promotionService.getAllCoupons();
    return ResponseEntity.ok(coupons);
  }

  @GetMapping("/coupons/{id}")
  public ResponseEntity<CouponCode> getCouponById(@PathVariable Integer id) {
    CouponCode coupon = promotionService.getCouponById(id)
        .orElseThrow(() -> new RuntimeException("Coupon not found"));
    return ResponseEntity.ok(coupon);
  }

  @PostMapping("/coupons")
  public ResponseEntity<?> createCoupon(@RequestBody CouponCode coupon) {
    CouponCode createdCoupon = promotionService.createCoupon(coupon);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
  }

  @PutMapping("/coupons/{id}")
  public ResponseEntity<CouponCode> updateCoupon(@PathVariable Integer id, @RequestBody CouponCode couponDetails) {
    CouponCode updatedCoupon = promotionService.updateCoupon(id, couponDetails);
    return ResponseEntity.ok(updatedCoupon);
  }

  @DeleteMapping("/coupons/{id}")
  public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
    promotionService.deleteCoupon(id);
    return ResponseEntity.noContent().build();
  }
}