package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;

public interface IProductoMasVendidos {
  String getProducto();

  Integer getCantidadVendida();

  BigDecimal getTotalVendido();

  String getMargenPromedio();
}
