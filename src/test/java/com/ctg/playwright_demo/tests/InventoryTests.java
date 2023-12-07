package com.ctg.playwright_demo.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ctg.playwright_demo.tests.pages.InventoryPage;
import com.ctg.playwright_demo.tests.pages.LoginPage;

public class InventoryTests extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    
    @BeforeEach
    public void setUp() {
        loginPage = new LoginPage(page);
        inventoryPage = new InventoryPage(page);
        loginPage.navigate();
        loginPage.login("standard_user", "secret_sauce");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6}) // Assuming there are 6 products
    public void testProductDetails(int productId) {
        assertNotNull(inventoryPage.getProductTitle(productId), "Product title is not visible for product ID: " + productId);
        assertNotNull(inventoryPage.getProductDescription(productId), "Product description is not visible for product ID: " + productId);
        assertNotNull(inventoryPage.getProductPrice(productId), "Product price is not visible for product ID: " + productId);
        assertTrue(inventoryPage.isAddToCartButtonVisible(productId), "Add to Cart button is not visible for product ID: " + productId);
    }

    @Test
    public void testSeeingProductsAfterLogin() {

        loginPage.navigate();
        loginPage.login("standard_user", "secret_sauce");

        assertTrue(inventoryPage.isProductListVisible(), "Product list is not visible on the inventory page");
    }

     @ParameterizedTest
    @CsvSource({
        "lohi, true",   // Sort by Price (low to high)
        "hilo, false",  // Sort by Price (high to low)
        "az, true",     // Sort by Name (A to Z)
        "za, false"     // Sort by Name (Z to A)
    })
    public void testProductSorting(String sortOption, boolean ascending) {
        inventoryPage.selectSortOption(sortOption);

        if (sortOption.equals("az") || sortOption.equals("za")) {
            List<String> productNames = inventoryPage.getProductNames();
            assertTrue(isSorted(productNames, ascending), "Product names are not sorted correctly");
        } else {
            List<Double> productPrices = inventoryPage.getProductPrices();
            assertTrue(isSorted(productPrices, ascending), "Product prices are not sorted correctly");
        }
    }

    private <T extends Comparable<T>> boolean isSorted(List<T> list, boolean ascending) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (ascending && list.get(i).compareTo(list.get(i + 1)) > 0) {
                return false;
            } else if (!ascending && list.get(i).compareTo(list.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }

}
