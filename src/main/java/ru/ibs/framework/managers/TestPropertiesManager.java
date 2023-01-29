package ru.ibs.framework.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class TestPropertiesManager {
    private final Properties properties = new Properties();
    private static TestPropertiesManager INSTANCE = null;

    private TestPropertiesManager() {
        try {
            properties.load(Files.newInputStream(new File("src/main/resources/" +
                    System.getProperty("propFile", "application") + ".properties").toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TestPropertiesManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestPropertiesManager();
        }
        return INSTANCE;
    }

    public String getProperty(String key) {
        return TestPropertiesManager.getInstance().properties.getProperty(key);
    }
    public String getMavenProperty(String key, String defaultValue) {
        String mavenValue = System.getProperty(key);
        if (mavenValue!=null) {
            return mavenValue.toLowerCase();
        }

        return TestPropertiesManager.getInstance().properties.getProperty(key.toLowerCase(), defaultValue);
    }
}
