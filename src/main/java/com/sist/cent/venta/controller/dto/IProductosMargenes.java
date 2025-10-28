package com.sist.cent.venta.controller.dto;

public interface IProductosMargenes {
  String getProducto();

  Float getCostoPromedio();

  Float getPrecioVentaPromedio();

  Float getMargen();

  String getMargenPorcentaje();
}
