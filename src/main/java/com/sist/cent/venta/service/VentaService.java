package com.sist.cent.venta.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.sist.cent.venta.controller.dto.IDashBoard;
import com.sist.cent.venta.controller.dto.IDatosSucursal;
import com.sist.cent.venta.controller.dto.IProductoMasVendido;
import com.sist.cent.venta.controller.dto.SucursalDashboardDTO;
import com.sist.cent.venta.controller.dto.TopProductos;
import com.sist.cent.venta.controller.dto.VentaDTO;
import com.sist.cent.venta.controller.dto.VentaRequest;
import com.sist.cent.venta.entity.Venta;
import com.sist.cent.venta.repository.IVentaRepository;

import lombok.RequiredArgsConstructor;

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
