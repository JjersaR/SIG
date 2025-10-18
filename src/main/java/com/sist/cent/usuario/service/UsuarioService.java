package com.sist.cent.usuario.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.sist.cent.usuario.controller.dto.UsuarioDTO;
import com.sist.cent.usuario.controller.dto.UsuarioRequest;
import com.sist.cent.usuario.entity.Usuario;
import com.sist.cent.usuario.repository.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final IUsuarioRepository repository;

  public List<UsuarioDTO> getAll() {
    return toUsuarioDTOs(repository.findAll());
  }

  public UsuarioDTO getById(Long id) {
    var usuario = repository.findById(id);
    return (usuario.isPresent()) ? toUsuarioDTO(usuario.get()) : null;
  }

  public void save(UsuarioRequest usuario) {
    repository.save(toUsuario(usuario));
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  // mapper
  private Usuario toUsuario(UsuarioRequest usuario) {
    return Usuario.builder()
        .id(usuario.getId())
        .username(usuario.getUsername())
        .password(usuario.getPassword())
        .isAdmin(usuario.isAdmin())
        .build();
  }

  private UsuarioDTO toUsuarioDTO(Usuario usuario) {
    return UsuarioDTO.builder()
        .id(usuario.getId())
        .username(usuario.getUsername())
        .password(usuario.getPassword())
        .isAdmin(usuario.isAdmin())
        .build();
  }

  private List<UsuarioDTO> toUsuarioDTOs(List<Usuario> usuarios) {
    if (usuarios == null || usuarios.isEmpty()) {
      return Collections.emptyList();
    }

    return usuarios.stream()
        .filter(Objects::nonNull)
        .map(this::toUsuarioDTO)
        .toList();
  }
}
