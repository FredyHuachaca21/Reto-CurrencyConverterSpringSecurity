package com.fredgar.pe.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fredgar.pe.business.enums.Currency;

import java.math.BigDecimal;

public record ExchangeResponseDTO(
    @JsonProperty("monto")
    BigDecimal amount, // monto a convertir
    @JsonProperty("montoConvertido")
    BigDecimal exchangedAmount, // monto convertido
    @JsonProperty("monedaOrigen")
    Currency currencyFrom, // moneda de origen
    @JsonProperty("monedaDestino")
    Currency currencyTo, // moneda de destino
    @JsonProperty("tipoCambio")
    BigDecimal rate // tipo de cambio
) {
}
