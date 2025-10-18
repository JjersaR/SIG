package com.sist.cent.producto.controller.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoDTO {
  private Long id;

  // Nombre del producto
  private String nombre;

  // Precio de venta actual
  private BigDecimal precio;

  private String descripcion;

  private String categoria;

  private Long sucursalId;

  private Boolean activo;

  // cantidad total vendida
  private Long totalVendidos;
}
