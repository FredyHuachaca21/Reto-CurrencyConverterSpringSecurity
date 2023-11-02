package com.fredgar.pe.business.mapper;

import com.fredgar.pe.business.dto.ExchangeRateDTO;
import com.fredgar.pe.business.dto.ExchangeResponseDTO;
import com.fredgar.pe.business.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeRateMapper {

  @Mappings({
      @Mapping(target = "amount", source = "amount"),
      @Mapping(target = "exchangedAmount", expression = "java(amount.multiply(exchangeRate.getRate()))"),
      @Mapping(target = "currencyFrom", source = "exchangeRate.currencyFrom"),
      @Mapping(target = "currencyTo", source = "exchangeRate.currencyTo"),
      @Mapping(target = "rate", source = "exchangeRate.rate")
  })
  ExchangeResponseDTO toResponseDTO(ExchangeRate exchangeRate, BigDecimal amount);

  ExchangeRateDTO toExchangeRateDTO(ExchangeRate exchangeRate);

  ExchangeRate toExchangeRate(ExchangeRateDTO exchangeRateDTO);

}
