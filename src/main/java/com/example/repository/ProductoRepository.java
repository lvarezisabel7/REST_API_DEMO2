package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
   
    List<Producto> findProductosByPresentacionesId(Integer presentacionId);
    List<Producto> findByName(String name);
  
    
}
