package net.lugburz.tinytransfer;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.lugburz.tinytransfer.api.BankingResource;
import net.lugburz.tinytransfer.repository.AccountRepository;

public class TinyTransferApplication extends Application<TinyTransferConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TinyTransferApplication().run(args);
    }

    @Override
    public String getName() {
        return "TinyTransfer";
    }

    @Override
    public void initialize(final Bootstrap<TinyTransferConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(TinyTransferConfiguration configuration,
                    Environment environment) {
        final AccountRepository repository = new AccountRepository();
        final BankingResource resource = new BankingResource(repository);
        environment.jersey().register(resource);
    }

}
