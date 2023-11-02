package com.fredgar.pe.business.service;

import com.fredgar.pe.business.dto.ExchangeRateDTO;
import com.fredgar.pe.business.dto.ExchangeRequestDTO;
import com.fredgar.pe.business.dto.ExchangeResponseDTO;
import com.fredgar.pe.business.exception.ExchangeRateBadRequestException;
import com.fredgar.pe.business.exception.ExchangeRateNotFoundException;
import com.fredgar.pe.business.mapper.ExchangeRateMapper;
import com.fredgar.pe.business.model.ExchangeRate;
import com.fredgar.pe.business.repository.ExchangeRateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeServiceImpl implements ExchangeService {

  private final ExchangeRateRepository exchangeRateRepository;
  private final ExchangeRateMapper exchangeRateMapper;

  @Override
  public ExchangeResponseDTO convertCurrency(ExchangeRequestDTO request) {

    List<String> errors = new ArrayList<>();

    // Verifica si las monedas en la solicitud son iguales
    if (request.currencyFrom().equals(request.currencyTo())) {
      errors.add("Las monedas origen y destino no pueden ser las mismas");
    }

    // Verifica si el monto es mayor a 0
    if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
      errors.add("El monto debe ser mayor a 0");
    }

    // Si hay errores, lanza una excepción
    if (!errors.isEmpty()) {
      String detail = String.join("; ", errors);
      String errorDetail = "Errores de validación: " + detail;
      throw new ExchangeRateBadRequestException(errors, detail, errorDetail);
    }

    ExchangeRate exchangeRate = exchangeRateRepository
        .findByCurrencyFromAndCurrencyTo(request.currencyFrom(), request.currencyTo());

    if (exchangeRate == null) {
      throw new ExchangeRateNotFoundException(request.currencyFrom(), request.currencyTo());
    }

    return exchangeRateMapper.toResponseDTO(exchangeRate, request.amount());
  }

  @Override
  public Optional<ExchangeRateDTO> createExchangeRate(ExchangeRateDTO exchangeRateDTO) {
    ExchangeRate exchangeRate = exchangeRateMapper.toExchangeRate(exchangeRateDTO);
    List<String> errors = new ArrayList<>();

    // Verifica si las monedas son iguales
    if (exchangeRate.getCurrencyTo().equals(exchangeRate.getCurrencyFrom())) {
      errors.add("Las monedas origen y destino no pueden ser las mismas");
    }

    // Si hay errores, lanza una excepción
    if (!errors.isEmpty()) {
      String detail = String.join("; ", errors);
      String errorDetail = "Errores de validación: " + detail;
      throw new ExchangeRateBadRequestException(errors, detail, errorDetail);
    }
    exchangeRate = exchangeRateRepository.save(exchangeRate);
    return Optional.of(exchangeRateMapper.toExchangeRateDTO(exchangeRate));
  }

  @Override
  public Optional<ExchangeRateDTO> updateExchangeRate(Integer id, ExchangeRateDTO exchangeRateDTO) {
    Optional<ExchangeRate> optionalExchangeRate = exchangeRateRepository.findById(id);
    if (optionalExchangeRate.isPresent()) {
      ExchangeRate exchangeRate = optionalExchangeRate.get();
      exchangeRate.setCurrencyFrom(exchangeRateDTO.currencyFrom());
      exchangeRate.setCurrencyTo(exchangeRateDTO.currencyTo());
      exchangeRate.setRate(exchangeRateDTO.rate());
      exchangeRate = exchangeRateRepository.save(exchangeRate);
      return Optional.of(exchangeRateMapper.toExchangeRateDTO(exchangeRate));
    }
    return Optional.empty();
  }

  @Override
  public List<ExchangeRateDTO> findAll() {
    return exchangeRateRepository.findAll()
        .stream()
        .map(exchangeRateMapper::toExchangeRateDTO)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ExchangeRateDTO> findById(Integer id) {
    return exchangeRateRepository.findById(id)
        .map(exchangeRateMapper::toExchangeRateDTO);
  }
}
