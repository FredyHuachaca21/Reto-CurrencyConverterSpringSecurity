package com.fredgar.pe.business.repository;


import com.fredgar.pe.business.enums.Currency;
import com.fredgar.pe.business.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

  ExchangeRate findByCurrencyFromAndCurrencyTo(Currency currencyFrom, Currency currencyTo);

}
