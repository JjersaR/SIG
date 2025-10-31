package com.sist.cent.venta.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
  private String hora;

  private String producto;

  private Long cantidad;
}
