package com.sist.cent.producto.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class Producto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Nombre del producto
  @Column(nullable = false, unique = true)
  private String nombre;

  // Precio de venta actual
  @Column(nullable = false)
  private BigDecimal precio;

  @Column(nullable = true)
  private String descripcion;

  @Column(nullable = false)
  private String categoria;

  @Column(nullable = false)
  private Long sucursalId;

  private Boolean activo;

  // cantidad total vendida
  private Long totalVendidos;
}
