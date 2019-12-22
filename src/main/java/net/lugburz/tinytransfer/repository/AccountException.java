package net.lugburz.tinytransfer.repository;

/**
 * Thrown on attempts to create or manipulate an account instance with invalid field values.
 */
public class AccountException extends RuntimeException {

    AccountException(final String message)  {
        super(message);
    }
}
