package net.lugburz.tinytransfer.account;

/**
 * Thrown on attempts to create or manipulate an account instance with invalid field values.
 */
public final class AccountException extends RuntimeException {

    public AccountException(final String message)  {
        super(message);
    }
}
