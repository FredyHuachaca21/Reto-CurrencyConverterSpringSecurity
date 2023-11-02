package com.fredgar.pe.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fredgar.pe.business.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExchangeRequestDTO(
    @JsonProperty("monto")
    @NotNull(message = "El monto es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0.")
    BigDecimal amount,

    @JsonProperty("monedaOrigen")
    @NotNull(message = "La moneda de origen es obligatoria.")
    Currency currencyFrom,

    @JsonProperty("monedaDestino")
    @NotNull(message = "La moneda de destino es obligatoria.")
    Currency currencyTo
) {
}
