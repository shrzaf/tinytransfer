package net.lugburz.tinytransfer.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Minimal model for a bank account.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public final class Account {
    private final String accountNo;
    private final long balance;
}
