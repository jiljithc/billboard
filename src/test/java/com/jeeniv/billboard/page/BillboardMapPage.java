package com.jeeniv.billboard.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BillboardMapPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By mapElement = By.id("map");
    private final By searchInput = By.id("searchInput");
    private final By areaFilter = By.id("areaFilter");
    private final By statusFilter = By.id("statusFilter");
    private final By boardCards = By.cssSelector("#boardList > div");
    private final By addBillboardButton = By.xpath("//button[contains(., 'Add Billboard')]");

    // Add Billboard Modal Locators
    private final By addModal = By.id("addModal");
    private final By inputCode = By.id("newCode");
    private final By inputArea = By.id("newArea");
    private final By inputTitle = By.id("newTitle");
    private final By inputLat = By.id("newLat");
    private final By inputLng = By.id("newLng");
    private final By selectType = By.id("newType");
    private final By submitButton = By.cssSelector("#addForm button[type='submit']");

    // KPI Counters
    private final By countExpired = By.id("count-expired");

    public BillboardMapPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapElement));
        // Wait until boards are populated in sidebar
        wait.until(driver -> driver.findElements(boardCards).size() > 0);
    }

    public int getBoardCount() {
        return driver.findElements(boardCards).size();
    }

    public void search(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(text);
    }

    public void filterByStatus(String status) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(statusFilter)));
        select.selectByValue(status);
    }

    public void filterByArea(String area) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(areaFilter)));
        select.selectByVisibleText(area);
    }

    public void openAddBillboardModal() {
        driver.findElement(addBillboardButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(addModal));
    }

    public void fillBillboardForm(String code, String area, String title, double lat, double lng, String type) {
        driver.findElement(inputCode).sendKeys(code);
        driver.findElement(inputArea).sendKeys(area);
        driver.findElement(inputTitle).sendKeys(title);
        driver.findElement(inputLat).sendKeys(String.valueOf(lat));
        driver.findElement(inputLng).sendKeys(String.valueOf(lng));

        Select typeSelect = new Select(driver.findElement(selectType));
        typeSelect.selectByValue(type);
    }

    public void submitBillboardForm() {
        driver.findElement(submitButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(addModal));
    }

    public boolean isBoardPresent(String code) {
        List<WebElement> cards = driver.findElements(boardCards);
        return cards.stream().anyMatch(c -> c.getText().contains(code));
    }

    public String getExpiredKpiCount() {
        return driver.findElement(countExpired).getText();
    }
}