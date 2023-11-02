package com.fredgar.pe.business.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ExchangeRateBadRequestException extends RuntimeException {

  private final List<String> validationErrors;
  private final String detail;
  private final String errorDetail;

  public ExchangeRateBadRequestException(List<String> validationErrors, String detail, String errorDetail) {
    super(validationErrors.toString());
    this.validationErrors = validationErrors;
    this.detail = detail;
    this.errorDetail = errorDetail;
  }
}
