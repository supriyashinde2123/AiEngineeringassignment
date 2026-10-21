package browserstack;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class BS_Login_saucedemo_TestNG {
    public WebDriver obj_driver;

    public static final String USERNAME = "supriyashinde_oSBVHg";
    public static final String ACCESS_KEY = "nBp8KdNqQyPpoR68VJdA";
    public static final String BROWSERSTACK_URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    WebDriverWait wait;

    public void markTestStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("browserstack_executor:" + "{\"action\":\"setSessionStatus\",\"arguments\":{\"status\":\"" + status + "\",\"reason\":\"" + reason + "\"}}");
    }

    By usernameField = By.id("user-name");
    By passwordField = By.id("password");
    By loginButton = By.id("login-button");
    By errorMessage = By.xpath("//h3[@data-test='error']");

    @Parameters({"os", "osVersion", "browserName", "browserVersion"})
    @BeforeMethod
    public void setUp(
            @Optional("") String os,
            @Optional("") String osVersion,
            @Optional("") String browserName,
            @Optional("") String browserVersion) throws MalformedURLException {

        DesiredCapabilities obj_caps = new DesiredCapabilities();
        HashMap<String, Object> obj_stackOption = new HashMap<>();
        obj_stackOption.put("os", os);
        obj_stackOption.put("osVersion", osVersion);
        obj_stackOption.put("projectName", "CCST BrowserStack Demo");
        obj_stackOption.put("buildName", "TestNG Build 1.0");
        obj_stackOption.put("sessionName", "CCST BrowserStack Demo");
        obj_caps.setCapability("bstack:options", obj_stackOption);
        obj_caps.setCapability("browserName", browserName);
        obj_caps.setCapability("browserVersion", browserVersion);

        obj_driver = new RemoteWebDriver(new URL(BROWSERSTACK_URL), obj_caps);
        wait = new WebDriverWait(obj_driver, Duration.ofSeconds(10));
        obj_driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void testPositiveLoginSauceDemo() throws InterruptedException {

        try {
            obj_driver.get("https://www.saucedemo.com");
            Thread.sleep(1000);

            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            WebElement password = obj_driver.findElement(passwordField);
            WebElement loginBtn = obj_driver.findElement(loginButton);

            username.sendKeys("standard_user");
            password.sendKeys("secret_sauce");
            Thread.sleep(1000);
            loginBtn.click();

            wait.until(ExpectedConditions.urlContains("inventory.html"));
            String currenturl = obj_driver.getCurrentUrl();
            Assert.assertTrue(currenturl.contains("inventory.html"), "login failed.URL:" + currenturl);

            markTestStatus("Passed", "User Successfully logged in.", obj_driver);
        } catch (Exception e) {
            markTestStatus("Failed", "Login test failed:" + e.getMessage(), obj_driver);
            throw e;
        }
    }

    @Test(priority = 2)
    public void testNegativeLoginSauceDemo() throws InterruptedException {
        try {
            obj_driver.get("https://www.saucedemo.com");
            Thread.sleep(1000);

            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            WebElement password = obj_driver.findElement(passwordField);
            WebElement loginBtn = obj_driver.findElement(loginButton);

            username.sendKeys("invalid_user");
            password.sendKeys("wrong_password");
            Thread.sleep(1000);
            loginBtn.click();

            WebElement errorMsgElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String actualErrorText = errorMsgElement.getText();

            String expectedErrorText = "Username and password do not match any user in this service";

            Assert.assertTrue(actualErrorText.contains(expectedErrorText), "Error message text mismatch! " + actualErrorText);
            markTestStatus("Passed", "Negative login validated successfully with error message.", obj_driver);
        } catch (Exception e) {
            markTestStatus("Failed", "Negative login test failed: " + e.getMessage(), obj_driver);
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        if (obj_driver != null) {
            obj_driver.quit();
        }
    }
}
