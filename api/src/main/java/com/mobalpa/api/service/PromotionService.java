package com.mobalpa.api.service;

import com.mobalpa.api.dto.catalogue.CategoryDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.catalogue.SubcategoryDTO;
import com.mobalpa.api.model.Campaign;
import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.UserCoupon;
import com.mobalpa.api.repository.CampaignRepository;
import com.mobalpa.api.repository.CouponCodeRepository;
import com.mobalpa.api.repository.UserCouponRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CouponCodeRepository couponCodeRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private CatalogueService catalogueService;

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Optional<Campaign> getCampaignById(Integer id) {
        return campaignRepository.findById(id);
    }

    public Campaign createCampaign(Campaign campaign) {
        if (campaign.getType().toString().equals("PRODUCT")) {
            Optional<ProductDTO> product = Optional.of(catalogueService.getProductById(campaign.getTargetUuid()));
            if (product.isPresent()) {
                return campaignRepository.save(campaign);
            } else {
                throw new RuntimeException("Product not found");
            }
        } else if (campaign.getType().toString().equals("CATEGORY")) {
            Optional<CategoryDTO> category = Optional.of(catalogueService.getCategoryById(campaign.getTargetUuid()));
            if (category.isPresent()) {
                return campaignRepository.save(campaign);
            } else {
                throw new RuntimeException("Category not found");
            }
        } else if (campaign.getType().toString().equals("SUBCATEGORY")) {
            Optional<SubcategoryDTO> subcategory = Optional.of(catalogueService.getSubcategoryById(campaign.getTargetUuid()));
            if (subcategory.isPresent()) {
                return campaignRepository.save(campaign);
            } else {
                throw new RuntimeException("Subcategory not found");
            }
        } else {
            throw new RuntimeException("Invalid campaign type");
        }
    }

    public Campaign updateCampaign(Integer id, Campaign campaignDetails) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        if (campaignDetails.getType() != null)
            campaign.setType(campaignDetails.getType());
        if (campaignDetails.getName() != null)
            campaign.setName(campaignDetails.getName());
        if (campaignDetails.getDiscountRate() != null)
            campaign.setDiscountRate(campaignDetails.getDiscountRate());
        if (campaignDetails.getTargetUuid() != null) {
            if (campaignDetails.getType().equals(Campaign.Type.PRODUCT)) {
                Optional<ProductDTO> product = Optional.of(catalogueService.getProductById(campaignDetails.getTargetUuid()));
                if (product.isPresent()) {
                    campaign.setTargetUuid(campaignDetails.getTargetUuid());
                } else {
                    throw new RuntimeException("Product not found");
                }
            } else if (campaignDetails.getType().equals(Campaign.Type.CATEGORY)) {
                Optional<CategoryDTO> category = Optional.of(catalogueService.getCategoryById(campaignDetails.getTargetUuid()));
                if (category.isPresent()) {
                    campaign.setTargetUuid(campaignDetails.getTargetUuid());
                } else {
                    throw new RuntimeException("Category not found");
                }
            } else if (campaignDetails.getType().equals(Campaign.Type.SUBCATEGORY)) {
                Optional<SubcategoryDTO> subcategory = Optional.of(catalogueService.getSubcategoryById(campaignDetails.getTargetUuid()));
                if (subcategory.isPresent()) {
                    campaign.setTargetUuid(campaignDetails.getTargetUuid());
                } else {
                    throw new RuntimeException("Subcategory not found");
                }
            } else {
                throw new RuntimeException("Invalid campaign type");
            }
        }
        if (campaignDetails.getDateStart() != null)
            campaign.setDateStart(campaignDetails.getDateStart());
        if (campaignDetails.getDateEnd() != null)
            campaign.setDateEnd(campaignDetails.getDateEnd());

        return campaignRepository.save(campaign);
    }

    public void deleteCampaign(Integer id) {
        campaignRepository.deleteById(id);
    }

    public List<CouponCode> getAllCoupons() {
        return couponCodeRepository.findAll();
    }

    public Optional<CouponCode> getCouponById(Integer id) {
        return couponCodeRepository.findById(id);
    }

    public Optional<CouponCode> getCouponByName(String name) {
        return couponCodeRepository.findByName(name);
    }

    public CouponCode createCoupon(CouponCode coupon) {
        if (coupon.getDateStart().isAfter(coupon.getDateEnd())) {
            throw new IllegalArgumentException("The start date cannot be after the end date.");
        }
        if (coupon.getDateStart().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("The start date cannot be in the past.");
        }
        if (coupon.getDateEnd().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("The end date cannot be in the past.");
        }
        if (coupon.getName() == null || coupon.getName().length() < 3) {
            throw new IllegalArgumentException("The coupon name must be at least 3 characters long.");
        }
        if (coupon.getDiscountType() == null) {
            throw new IllegalArgumentException("The discount type must be specified.");
        }
        if (coupon.getDiscountType() == CouponCode.DiscountType.PERCENTAGE && (coupon.getDiscountRate() < 0 || coupon.getDiscountRate() > 100)) {
            throw new IllegalArgumentException("The discount rate for a percentage must be between 0 and 100.");
        }
        if (coupon.getDiscountType() == CouponCode.DiscountType.AMOUNT && coupon.getDiscountRate() < 0) {
            throw new IllegalArgumentException("The discount amount cannot be negative.");
        }

        CouponCode existingCoupon = couponCodeRepository.findByName(coupon.getName()).orElse(null);
        if (existingCoupon != null) {
            throw new IllegalArgumentException("A coupon with this name already exists.");
        }
    
        return couponCodeRepository.save(coupon);
    }

    public CouponCode updateCoupon(Integer id, CouponCode couponDetails) {
        CouponCode coupon = couponCodeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Coupon not found"));

        if (couponDetails.getName() != null) {
            coupon.setName(couponDetails.getName());
        }
        if (couponDetails.getDiscountRate() != null) {
            coupon.setDiscountRate(couponDetails.getDiscountRate());
        }
        if (couponDetails.getDateStart() != null) {
            coupon.setDateStart(couponDetails.getDateStart());
        }
        if (couponDetails.getDateEnd() != null) {
            coupon.setDateEnd(couponDetails.getDateEnd());
        }
        return couponCodeRepository.save(coupon);
    }

    public void deleteCoupon(Integer id) {
        couponCodeRepository.deleteById(id);
    }

    @Transactional
    public void assignCouponToUser(User user, CouponCode couponCode) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUser(user);
        userCoupon.setCoupon(couponCode);
        userCoupon.setClaimed(false);
        userCouponRepository.save(userCoupon);
    }

    @Transactional
    public Double claimCoupon(User user, CouponCode couponCode) {
        UserCoupon userCoupon = userCouponRepository.findByUserAndCoupon(user, couponCode);
        if (userCoupon != null && !userCoupon.isClaimed()) {
            userCoupon.setClaimed(true);
            userCouponRepository.save(userCoupon);
            return couponCode.getDiscountRate();
        } else {
            throw new RuntimeException("Coupon not found for the user or already claimed");
        }
    }
}