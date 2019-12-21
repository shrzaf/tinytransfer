package net.lugburz.tinytransfer.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link AccountRepository}.
 */
class AccountRepositoryTest {

    private static final String ACCOUNT_NO_1 = "123";
    private static final String ACCOUNT_NO_2 = "456";
    private static final BigDecimal ACCOUNT_BALANCE_1 = BigDecimal.valueOf(100);
    private static final BigDecimal ACCOUNT_BALANCE_2 = BigDecimal.valueOf(20);

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

        assertThat(acc).isEqualTo(new Account(ACCOUNT_NO_1, BigDecimal.valueOf(100)));
    }

    @Test
    public void create_onExistingAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(ACCOUNT_NO_1, BigDecimal.valueOf(0)));
    }

    @Test
    public void create_onNullAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(null, BigDecimal.valueOf(0)));
    }

    @Test
    public void create_onBlankAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(" ", BigDecimal.valueOf(0)));
    }

    @Test
    public void create_onNegativeBalance_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.create(ACCOUNT_NO_1, BigDecimal.valueOf(-1)));
    }

    @Test
    public void create_onNewAccountNo_shouldCreateAccount() {
        sut.create("789", BigDecimal.valueOf(42));

        assertThat(sut.find("789")).isEqualTo(new Account("789", BigDecimal.valueOf(42)));
    }

    @Test
    public void update_onUnknownAccount_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update("789", BigDecimal.valueOf(0)));
    }

    @Test
    public void update_onNullAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update(null, BigDecimal.valueOf(0)));
    }

    @Test
    public void update_onBlankAccountNo_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update(" ", BigDecimal.valueOf(0)));
    }

    @Test
    public void update_onNegativeBalance_shouldThrowRepositoryException() {
        assertThrows(RepositoryException.class, () -> sut.update(" ", BigDecimal.valueOf(-1)));
    }

    @Test
    public void update_onExistingAccount_shouldUpdateBalance() {
        sut.update(ACCOUNT_NO_1, BigDecimal.valueOf(42.042));

        assertThat(sut.find("123")).isEqualTo(new Account(ACCOUNT_NO_1, BigDecimal.valueOf(42.042)));
    }
}
