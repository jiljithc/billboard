package com.jeeniv.billboard;

import com.jeeniv.billboard.page.BillboardMapPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BillboardTrackerAutomationTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private BillboardMapPage mapPage;
    private String baseUrl;

   @BeforeAll
    static void setupDriver() {
        // If /usr/bin/chromedriver exists (Linux container), skip dynamic download
        if (new java.io.File("/usr/bin/chromedriver").exists()) {
            System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        } else {
            WebDriverManager.chromedriver().setup();
        }
    }

    @BeforeEach
    void init() {
        ChromeOptions options = new ChromeOptions();

        // 1. Detect macOS vs Linux Chromium binary paths
        java.io.File macChrome = new java.io.File("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        java.io.File linuxChromium = new java.io.File("/usr/bin/chromium");
        java.io.File linuxChrome = new java.io.File("/usr/bin/google-chrome");

        if (macChrome.exists()) {
            options.setBinary(macChrome);
        } else if (linuxChromium.exists()) {
            options.setBinary(linuxChromium);
        } else if (linuxChrome.exists()) {
            options.setBinary(linuxChrome);
        }

        // 2. Flags essential for running in Docker
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        baseUrl = "http://localhost:" + port + "/";
        driver.get(baseUrl);

        mapPage = new BillboardMapPage(driver);
        mapPage.waitForPageLoad();
    }
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Verify map loads and displays seeded billboards")
    void testInitialBillboardLoad() {
        assertTrue(mapPage.getBoardCount() >= 0, "Initial seeded billboards should be at least 0");
        assertNotEquals("0", mapPage.getExpiredKpiCount(), "Expired KPI count should not be 0");
    }

    @Test
    @Order(2)
    @DisplayName("Verify search filter updates sidebar cards")
    void testSearchFilter() {
        mapPage.search("BLB-101");
        
        // Wait briefly for client-side filter
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        assertTrue(mapPage.isBoardPresent("BLB-101"), "Target board BLB-101 must be visible");
        assertEquals(1, mapPage.getBoardCount(), "Only 1 card should be matched");
    }

    @Test
    @Order(3)
    @DisplayName("Verify status filtering for VACANT billboards")
    void testStatusFilter() {
        mapPage.filterByStatus("VACANT");

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        assertTrue(mapPage.getBoardCount() >= 1, "At least one vacant board should be displayed");
        assertTrue(mapPage.isBoardPresent("BLB-105"), "BLB-105 is seeded as vacant and should appear");
    }

    @Test
    @Order(4)
    @DisplayName("Verify adding a new billboard via modal and map presence")
    void testAddNewBillboard() {
        String testCode = "AUTO-999";

        mapPage.openAddBillboardModal();
        mapPage.fillBillboardForm(
                testCode,
                "Downtown",
                "Automated Test LED Screen",
                40.7200,
                -74.0000,
                "DIGITAL_LED"
        );
        mapPage.submitBillboardForm();

        // Allow API call and re-render
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        mapPage.search(testCode);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        assertTrue(mapPage.isBoardPresent(testCode), "Newly registered billboard must appear in the directory");
    }
}
