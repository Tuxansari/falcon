package com.fameoflight.falcon.controller;

import com.fameoflight.falcon.model.ProductInfo;
import com.fameoflight.falcon.service.ICatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private ICatalogService catalogService;

    @GetMapping(value = "/product/{id}")
    public ProductInfo getAdmin(@PathVariable(value = "id") Integer productId) {
        return catalogService.getProduct(productId);
    }
}
