package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;

public interface IVentasPorHorario {
  String getHora();

  BigDecimal getVentasTotales();

  Long getCantidadVentas();
}
