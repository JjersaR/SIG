package com.sist.cent.producto.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.cent.producto.controller.dto.ProductoDTO;
import com.sist.cent.producto.controller.dto.ProductoRequest;
import com.sist.cent.producto.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/producto")
public class ProductoController {

  private final ProductoService service;

  private static final String BASE_URL = "/api/producto";

  @GetMapping("/todos")
  public ResponseEntity<List<ProductoDTO>> getAll() {
    var productos = service.getAll();
    return (!productos.isEmpty()) ? ResponseEntity.ok().body(productos) : ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<ProductoDTO> getById(@RequestParam Long id) {
    var producto = service.getById(id);
    return (producto != null) ? ResponseEntity.ok().body(producto) : ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<String> save(@RequestBody @Valid ProductoRequest producto) throws URISyntaxException {
    service.save(producto);
    return ResponseEntity.created(new URI(BASE_URL)).build();
  }

  @PutMapping
  public ResponseEntity<String> update(@RequestParam Long id, @RequestBody @Valid ProductoRequest producto) {
    producto.setId(id);
    service.update(producto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<String> delete(@RequestParam Long id) {
    service.delete(id);
    return ResponseEntity.ok().build();
  }

}
