package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
  private Long id;

  // Relación con la venta principal
  private Long ventaId;

  // Relación con el producto vendido
  private Long productoId;

  // Cantidad de unidades vendidas
  private Integer cantidad;

  // Precio unitario en el momento de la venta (aunque cambie después)
  private BigDecimal precioUnitario;

  // Subtotal de esta línea (precioUnitario * cantidad)
  private BigDecimal subtotal;

  // Precio de costo del producto al momento de la venta (para análisis de margen)
  private BigDecimal costoUnitario;

  // Margen de ganancia por línea (precioUnitario - costoUnitario)
  private BigDecimal margenUnitario;

  // Fecha y hora exacta de la venta (para análisis por hora o día)
  private LocalDateTime fechaVenta;

  // ID o nombre de la sucursal
  private Long sucursalId;

  // Medio de pago (efectivo, tarjeta, etc.)
  private String medioPago;

  // Vendedor o cajero que realizó la venta (para desempeño individual)
  private Long usuarioId;
}
