package com.sist.cent.venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sist.cent.venta.controller.dto.IDashBoard;
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

}
