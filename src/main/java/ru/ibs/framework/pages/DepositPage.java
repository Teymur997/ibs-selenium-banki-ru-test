package ru.ibs.framework.pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import ru.ibs.framework.utils.Deposit;

import java.util.List;

public class DepositPage extends BasePage {
    @FindBy(xpath = "//div[@class=\"sc-hBEYos czEoCN\"]")
    private WebElement cashBackWindow;

    @FindBy(xpath = "//div[@class=\"SearchModal__StyledBody-sc-wuz0ak-1 eQdrBU\"]//input")
    private List<WebElement> depositInputFields;

    @FindBy(xpath = "//label[@class=\"Checkbox___sc-h4eidb-1 lmixWG\"]//span")
    private List<WebElement> depositCheckbox;

    @FindBy(xpath = "//div[@data-placement]//li")
    private List<WebElement> dropDownList;

    @FindBy(xpath = "//ul[@class=\"MultiSelectDropdownList___sc-1rhwus9-0 crLBBK\"]//li")
    private List<WebElement> checkedBanks;

    @FindBy(xpath = "//button[@class=\"Button___sc-mcd2wg-2 gjpWjG\"]")
    private WebElement settingsShowButton;
    @FindBy(xpath = "//button[@class=\"Button___sc-mcd2wg-2 EXrjP\"]")
    private WebElement showButton;

    @FindBy(xpath = "//div[@class=\"Text___sc-14s2757-0 kDxXVe\"]")
    private WebElement suitableDepositCount;

    @FindBy(xpath = "//div[contains(@class, 'styled__StyledListItem')]")
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
                        .findAny()
                        .get();
                banksDropDownField.click();
                for (String bankName : value) {
                    banksDropDownField.sendKeys(bankName);
                    WebElement bank = dropDownList.stream()
                            .filter(element -> element.getText().contains(bankName))
                            .findAny()
                            .get();
                    bank.click();
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
                }
                break;
            default:
                Assertions.fail("Поле " + fieldName + " отсутствует");
        }
        waitForStability(5000, 250);
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Проверка корректности введённой суммы вклада")
    public DepositPage checkValueField(String expected) {
        String actual = depositInputFields.stream()
                .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                        .getText().contains("Сумма"))
                .findAny()
                .get().getAttribute("value");
        Assertions.assertEquals(expected, actual, "Значение в поле \"Сумма\" не равно введенному значению " + expected);
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Проверка корректности выбранного срока вклада")
    public DepositPage checkPeriodField(String expected) {
        String actual = depositInputFields.stream()
                .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                        .getText().contains("Срок"))
                .map(element -> element.findElement(By.xpath(".//following-sibling::div[@data-test=\"dropdown\"]")))
                .findAny()
                .get().getText();
        Assertions.assertEquals(expected, actual, "Значение в поле \"Срок\" не равно выбранному значению \"" + expected + "\"");
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Проверка корректности выбранного типа вклада")
    public DepositPage checkTypeField(String expected) {
        String actual = depositInputFields.stream()
                .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                        .getText().contains("Тип вклада"))
                .map(element -> element.findElement(By.xpath(".//following-sibling::div[@data-test=\"dropdown\"]")))
                .findAny()
                .get().getText();
        Assertions.assertEquals(expected, actual, "Значение в поле \"Тип вклада\" не равно выбранному значению \"" + expected + "\"");
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Проверка состояния чекбоксов выбранных банков")
    public DepositPage checkSelectedBanks(String... expectedBanks) {
        WebElement banksDropDownField = depositInputFields.stream()
                .filter(element -> element.findElement(By.xpath(".//following-sibling::label"))
                        .getText().contains("Банки"))
                .map(element -> element.findElement(By.xpath(".//..")))
                .findAny()
                .get();
        banksDropDownField.click();
        for (String bankName : expectedBanks) {
            boolean isBankChoosed = checkedBanks.stream()
                    .anyMatch(element -> element.getText().contains(bankName));
            Assertions.assertTrue(isBankChoosed, "Банк \"" + bankName + "\"не выбран");
        }
        banksDropDownField.click();
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Проверка состояния чекбоксов выбора дополнительных опций")
    public DepositPage checkSelectedAdditionals(String... additionals) {
        for (String additional : additionals) {
            WebElement checkBox = depositCheckbox.stream()
                    .filter(element -> element.getText().contains(additional))
                    .findAny()
                    .get();
            boolean isAdditionalChecked = checkBox.findElement(By.xpath(".//preceding-sibling::input"))
                    .getAttribute("checked")
                    .contains("true");
            Assertions.assertTrue(isAdditionalChecked, "Дополнительная опция \"" + additional + "\" не выбрана");
        }
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
                        "Актуальное значение процентной ставки вклада не равно " + deposit.getRate()),
                () -> Assertions.assertEquals(deposit.getPeriod(),
                        getValue(deposit.getBankName(), "Срок"),
                        "Актуальное значение срока вклада не равно " + deposit.getPeriod()),
                () -> Assertions.assertEquals(deposit.getProfit(),
                        getValue(deposit.getBankName(), "Доход"),
                        "Актуальное значение дохода вклада не равно " + deposit.getProfit())
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
