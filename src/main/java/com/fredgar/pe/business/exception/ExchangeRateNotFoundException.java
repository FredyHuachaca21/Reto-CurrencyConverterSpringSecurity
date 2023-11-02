package com.fredgar.pe.business.exception;

import com.fredgar.pe.business.enums.Currency;
import lombok.Getter;

@Getter
public class ExchangeRateNotFoundException extends RuntimeException {

  private final Currency currencyFrom;
  private final Currency currencyTo;

  public ExchangeRateNotFoundException(Currency currencyFrom, Currency currencyTo) {
    super(String.format("Tipo de cambio no encontrado para las monedas: %s a %s", currencyFrom, currencyTo));
    this.currencyFrom = currencyFrom;
    this.currencyTo = currencyTo;
  }

}
