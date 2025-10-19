package com.sist.cent.venta.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentaRequest {
  private Long id;

  // Relación con la venta principal
  @NotNull
  @Positive
  private Long ventaId;

  // Relación con el producto vendido
  @NotNull
  @Positive
  private Long productoId;

  // Cantidad de unidades vendidas
  @NotNull
  @Positive
  private Integer cantidad;

  // Precio unitario en el momento de la venta (aunque cambie después)
  @NotNull
  @DecimalMin("0.00")
  @Digits(integer = 10, fraction = 2)
  private BigDecimal precioUnitario;

  // Subtotal de esta línea (precioUnitario * cantidad)
  @NotNull
  @DecimalMin("0.00")
  @Digits(integer = 12, fraction = 2)
  private BigDecimal subtotal;

  // Precio de costo del producto al momento de la venta (para análisis de margen)
  @NotNull
  @DecimalMin("0.00")
  @Digits(integer = 10, fraction = 2)
  private BigDecimal costoUnitario;

  // Margen de ganancia por línea (precioUnitario - costoUnitario)
  @DecimalMin("0.00")
  @Digits(integer = 10, fraction = 2)
  private BigDecimal margenUnitario;

  // Fecha y hora exacta de la venta (para análisis por hora o día)
  @NotNull
  @PastOrPresent
  private LocalDateTime fechaVenta;

  // ID o nombre de la sucursal
  @NotNull
  @Positive
  private Long sucursalId;

  // Medio de pago (efectivo, tarjeta, etc.)
  @NotBlank
  @Size(max = 50)
  @Pattern(regexp = "^(EFECTIVO|TARJETA|TRANSFERENCIA|CHEQUE|OTRO)$")
  private String medioPago;

  // Vendedor o cajero que realizó la venta (para desempeño individual)
  @NotNull
  @Positive
  private Long usuarioId;
}
