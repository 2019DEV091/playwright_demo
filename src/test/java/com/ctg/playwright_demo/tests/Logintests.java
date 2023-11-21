package com.ctg.playwright_demo.tests;

import com.microsoft.playwright.*;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.*;

public class Logintests {

    Page page;

    @BeforeEach
    void setUp() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setSlowMo(1000).setHeadless(false));
        BrowserContext context = browser.newContext();
        page = context.newPage();
        page.navigate("https://www.saucedemo.com/v1/"); // Replace with the URL where your HTML page is hosted
    }

    @Test
    public void testLoginWithValidCredentials() {
        page.fill("#user-name", "standard_user");
        page.fill("#password", "secret_sauce");
        page.click("#login-button");
        // Add assertions here to verify successful login
        // Assertion for URL
        Assertions.assertEquals("https://www.saucedemo.com/v1/inventory.html", page.url(),
                "URL does not match the expected inventory page URL");

        // Additional assertions based on the inventory page HTML
        Assertions.assertTrue(page.isVisible("#inventory_container"), "Inventory container is not visible");
        Assertions.assertTrue(page.isVisible(".product_label"), "Product label is not visible");
        Assertions.assertTrue(page.isVisible(".inventory_item"), "Inventory items are not visible");
        Assertions.assertTrue(page.isVisible(".shopping_cart_link"), "Shopping cart link is not visible");
        Assertions.assertTrue(page.isVisible(".footer"), "Footer is not visible");

        // Assertions for specific product existence (e.g., Sauce Labs Backpack)
        Assertions.assertTrue(page.isVisible("text=Sauce Labs Backpack"), "Sauce Labs Backpack is not visible");

    }

    @Test
    public void testLoginWithInvalidCredentials() {
        page.fill("#user-name", "invalid_user");
        page.fill("#password", "invalid_password");
        page.click("#login-button");
        // Add assertions here to verify login failure
    }

    @Test
    public void testUILoginElements() {
        Assertions.assertTrue(page.isVisible("#user-name"), "Username field is not visible");
        Assertions.assertTrue(page.isVisible("#password"), "Password field is not visible");
        Assertions.assertTrue(page.isVisible("#login-button"), "Login button is not visible");
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        // Take a screenshot with the test method name and timestamp
        String testMethodName = testInfo.getTestMethod().map(Method::getName).orElse("unknown");
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(testMethodName + "_" + timeStamp + ".png")));

        page.close();
    }
}
