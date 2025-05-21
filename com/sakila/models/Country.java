package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa un país en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Country extends Entity {
    private String country;
    
    /**
     * Constructor por defecto
     */
    public Country() {
        super();
    }
    
    /**
     * Constructor con nombre
     * @param country Nombre del país
     */
    public Country(String country) {
        super();
        this.country = country;
    }
    
    /**
     * Constructor con ID y nombre
     * @param id ID del país
     * @param country Nombre del país
     */
    public Country(int id, String country) {
        super(id);
        this.country = country;
    }
    
    /**
     * Constructor completo
     * @param id ID del país
     * @param country Nombre del país
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado del país
     */
    public Country(int id, String country, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.country = country;
    }
    
    /**
     * Obtiene el nombre del país
     * @return Nombre del país
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * Establece el nombre del país
     * @param country Nombre del país
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    @Override
    public boolean validar() {
        return country != null && !country.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
