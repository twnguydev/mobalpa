package com.mobalpa.api.delivery;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @GetMapping
    public String getDeliveryInfo() {
        return "Delivery information";
    }
}