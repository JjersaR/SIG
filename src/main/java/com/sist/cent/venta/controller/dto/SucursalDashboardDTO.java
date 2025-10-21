package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDashboardDTO {
  private String sucursal;

  private Integer ventasHoy;

  private BigDecimal ticketPromedio;

  private List<TopProductos> topProductos;

  private String horarioPico;

  private Integer ventas;

}
