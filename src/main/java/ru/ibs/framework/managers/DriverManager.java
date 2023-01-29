package ru.ibs.framework.managers;

import org.apache.commons.exec.OS;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static ru.ibs.framework.utils.PropsConst.*;


public class DriverManager {
    private static DriverManager INSTANCE;

    private TestPropertiesManager testProperties = TestPropertiesManager.getInstance();
    private WebDriver driver;



    public static DriverManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriverManager();
        }
        return INSTANCE;
    }

    private DriverManager() {
    }

    public WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }

    private void initDriver() {
        if (OS.isFamilyWindows()) {
            initDriverWindowsOsFamily();
        } else if (OS.isFamilyMac()) {
            initDriverMacOsFamily();
        }
    }

    private void initDriverWindowsOsFamily() {
        switch (testProperties.getMavenProperty("browser", "chrome")) {
            case "edge":
                System.setProperty(PATH_EDGE_DRIVER, testProperties.getProperty(PATH_EDGE_DRIVER_WINDOWS));
                driver = new EdgeDriver();
                break;
            case "firefox":
                System.setProperty(PATH_FIREFOX_DRIVER, testProperties.getProperty(PATH_FIREFOX_DRIVER_WINDOWS));
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                System.setProperty(PATH_CHROME_DRIVER, testProperties.getProperty(PATH_CHROME_DRIVER_WINDOWS));
                driver = new ChromeDriver();
                break;
        }
    }

    private void initDriverMacOsFamily() {
        switch (testProperties.getMavenProperty("browser", "chrome")) {
            case "edge":
                System.setProperty(PATH_EDGE_DRIVER, testProperties.getProperty(PATH_EDGE_DRIVER_MAC));
                driver = new EdgeDriver();
            case "firefox":
                System.setProperty(PATH_FIREFOX_DRIVER, testProperties.getProperty(PATH_FIREFOX_DRIVER_MAC));
                driver = new FirefoxDriver();
            case "chrome":
            default:
                System.setProperty(PATH_CHROME_DRIVER, testProperties.getProperty(PATH_CHROME_DRIVER_MAC));
                driver = new ChromeDriver();
        }
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
