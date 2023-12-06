package com.ctg.playwright_demo.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    public static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setSlowMo(1000).setHeadless(false));
    }

    @AfterAll
    public static void closeBrowser() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    public void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        // Any additional setup can be done here
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        captureScreenshot(testInfo);
        page.close();
        context.close();
    }

    private void captureScreenshot(TestInfo testInfo) {
        String testMethodName = testInfo.getTestMethod().map(Method::getName).orElse("unknown");
        String dirPath = "target" + File.separator + testMethodName;
        Path directory = createDirectoryForScreenshots(dirPath);
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Path screenshotPath = directory.resolve(testMethodName + "_" + timeStamp + ".png");
        page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
    }

    private Path createDirectoryForScreenshots(String dirPath) {
        Path directory = Paths.get(dirPath);
        try {
            return Files.createDirectories(directory);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
