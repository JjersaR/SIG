package com.sist.cent.venta.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sist.cent.venta.controller.dto.EAgrupacion;
import com.sist.cent.venta.controller.dto.IAnalisisVentas;
import com.sist.cent.venta.controller.dto.IDashBoard;
import com.sist.cent.venta.controller.dto.IDiasSemana;
import com.sist.cent.venta.controller.dto.IProductoMasVendido;
import com.sist.cent.venta.controller.dto.IProductoMasVendidos;
import com.sist.cent.venta.controller.dto.IProductosMargenes;
import com.sist.cent.venta.controller.dto.IProductosPorHora;
import com.sist.cent.venta.controller.dto.ProductoDTO;
import com.sist.cent.venta.controller.dto.SucursalDashboardDTO;
import com.sist.cent.venta.controller.dto.TopProductos;
import com.sist.cent.venta.controller.dto.VentaDTO;
import com.sist.cent.venta.controller.dto.VentaRequest;
import com.sist.cent.venta.controller.dto.VentasPorHorarioDTO;
import com.sist.cent.venta.entity.Venta;
import com.sist.cent.venta.repository.IVentaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VentaService {

  private final IVentaRepository repository;

  public List<VentaDTO> getAll() {
    return toVentaDTOList(repository.findAll());
  }

  public VentaDTO getById(Long id) {
    var venta = repository.findById(id);
    return (venta.isPresent()) ? toVentaDTO(venta.get()) : null;
  }

  public void save(VentaRequest venta) {
    repository.save(toVenta(venta));
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public IDashBoard getDashBoard() {
    return repository.getDashboard();
  }

  public SucursalDashboardDTO getSucursalDashboard(Long id) {
    var datos = repository.datosSucursal(id);
    var productos = repository.getProductoMasVendidos(id);
    var horario = repository.getHorarioPico(id);

    return SucursalDashboardDTO.builder()
        .sucursal(datos.getSucursal())
        .ventasHoy(datos.getVentasHoy())
        .ticketPromedio(datos.getTicketPromedio())
        .topProductos(productos.stream()
            .map(this::toTopProductos).toList())
        .horarioPico(horario.getHorarioPico())
        .ventas(horario.getVentas())
        .build();
  }

  public List<IAnalisisVentas> getAnalisisVentas(String fechaInicio, String fechaFin, EAgrupacion agrupacion) {
    switch (agrupacion) {
      case EAgrupacion.DIARIA:
        return repository.getAnalisisDiaria(fechaInicio, fechaFin);
      case EAgrupacion.SEMANAL:
        return repository.getAnalisisSemanal(fechaInicio, fechaFin);
      case EAgrupacion.MENSUAL:
        return repository.getAnalisisMensual(fechaInicio, fechaFin);
      case EAgrupacion.ANUAL:
        return repository.getAnalisisAnual(fechaInicio, fechaFin);
      default:
        return Collections.emptyList();
    }
  }

  public List<IProductoMasVendidos> getProductoMasVendidos(String fechaInicio, String fechaFin, Long sucursalId,
      int top) {
    return repository.getProductoMasVendidos(fechaInicio, fechaFin, sucursalId, top);
  }

  public List<IProductosMargenes> getProductosMargenes(String fechaInicio, String fechaFin, Long sucursalId) {
    return repository.getProductosMargenes(fechaInicio, fechaFin, sucursalId);
  }

  public List<VentasPorHorarioDTO> getVentasPorHorario(String fechaInicio, String fechaFin) {
    var horas = repository.getVentasPorHorario(fechaInicio, fechaFin);
    var productos = repository.getProductosPorHora(fechaInicio, fechaFin);

    // Agrupar productos por hora usando Streams
    Map<String, List<ProductoDTO>> productosPorHoraMap = productos.stream()
        .collect(Collectors.groupingBy(
            IProductosPorHora::getHora,
            Collectors.mapping(
                producto -> ProductoDTO.builder()
                    .hora(producto.getHora())
                    .producto(producto.getProducto())
                    .cantidad(producto.getCantidad())
                    .build(),
                Collectors.toList())));

    // Combinar ventas con productos usando Streams
    return horas.stream()
        .map(venta -> {
          // Obtener los productos más vendidos para esta hora (top 3)
          List<String> productosMasVendidos = productosPorHoraMap
              .getOrDefault(venta.getHora(), Collections.emptyList())
              .stream()
              .sorted((p1, p2) -> p2.getCantidad().compareTo(p1.getCantidad())) // Orden descendente
              .limit(3) // Top 3 productos
              .map(ProductoDTO::getProducto)
              .toList();

          return VentasPorHorarioDTO.builder()
              .hora(venta.getHora())
              .ventasTotales(venta.getVentasTotales())
              .cantidadVentas(venta.getCantidadVentas())
              .productos(productosMasVendidos) // Solo los nombres
              .build();
        }).toList();

  }

  public List<IDiasSemana> getVentasDiarias(String fechaInicio, String fechaFin) {
    return repository.getVentasDiarias(fechaInicio, fechaFin);
  }

  // mapper
  private VentaDTO toVentaDTO(Venta venta) {
    return VentaDTO.builder()
        .id(venta.getId())
        .ventaId(venta.getVentaId())
        .productoId(venta.getProductoId())
        .cantidad(venta.getCantidad())
        .precioUnitario(venta.getPrecioUnitario())
        .subtotal(venta.getSubtotal())
        .costoUnitario(venta.getCostoUnitario())
        .margenUnitario(venta.getMargenUnitario())
        .fechaVenta(venta.getFechaVenta())
        .sucursalId(venta.getSucursalId())
        .medioPago(venta.getMedioPago())
        .usuarioId(venta.getUsuarioId())
        .build();
  }

  private List<VentaDTO> toVentaDTOList(List<Venta> ventas) {
    if (ventas == null || ventas.isEmpty()) {
      return Collections.emptyList();
    }

    return ventas.stream()
        .filter(Objects::nonNull)
        .map(this::toVentaDTO)
        .toList();
  }

  private Venta toVenta(VentaRequest venta) {
    return Venta.builder()
        .ventaId(venta.getVentaId())
        .productoId(venta.getProductoId())
        .cantidad(venta.getCantidad())
        .precioUnitario(venta.getPrecioUnitario())
        .subtotal(venta.getSubtotal())
        .costoUnitario(venta.getCostoUnitario())
        .margenUnitario(venta.getMargenUnitario())
        .fechaVenta(venta.getFechaVenta())
        .sucursalId(venta.getSucursalId())
        .medioPago(venta.getMedioPago())
        .usuarioId(venta.getUsuarioId())
        .build();
  }

  private TopProductos toTopProductos(IProductoMasVendido producto) {
    return TopProductos.builder()
        .nombre(producto.getNombre())
        .cantidad(producto.getCantidad())
        .build();
  }
}
