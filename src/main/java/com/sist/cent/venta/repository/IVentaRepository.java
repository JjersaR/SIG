package com.sist.cent.venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sist.cent.venta.entity.Venta;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long> {

}
