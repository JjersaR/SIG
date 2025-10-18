package com.sist.cent.sucursal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sist.cent.sucursal.entity.Sucursal;

@Repository
public interface ISucursalRepository extends JpaRepository<Sucursal, Long> {

}
