package com.fredgar.pe.business.controller;

import com.fredgar.pe.business.dto.ExchangeRateDTO;
import com.fredgar.pe.business.dto.ExchangeRequestDTO;
import com.fredgar.pe.business.dto.ExchangeResponseDTO;
import com.fredgar.pe.business.exception.ApiError;
import com.fredgar.pe.business.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(name = "Exchange Rate Controller")
@RestController
@RequestMapping("/api/exchange-rate/v1")
@RequiredArgsConstructor
public class ExchangeController {

  private final ExchangeService exchangeService;

  @Operation(
      summary = "Convierte una moneda a otra",
      description = "Este endpoint permite convertir una cantidad de una moneda a otra según el tipo de cambio actual."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Conversión exitosa",
      content = @Content(schema = @Schema(implementation = ExchangeResponseDTO.class))
  )
  @ApiResponse(
      responseCode = "400",
      description = "Solicitud mal formada",
      content = @Content(schema = @Schema(implementation = ApiError.class))
  )
  @ApiResponse(
      responseCode = "404",
      description = "Tipo de cambio no encontrado",
      content = @Content(schema = @Schema(implementation = ApiError.class))
  )
  @PostMapping("/convert")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> convertCurrency(
      @Parameter(description = "Detalles de la conversión de moneda")
      @Valid
      @RequestBody ExchangeRequestDTO request) {
    ExchangeResponseDTO response = exchangeService.convertCurrency(request);
    return ResponseEntity.ok(response);
  }

  @Operation(
      description = "Crea un nuevo tipo de cambio",
      summary = "Método que permite crear un nuevo tipo de cambio"
  )
  @ApiResponse(
      responseCode = "400",
      description = "Solicitud mal formada",
      content = @Content(schema = @Schema(implementation = ApiError.class))
  )
  @ApiResponse(
      responseCode = "404",
      description = "Tipo de cambio no encontrado",
      content = @Content(schema = @Schema(implementation = ApiError.class))
  )
  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Optional<ExchangeRateDTO>> createExchangeRate(@RequestBody @Valid ExchangeRateDTO request) {
    Optional<ExchangeRateDTO> response = exchangeService.createExchangeRate(request);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .replacePath("/api/exchange-rate/v1/{id}")
        .buildAndExpand(response.get().id())
        .toUri();
    return ResponseEntity.created(location).body(response);
  }

  @Operation(
      description = "Actualiza un tipo de cambio",
      summary = "Método que permite actualizar un tipo de cambio"
  )
  @ApiResponse(
      responseCode = "400",
      description = "Solicitud mal formada",
      content = @Content(schema = @Schema(implementation = ApiError.class))
  )
  @ApiResponse(
      responseCode = "404",
      description = "Tipo de cambio no encontrado",
      content = @Content(schema = @Schema(implementation = ApiError.class))
  )
  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Optional<ExchangeRateDTO>> updateExchangeRate(@PathVariable Integer id, @RequestBody @Valid ExchangeRateDTO request) {
    Optional<ExchangeRateDTO> response = exchangeService.updateExchangeRate(id, request);
    return ResponseEntity.ok(response);
  }

  @Operation(
      description = "Obtiene todos los tipos de cambios",
      summary = "Metodo que permite obtener todos los tipos de cambios",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              description = "Success",
              responseCode = "200"
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              description = "Bad Request",
              responseCode = "400"
          )
      }

  )
  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<List<ExchangeRateDTO>> getAllExchangeRates() {
    List<ExchangeRateDTO> exchangeRates = exchangeService.findAll();
    return ResponseEntity.ok(exchangeRates);
  }

  @Operation(
      description = "Obtiene un tipo de cambio por su id",
      summary = "Método que permite obtener un tipo de cambio por su id",
      responses = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              description = "Success",
              responseCode = "200"
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              description = "Bad Request",
              responseCode = "400"
          )
      }

  )
  @GetMapping("/{id}")
  public ResponseEntity<ExchangeRateDTO> findById(@PathVariable Integer id) {
    Optional<ExchangeRateDTO> optionalExchangeRateDTO = exchangeService.findById(id);
    if (optionalExchangeRateDTO.isPresent()) {
      return ResponseEntity.ok(optionalExchangeRateDTO.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }


}
