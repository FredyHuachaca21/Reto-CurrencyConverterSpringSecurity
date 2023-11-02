package com.fredgar.pe.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fredgar.pe.business.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExchangeRateDTO(

    @JsonProperty("id")
    Long id,
    @JsonProperty("monedaOrigen")
    @NotNull(message = "La moneda origen no puede ser nula")
    Currency currencyFrom,
    @JsonProperty("monedaDestino")
    @NotNull(message = "La moneda destino no puede ser nula")
    Currency currencyTo,
    @JsonProperty("tipoCambio")
    @NotNull(message = "El tipo de cambio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El tipo de cambio debe ser mayor que cero")
    BigDecimal rate) {}
