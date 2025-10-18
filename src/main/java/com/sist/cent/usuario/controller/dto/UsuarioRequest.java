package com.sist.cent.usuario.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioRequest {
  private Long id;

  private String username;

  private String password;

  private boolean isAdmin;
}
