package com.sist.cent.sucursal.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.cent.sucursal.controller.dto.IComparativaSucursales;
import com.sist.cent.sucursal.controller.dto.ISucursalTendencia;
import com.sist.cent.sucursal.controller.dto.SucursalDTO;
import com.sist.cent.sucursal.controller.dto.SucursalRequest;
import com.sist.cent.sucursal.service.SucursalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sucursal")
public class SucursalController {

  private final SucursalService service;

  private static final String BASE_URL = "/api/sucursal";

  @GetMapping("/todos")
  public ResponseEntity<List<SucursalDTO>> getAll() {
    var sucursales = service.getAll();
    return (!sucursales.isEmpty()) ? ResponseEntity.ok().body(sucursales) : ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<SucursalDTO> getById(@RequestParam Long id) {
    var sucursal = service.getById(id);
    return (sucursal != null) ? ResponseEntity.ok().body(sucursal) : ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<String> save(@RequestBody @Valid SucursalRequest sucursal) throws URISyntaxException {
    service.save(sucursal);
    return ResponseEntity.created(new URI(BASE_URL)).build();
  }

  @PutMapping
  public ResponseEntity<String> update(@RequestParam Long id, @RequestBody @Valid SucursalDTO sucursal) {
    sucursal.setId(id);
    service.update(sucursal);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<String> delete(@RequestParam Long id) {
    service.delete(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/analisis/comparativa")
  public ResponseEntity<List<IComparativaSucursales>> getComparativaSucursales(@RequestParam String fechaInicio,
      @RequestParam String fechaFin) {
    return ResponseEntity.ok(service.getComparativaSucursales(fechaInicio, fechaFin));
  }

  @GetMapping("/analisis/{sucursalId}/tendencia")
  public ResponseEntity<List<ISucursalTendencia>> getSucursalTendencia(@RequestParam String fechaInicio,
      @RequestParam String fechaFin, @PathVariable Long sucursalId) {
    return ResponseEntity.ok(service.getSucursalTendencia(fechaInicio, fechaFin, sucursalId));
  }

}
