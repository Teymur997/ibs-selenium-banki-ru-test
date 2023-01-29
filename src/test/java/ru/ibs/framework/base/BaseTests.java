package ru.ibs.framework.base;

import org.junit.jupiter.api.*;
import ru.ibs.framework.managers.DriverManager;
import ru.ibs.framework.managers.InitManager;
import ru.ibs.framework.managers.PageManager;
import ru.ibs.framework.managers.TestPropertiesManager;
import ru.ibs.framework.utils.PropsConst;

public class BaseTests {
    protected DriverManager driverManager = DriverManager.getInstance();

    protected PageManager pageManager = PageManager.getInstance();

    protected TestPropertiesManager testPropertiesManager = TestPropertiesManager.getInstance();

//    @BeforeAll
//    public static void beforeAll() {
//        InitManager.initFramework();
//    }

    @BeforeEach
    public void setUp() {
        InitManager.initFramework();
        driverManager.getDriver().get(testPropertiesManager.getProperty(PropsConst.MAIN_PAGE_URL));
    }

    @AfterEach
    public void teardown() {
        pageManager.clearPages();
        InitManager.quitFramework();
    }

//    @AfterAll
//    static void afterAll() {
//        InitManager.quitFramework();
//    }
}
