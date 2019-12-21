package net.lugburz.tinytransfer.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for bank accounts.
 *
 * <p>
 * Assumptions:
 * <ul>
 *     <li>Account numbers are non-empty strings./li>
 *     <li>Balance may never be negative, maximum balance is {@link Long#MAX_VALUE}.</li>
 * </ul>
 */
public class AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Returns the account data for a given account number.
     *
     * @param accountNo the account number
     * @return an instance of {@link Account}
     * @throws RepositoryException if the provided account number is unknown
     */
    public Account find(final String accountNo) {
        if (!accounts.containsKey(accountNo)) {
            throw new RepositoryException("Unknown account provided");
        }
        return accounts.get(accountNo);
    }

    /**
     * Creates a new account.
     *
     * @param accountNo the account number, may not be null
     * @param balance   the starting balance
     * @throws RepositoryException if the provided account number is invalid or belongs to an existing account
     */
    public void create(final String accountNo, final long balance) {
        validateAccountNo(accountNo);
        validateBalance(balance);
        if (accounts.containsKey(accountNo)) {
            throw new RepositoryException("Account No. already exists.");
        }
        accounts.put(accountNo, new Account(accountNo, balance));
    }

    /**
     * Updates the balance of an existing account.
     *
     * @param accountNo  the account number, may not be null
     * @param newBalance the new balance to store
     * @throws RepositoryException if the provided account number is invalid or is unknown
     */
    public void update(final String accountNo, final long newBalance) {
        validateAccountNo(accountNo);
        validateBalance(newBalance);
        if (!accounts.containsKey(accountNo)) {
            throw new RepositoryException("Unknown account provided.");
        }
        accounts.put(accountNo, new Account(accountNo, newBalance));
    }

    private void validateAccountNo(final String accountNo) {
        if (accountNo == null || accountNo.trim().isEmpty()) {
            throw new RepositoryException("Invalid account number provided.");
        }
    }

    private void validateBalance(final long balance) {
        if (balance < 0) {
            throw new RepositoryException("Balance may not be negative.");
        }
    }
}
