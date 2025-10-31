package com.sist.cent.venta.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.cent.venta.controller.dto.EAgrupacion;
import com.sist.cent.venta.controller.dto.IAnalisisVentas;
import com.sist.cent.venta.controller.dto.IDashBoard;
import com.sist.cent.venta.controller.dto.IProductoMasVendidos;
import com.sist.cent.venta.controller.dto.IProductosMargenes;
import com.sist.cent.venta.controller.dto.SucursalDashboardDTO;
import com.sist.cent.venta.controller.dto.VentaDTO;
import com.sist.cent.venta.controller.dto.VentaRequest;
import com.sist.cent.venta.controller.dto.VentasPorHorarioDTO;
import com.sist.cent.venta.service.VentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venta")
public class VentaController {

  private final VentaService service;
  private static final String BASE_URL = "/api/venta";

  @GetMapping("/todos")
  public ResponseEntity<List<VentaDTO>> getAll() {
    var ventas = service.getAll();
    return (ventas.isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(ventas);
  }

  @GetMapping
  public ResponseEntity<VentaDTO> getById(@RequestParam Long id) {
    var venta = service.getById(id);
    return (venta == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(venta);
  }

  @PostMapping
  public ResponseEntity<String> save(@RequestBody @Validated VentaRequest venta) throws URISyntaxException {
    service.save(venta);
    return ResponseEntity.created(new URI(BASE_URL)).build();
  }

  @PutMapping
  public ResponseEntity<String> update(@RequestParam Long id, @RequestBody @Validated VentaRequest venta) {
    venta.setId(id);
    service.save(venta);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<String> delete(@RequestParam Long id) {
    service.delete(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/dashboard")
  public ResponseEntity<IDashBoard> getDashBoard() {
    return ResponseEntity.ok(service.getDashBoard());
  }

  @GetMapping("/dashboard/sucursal")
  public ResponseEntity<SucursalDashboardDTO> getDashBoardSucursal(@RequestParam Long id) {
    return ResponseEntity.ok(service.getSucursalDashboard(id));
  }

  @GetMapping("/analisis")
  public ResponseEntity<List<IAnalisisVentas>> getAnalisis(@RequestParam String fechaInicio,
      @RequestParam String fechaFin, @RequestParam EAgrupacion agrupacion) {
    return ResponseEntity.ok(service.getAnalisisVentas(fechaInicio, fechaFin, agrupacion));
  }

  @GetMapping("/analisis/productos")
  public ResponseEntity<List<IProductoMasVendidos>> getProductoMasVendidos(@RequestParam String fechaInicio,
      @RequestParam String fechaFin, @RequestParam Long sucursalId, @RequestParam int top) {
    return ResponseEntity.ok(service.getProductoMasVendidos(fechaInicio, fechaFin, sucursalId, top));
  }

  @GetMapping("/analisis/productos/margenes")
  public ResponseEntity<List<IProductosMargenes>> getProductoMargenes(@RequestParam String fechaInicio,
      @RequestParam String fechaFin, @RequestParam Long sucursalId) {
    return ResponseEntity.ok(service.getProductosMargenes(fechaInicio, fechaFin, sucursalId));
  }

  @GetMapping("/analisis/horarios")
  public ResponseEntity<List<VentasPorHorarioDTO>> getProductosPorHora(@RequestParam String fechaInicio,
      @RequestParam String fechaFin) {
    return ResponseEntity.ok(service.getVentasPorHorario(fechaInicio, fechaFin));
  }

}
