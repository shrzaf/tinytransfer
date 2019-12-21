package net.lugburz.tinytransfer.repository;

/**
 * Thrown on invalid input provided to {@link AccountRepository}.
 */
public class RepositoryException extends RuntimeException {

    RepositoryException(final String message)  {
        super(message);
    }
}
