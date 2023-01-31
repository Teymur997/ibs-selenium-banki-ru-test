package ru.ibs.framework.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.ibs.framework.base.BaseTests;
import ru.ibs.framework.pages.blocks.MenuBlockPage;
import ru.ibs.framework.utils.AllureListener;
import ru.ibs.framework.utils.Deposit;

@ExtendWith(AllureListener.class)
public class BankiRuTest extends BaseTests {
    @Test
    @Disabled
    @DisplayName("Поиск вклада")
    public void bankiTest() {
        Deposit SberBank = new Deposit("Сбербанк", "6,80%", "730 дн.", "от 72 514 ₽");
        pageManager.getPage(MenuBlockPage.class)
                .checkIfPageIsOpened()
                .closeRegionSelectButton()
                .chooseMenu("Вклады")
                .clickDepositSettingsButton()
                .valueInput("Сумма", "500000")
                .valueInput("Срок", "2 года")
                .valueInput("Тип вклада", "Детский")
                .valueInput("Банки", "Ак Барс Банк", "Банк «РОССИЯ»", "Сбербанк")
                .valueInput("Опции", "С выплатой процентов")
                .clickShowButton()
                .closeCashBackWindow()
                .checkResult("7", SberBank);
    }
}
