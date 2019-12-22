package net.lugburz.tinytransfer.transfer;

import net.lugburz.tinytransfer.repository.Account;
import net.lugburz.tinytransfer.repository.AccountException;
import net.lugburz.tinytransfer.repository.AccountRepository;

import java.math.BigDecimal;

/**
 * Provides functionality to transfer money between accounts.
 */
public class TransferService {

    private final AccountRepository repository;

    public TransferService(final AccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Performs a money transfer.
     *
     * @param senderAccNo   the account number of the sender
     * @param receiverAccNo the account number of the receiver
     * @param amount        the amount to be transferred, must be a non-zero positive value
     * @throws TransferException on failure to transfer due to unknown sender or receiver account,
     */
    public void transfer(final String senderAccNo, final String receiverAccNo, final BigDecimal amount) {

        try {
            final Account sender = repository.find(senderAccNo);
            final Account receiver = repository.find(receiverAccNo);

            validateAmount(amount);

            adjustBalances(amount, sender, receiver);

        } catch (final AccountException exception) {
            throw new TransferException("Money transfer failed.", exception);
        }
    }

    private void adjustBalances(final BigDecimal amount, final Account sender, final Account receiver) {
        sender.withdraw(amount);
        receiver.deposit(amount);
    }

    private void validateAmount(final BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferException("The provided amount is not greater than zero.");
        }
    }
}
