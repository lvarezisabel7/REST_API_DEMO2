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
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Presentacion;
import com.example.entities.Producto;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.PresentacionRepository;
import com.example.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PresentacionController {

    private final ProductoRepository productoRepository;
    private final PresentacionRepository presentacionRepository;

@GetMapping("/presentaciones")
public ResponseEntity<List<Presentacion>> getAllPresentaciones() {
    List<Presentacion> presentaciones = new ArrayList<>();

    presentacionRepository.findAll().forEach(presentaciones::add);

    if (presentaciones.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

  return new ResponseEntity<>(presentaciones, HttpStatus.OK);
}

@SuppressWarnings("null")
@GetMapping("/productos/{productoId}/presentaciones")
public ResponseEntity<List<Presentacion>> getAllPresentacionesByProductoId(@PathVariable(value = "productoId") Integer productoId) {
    if (!productoRepository.existsById(productoId)) {
        throw new ResourceNotFoundException("Not found Producto with id = " + productoId);
    }
    List<Presentacion> presentaciones = presentacionRepository.findPresentacionesByProductosId(productoId);
    return new ResponseEntity<>(presentaciones, HttpStatus.OK);
}

@GetMapping("/presentaciones/{id}")
public ResponseEntity<Presentacion> getPresentacioensById(@PathVariable(value = "id") Integer id) {
    Presentacion presentacion = presentacionRepository.findById(id)
    .orElseThrow(
        () -> new ResourceNotFoundException("Not found Presentacion with id = " + id));

        return new ResponseEntity<>(presentacion, HttpStatus.OK);
 }

@GetMapping("/presentaciones/{presentacionId}/productos")
public ResponseEntity<List<Producto>> getAllProductosByPresentacionId(@PathVariable(value = "presentacionId") Integer presentacionId) {
    if (!presentacionRepository.existsById(presentacionId)) {
        throw new ResourceNotFoundException("Not found Presentacion with id = " + presentacionId);
    }

    List<Producto> productos = productoRepository.findProductosByPresentacionesId(presentacionId);
    return new ResponseEntity<>(productos, HttpStatus.OK);

}

@PostMapping("/productos/{productoId}/presentaciones")
public ResponseEntity<Presentacion> addPresentacion(@PathVariable(value = "productoId") Integer productoId, 
    @RequestBody Presentacion presentacionRequest)  {
        Presentacion presentacion = productoRepository.findById(productoId).map(producto -> {
            int presentacionId = presentacionRequest.getId();

        // presentacion existe
        if (presentacionId != 0L) {
            Presentacion _presentacion = presentacionRepository.findById(presentacionId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Not found Presentacion with id = " + presentacionId));
            producto.addPresentacion(_presentacion);
            productoRepository.save(producto);
            return _presentacion;  
        }

        // add and create a new presentacion
        producto.addPresentacion(presentacionRequest);
        return presentacionRepository.save(presentacionRequest);
        }) .orElseThrow(
        () -> new ResourceNotFoundException("Not found Producto with id = " + productoId));

        return new ResponseEntity<>(presentacion, HttpStatus.CREATED);
    }

    @PutMapping("/presentaciones/{id}")
    public ResponseEntity<Presentacion> updatePresentacion(@PathVariable("id") int id, @RequestBody Presentacion presentacionRequest) {
        Presentacion presentacion = presentacionRepository.findById(id)
          .orElseThrow(
             () -> new ResourceNotFoundException("PresentacionId " + id + "not found"));
        
        presentacion.setName(presentacionRequest.getName());

        return new ResponseEntity<>(presentacionRepository.save(presentacion), HttpStatus.OK);
    }

    @DeleteMapping("/productos/{productoId}/presentaciones/{presentacionId}")
    public ResponseEntity<HttpStatus> deletePresentacionFromProducto(
        @PathVariable(value = "productoId") Integer productoId,
        @PathVariable(value = "presentacionId") Integer presentacionId ) {
        Producto producto = productoRepository.findById(productoId)
          .orElseThrow(
               () -> new ResourceNotFoundException("Not found Producto with id = " + productoId));
        
    
        producto.removePresentacion(presentacionId);
        productoRepository.save(producto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/presentaciones/{id}")
      public ResponseEntity<HttpStatus> deletePresentacion(@PathVariable("id") int id) {
        presentacionRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

}
 

