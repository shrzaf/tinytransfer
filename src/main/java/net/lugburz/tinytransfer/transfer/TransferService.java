package net.lugburz.tinytransfer.transfer;

import java.math.BigDecimal;

/**
 * Interface for money transfer functionality.
 */
public interface TransferService {

    /**
     * Performs a money transfer.
     *
     * @param senderAccNo   the account number of the sender
     * @param receiverAccNo the account number of the receiver
     * @param amount        the amount to be transferred, must be a non-zero positive value
     * @throws TransferException on failure to transfer due to unknown sender or receiver account,
     */
    void transfer(String senderAccNo, String receiverAccNo, BigDecimal amount);
}
