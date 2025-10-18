package com.sist.cent.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sist.cent.producto.entity.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {

}
