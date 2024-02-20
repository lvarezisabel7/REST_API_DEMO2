package com.example.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "productos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String name;

    @NonNull
    private String descripcion;

    private int stock;
    private int price;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    @JoinTable(name = "producto_presentaciones",
        joinColumns = { @JoinColumn(name = "producto_id") },
        inverseJoinColumns = { @JoinColumn(name = "presentacion_id")
    })
    private Set<Presentacion> presentaciones = new HashSet<>();

    // Método para agregar una presentación al conjunto de presentaciones
    public void addPresentacion(Presentacion presentacion) {
        this.presentaciones.add(presentacion);
        presentacion.getProductos().add(this);
    }

    // Método para eliminar la presentacion del producto
    public void removePresentacion(int presentacionId) {
        Presentacion presentacion = this.presentaciones.stream().filter(p -> p.getId() == presentacionId).findFirst().orElse(null);
        if (presentacion != null) {
          this.presentaciones.remove(presentacion);
          presentacion.getProductos().remove(this);
        }
}

}
