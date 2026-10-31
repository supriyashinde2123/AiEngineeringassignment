import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://www.saucedemo.com";

    @BeforeEach
    public void setUp() {
        // Set ChromeDriver path if necessary, e.g., System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // run headless for CI environments
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Valid login shows inventory list")
    public void testValidLoginShowsInventory() {
        // Enter valid credentials
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Wait for inventory list to be visible
        WebElement inventory = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        assertTrue(inventory.isDisplayed(), "Inventory list should be displayed after successful login");
    }

    @Test
    @DisplayName("Wrong password shows error message")
    public void testWrongPasswordShowsError() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        assertTrue(error.isDisplayed(), "Error message should be displayed for wrong password");
        String errorText = error.getText();
        assertTrue(errorText.toLowerCase().contains("password"), "Error message should mention password");
    }

    @Test
    @DisplayName("Empty username shows error message")
    public void testEmptyUsernameShowsError() {
        // Leave username empty, fill password
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        assertTrue(error.isDisplayed(), "Error message should be displayed when username is empty");
        String errorText = error.getText();
        assertTrue(errorText.toLowerCase().contains("username"), "Error message should mention username");
    }
}
