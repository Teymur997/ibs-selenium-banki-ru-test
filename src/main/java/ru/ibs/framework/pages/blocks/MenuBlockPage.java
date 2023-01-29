package ru.ibs.framework.pages.blocks;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.ibs.framework.pages.BasePage;
import ru.ibs.framework.pages.DepositPage;

import java.util.List;

public class MenuBlockPage extends BasePage {
    @FindBy(xpath = "//button[@data-role=\"icon-button\"]")
    private WebElement regionSelectCloseButton;

    @FindBy(xpath = "//li[contains(@class, 'main-menu__sections-item')]")
    private List<WebElement> topMenu;

    @Step("Проверка корректности открытия нужной страницы")
    public MenuBlockPage checkIfPageIsOpened() {
        Assertions.assertTrue(driverManager.getDriver()
                .getTitle()
                .contains("Банки.ру — финансовый маркетплейс. Вклады, кредиты, ипотека, страховые и инвестиционные продукты"),
                "Страница не открыта");
        return pageManager.getPage(MenuBlockPage.class);
    }

    @Step("Закрытие окна выбора региона")
    public MenuBlockPage closeRegionSelectButton() {
        try {
            waitUntilElementIsVisible(regionSelectCloseButton);
            regionSelectCloseButton.click();
        } catch (NoSuchElementException ignore) {
        }
        return pageManager.getPage(MenuBlockPage.class);
    }

    @Step("Выбор меню \"{menuName}\"")
    public DepositPage chooseMenu(String menuName) {
        topMenu.stream()
                .filter(element -> element.getText()
                        .contains(menuName)).findAny()
                .get()
                .click();
        return pageManager.getPage(DepositPage.class);
    }
}
