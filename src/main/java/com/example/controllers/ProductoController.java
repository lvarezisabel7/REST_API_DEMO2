package com.example.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Producto;
import com.example.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:8081")
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
    
    // @GetMapping("/productos/{id}")
    // public ResponseEntity<Producto> getProductoById(@PathVariable("id") int id) {
    //     Producto producto = productoRepository.findById(id)
            
    // }


//  @GetMapping("/tutorials/{id}")
//   public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
//     Tutorial tutorial = tutorialRepository.findById(id)
//         .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

//     return new ResponseEntity<>(tutorial, HttpStatus.OK);
//   }

//   @PostMapping("/tutorials")
//   public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
//     Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), true));
//     return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
//   }

}
