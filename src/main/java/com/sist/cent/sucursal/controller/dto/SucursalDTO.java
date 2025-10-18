package com.sist.cent.sucursal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SucursalDTO {
  private Long id;

  private String nombre;

  private String direccion;

  private String telefono;

  private String encargado;
}
