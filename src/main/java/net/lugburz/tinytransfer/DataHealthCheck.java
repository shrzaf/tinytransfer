package net.lugburz.tinytransfer;


import com.codahale.metrics.health.HealthCheck;
import net.lugburz.tinytransfer.account.AccountRepository;

/**
 * Health check for in-memory data store.
 */
public class DataHealthCheck extends HealthCheck {
    private final AccountRepository repository;

    public DataHealthCheck(final AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Result check() {
        if (!repository.isConsistent()) {
            return Result.unhealthy("The in-memory data store contains inconsistent data.");
        }
        return Result.healthy();
    }
}