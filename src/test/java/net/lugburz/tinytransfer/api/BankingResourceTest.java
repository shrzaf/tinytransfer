package net.lugburz.tinytransfer.api;

import com.google.gson.JsonObject;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import net.lugburz.tinytransfer.TinyTransferApplication;
import net.lugburz.tinytransfer.TinyTransferConfiguration;
import net.lugburz.tinytransfer.repository.Account;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the REST endpoints in {@link BankingResource}.
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class BankingResourceTest {

    @ClassRule
    private static final DropwizardAppExtension<TinyTransferConfiguration> APP = new DropwizardAppExtension<>(TinyTransferApplication.class);

    private final Client client = APP.client();

    @BeforeEach
    public void setup() {
        client.target(getPath("/accounts")).request().post(Entity.json(new Account("foo", BigDecimal.valueOf(100.42))));
        client.target(getPath("/accounts")).request().post(Entity.json(new Account("bar", BigDecimal.valueOf(200))));
    }

    @AfterEach
    public void reset() {
        client.target(getPath("/accounts/reset")).request().get();
    }

    @Test
    public void getAccount_onExistingAccount_shouldReturn200WithAccount() {
        when().get("/accounts/foo").
                then().statusCode(200).
                body("accountNo", is("foo")).
                body("balance", is(100.42f));
    }

    @Test
    public void getAccount_onUnknownAccount_shouldReturn404() {
        when().get("/accounts/baz").
                then().statusCode(404);
    }

    @Test
    public void transfer_onValidRequest_shouldTransferAmount() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "bar");
        payload.addProperty("receiverAccNo", "foo");
        payload.addProperty("amount", 100);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(200);
        when().get("/accounts/foo").
                then().body("balance", is(200.42f));
        when().get("/accounts/bar").
                then().body("balance", is(100));
    }

    @Test
    public void transfer_onEmptyPayload_shouldReturn422() {
        given().contentType(MediaType.APPLICATION_JSON).
                when().post("/transfer").
                then().statusCode(422);
    }

    @Test
    public void transfer_onUnknownSender_shouldReturn400() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "baz");
        payload.addProperty("receiverAccNo", "foo");
        payload.addProperty("amount", 100);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(400);
    }

    @Test
    public void transfer_onEmptySender_shouldReturn422() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "");
        payload.addProperty("receiverAccNo", "foo");
        payload.addProperty("amount", 100);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(422);
    }

    @Test
    public void transfer_onMissingSender_shouldReturn422() {
        JsonObject payload = new JsonObject();
        payload.addProperty("receiverAccNo", "foo");
        payload.addProperty("amount", 100);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(422);
    }

    @Test
    public void transfer_onUnknownReceiver_shouldReturn400() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "bar");
        payload.addProperty("receiverAccNo", "baz");
        payload.addProperty("amount", 100);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(400);
    }

    @Test
    public void transfer_onEmptyReceiver_shouldReturn422() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "bar");
        payload.addProperty("receiverAccNo", "");
        payload.addProperty("amount", 100);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(422);
    }

    @Test
    public void transfer_onMissingReceiver_shouldReturn422() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "bar");
        payload.addProperty("amount", 100);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(422);
    }

    @Test
    public void transfer_onNegativeAmount_shouldReturn422() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "bar");
        payload.addProperty("receiverAccNo", "foo");
        payload.addProperty("amount", -1);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(422);
    }

    @Test
    public void transfer_onZero_shouldReturn422() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "bar");
        payload.addProperty("receiverAccNo", "foo");
        payload.addProperty("amount", 0);

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(422);
    }

    @Test
    public void transfer_onMissingAmount_shouldReturn422() {
        JsonObject payload = new JsonObject();
        payload.addProperty("senderAccNo", "bar");
        payload.addProperty("receiverAccNo", "foo");

        given().contentType(MediaType.APPLICATION_JSON).body(payload.toString()).
                when().post("/transfer").
                then().statusCode(422);
    }

    private static String getPath(String path) {
        return String.format("http://localhost:%d" + path, APP.getLocalPort());
    }
}
