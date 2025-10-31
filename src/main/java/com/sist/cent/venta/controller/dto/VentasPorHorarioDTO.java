package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentasPorHorarioDTO {
  // datos de la hora
  private String hora;

  private BigDecimal ventasTotales;

  private Long cantidadVentas;

  // datos de los productos
  // se ligan por hora
  private List<String> productos;

}
