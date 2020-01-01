package net.lugburz.tinytransfer.transfer;

import net.lugburz.tinytransfer.account.Account;
import net.lugburz.tinytransfer.account.AccountException;
import net.lugburz.tinytransfer.account.AccountRepository;

import java.math.BigDecimal;

/**
 * Transfer service implementation for in-memory account store.
 */
public final class InMemoryTransferService implements TransferService {

    private final AccountRepository repository;

    public InMemoryTransferService(final AccountRepository repository) {
        this.repository = repository;
    }


    @Override
    public void transfer(final String senderAccNo, final String receiverAccNo, final BigDecimal amount) {

        try {
            final Account sender = repository.find(senderAccNo);
            final Account receiver = repository.find(receiverAccNo);

            validateAmount(amount);

            adjustBalances(amount, sender, receiver);

        } catch (final AccountException exception) {
            throw new TransferException("Money transfer failed: " + exception.getMessage(), exception);
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
