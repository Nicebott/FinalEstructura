package com.sakila.models;

import java.util.Date;

/**
 * Clase abstracta base para todas las entidades del sistema
 * @author Nicolas Zierow Fermin
 */
public abstract class Entity {
    protected int id;
    protected Date fechaCreacion;
    protected Date ultimaActualizacion;
    protected boolean activo = true;
    
    /**
     * Constructor por defecto
     */
    public Entity() {
        this.fechaCreacion = new Date();
        this.ultimaActualizacion = new Date();
    }
    
    /**
     * Constructor con ID
     * @param id ID de la entidad
     */
    public Entity(int id) {
        this();
        this.id = id;
    }
    
    /**
     * Constructor completo
     * @param id ID de la entidad
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado de la entidad
     */
    public Entity(int id, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
        this.activo = activo;
    }
    
    /**
     * Obtiene el ID de la entidad
     * @return ID de la entidad
     */
    public int getId() {
        return id;
    }
    
    /**
     * Establece el ID de la entidad
     * @param id ID de la entidad
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtiene la fecha de creación
     * @return Fecha de creación
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    /**
     * Establece la fecha de creación
     * @param fechaCreacion Fecha de creación
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    /**
     * Obtiene la fecha de última actualización
     * @return Fecha de última actualización
     */
    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }
    
    /**
     * Establece la fecha de última actualización
     * @param ultimaActualizacion Fecha de última actualización
     */
    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
    
    /**
     * Verifica si la entidad está activa
     * @return true si está activa, false en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }
    
    /**
     * Establece el estado de la entidad
     * @param activo Estado de la entidad
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    /**
     * Actualiza la fecha de última actualización
     */
    public void actualizarFecha() {
        this.ultimaActualizacion = new Date();
    }
    
    /**
     * Método abstracto para validar la entidad
     * @return true si la entidad es válida, false en caso contrario
     */
    public abstract boolean validar();
}
