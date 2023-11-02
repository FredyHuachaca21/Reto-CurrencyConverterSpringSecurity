package com.fredgar.pe.business.model;

import com.fredgar.pe.business.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity(name = "exchange_rate")
public class ExchangeRate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency_from",length = 3)
  @NotNull
  private Currency currencyFrom; // moneda de origen

  @Enumerated(EnumType.STRING)
  @Column(name = "currency_to" ,length = 3)
  @NotNull
  private Currency currencyTo; // moneda de destino

  @Positive
  private BigDecimal rate; // tipo de cambio

}
