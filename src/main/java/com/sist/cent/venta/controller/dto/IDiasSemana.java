package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;

public interface IDiasSemana {
  String getDia();

  BigDecimal getVentasPromedio();

  Long getCantidadVentas();

  BigDecimal getTotalVentas();
}
