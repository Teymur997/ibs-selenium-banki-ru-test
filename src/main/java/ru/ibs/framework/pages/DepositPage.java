package ru.ibs.framework.pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.ibs.framework.utils.Deposit;

import java.util.List;

public class DepositPage extends BasePage {
    @FindBy(xpath = "//div[@class=\"sc-hBEYos czEoCN\"]")
    private WebElement cashBackWindow;

    @FindBy(xpath = "//div[@id=\"deposit_search_form\"]")
    private WebElement depositSearchForm;

    @FindBy(xpath = "//div[@class=\"SearchModal__StyledBody-sc-wuz0ak-1 eQdrBU\"]//input")
    private List<WebElement> depositInputFields;

    @FindBy(xpath = "//label[@class=\"sc-bBXqnf kzFhYx\"]//span ")
    private List<WebElement> depositCheckbox;

    @FindBy(xpath = "//div[@data-placement]//li")
    private List<WebElement> dropDownList;

    @FindBy(xpath = "//div[@class=\"MultiSelectDropdown___sc-1mda0kj-1 kFKwxK\"]")
    private WebElement bankList;

    @FindBy(xpath = "//button[@class=\"sc-eCssSg xVSDo\"]")
    private WebElement settingsShowButton;
    @FindBy(xpath = "//button[@class=\"sc-eCssSg blUkiR\"]")
    private WebElement showButton;

    @FindBy(xpath = "//div[@class=\"sc-kIeTtH hNSQWJ\"]")
    private WebElement suitableDepositCount;

    @FindBy(xpath = "//div[@class=\"sc-gsTCUz LVeSn\"]")
    private List<WebElement> depositResults;

    @Step("Закрытие окна акции \"Кэшбэк за вклад\"")
    public DepositPage closeCashBackWindow() {
        try {
            waitUntilElementIsVisible(cashBackWindow);
            actions.moveByOffset(200, 200).click().perform();
        } catch (NoSuchElementException | TimeoutException ignore) {
        }
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Нажатие кнопки настроек вклада")
    public DepositPage clickDepositSettingsButton() {
        settingsShowButton.click();
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Ввод/Выбор значения \"{value}\" в поле \"{fieldName}\"")
    public DepositPage valueInput(String fieldName, String... value) {
        switch (fieldName) {
            case "Сумма":
                WebElement inputField = depositInputFields.stream()
                        .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                                .getText().contains(fieldName))
                        .findAny()
                        .get();
                inputField.sendKeys(value[0]);
                break;
            case "Срок":
                WebElement periodDropDownField = depositInputFields.stream()
                        .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                                .getText().contains(fieldName))
                        .map(element -> element.findElement(By.xpath(".//following-sibling::div[@data-test=\"dropdown\"]")))
                        .findAny()
                        .get();
                periodDropDownField.click();
                dropDownList.stream()
                        .filter(element -> element.findElement(By.xpath("./div")).getText()
                                .contains(value[0]))
                        .findAny()
                        .get()
                        .click();
                break;
            case "Банки":
                WebElement banksDropDownField = depositInputFields.stream()
                        .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                                .getText().contains(fieldName))
                        .map(element -> element.findElement(By.xpath(".//..")))
                        .findAny()
                        .get();
                banksDropDownField.click();
                for (String bankName : value) {
                    WebElement bank = dropDownList.stream()
                            .filter(element -> element.getText().contains(bankName))
                            .findAny()
                            .get();
                    bank.click();
//                    wait.until(ExpectedConditions.attributeContains(bank.findElement(By.xpath(".//input")),
//                            "checked",
//                            ""));
                }
                banksDropDownField.click();
                break;
            case "Тип вклада":
                WebElement depositTypeDropDownField = depositInputFields.stream()
                        .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                                .getText().contains(fieldName))
                        .map(element -> element.findElement(By.xpath(".//following-sibling::div[@data-test=\"dropdown\"]")))
                        .findAny()
                        .get();
                depositTypeDropDownField.click();
                dropDownList.stream()
                        .filter(element -> element.findElement(By.xpath("./div")).getText().contains(value[0]))
                        .findAny()
                        .get()
                        .click();
                break;
            case "Опции":
                for (String option : value) {
                    WebElement checkBox = depositCheckbox.stream()
                            .filter(element -> element.getText().contains(option))
                            .findAny()
                            .get();
                    checkBox.click();

                    wait.until(ExpectedConditions.attributeContains(checkBox.findElement(By.xpath(".//preceding-sibling::input")),
                            "checked",
                            "true"));
                }
                break;
            default:
                Assertions.fail("Поле " + fieldName + " отсутствует");
        }
        waitForStability(5000, 250);
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Нажатие кнопки \"Показать\"")
    public DepositPage clickShowButton() {
        showButton.click();
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Проверка соответствия ожидаемых значений индикаторов вклада \"{deposit}\" актуальным")
    public DepositPage checkResult(String count, Deposit deposit) {
        Assertions.assertAll("Сравнение показателей",
                () -> Assertions.assertEquals(count,
                        valueTextHandler(suitableDepositCount.getText()),
                        "Количество подходящих вкладов не соответствует " + count),
                () -> Assertions.assertEquals(deposit.getRate(),
                        getValue(deposit.getBankName(), "Cтавка"),
                        "Актуальное значение процентной ставки вклада не равна " + deposit.getRate()),
                () -> Assertions.assertEquals(deposit.getPeriod(),
                        getValue(deposit.getBankName(), "Срок"),
                        "Актуальное значение срока вклада не равна " + deposit.getPeriod()),
                () -> Assertions.assertEquals(deposit.getProfit(),
                        getValue(deposit.getBankName(), "Доход"),
                        "Актуальное значение дохода вклада не равна " + deposit.getProfit())
        );
        return pageManager.getPage(DepositPage.class);
    }

    private String getValue(String bankName, String indicator) {
        WebElement currentBank = depositResults.stream()
                .filter(element -> element.findElement(By.xpath(".//div[@data-test=\"text\"]"))
                        .getText().contains(bankName))
                .findAny()
                .get();
        return currentBank.findElements(By.xpath(".//div[@data-test=\"text\"]")).stream()
                .filter(element -> element.getText().contains(indicator))
                .map(element -> element.findElement(By.xpath(".//following-sibling::div[@data-test=\"text\"]")).getText())
                .findAny()
                .get();
    }

    private void waitForStability(int maxWaitMillis, int pollDelimiter) {
        double start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + maxWaitMillis) {
            String prevState = driverManager.getDriver().getPageSource();
            try {
                Thread.sleep(pollDelimiter);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (prevState.equals(driverManager.getDriver().getPageSource())) {
                return;
            }
        }
    }

}
