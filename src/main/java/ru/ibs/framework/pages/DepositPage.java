package ru.ibs.framework.pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.ibs.framework.utils.Deposit;

import java.util.List;

public class DepositPage extends BasePage {
    @FindBy(xpath = "//div[@class=\"Modal___sc-fqhr8t-2 cDCRpi\"]")
    private WebElement cashBackWindow;

    @FindBy(xpath = "//div[@id=\"deposit_search_form\"]")
    private WebElement depositSearchForm;

    @FindBy(xpath = "//div[@class=\"SearchModal__StyledBody-sc-wuz0ak-1 eQdrBU\"]//input")
    private List<WebElement> depositInputFields;

    @FindBy(xpath = "//div[@class=\"FlexboxGrid___sc-fhs6fg-0 cEciIm\"]//span")
    private List<WebElement> depositCheckbox;

    @FindBy(xpath = "//div[@class=\"DropdownList___sc-19399gi-3 ccVKRu\"]//li")
    private List<WebElement> dropDownList;

    @FindBy(xpath = "//li[@class=\"MultiSelectDropdownList___sc-1rhwus9-1 fahRpT\"]")
    private List<WebElement> bankListDropdown;

    @FindBy(xpath = "//div[@class=\"MultiSelectDropdown___sc-1mda0kj-1 kFKwxK\"]")
    private WebElement bankList;

    @FindBy(xpath = "//div[contains(@class,'FlexboxGrid___sc-fhs6fg-0 hjbxXD')]//button")
    private WebElement showButton;

    @FindBy(xpath = "//div[@class=\"Text___sc-14s2757-0 kDxXVe\"]")
    private WebElement suitableDepositCount;

    @FindBy(xpath = "//div[@class=\"FlexboxGrid___sc-fhs6fg-0 cLYEEl\"]")
    private List<WebElement> depositResults;

    @Step("Закрытие окна акции \"Кэшбэк за вклад\"")
    public DepositPage closeCashBackWindow() {
        try {
            waitUntilElementIsVisible(cashBackWindow);
            driverManager.getDriver().switchTo().defaultContent();
        } catch (NoSuchElementException | TimeoutException ignore) {
        }
        return pageManager.getPage(DepositPage.class);
    }

    @Step("Нажатие кнопки настроек вклада")
    public DepositPage clickDepositSettingsButton() {
        depositSearchForm
                .findElements(By.xpath(".//button"))
                .stream()
                .filter(element -> element.getText().length() == 0)
                .findAny()
                .get()
                .click();
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
                    WebElement bank = bankListDropdown.stream()
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
                .map(element -> element.findElement(By.xpath(".//following-sibling::div")).getText())
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
