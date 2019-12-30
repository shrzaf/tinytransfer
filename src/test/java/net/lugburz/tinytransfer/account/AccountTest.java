package net.lugburz.tinytransfer.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Account}.
 */
class AccountTest {

    private Account sut;

    @BeforeEach
    public void setup() {
        sut = new Account("123", BigDecimal.valueOf(10.42));
    }

    @Test
    public void withdraw_negativeAmount_shouldThrowAccountException() {
        assertThrows(AccountException.class, () -> sut.withdraw(BigDecimal.valueOf(-1)));
    }

    @Test
    public void withdraw_amountGtBalance_shouldThrowAccountException() {
        assertThrows(AccountException.class, () -> sut.withdraw(BigDecimal.valueOf(10.421)));
    }

    @Test
    public void withdraw_amountEqBalance_shouldYieldZeroBalance() {
        sut.withdraw(BigDecimal.valueOf(10.42));

        assertThat(sut.getBalance().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    public void withdraw_amountGrBalance_shouldSubtractAmount() {
        sut.withdraw(BigDecimal.valueOf(10.41));

        assertThat(sut.getBalance()).isEqualTo(BigDecimal.valueOf(0.01));
    }

    @Test
    public void deposit_negativeAmount_shouldThrowAccountException() {
        assertThrows(AccountException.class, () -> sut.deposit(BigDecimal.valueOf(-1)));
    }

    @Test
    public void deposit_positiveAmount_shouldAddAmount() {
        sut.deposit(BigDecimal.valueOf(0.42));

        assertThat(sut.getBalance()).isEqualTo(BigDecimal.valueOf(10.84));
    }
}
