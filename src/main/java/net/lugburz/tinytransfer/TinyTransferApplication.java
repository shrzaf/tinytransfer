package net.lugburz.tinytransfer;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import net.lugburz.tinytransfer.account.AccountRepository;
import net.lugburz.tinytransfer.api.BankingResource;

/**
 * Main class of the application.
 */
public class TinyTransferApplication extends Application<TinyTransferConfiguration> {

    private static final String APP_NAME = "TinyTransfer";

    public static void main(final String[] args) throws Exception {
        new TinyTransferApplication().run(args);
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public void run(TinyTransferConfiguration configuration, Environment environment) {
        final AccountRepository repository = new AccountRepository();
        final BankingResource resource = new BankingResource(repository);
        environment.healthChecks().register("data", new DataHealthCheck(repository));
        environment.jersey().register(resource);
    }

}
