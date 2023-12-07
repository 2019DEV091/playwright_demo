package com.ctg.playwright_demo.tests.pages;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class InventoryPage {
    private final Page page;
    private static final String SORT_SELECTOR = ".product_sort_container";
    private static final String PRODUCT_NAME_SELECTOR = ".inventory_item_name";
    private static final String PRODUCT_PRICE_SELECTOR = ".inventory_item_price";
    private static final String ADD_TO_CART_BUTTON_SELECTOR = ".btn_primary.btn_inventory";


    public InventoryPage(Page page) {
        this.page = page;
    }

    public boolean isProductListVisible() {
        return page.isVisible(".inventory_list");
    }

    public String getProductTitle(int productId) {
        return page.textContent(".inventory_item:nth-of-type(" + productId + ") .inventory_item_name");
    }

    public String getProductDescription(int productId) {
        return page.textContent(".inventory_item:nth-of-type(" + productId + ") .inventory_item_desc");
    }

    public String getProductPrice(int productId) {
        return page.textContent(".inventory_item:nth-of-type(" + productId + ") .inventory_item_price");
    }

    public boolean isAddToCartButtonVisible(int productId) {
        return page.isVisible(".inventory_item:nth-of-type(" + productId + ") .btn_primary.btn_inventory");
    }

     public void selectSortOption(String optionValue) {
        page.selectOption(SORT_SELECTOR, optionValue);
    }

    public List<String> getProductNames() {
        return page.querySelectorAll(PRODUCT_NAME_SELECTOR).stream()
                .map(element -> element.textContent().trim())
                .collect(Collectors.toList());
    }

    public List<Double> getProductPrices() {
        return page.querySelectorAll(PRODUCT_PRICE_SELECTOR).stream()
                .map(element -> Double.parseDouble(element.textContent().replace("$", "").trim()))
                .collect(Collectors.toList());
    }

    public String addRandomProductToCart() {
        List<String> productNames = getProductNames();
        int randomIndex = new Random().nextInt(productNames.size());
        String randomProductName = productNames.get(randomIndex);
        Locator child = page.getByText(randomProductName);
        Locator parent = page.locator("css=div.inventory_item").filter(new Locator.FilterOptions().setHas(child));
        parent.locator(ADD_TO_CART_BUTTON_SELECTOR).click();
        return randomProductName;
    }
}
