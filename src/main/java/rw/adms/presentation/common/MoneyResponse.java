package rw.adms.presentation.common;

import rw.adms.domain.shared.vo.Money;

import java.math.BigDecimal;

public record MoneyResponse(
        BigDecimal amount,
        String currency
) {

    public static MoneyResponse from(Money money) {

        if (money == null) {
            return null;
        }

        return new MoneyResponse(
                money.getAmount(),
                money.getCurrency()
        );
    }
}
