package com.mobalpa.api.catalogue;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {

    @GetMapping
    public String getCatalogue() {
        return "Catalogue information";
    }
}
