package com.ctg.playwright_demo.tests.pages;

import java.util.List;
import java.util.stream.Collectors;

import com.microsoft.playwright.Page;

public class CartPage {

    
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public List<String> getCartProductNames() {
        return page.querySelectorAll(".cart_item .inventory_item_name").stream()
                .map(element -> element.textContent().trim())
                .collect(Collectors.toList());
    }
    

}
