package net.lugburz.tinytransfer.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO for deserializing account creation requests.
 */
@Getter
@Setter
@NoArgsConstructor
final class AccountCreationRequest {

    @JsonProperty
    @NotEmpty
    private String accountNo;

    @JsonProperty
    @NotNull
    @Min(value = 0)
    private BigDecimal balance;
}
