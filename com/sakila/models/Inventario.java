package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa un inventario en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Inventario extends Entity {
    private Pelicula pelicula;
    private Store tienda;
    
    /**
     * Constructor por defecto
     */
    public Inventario() {
        super();
    }
    
    /**
     * Constructor con película y tienda
     * @param pelicula Película
     * @param tienda Tienda
     */
    public Inventario(Pelicula pelicula, Store tienda) {
        super();
        this.pelicula = pelicula;
        this.tienda = tienda;
    }
    
    /**
     * Constructor con ID, película y tienda
     * @param id ID del inventario
     * @param pelicula Película
     * @param tienda Tienda
     */
    public Inventario(int id, Pelicula pelicula, Store tienda) {
        super(id);
        this.pelicula = pelicula;
        this.tienda = tienda;
    }
    
    /**
     * Constructor completo
     * @param id ID del inventario
     * @param pelicula Película
     * @param tienda Tienda
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado del inventario
     */
    public Inventario(int id, Pelicula pelicula, Store tienda, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.pelicula = pelicula;
        this.tienda = tienda;
    }
    
    /**
     * Obtiene la película
     * @return Película
     */
    public Pelicula getPelicula() {
        return pelicula;
    }
    
    /**
     * Establece la película
     * @param pelicula Película
     */
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }
    
    /**
     * Obtiene la tienda
     * @return Tienda
     */
    public Store getTienda() {
        return tienda;
    }
    
    /**
     * Establece la tienda
     * @param tienda Tienda
     */
    public void setTienda(Store tienda) {
        this.tienda = tienda;
    }
    
    @Override
    public boolean validar() {
        return pelicula != null && pelicula.validar() && 
               tienda != null && tienda.validar();
    }
    
    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", pelicula=" + (pelicula != null ? pelicula.getTitulo() : "null") +
                ", tienda=" + (tienda != null ? tienda.getId() : "null") +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
