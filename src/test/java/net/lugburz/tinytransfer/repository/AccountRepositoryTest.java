package net.lugburz.tinytransfer.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link AccountRepository}.
 */
class AccountRepositoryTest {

    private static final String ACCOUNT_NO_1 = "123";
    private static final String ACCOUNT_NO_2 = "456";
    private static final long ACCOUNT_BALANCE_1 = 100;
    private static final long ACCOUNT_BALANCE_2 = 20;

    private AccountRepository sut;

    @BeforeEach
    public void setup() {
        final Account foo = new Account(ACCOUNT_NO_1, ACCOUNT_BALANCE_1);
        final Account bar = new Account(ACCOUNT_NO_2, ACCOUNT_BALANCE_2);

        sut = new AccountRepository();
        sut.create(foo.getAccountNo(), foo.getBalance());
        sut.create(bar.getAccountNo(), bar.getBalance());
    }

    @Test
    public void find_onUnknownAccount_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.find("789"));
    }

    @Test
    public void find_onExistingAccount_shouldReturnAccount() {
        final Account acc = sut.find(ACCOUNT_NO_1);

        assertThat(acc).isEqualTo(new Account(ACCOUNT_NO_1, 100));
    }

    @Test
    public void create_onExistingAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(ACCOUNT_NO_1, 0));
    }

    @Test
    public void create_onNullAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(null, 0));
    }

    @Test
    public void create_onBlankAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(" ", 0));
    }

    @Test
    public void create_onNegativeBalance_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(ACCOUNT_NO_1, -1));
    }

    @Test
    public void create_onNewAccountNo_shouldCreateAccount() {
        sut.create("789", 42);

        assertThat(sut.find("789")).isEqualTo(new Account("789", 42));
    }

    @Test
    public void update_onUnknownAccount_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update("789", 0));
    }

    @Test
    public void update_onNullAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update(null, 0));
    }

    @Test
    public void update_onBlankAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update(" ", 0));
    }

    @Test
    public void update_onNegativeBalance_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update(" ", -1));
    }

    @Test
    public void update_onExistingAccount_shouldUpdateBalance() {
        sut.update(ACCOUNT_NO_1, 42);

        assertThat(sut.find("123")).isEqualTo(new Account(ACCOUNT_NO_1, 42));
    }
}