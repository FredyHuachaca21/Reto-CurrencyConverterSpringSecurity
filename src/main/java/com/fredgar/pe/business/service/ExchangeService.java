package com.fredgar.pe.business.service;


import com.fredgar.pe.business.dto.ExchangeRateDTO;
import com.fredgar.pe.business.dto.ExchangeRequestDTO;
import com.fredgar.pe.business.dto.ExchangeResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ExchangeService {

  ExchangeResponseDTO convertCurrency(ExchangeRequestDTO request);
  Optional<ExchangeRateDTO> createExchangeRate(ExchangeRateDTO exchangeRateDTO);
  Optional<ExchangeRateDTO> updateExchangeRate( Integer id, ExchangeRateDTO exchangeRateDTO);
  List<ExchangeRateDTO> findAll();
  Optional<ExchangeRateDTO> findById(Integer id);

}
