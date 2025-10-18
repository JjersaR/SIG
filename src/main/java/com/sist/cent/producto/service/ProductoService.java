package com.sist.cent.producto.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.sist.cent.producto.controller.dto.ProductoDTO;
import com.sist.cent.producto.controller.dto.ProductoRequest;
import com.sist.cent.producto.entity.Producto;
import com.sist.cent.producto.repository.IProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

  private final IProductoRepository repository;

  public List<ProductoDTO> getAll() {
    return toProductoDTOList(repository.findAll());
  }

  public ProductoDTO getById(Long id) {
    var producto = repository.findById(id);
    return (!producto.isEmpty()) ? toProductoDTO(producto.get()) : null;
  }

  public void save(ProductoRequest producto) {
    repository.save(toProducto(producto));
  }

  public void update(ProductoRequest producto) {
    repository.save(toProducto(producto));
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  // mapper
  private ProductoDTO toProductoDTO(Producto producto) {
    return ProductoDTO.builder()
        .id(producto.getId())
        .nombre(producto.getNombre())
        .precio(producto.getPrecio())
        .descripcion(producto.getDescripcion())
        .categoria(producto.getCategoria())
        .sucursalId(producto.getSucursalId())
        .activo(producto.getActivo())
        .totalVendidos(producto.getTotalVendidos())
        .build();
  }

  private Producto toProducto(ProductoRequest producto) {
    return Producto.builder()
        .id(producto.getId())
        .nombre(producto.getNombre())
        .precio(producto.getPrecio())
        .descripcion(producto.getDescripcion())
        .categoria(producto.getCategoria())
        .sucursalId(producto.getSucursalId())
        .activo(producto.getActivo())
        .totalVendidos(producto.getTotalVendidos())
        .build();
  }

  private List<ProductoDTO> toProductoDTOList(List<Producto> productos) {
    if (productos == null || productos.isEmpty()) {
      return Collections.emptyList();
    }

    return productos.stream()
        .filter(Objects::nonNull)
        .map(this::toProductoDTO)
        .toList();
  }

}
