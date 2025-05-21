package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa un actor en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Actor extends Entity {
    private String nombrePrimer;
    private String apellido;
    
    /**
     * Constructor por defecto
     */
    public Actor() {
        super();
    }
    
    /**
     * Constructor con nombre y apellido
     * @param nombrePrimer Nombre del actor
     * @param apellido Apellido del actor
     */
    public Actor(String nombrePrimer, String apellido) {
        super();
        this.nombrePrimer = nombrePrimer;
        this.apellido = apellido;
    }
    
    /**
     * Constructor con ID, nombre y apellido
     * @param id ID del actor
     * @param nombrePrimer Nombre del actor
     * @param apellido Apellido del actor
     */
    public Actor(int id, String nombrePrimer, String apellido) {
        super(id);
        this.nombrePrimer = nombrePrimer;
        this.apellido = apellido;
    }
    
    /**
     * Constructor completo
     * @param id ID del actor
     * @param nombrePrimer Nombre del actor
     * @param apellido Apellido del actor
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado del actor
     */
    public Actor(int id, String nombrePrimer, String apellido, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.nombrePrimer = nombrePrimer;
        this.apellido = apellido;
    }
    
    /**
     * Obtiene el nombre del actor
     * @return Nombre del actor
     */
    public String getNombrePrimer() {
        return nombrePrimer;
    }
    
    /**
     * Establece el nombre del actor
     * @param nombrePrimer Nombre del actor
     */
    public void setNombrePrimer(String nombrePrimer) {
        this.nombrePrimer = nombrePrimer;
    }
    
    /**
     * Obtiene el apellido del actor
     * @return Apellido del actor
     */
    public String getApellido() {
        return apellido;
    }
    
    /**
     * Establece el apellido del actor
     * @param apellido Apellido del actor
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    @Override
    public boolean validar() {
        return nombrePrimer != null && !nombrePrimer.isEmpty() && 
               apellido != null && !apellido.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", nombrePrimer='" + nombrePrimer + '\'' +
                ", apellido='" + apellido + '\'' +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
