package com.fredgar.pe.business.exception;

import com.fredgar.pe.business.components.ErrorPropertiesBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @Value("${error.documentation.base-url}")
  private String errorDocumentationBaseUrl;

  private final ErrorPropertiesBuilder buildErrorProperties;

  @ExceptionHandler(ExchangeRateBadRequestException.class)
  public ResponseEntity<ApiError> handleExchangeRateBadRequestException(ExchangeRateBadRequestException ex) {
    URI errorType = URI.create(errorDocumentationBaseUrl + "/errors#bad-request");
    URI instance = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    Map<String, Object> properties = buildErrorProperties.buildErrorProperties();
    properties.put("method", "POST");
    properties.put("validationErrors", ex.getValidationErrors());

    String errorDetail = "Errores de validación: " + String.join(", ", ex.getValidationErrors());

    ApiError apiError = ApiError.create(
        errorType,
        "Solicitud Mal Formada",
        HttpStatus.BAD_REQUEST,
        ex.getMessage(),
        instance,
        properties,
        "BAD_REQUEST",
        "Los valores de moneda proporcionados no son válidos. Por favor, verifique los valores de moneda proporcionados.",
        errorDetail
    );
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  @ExceptionHandler(ExchangeRateNotFoundException.class)
  public ResponseEntity<ApiError> handleExchangeRateNotFoundException(ExchangeRateNotFoundException ex) {
    URI errorType = URI.create(errorDocumentationBaseUrl + "/errors#not-found");
    URI instance = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    Map<String, Object> properties = buildErrorProperties.buildErrorProperties();
    String errorDetail = String.format(
        "No se encontró una tasa de cambio para las monedas '%s' a '%s'. Por favor, verifique que las monedas especificadas sean correctas y vuelva a intentarlo.",
        ex.getCurrencyFrom(), ex.getCurrencyTo()
    );
    ApiError apiError = ApiError.create(
        errorType,
        "Tipo de Cambio No Encontrado",
        HttpStatus.NOT_FOUND,
        ex.getMessage(),
        instance,
        properties,
        "NOT_FOUND",
        "No se pudo encontrar la tasa de cambio para las monedas especificadas.",
        errorDetail
    );
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    String errorMessage = ex.getMessage();
    String errorDetail = "La solicitud no pudo ser leída debido a un formato incorrecto. Verifica tu solicitud y vuelve a intentarlo.";
    URI errorType = URI.create(errorDocumentationBaseUrl + "/errors#bad-request");
    URI instance = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    Map<String, Object> properties = buildErrorProperties.buildErrorProperties();
    ApiError apiError = ApiError.create(
        errorType,
        "Solicitud Mal Formada",
        HttpStatus.BAD_REQUEST,
        errorMessage,
        instance,
        properties,
        "BAD_REQUEST",
        errorMessage,
        errorDetail
    );
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
    // Recopila los errores de validación
    List<String> validationErrors = new ArrayList<>();
    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      validationErrors.add(error.getDefaultMessage());
    }

    URI errorType = URI.create(errorDocumentationBaseUrl + "/errors#bad-request");
    URI instance = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    Map<String, Object> properties = buildErrorProperties.buildErrorProperties();

    String detail = "Los valores de moneda proporcionados no son válidos. Por favor, verifique los valores de moneda proporcionados.";
    String errorDetail = "Errores de validación: " + String.join(", ", validationErrors);
    ApiError apiError = ApiError.create(
        errorType,
        "Solicitud Mal Formada",
        HttpStatus.BAD_REQUEST,
        detail,
        instance,
        properties,
        "BAD_REQUEST",
        detail,
        errorDetail
    );
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

}
