package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;

public interface IAnalisisVentas {
  String getFecha();

  BigDecimal getTotalVentas();

  Long getCantidadVentas();

  BigDecimal getTicketPromedio();
}
