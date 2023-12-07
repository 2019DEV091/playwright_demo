package com.ctg.playwright_demo.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ctg.playwright_demo.tests.pages.CartPage;
import com.ctg.playwright_demo.tests.pages.InventoryPage;
import com.ctg.playwright_demo.tests.pages.LoginPage;

public class CartTests extends BaseTest{
    
     @Test
    public void testAddRandomProductToCartAndVerify() {
        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);

        loginPage.navigate();
        loginPage.login("standard_user", "secret_sauce");

        String addedProductName = inventoryPage.addRandomProductToCart();
        page.click(".shopping_cart_link"); // Navigate to the cart page
        List<String> cartProductNames = cartPage.getCartProductNames();

        assertTrue(cartProductNames.contains(addedProductName), "Added product is not present in the cart");
    }
}
