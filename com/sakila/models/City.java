package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa una ciudad en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class City extends Entity {
    private String city;
    private Country country;
    
    /**
     * Constructor por defecto
     */
    public City() {
        super();
    }
    
    /**
     * Constructor con nombre y país
     * @param city Nombre de la ciudad
     * @param country País al que pertenece
     */
    public City(String city, Country country) {
        super();
        this.city = city;
        this.country = country;
    }
    
    /**
     * Constructor con ID, nombre y país
     * @param id ID de la ciudad
     * @param city Nombre de la ciudad
     * @param country País al que pertenece
     */
    public City(int id, String city, Country country) {
        super(id);
        this.city = city;
        this.country = country;
    }
    
    /**
     * Constructor completo
     * @param id ID de la ciudad
     * @param city Nombre de la ciudad
     * @param country País al que pertenece
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado de la ciudad
     */
    public City(int id, String city, Country country, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.city = city;
        this.country = country;
    }
    
    /**
     * Obtiene el nombre de la ciudad
     * @return Nombre de la ciudad
     */
    public String getCity() {
        return city;
    }
    
    /**
     * Establece el nombre de la ciudad
     * @param city Nombre de la ciudad
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /**
     * Obtiene el país al que pertenece la ciudad
     * @return País al que pertenece
     */
    public Country getCountry() {
        return country;
    }
    
    /**
     * Establece el país al que pertenece la ciudad
     * @param country País al que pertenece
     */
    public void setCountry(Country country) {
        this.country = country;
    }
    
    @Override
    public boolean validar() {
        return city != null && !city.isEmpty() && country != null && country.validar();
    }
    
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", country=" + (country != null ? country.getCountry() : "null") +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
