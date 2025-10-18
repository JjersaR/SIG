package com.sist.cent.sucursal.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.sist.cent.sucursal.controller.dto.SucursalDTO;
import com.sist.cent.sucursal.controller.dto.SucursalRequest;
import com.sist.cent.sucursal.entity.Sucursal;
import com.sist.cent.sucursal.repository.ISucursalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SucursalService {

  private final ISucursalRepository repository;

  public List<SucursalDTO> getAll() {
    return toSucursalDTOList(repository.findAll());
  }

  public SucursalDTO getById(Long id) {
    var sucursal = repository.findById(id);
    return (sucursal.isPresent()) ? toSucursalDTO(sucursal.get()) : null;
  }

  public void save(SucursalRequest sucursal) {
    repository.save(toSucursalRequest(sucursal));
  }

  public void update(SucursalDTO sucursal) {
    repository.save(toSucursal(sucursal));
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  // mapper
  private SucursalDTO toSucursalDTO(Sucursal sucursal) {
    return SucursalDTO.builder()
        .id(sucursal.getId())
        .nombre(sucursal.getNombre())
        .direccion(sucursal.getDireccion())
        .telefono(sucursal.getTelefono())
        .encargado(sucursal.getEncargado())
        .build();
  }

  private Sucursal toSucursal(SucursalDTO sucursal) {
    return Sucursal.builder()
        .id(sucursal.getId())
        .nombre(sucursal.getNombre())
        .direccion(sucursal.getDireccion())
        .telefono(sucursal.getTelefono())
        .encargado(sucursal.getEncargado())
        .build();
  }

  private List<SucursalDTO> toSucursalDTOList(List<Sucursal> sucursales) {
    if (sucursales == null || sucursales.isEmpty()) {
      return Collections.emptyList();
    }

    return sucursales.stream()
        .filter(Objects::nonNull)
        .map(this::toSucursalDTO)
        .toList();
  }

  private Sucursal toSucursalRequest(SucursalRequest sucursal) {
    return Sucursal.builder()
        .nombre(sucursal.getNombre())
        .direccion(sucursal.getDireccion())
        .telefono(sucursal.getTelefono())
        .encargado(sucursal.getEncargado())
        .build();
  }

}
