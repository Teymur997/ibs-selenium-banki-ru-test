package ru.ibs.framework.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ibs.framework.base.BaseTests;
import ru.ibs.framework.pages.blocks.MenuBlockPage;
import ru.ibs.framework.utils.*;

import java.util.stream.Stream;

@ExtendWith(AllureListener.class)
public class BankiRuParameterizedTest extends BaseTests {
    @ParameterizedTest
    @MethodSource("depositInfo")
    @DisplayName("Поиск вклада с заданными параметрами")
    public void bankiTest(Parameters parameter) {
        Deposit deposit = parameter.getDeposit();
        String count = parameter.getCount();
        DepositParameters depositParameters = parameter.getDepositParameters();


        pageManager.getPage(MenuBlockPage.class)
                .checkIfPageIsOpened()
                .closeRegionSelectButton()
                .chooseMenu("Вклады")
                .clickDepositSettingsButton()
                .valueInput("Сумма", depositParameters.getDepositValue())
                .valueInput("Срок", depositParameters.getDepositPeriod())
                .valueInput("Тип вклада", depositParameters.getType())
                .valueInput("Банки", depositParameters.getBanks())
                .valueInput("Опции", depositParameters.getAdditionals())
                .checkValueField(depositParameters.getDepositValue())
                .checkPeriodField(depositParameters.getDepositPeriod())
                .checkTypeField(depositParameters.getType())
                .checkSelectedBanks(depositParameters.getBanks())
                .checkSelectedAdditionals(depositParameters.getAdditionals())
                .clickShowButton()
                .closeCashBackWindow()
                .checkResult(count, deposit);
    }


    public static Stream<Parameters> depositInfo() {
        return Stream.of(
                new Parameters(
                        new DepositParameters("1 000 000", DepositPeriod.SIX_MONTH.getPeriod(), DepositType.ORDINARY_DEPOSITS.getType(),
                                new String[]{"Тинькофф", "ВТБ", "Открытие", "Газпромбанк", "Сбербанк"},
                                new String[]{"Со снятием", "С пополнением", "С капитализацией"}),
                        "15",
                        new Deposit("Тинькофф Банк", "5,63%", "182 дн.", "от 27 740 ₽")),
                new Parameters(
                        new DepositParameters("500 000", DepositPeriod.TWO_YEARS.getPeriod(), DepositType.CHILDISH.getType(),
                                new String[]{"Ак Барс Банк", "Банк «РОССИЯ»", "Сбербанк"},
                                new String[]{"С выплатой процентов"}),
                        "7",
                        new Deposit("Сбербанк", "6,80%", "730 дн.", "от 72 514 ₽"))
        );
    }
}
