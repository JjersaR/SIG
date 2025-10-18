package com.sist.cent.usuario.controller;

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

import com.sist.cent.usuario.controller.dto.UsuarioDTO;
import com.sist.cent.usuario.controller.dto.UsuarioRequest;
import com.sist.cent.usuario.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuario")
public class UsuarioController {

  private final UsuarioService service;

  private static final String BASE_URL = "/api/usuario";

  @GetMapping("/todos")
  public ResponseEntity<List<UsuarioDTO>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @GetMapping
  public ResponseEntity<UsuarioDTO> getById(@RequestParam Long id) {
    var usuario = service.getById(id);
    return (usuario != null) ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<String> save(@RequestBody @Valid UsuarioRequest usuario) throws URISyntaxException {
    service.save(usuario);
    return ResponseEntity.created(new URI(BASE_URL)).build();
  }

  @PutMapping
  public ResponseEntity<String> update(@RequestParam Long id, @RequestBody @Valid UsuarioRequest usuario) {
    usuario.setId(id);
    service.save(usuario);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<String> delete(@RequestParam Long id) {
    service.delete(id);
    return ResponseEntity.ok().build();
  }

}
