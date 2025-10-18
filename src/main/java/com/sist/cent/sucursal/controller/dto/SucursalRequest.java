package com.sist.cent.sucursal.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SucursalRequest {

  @NotEmpty
  private String nombre;

  @NotEmpty
  private String direccion;

  @NotEmpty
  private String telefono;

  @NotEmpty
  private String encargado;
}
