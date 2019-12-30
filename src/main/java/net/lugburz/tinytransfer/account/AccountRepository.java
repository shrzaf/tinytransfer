package net.lugburz.tinytransfer.account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for bank accounts.
 *
 * <p>
 * Assumptions:
 * <ul>
 *     <li>Account numbers are non-empty strings./li>
 *     <li>Balance may never be negative.</li>
 * </ul>
 */
public class AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Returns the account data for a given account number.
     *
     * @param accountNo the account number
     * @return an instance of {@link Account}
     * @throws AccountException if the provided account number is unknown
     */
    public Account find(final String accountNo) {
        if (!accounts.containsKey(accountNo)) {
            throw new AccountException("Unknown account provided");
        }
        return accounts.get(accountNo);
    }

    /**
     * Creates a new account in a thread-safe manner.
     *
     * @param accountNo the account number, may not be null
     * @param balance   the starting balance, may not be negative
     * @throws AccountException if the provided account number is invalid or belongs to an existing account
     *                          or if the provided balance is negative
     */
    public synchronized void create(final String accountNo, final BigDecimal balance) {
        validateAccountNo(accountNo);
        validateBalance(balance);
        if (accounts.containsKey(accountNo)) {
            throw new AccountException("Account No. already exists.");
        }
        accounts.put(accountNo, new Account(accountNo, balance));
    }

    /**
     * Clears the repository.
     */
    public synchronized void clear() {
        accounts.clear();
    }

    /**
     * Determines data consistency by checking whether there is any negative account balance in the store.
     * <p>
     * Used for health-check purposes.
     *
     * @return true if no account has a negative balance, false otherwise
     */
    public boolean isConsistent() {
        return accounts.values().stream().noneMatch(acc -> acc.getBalance().compareTo(BigDecimal.ZERO) < 0);
    }


    private void validateAccountNo(final String accountNo) {
        if (accountNo == null || accountNo.trim().isEmpty()) {
            throw new AccountException("Invalid account number provided.");
        }
    }

    private void validateBalance(final BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountException("Balance may not be negative.");
        }
    }
}
