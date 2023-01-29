package ru.ibs.framework.managers;


import ru.ibs.framework.pages.BasePage;
import ru.ibs.framework.pages.DepositPage;
import ru.ibs.framework.pages.blocks.MenuBlockPage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PageManager {
    private static PageManager INSTANCE;

    private TestPropertiesManager testPropertiesManager;

    private static Map<String, Object> mapPages = new HashMap<>();

    MenuBlockPage menuBlockPage;

    DepositPage depositPage;

    public static PageManager getInstance () {
        if (INSTANCE == null) {
            INSTANCE = new PageManager();
        }
        return INSTANCE;
    }

    public <T extends BasePage> T getPage(Class<T> page) {
        if (mapPages.isEmpty() || mapPages.get(page.getName()) == null) {
            try {
                mapPages.put(page.getName(), page.getConstructor().newInstance());
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return (T) mapPages.get(page.getName());
    }

    public void clearPages() {
        mapPages.clear();
    }
}
