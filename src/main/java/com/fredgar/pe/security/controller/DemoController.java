package com.fredgar.pe.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

  // Endpoint accesible para cualquier usuario autenticado
  @GetMapping("/public")
  public ResponseEntity<String> sayPublicHello() {
    return ResponseEntity.ok("Hello from public endpoint");
  }

  // Endpoint accesible solo para usuarios con rol ADMIN
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<String> sayAdminHello() {
    return ResponseEntity.ok("Hello from admin endpoint");
  }

  // Endpoint accesible solo para usuarios con rol MANAGER
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/manager")
  public ResponseEntity<String> sayManagerHello() {
    return ResponseEntity.ok("Hello from manager endpoint");
  }

}

