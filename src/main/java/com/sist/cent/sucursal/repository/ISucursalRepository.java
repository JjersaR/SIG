package com.sist.cent.sucursal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sist.cent.sucursal.controller.dto.IComparativaSucursales;
import com.sist.cent.sucursal.controller.dto.ISucursalTendencia;
import com.sist.cent.sucursal.entity.Sucursal;

@Repository
public interface ISucursalRepository extends JpaRepository<Sucursal, Long> {

  @Query(value = """
      SELECT
          s.nombre as sucursal,
          SUM(v.subtotal) as ventasTotales,
          SUM(v.subtotal) / DATEDIFF(:fechaFin, :fechaInicio) + 1 as ventasPromedioDiaria,
          SUM(v.cantidad) as productosVendidos,
          AVG(v.subtotal) as ticketPromedio
      FROM sucursal s
      JOIN venta v ON s.id = v.sucursal_id
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY s.id, s.nombre
      ORDER BY ventasTotales DESC;
          """, nativeQuery = true)
  public List<IComparativaSucursales> getComparativaSucursales(String fechaInicio, String fechaFin);

  @Query(value = """
      SELECT
          DATE_FORMAT(v.fecha_venta, '%Y-%m') as mes,
          SUM(v.subtotal) as ventas,
          SUM(v.cantidad) as productosVendidos,
          CONCAT(ROUND(
              (SUM(v.subtotal) - LAG(SUM(v.subtotal)) OVER (ORDER BY DATE_FORMAT(v.fecha_venta, '%Y-%m'))) /
              LAG(SUM(v.subtotal)) OVER (ORDER BY DATE_FORMAT(v.fecha_venta, '%Y-%m')) * 100,
          1), '%') as crecimiento
      FROM venta v
      JOIN sucursal s ON v.sucursal_id = s.id
      WHERE s.id = :sucursalId
          AND v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY DATE_FORMAT(v.fecha_venta, '%Y-%m')
      ORDER BY mes;
          """, nativeQuery = true)
  public List<ISucursalTendencia> getSucursalTendencia(String fechaInicio, String fechaFin, Long sucursalId);
}
