package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;

public interface IDatosSucursal {
  String getSucursal();

  Integer getVentasHoy();

  BigDecimal getTicketPromedio();
}
