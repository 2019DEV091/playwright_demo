package com.ctg.playwright_demo.tests;

import com.ctg.playwright_demo.tests.pages.LoginPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class Logintests extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    @Override
    public void createContextAndPage() {

        super.createContextAndPage();
        loginPage = new LoginPage(page);
        loginPage.navigate();
    }

    @Test
    public void testLoginWithValidCredentials() {
        loginPage.login("standard_user", "secret_sauce");

        assertEquals("https://www.saucedemo.com/v1/inventory.html", page.url(),
                "URL does not match the expected inventory page URL");
        assertTrue(page.isVisible("#inventory_container"), "Inventory container is not visible");
        assertTrue(page.isVisible(".product_label"), "Product label is not visible");
        assertTrue(page.isVisible(".inventory_item"), "Inventory items are not visible");
        assertTrue(page.isVisible(".shopping_cart_link"), "Shopping cart link is not visible");
        assertTrue(page.isVisible(".footer"), "Footer is not visible");
        assertTrue(page.isVisible("text=Sauce Labs Backpack"), "Sauce Labs Backpack is not visible");
    }

    @ParameterizedTest
    @CsvSource({ "user,secret_sauce", "standard_user,psst", "fjklqfjq,jfkldqjfqdl" })
    public void testLoginWithInvalidCredentials(String userName, String password) {
        loginPage.login(userName, password);

        assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible after failed login");
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                loginPage.getErrorMessage(), "Error message text should match");
        assertTrue(loginPage.isOnLoginPage(), "URL should remain the login page after failed login attempt");
    }

    @Test
    public void testLoginWithLockedOutUser() {
        loginPage.login("locked_out_user", "secret_sauce");

        assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible for locked-out user");
        assertEquals("Epic sadface: Sorry, this user has been locked out.",
                loginPage.getErrorMessage(), "Error message text should match for locked-out user");
        assertTrue(loginPage.isOnLoginPage(),
                "URL should remain the login page after failed login attempt with locked-out user");
    }

    @Test
    public void testUILoginElements() {
        assertTrue(loginPage.isUsernameFieldVisible(), "Username field is not visible");
        assertTrue(loginPage.isPasswordFieldVisible(), "Password field is not visible");
        assertTrue(loginPage.isLoginButtonVisible(), "Login button is not visible");
    }
}
