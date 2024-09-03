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
import com.mobalpa.api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

@Service
public class PromotionService {

    @Autowired
    private UserRepository userRepository;

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

    public List<Campaign> getProductCampaigns(UUID productUuid) {
        Optional<ProductDTO> product = Optional.of(catalogueService.getProductById(productUuid));
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        List<Campaign> allCampaigns = campaignRepository.findAll();

        return allCampaigns.stream()
            .filter(campaign -> 
                campaign.getType().equals(Campaign.Type.PRODUCT) && campaign.getTargetUuid().equals(productUuid) ||
                campaign.getType().equals(Campaign.Type.CATEGORY) && catalogueService.getProductById(productUuid).getCategory().getUuid().equals(campaign.getTargetUuid()) ||
                campaign.getType().equals(Campaign.Type.SUBCATEGORY) && catalogueService.getProductById(productUuid).getSubcategory().getUuid().equals(campaign.getTargetUuid())
            )
            .toList();
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
        List<CouponCode> coupons = couponCodeRepository.findAll();

        for (CouponCode coupon : coupons) {
            if (CouponCode.TargetType.SPECIFIC_USERS.equals(coupon.getTargetType())) {
                List<User> targetUsers = new ArrayList<>();
                for (String uuid : coupon.getTargetUsers()) {
                    User user = userRepository.findByUuid(UUID.fromString(uuid))
                        .orElseThrow(() -> new RuntimeException("User not found"));
                    targetUsers.add(user);
                }
                coupon.setEnrichedTargetUsers(targetUsers); 
            }
        }
        return coupons;
    }

    public Optional<CouponCode> getCouponById(Integer id) {
        return couponCodeRepository.findById(id);
    }

    public CouponCode getCouponByName(String name) {
        return couponCodeRepository.findByName(name).orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public CouponCode createCoupon(CouponCode coupon) {
        if (coupon.getDateStart().isAfter(coupon.getDateEnd())) {
            throw new IllegalArgumentException("The start date cannot be after the end date.");
        }
        if (coupon.getDateStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("The start date cannot be in the past.");
        }
        if (coupon.getDateEnd().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("The end date cannot be in the past.");
        }
        if (coupon.getName() == null || coupon.getName().length() < 3) {
            throw new IllegalArgumentException("The coupon name must be at least 3 characters long.");
        }
        if (coupon.getDiscountType() == null) {
            throw new IllegalArgumentException("The discount type must be specified.");
        }
        if (coupon.getDiscountType() == CouponCode.DiscountType.PERCENTAGE &&
                (coupon.getDiscountRate() < 0 || coupon.getDiscountRate() > 100)) {
            throw new IllegalArgumentException("The discount rate for a percentage must be between 0 and 100.");
        }
        if (coupon.getDiscountType() == CouponCode.DiscountType.AMOUNT &&
                coupon.getDiscountRate() < 0) {
            throw new IllegalArgumentException("The discount amount cannot be negative.");
        }
        if (coupon.getTargetType() == null) {
            throw new IllegalArgumentException("The target type must be specified.");
        }
        if (coupon.getTargetType() == CouponCode.TargetType.SPECIFIC_USERS &&
                (coupon.getTargetUsers() == null || coupon.getTargetUsers().isEmpty())) {
            throw new IllegalArgumentException("The target users must be specified.");
        }
        if (coupon.getMaxUse() < 0) {
            throw new IllegalArgumentException("The maximum use count cannot be negative.");
        }

        CouponCode existingCoupon = couponCodeRepository.findByName(coupon.getName()).orElse(null);
        if (existingCoupon != null) {
            throw new IllegalArgumentException("A coupon with this name already exists.");
        }

        CouponCode savedCoupon = couponCodeRepository.save(coupon);

        switch (coupon.getTargetType()) {
            case ALL_USERS:
                List<User> users = userRepository.findAll();
                for (User user : users) {
                    UserCoupon userCoupon = new UserCoupon();
                    userCoupon.setUser(user);
                    userCoupon.setCoupon(savedCoupon);
                    userCoupon.setClaimed(false);
                    userCouponRepository.save(userCoupon);
                }
                break;
            case SPECIFIC_USERS:
                for (String targetUserUuid : coupon.getTargetUsers()) {
                    User user = userRepository.findByUuid(UUID.fromString(targetUserUuid))
                        .orElseThrow(() -> new RuntimeException("User not found"));
                    UserCoupon userCoupon = new UserCoupon();
                    userCoupon.setUser(user);
                    userCoupon.setCoupon(savedCoupon);
                    userCoupon.setClaimed(false);
                    userCouponRepository.save(userCoupon);
                }
                break;
            case USER:
                String targetUserUuid = coupon.getTargetUsers().get(0);
                User user = userRepository.findByUuid(UUID.fromString(targetUserUuid))
                    .orElseThrow(() -> new RuntimeException("User not found"));
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setUser(user);
                userCoupon.setCoupon(savedCoupon);
                userCoupon.setClaimed(false);
                userCouponRepository.save(userCoupon);
                break;
        }

        return savedCoupon;
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
        if (couponDetails.getTargetType() != null) {
            coupon.setTargetType(couponDetails.getTargetType());
        }
        if (couponDetails.getMaxUse() != null) {
            coupon.setMaxUse(couponDetails.getMaxUse());
        }
        if (couponDetails.getTargetUsers() != null) {
            coupon.setTargetUsers(couponDetails.getTargetUsers());
        }
        return couponCodeRepository.save(coupon);
    }

    @Transactional
    public void deleteCoupon(Integer id) {
        userCouponRepository.deleteByCouponId(id);
        couponCodeRepository.deleteById(id);
    }
    
    @Transactional
    public Map<String, Object> claimCoupon(User user, CouponCode couponCode) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        System.out.println("Current Time: " + now);

        ZonedDateTime couponStart = couponCode.getDateStart().atZone(ZoneId.of("Europe/Paris"));
        System.out.println("Coupon Start Time: " + couponStart);

        ZonedDateTime couponEnd = couponCode.getDateEnd().atZone(ZoneId.of("Europe/Paris"));
        System.out.println("Coupon End Time: " + couponEnd);

        if (couponCode.getDateStart() != null && couponStart.isAfter(now)) {
            throw new RuntimeException("Coupon not yet valid");
        }
        if (couponCode.getDateEnd() != null && couponEnd.isBefore(now)) {
            throw new RuntimeException("Coupon expired");
        }
        if (couponCode.getMaxUse() != null && couponCode.getCurrentUse() >= couponCode.getMaxUse()) {
            throw new RuntimeException("Coupon usage limit reached");
        }

        UserCoupon userCoupon = userCouponRepository.findByUserAndCoupon(user, couponCode)
            .orElseThrow(() -> new RuntimeException("Coupon not found for the user"));

        if (userCoupon.isClaimed()) {
            throw new RuntimeException("Coupon already claimed");
        }

        userCoupon.setClaimed(true);
        userCoupon.setDateClaimed(LocalDate.now());
        userCouponRepository.save(userCoupon);

        couponCode.setCurrentUse(couponCode.getCurrentUse() + 1);
        couponCodeRepository.save(couponCode);

        return Map.of("discountRate", couponCode.getDiscountRate(), "discountType", couponCode.getDiscountType());
    }    
}