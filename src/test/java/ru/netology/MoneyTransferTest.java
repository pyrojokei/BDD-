package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        String firstCardNumber = DataHelper.getFirstCardMaskedNumber();
        String secondCardNumber = DataHelper.getSecondCardMaskedNumber();

        int firstCardBalance = dashboardPage.getCardBalance(firstCardNumber);
        int secondCardBalance = dashboardPage.getCardBalance(secondCardNumber);

        int amount = 5000;
        var secondCardInfo = DataHelper.getSecondCardInfo();

        var transferPage = dashboardPage.selectCardToTransfer(firstCardNumber);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);

        int expectedBalanceFirstCard = firstCardBalance + amount;
        int expectedBalanceSecondCard = secondCardBalance - amount;

        assertEquals(expectedBalanceFirstCard, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(expectedBalanceSecondCard, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        String firstCardNumber = DataHelper.getFirstCardMaskedNumber();
        String secondCardNumber = DataHelper.getSecondCardMaskedNumber();

        int firstCardBalance = dashboardPage.getCardBalance(firstCardNumber);
        int secondCardBalance = dashboardPage.getCardBalance(secondCardNumber);

        int amount = 3000;
        var firstCardInfo = DataHelper.getFirstCardInfo();

        var transferPage = dashboardPage.selectCardToTransfer(secondCardNumber);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), firstCardInfo);

        int expectedBalanceFirstCard = firstCardBalance - amount;
        int expectedBalanceSecondCard = secondCardBalance + amount;

        assertEquals(expectedBalanceFirstCard, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(expectedBalanceSecondCard, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldNotChangeBalanceIfTransferZero() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        String firstCardNumber = DataHelper.getFirstCardMaskedNumber();
        String secondCardNumber = DataHelper.getSecondCardMaskedNumber();

        int firstCardBalance = dashboardPage.getCardBalance(firstCardNumber);
        int secondCardBalance = dashboardPage.getCardBalance(secondCardNumber);

        int amount = 0;
        var secondCardInfo = DataHelper.getSecondCardInfo();

        var transferPage = dashboardPage.selectCardToTransfer(firstCardNumber);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);

        assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldGetErrorIfTransferAmountIsMoreThanBalance() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        String firstCardNumber = DataHelper.getFirstCardMaskedNumber();
        String secondCardNumber = DataHelper.getSecondCardMaskedNumber();

        int secondCardBalance = dashboardPage.getCardBalance(secondCardNumber);

        int amount = secondCardBalance + 5000;
        var secondCardInfo = DataHelper.getSecondCardInfo();

        var transferPage = dashboardPage.selectCardToTransfer(firstCardNumber);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);

        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardNumber));
    }

}
