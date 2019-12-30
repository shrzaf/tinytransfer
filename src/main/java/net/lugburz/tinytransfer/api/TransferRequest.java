package net.lugburz.tinytransfer.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO for a transfer request contained in the request body.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class TransferRequest {

    @JsonProperty
    @NotEmpty
    private String senderAccNo;

    @JsonProperty
    @NotEmpty
    private String receiverAccNo;

    @JsonProperty
    @NotNull
    @Positive
    private BigDecimal amount;
}
