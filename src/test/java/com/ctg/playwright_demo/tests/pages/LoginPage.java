package com.ctg.playwright_demo.tests.pages;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {

     private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static final String URL = "https://www.saucedemo.com/v1/";
    private static final String USERNAME_SELECTOR = "#user-name";
    private static final String PASSWORD_SELECTOR = "#password";
    private static final String LOGIN_BUTTON_SELECTOR = "#login-button";
    private static final String ERROR_MESSAGE_SELECTOR = "h3[data-test='error']";

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigate() {
        logger.info("Navigating to login page: " + URL);
        page.navigate(URL);
        validatePage();
    }

    private void validatePage() {
        logger.info("Validating the login page");
        if (!page.isVisible(USERNAME_SELECTOR) || !page.isVisible(PASSWORD_SELECTOR)) {
            throw new IllegalStateException("Login page not loaded properly");
        }
    }

    public void login(String username, String password) {
        logger.info("Logging in with username: " + username);
        page.fill(USERNAME_SELECTOR, username);
        page.fill(PASSWORD_SELECTOR, password);
        page.click(LOGIN_BUTTON_SELECTOR);
    }

    public boolean isErrorMessageVisible() {
        logger.info("Checking if error message is visible");
        return page.isVisible(ERROR_MESSAGE_SELECTOR);
    }

    public String getErrorMessage() {
        logger.info("Getting error message");
        return page.textContent(ERROR_MESSAGE_SELECTOR);
    }

    public boolean isOnLoginPage() {
        logger.info("Checking if current page is login page");
        return page.url().equals(URL);
    }

    public boolean isUsernameFieldVisible() {
        logger.info("Checking if username field is visible");
        return page.isVisible(USERNAME_SELECTOR);
    }

    public boolean isPasswordFieldVisible() {
        logger.info("Checking if password field is visible");
        return page.isVisible(PASSWORD_SELECTOR);
    }

    public boolean isLoginButtonVisible() {
        logger.info("Checking if login button is visible");
        return page.isVisible(LOGIN_BUTTON_SELECTOR);
    }


}