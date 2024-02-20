package com.example.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Producto;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "http://localhost:8080") -> es que aceptas otro json aunque tu estes trabajando en otro puerto
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository productoRepository;

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> getAllProductos(@RequestParam(required = false) String name) {
        List<Producto> productos = new ArrayList<Producto>();

        if (name == null) 
            productoRepository.findAll().forEach(productos::add);
        else
            productoRepository.findByName(name).forEach(productos::add);
       
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } 

        return new ResponseEntity<>(productos, HttpStatus.OK);
    }
    
    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable("id") int id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Not found Producto with id = " + id));
        
        return new ResponseEntity<>(producto, HttpStatus.OK);    
    }

    @PostMapping("/productos")
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {


        Producto nuevoProducto = productoRepository.save(
            Producto.builder()
                .name(producto.getName())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .price(producto.getPrice())
                .presentaciones(producto.getPresentaciones())
                .build()
        );
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);   
    } 

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable("id") int id, @RequestBody Producto producto) {
        Producto _producto = productoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

        _producto.setName(producto.getName());
        _producto.setDescripcion(producto.getDescripcion());
        _producto.setStock(producto.getStock());
        _producto.setPrice(producto.getPrice());
        _producto.setPresentaciones(producto.getPresentaciones());


        return new ResponseEntity<>(productoRepository.save(_producto), HttpStatus.OK);
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<HttpStatus> deleteProducto(@PathVariable("id") int id) {
    productoRepository.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/productos")
    public ResponseEntity<HttpStatus> deleteAllProductos() {
        productoRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    }

  