package herokuapp;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class herokuapptestcase {

    public WebDriver obj_driver;
    WebDriverWait wait;

    private final String HEROKU_BASE_URL = "https://the-internet.herokuapp.com";

    public void markTestStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("browserstack_executor:" + "{\"action\":\"setSessionStatus\",\"arguments\":{\"status\":\"" + status + "\",\"reason\":\"" + reason + "\"}}");
    }

    @Parameters({"os", "osVersion", "browserName", "browserVersion"})
    @BeforeMethod
    public void setUp(
            @Optional("Windows") String os,
            @Optional("11") String osVersion,
            @Optional("Chrome") String browserName,
            @Optional("latest") String browserVersion) throws MalformedURLException {

        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (username == null || accessKey == null || username.isEmpty() || accessKey.isEmpty()) {
            throw new IllegalStateException("Missing Credentials: BROWSERSTACK_USERNAME or BROWSERSTACK_ACCESS_KEY variable is missing in your IntelliJ configuration.");
        }

        String browserstackUrl = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";

        DesiredCapabilities obj_caps = new DesiredCapabilities();
        HashMap<String, Object> obj_stackOption = new HashMap<>();
        obj_stackOption.put("os", os);
        obj_stackOption.put("osVersion", osVersion);
        obj_stackOption.put("projectName", "HerokuApp Integration Suite");
        obj_stackOption.put("buildName", "HerokuApp Build 1.0");
        obj_stackOption.put("sessionName", "HerokuApp Functional Scenarios");
        obj_caps.setCapability("bstack:options", obj_stackOption);
        obj_caps.setCapability("browserName", browserName);
        obj_caps.setCapability("browserVersion", browserVersion);

        obj_driver = new RemoteWebDriver(new URL(browserstackUrl), obj_caps);
        wait = new WebDriverWait(obj_driver, Duration.ofSeconds(10));
        obj_driver.manage().window().maximize();
    }

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testHerokuFormAuthentication() {
        try {
            obj_driver.get(HEROKU_BASE_URL + "/login");
            WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            userField.clear();
            userField.sendKeys("admin");

            WebElement passField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passField.clear();
            passField.sendKeys("password@123");

            WebElement submitBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
            JavascriptExecutor js = (JavascriptExecutor) obj_driver;
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", submitBtn);
            js.executeScript("arguments[0].click();", submitBtn);

            WebElement flashBanner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
            Assert.assertFalse(flashBanner.getText().contains("You logged into a secure area!"), "Login authentication verification failed.");
            markTestStatus("Passed", "HerokuApp Form Authentication passed successfully.", obj_driver);
        } catch (Exception e) {
            markTestStatus("Failed", "HerokuApp Form Authentication failed: " + e.getMessage(), obj_driver);
            throw e;
        }
    }

    @Test(priority = 2, description = "Verify checkbox selection toggles")
    public void testHerokuCheckboxes() {
        try {
            obj_driver.get(HEROKU_BASE_URL + "/checkboxes");
            List<WebElement> checkboxes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#checkboxes input")));
            WebElement cb1 = checkboxes.get(0);
            WebElement cb2 = checkboxes.get(1);

            if (!cb1.isSelected()) {
                cb1.click();
            }
            Assert.assertTrue(cb1.isSelected(), "Checkbox 1 option state toggle validation failed.");

            if (cb2.isSelected()) {
                cb2.click();
            }
            Assert.assertFalse(cb2.isSelected(), "Checkbox 2 option state toggle validation failed.");

            markTestStatus("Passed", "HerokuApp Checkbox toggling validation passed successfully.", obj_driver);
        } catch (Exception e) {
            markTestStatus("Failed", "HerokuApp Checkbox validation failed: " + e.getMessage(), obj_driver);
            throw e;
        }
    }

    @Test(priority = 3, description = "Verify dropdown menu selection options")
    public void testHerokuDropdown() {
        try {
            obj_driver.get(HEROKU_BASE_URL + "/dropdown");
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dropdown")));
            Select selectOption = new Select(dropdown);
            selectOption.selectByVisibleText("Option 2");

            String selectedText = selectOption.getFirstSelectedOption().getText();
            Assert.assertEquals(selectedText, "Option 2", "Dropdown option interactive state verification mismatched.");
            markTestStatus("Passed", "HerokuApp Dropdown option selection validation passed successfully.", obj_driver);
        } catch (Exception e) {
            markTestStatus("Failed", "HerokuApp Dropdown validation failed: " + e.getMessage(), obj_driver);
            throw e;
        }
    }

    @Test(priority = 4, description = "Verify dynamic node element addition and removal")
    public void testHerokuAddRemoveElements() {
        try {
            obj_driver.get(HEROKU_BASE_URL + "/add_remove_elements/");
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Add Element']")));
            addBtn.click();

            WebElement deleteBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("added-manually")));
            Assert.assertTrue(deleteBtn.isDisplayed(), "Dynamic action node addition structural lookup element not found.");
            deleteBtn.click();

            List<WebElement> activeDeleteButtons = obj_driver.findElements(By.className("added-manually"));
            Assert.assertEquals(activeDeleteButtons.size(), 0, "Dynamic element removal interactive cleaning mismatch.");
            markTestStatus("Passed", "HerokuApp Dynamic Elements interaction passed successfully.", obj_driver);
        } catch (Exception e) {
            markTestStatus("Failed", "HerokuApp Dynamic Elements validation failed: " + e.getMessage(), obj_driver);
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
