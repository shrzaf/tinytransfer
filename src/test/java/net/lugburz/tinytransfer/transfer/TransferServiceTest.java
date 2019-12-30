package net.lugburz.tinytransfer.transfer;

import net.lugburz.tinytransfer.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link TransferService}.
 */
class TransferServiceTest {

    private static final String ACC_NO_1 = "123";
    private static final String ACC_NO_2 = "456";

    private AccountRepository repo;
    private TransferService sut;

    @BeforeEach
    public void setup() {
        repo = new AccountRepository();
        repo.create(ACC_NO_1, BigDecimal.valueOf(10));
        repo.create(ACC_NO_2, BigDecimal.valueOf(10));
        sut = new TransferService(repo);
    }

    @Test
    public void transfer_onZeroAmount_shouldThrowTransferException() {
        assertThrows(TransferException.class, () -> sut.transfer(ACC_NO_1, ACC_NO_2, BigDecimal.valueOf(0)));
    }

    @Test
    public void transfer_onNegativeAmount_shouldThrowTransferException() {
        assertThrows(TransferException.class, () -> sut.transfer(ACC_NO_1, ACC_NO_2, BigDecimal.valueOf(-1)));
    }

    @Test
    public void transfer_onInsufficientSenderBalance_shouldThrowTransferException() {
        assertThrows(TransferException.class, () -> sut.transfer(ACC_NO_1, ACC_NO_2, BigDecimal.valueOf(11)));
    }

    @Test
    public void transfer_onUnknownSender_shouldThrowTransferException() {
        assertThrows(TransferException.class, () -> sut.transfer("42", ACC_NO_1, BigDecimal.valueOf(5)));
    }

    @Test
    public void transfer_onUnknownReceiver_shouldThrowTransferException() {
        assertThrows(TransferException.class, () -> sut.transfer(ACC_NO_1, "42", BigDecimal.valueOf(5)));
    }

    @Test
    public void transfer_onValidTransfer_shouldAdjustBalancesCorrectly() {
        sut.transfer(ACC_NO_1, ACC_NO_2, BigDecimal.valueOf(10));

        assertThat(repo.find(ACC_NO_1).getBalance()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(repo.find(ACC_NO_2).getBalance()).isEqualTo(BigDecimal.valueOf(20));
    }

    @Test
    public void transfer_multipleThreads_shouldYieldCorrectBalances() throws InterruptedException {
        repo.create("sender", BigDecimal.valueOf(1000));
        repo.create("receiver", BigDecimal.valueOf(1000));

        final List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep((int) ((Math.random() * (10)) + 1));
                } catch (InterruptedException e) {
                    fail("Test failed due to InterruptedException.");
                }
                sut.transfer("sender", "receiver", BigDecimal.valueOf(1));
            };
            runnables.add(runnable);
        }
        ExecutorService es = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            es.execute(runnables.get(i));
        }
        es.shutdown();
        es.awaitTermination(10, TimeUnit.SECONDS);

        final BigDecimal senderBalance = repo.find("sender").getBalance();
        final BigDecimal receiverBalance = repo.find("receiver").getBalance();

        assertThat(senderBalance).isEqualTo(BigDecimal.ZERO);
        assertThat(receiverBalance).isEqualTo(BigDecimal.valueOf(2000));
    }
}
