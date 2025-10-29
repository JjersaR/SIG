package com.sist.cent.sucursal.controller.dto;

import java.math.BigDecimal;

public interface ISucursalTendencia {
  String getMes();

  BigDecimal getVentas();

  Long getProductosVendidos();

  String getCrecimiento();
}
