package ccstdemo;

import io.percy.selenium.Percy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestPercyDemo {

    private WebDriver driver;
    private Percy percy;


    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        percy = new Percy(driver);
    }

    @Test
    void homePageSnapshot() {
        driver.get("https://saucedemo.com");
        percy.snapshot("Home Page");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
