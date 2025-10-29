package com.sist.cent.sucursal.controller.dto;

import java.math.BigDecimal;

public interface IComparativaSucursales {
  String getSucursal();

  BigDecimal getVentasTotales();

  BigDecimal getVentasPromedioDiaria();

  Long getProductosVendidos();

  BigDecimal getTicketPromedio();
}
