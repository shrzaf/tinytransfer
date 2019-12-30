package net.lugburz.tinytransfer.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Thread-safe implementation of a minimal model for a bank account.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Account {

    @JsonProperty
    private String accountNo;

    @JsonProperty
    private BigDecimal balance;

    /**
     * Subtracts a given amount from the balance in a thread-safe manner.
     *
     * @param amount the amount to withdraw
     * @throws AccountException if the amount to withdraw is greater than the balance or is negative
     */
    public synchronized void withdraw(final BigDecimal amount) {
        if (isNegative(amount)) {
            throw new AccountException("Cannot withdraw negative amount.");
        }
        if (balance.compareTo(amount) < 0) {
            throw new AccountException("The account does not have sufficient balance for the withdrawal.");
        }
        balance = balance.subtract(amount);
    }

    /**
     * Adds a given amount to the balance in a thread-safe manner.
     *
     * @param amount the amount to deposit
     * @throws AccountException if the amount is negative
     */
    public synchronized void deposit(final BigDecimal amount) {
        if (isNegative(amount)) {
            throw new AccountException("Cannot deposit negative amount.");
        }
        balance = balance.add(amount);
    }

    private boolean isNegative(final BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
}
