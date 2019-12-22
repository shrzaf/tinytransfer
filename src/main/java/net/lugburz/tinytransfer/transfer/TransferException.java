package net.lugburz.tinytransfer.transfer;

/**
 * Thrown on failure to perform money transfer.
 */
public class TransferException extends RuntimeException {

    public TransferException(final String message) {
        super(message);
    }
    public TransferException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
