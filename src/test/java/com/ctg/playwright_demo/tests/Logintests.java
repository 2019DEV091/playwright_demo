package com.ctg.playwright_demo.tests;

import com.ctg.playwright_demo.tests.pages.LoginPage;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class Logintests {

    private Page page;
    private LoginPage loginPage;

    // Shared between all tests in this class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setSlowMo(1000).setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        loginPage = new LoginPage(page);
        loginPage.navigate();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        String testMethodName = testInfo.getTestMethod().map(Method::getName).orElse("unknown");
        String dirPath = "target" + File.separator + testMethodName;
        Path directory = createDirectoryForScreenshots(dirPath);
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Path screenshotPath = directory.resolve(testMethodName + "_" + timeStamp + ".png");
        page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
        page.close();
        context.close();
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

    private Path createDirectoryForScreenshots(String dirPath) {
        Path directory = Paths.get(dirPath);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory;
    }
}
