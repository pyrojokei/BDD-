package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$("ul.list.list_theme_alfa-on-white li");

    private final String balanceStart = "баланс: ";
    private final String balanceEnd = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String maskedCardNumber) {
        SelenideElement card = cards.findBy(text(maskedCardNumber)).shouldBe(visible);
        String text = card.text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(String maskedCardNumber) {
        cards.findBy(text(maskedCardNumber)).$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceEnd);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}

