package net.lugburz.tinytransfer.repository;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Account}.
 */
class AccountTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(Account.class).verify();
    }

}