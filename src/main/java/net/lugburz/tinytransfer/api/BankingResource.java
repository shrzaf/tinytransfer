package net.lugburz.tinytransfer.api;

import net.lugburz.tinytransfer.account.Account;
import net.lugburz.tinytransfer.account.AccountException;
import net.lugburz.tinytransfer.account.AccountRepository;
import net.lugburz.tinytransfer.transfer.TransferException;
import net.lugburz.tinytransfer.transfer.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST API for banking operations.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class BankingResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final TransferService transferService;
    private final AccountRepository repository;

    public BankingResource(final AccountRepository repository) {
        this.repository = repository;
        this.transferService = new TransferService(repository);
    }

    /**
     * Returns a single account.
     *
     * @param accountNo the account number
     * @return HTTP 200 with a single {@link net.lugburz.tinytransfer.account.Account} on success,
     * HTTP 404 on unknown account number, HTTP 500 on internal errors
     */
    @GET
    @Path("/accounts/{accountNo}")
    public Account getAccount(@PathParam("accountNo") final String accountNo) {
        try {
            return repository.find(accountNo);

        } catch (final AccountException exception) {
            log.error("No account find for the given account number.", exception);
            throw new WebApplicationException(exception.getMessage(), Response.Status.NOT_FOUND);

        } catch (final RuntimeException exception) {
            log.error("Failed to fetch account information.", exception);
            throw new WebApplicationException(exception.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new account.
     *
     * @param account JSON representation of the account data. Example: {"accountNo": "123", balance: 10.42}
     * @return HTTP 200 on successful creation, HTTP 400 on failure due to bad parameters,
     * HTTP 422 on invalid parameters, HTTP 500 on internal errors
     */
    @POST
    @Path("/accounts")
    public Response createAccount(@NotNull @Valid final AccountCreationRequest account) {
        try {
            repository.create(account.getAccountNo(), account.getBalance());
            return Response.ok().build();

        } catch (final AccountException exception) {
            log.error("Account creation failed.", exception);
            throw new BadRequestException(exception.getMessage());

        } catch (final RuntimeException exception) {
            log.error("Account creation failed.", exception);
            throw new InternalServerErrorException(exception.getMessage());
        }
    }

    /**
     * Resets the internal data store by deleting all stored accounts.
     */
    @POST
    @Path("/accounts/reset")
    public Response reset() {
        repository.clear();
        return Response.ok().build();
    }

    /**
     * Transfers an amount from one account to another.
     *
     * @param request the transfer request containing the fields {@code senderAccNo}, {@code receiverAccNo} and
     *                {@code balance}. The balance must be a non-negative number. Example:
     *                {"senderAccNo": "123", "receiverAccNo": "456", balance: 10.42}
     * @return HTTP 200 on successful transfer, HTTP 400 on failure due to bad parameters,
     * HTTP 422 on invalid parameters, HTTP 500 on internal errors
     */
    @POST
    @Path("/transfer")
    public Response transfer(@NotNull @Valid final TransferRequest request) {
        try {
            transferService.transfer(request.getSenderAccNo(), request.getReceiverAccNo(), request.getAmount());
            return Response.ok().build();

        } catch (final TransferException exception) {
            log.error("Transfer request failed.", exception);
            throw new BadRequestException(exception.getMessage());

        } catch (final RuntimeException exception) {
            log.error("Transfer request failed.", exception);
            throw new InternalServerErrorException(exception.getMessage());
        }
    }
}
