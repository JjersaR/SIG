package com.sist.cent.venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sist.cent.venta.controller.dto.IAnalisisVentas;
import com.sist.cent.venta.controller.dto.IDashBoard;
import com.sist.cent.venta.controller.dto.IDatosSucursal;
import com.sist.cent.venta.controller.dto.IHoraPico;
import com.sist.cent.venta.controller.dto.IProductoMasVendido;
import com.sist.cent.venta.controller.dto.IProductoMasVendidos;
import com.sist.cent.venta.controller.dto.IProductosMargenes;
import com.sist.cent.venta.controller.dto.IProductosPorHora;
import com.sist.cent.venta.controller.dto.IVentasPorHorario;
import com.sist.cent.venta.entity.Venta;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long> {

  @Query(value = """
      SELECT
          -- Ventas de hoy
          (SELECT COALESCE(SUM(subtotal), 0) FROM venta
           WHERE DATE(fecha_venta) = CURDATE()) as ventasHoy,

          -- Ventas mes actual
          (SELECT COALESCE(SUM(subtotal), 0) FROM venta
           WHERE YEAR(fecha_venta) = YEAR(CURDATE())
             AND MONTH(fecha_venta) = MONTH(CURDATE())) as ventasMesActual,

          -- Crecimiento vs mes anterior
          ROUND(
              ((SELECT COALESCE(SUM(subtotal), 0) FROM venta
                WHERE YEAR(fecha_venta) = YEAR(CURDATE())
                  AND MONTH(fecha_venta) = MONTH(CURDATE())) -
               (SELECT COALESCE(SUM(subtotal), 0) FROM venta
                WHERE YEAR(fecha_venta) = YEAR(CURDATE() - INTERVAL 1 MONTH)
                  AND MONTH(fecha_venta) = MONTH(CURDATE() - INTERVAL 1 MONTH))) /
              GREATEST((SELECT COALESCE(SUM(subtotal), 1) FROM venta
                       WHERE YEAR(fecha_venta) = YEAR(CURDATE() - INTERVAL 1 MONTH)
                         AND MONTH(fecha_venta) = MONTH(CURDATE() - INTERVAL 1 MONTH)), 1) * 100, 1
          ) as crecimientoVsMesAnterior,

          -- Productos vendidos hoy
          (SELECT COALESCE(SUM(cantidad), 0) FROM venta
           WHERE DATE(fecha_venta) = CURDATE()) as productosVendidosHoy,

          -- Sucursal top del día
          (SELECT s.nombre FROM venta v
           JOIN sucursal s ON v.sucursal_id = s.id
           WHERE DATE(v.fecha_venta) = CURDATE()
           GROUP BY s.id, s.nombre
           ORDER BY SUM(v.subtotal) DESC
           LIMIT 1) as sucursalTop,

          -- Producto top del día
          (SELECT p.nombre FROM venta v
           JOIN producto p ON v.producto_id = p.id
           WHERE DATE(v.fecha_venta) = CURDATE()
           GROUP BY p.id, p.nombre
           ORDER BY SUM(v.cantidad) DESC
           LIMIT 1) as productoTop;
        """, nativeQuery = true)
  public IDashBoard getDashboard();

  @Query(value = """
      SELECT
          s.nombre as sucursal,
          COALESCE(SUM(v.subtotal), 0) as ventasHoy,
          COALESCE(AVG(v.subtotal), 0) as ticketPromedio
      FROM venta v
      JOIN sucursal s ON v.sucursal_id = s.id
      WHERE DATE(v.fecha_venta) = CURDATE()
        AND s.id = :id;
          """, nativeQuery = true)
  public IDatosSucursal datosSucursal(Long id);

  @Query(value = """
      SELECT
        p.nombre,
        SUM(v.cantidad) as cantidad
      FROM venta v
      JOIN producto p ON v.producto_id = p.id
      JOIN sucursal s ON v.sucursal_id = s.id
      WHERE DATE(v.fecha_venta) = CURDATE()
        AND s.id = :id
      GROUP BY p.id, p.nombre
      ORDER BY SUM(v.cantidad) DESC
      LIMIT 5;
        """, nativeQuery = true)
  public List<IProductoMasVendido> getProductoMasVendidos(Long id);

  @Query(value = """
      SELECT
          CONCAT(
              HOUR(v.fecha_venta),
              ':00-',
              HOUR(v.fecha_venta) + 2,
              ':00'
          ) as horarioPico,
          SUM(v.subtotal) as ventas
      FROM venta v
      JOIN sucursal s ON v.sucursal_id = s.id
      WHERE DATE(v.fecha_venta) = CURDATE()
          AND s.id = :id
      GROUP BY HOUR(v.fecha_venta), v.fecha_venta
      ORDER BY ventas DESC
      LIMIT 1;
          """, nativeQuery = true)
  public IHoraPico getHorarioPico(Long id);

  @Query(value = """
      SELECT
          DATE(v.fecha_venta) as fecha,
          SUM(v.subtotal) as totalVentas,
          COUNT(DISTINCT v.venta_id) as cantidadVentas,
          AVG(v.subtotal) as ticketPromedio
      FROM venta v
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY DATE(v.fecha_venta)
      ORDER BY fecha;
            """, nativeQuery = true)
  public List<IAnalisisVentas> getAnalisisDiaria(String fechaInicio, String fechaFin);

  @Query(value = """
      SELECT
          CONCAT('Semana ', LPAD(WEEK(v.fecha_venta), 2, '0'), ' ', YEAR(v.fecha_venta)) as fecha,
          SUM(v.subtotal) as totalVentas,
          COUNT(DISTINCT v.venta_id) as cantidadVentas,
          AVG(v.subtotal) as ticketPromedio
      FROM venta v
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY YEAR(v.fecha_venta), WEEK(v.fecha_venta), fecha
      ORDER BY MIN(v.fecha_venta);
          """, nativeQuery = true)
  public List<IAnalisisVentas> getAnalisisSemanal(String fechaInicio, String fechaFin);

  @Query(value = """
      SELECT
          DATE_FORMAT(v.fecha_venta, '%Y-%m') as fecha,
          SUM(v.subtotal) as totalVentas,
          COUNT(DISTINCT v.venta_id) as cantidadVentas,
          AVG(v.subtotal) as ticketPromedio
      FROM venta v
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY YEAR(v.fecha_venta), MONTH(v.fecha_venta), fecha
      ORDER BY fecha;
          """, nativeQuery = true)
  public List<IAnalisisVentas> getAnalisisMensual(String fechaInicio, String fechaFin);

  @Query(value = """
      SELECT
          YEAR(v.fecha_venta) as fecha,
          SUM(v.subtotal) as totalVentas,
          COUNT(DISTINCT v.venta_id) as cantidadVentas,
          AVG(v.subtotal) as ticketPromedio
      FROM venta v
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY YEAR(v.fecha_venta)
      ORDER BY fecha;
          """, nativeQuery = true)
  public List<IAnalisisVentas> getAnalisisAnual(String fechaInicio, String fechaFin);

  @Query(value = """
      SELECT
          p.nombre as producto,
          SUM(v.cantidad) as cantidadVendida,
          SUM(v.subtotal) as totalVendido,
          CONCAT(ROUND(
              (SUM(v.subtotal) - SUM(v.costo_unitario * v.cantidad)) /
              SUM(v.subtotal) * 100,
          1), '%') as margenPromedio
      FROM venta v
      JOIN producto p ON v.producto_id = p.id
      JOIN sucursal s ON v.sucursal_id = s.id
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
          AND (:sucursalId IS NULL OR s.id = :sucursalId)
      GROUP BY p.id, p.nombre
      ORDER BY cantidadVendida DESC
      LIMIT :top;
          """, nativeQuery = true)
  public List<IProductoMasVendidos> getProductoMasVendidos(String fechaInicio, String fechaFin, Long sucursalId,
      int top);

  @Query(value = """
      SELECT
          p.nombre as producto,
          AVG(v.costo_unitario) as costoPromedio,
          AVG(v.precio_unitario) as precioVentaPromedio,
          AVG(v.precio_unitario - v.costo_unitario) as margen,
          CONCAT(ROUND(
              (AVG(v.precio_unitario) - AVG(v.costo_unitario)) /
              AVG(v.precio_unitario) * 100,
          1), '%') as margenPorcentaje
      FROM producto p
      JOIN venta v ON p.id = v.producto_id
      JOIN sucursal s ON v.sucursal_id = s.id
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
          AND (:sucursalId IS NULL OR s.id = :sucursalId)
      GROUP BY p.id, p.nombre
      ORDER BY margenPorcentaje DESC;
          """, nativeQuery = true)
  public List<IProductosMargenes> getProductosMargenes(String fechaInicio, String fechaFin, Long sucursalId);

  @Query(value = """
      SELECT
          DATE_FORMAT(v.fecha_venta, '%H:00') as hora,
          SUM(v.subtotal) as ventasTotales,
          COUNT(DISTINCT v.venta_id) as cantidadVentas
      FROM venta v
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY DATE_FORMAT(v.fecha_venta, '%H:00')
      ORDER BY hora;
          """, nativeQuery = true)
  public List<IVentasPorHorario> getVentasPorHorario(String fechaInicio, String fechaFin);

  @Query(value = """
      SELECT
          DATE_FORMAT(v.fecha_venta, '%H:00') as hora,
          p.nombre as producto,
          SUM(v.cantidad) as cantidad
      FROM venta v
      JOIN producto p ON v.producto_id = p.id
      WHERE v.fecha_venta BETWEEN :fechaInicio AND :fechaFin
      GROUP BY DATE_FORMAT(v.fecha_venta, '%H:00'), p.id, p.nombre
      ORDER BY hora, cantidad DESC;
          """, nativeQuery = true)
  public List<IProductosPorHora> getProductosPorHora(String fechaInicio, String fechaFin);

}
