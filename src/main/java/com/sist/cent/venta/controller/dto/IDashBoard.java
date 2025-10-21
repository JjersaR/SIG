package com.sist.cent.venta.controller.dto;

public interface IDashBoard {
  Integer getVentasHoy();

  Integer getVentasMesActual();

  Float getCrecimientoVsMesAnterior();

  Integer getProductosVendidosHoy();

  String getSucursalTop();

  String getProductoTop();
}
