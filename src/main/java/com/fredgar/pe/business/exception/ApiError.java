package com.fredgar.pe.business.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.Map;

@Getter
@Setter
@Schema(description = "Detalles del error de la API")
public class ApiError extends ProblemDetail {

  @Schema(description = "Un identificador URI que identifica el tipo específico de error que ocurrió.",
      example = "http://localhost:8080/api/exchange-rate/v1/errors#bad-request"
  )
  private URI type;

  @Schema(description = "Un título corto que describe el tipo de error que ocurrió.",
      example = "Solicitud Mal Formada"
  )
  private String title;

  @Schema(description = "El código de estado HTTP que se devolvió en la respuesta HTTP.",
      example = "400"
  )
  private int status;

  @Schema(description = "Una explicación detallada sobre el error ocurrido.",
      example = "La moneda especificada en el campo 'currencyFrom' no es válida."
  )
  private String detail;

  @Schema(description = "Un identificador único para esta instancia específica del error, útil para rastrear errores en los logs del servidor.",
      example = "http://localhost:8080/api/exchange-rate/v1/errors/12345"
  )
  private URI instance;

  @Schema(description = "Un conjunto de propiedades adicionales que pueden proporcionar más contexto sobre el error.",
      example = "{\"serverName\": \"server1\", \"timestamp\": \"2023-10-31T12:34:56Z\"}"
  )
  private Map<String, Object> properties;

  @Schema(description = "Un código de error específico que identifica el tipo de error que ocurrió.",
      example = "BAD_REQUEST"
  )
  private String errorCode;

  @Schema(description = "Un mensaje de error legible para el ser humano que explica el error que ocurrió.",
      example = "La entrada de la solicitud no es válida. Por favor, verifique los valores de moneda proporcionados."
  )
  private String errorMessage;

  @Schema(description = "Detalles adicionales que pueden ayudar a los desarrolladores a depurar el error que ocurrió.",
      example = "El campo currencyFrom no puede ser nulo."
  )
  private String errorDetail;

  public static ApiError create(
    URI type, String title, HttpStatus status, String detail, URI instance,
    Map<String, Object> properties, String errorCode, String errorMessage, String errorDetail) {
    ApiError apiError = new ApiError();
    apiError.setType(type);
    apiError.setTitle(title);
    apiError.setStatus(status.value());
    apiError.setDetail(detail);
    apiError.setInstance(instance);
    apiError.setProperties(properties);
    apiError.setErrorCode(errorCode);
    apiError.setErrorMessage(errorMessage);
    apiError.setErrorDetail(errorDetail);
    return apiError;
  }
}
