package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa un idioma en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Language extends Entity {
    private String name;
    
    /**
     * Constructor por defecto
     */
    public Language() {
        super();
    }
    
    /**
     * Constructor con nombre
     * @param name Nombre del idioma
     */
    public Language(String name) {
        super();
        this.name = name;
    }
    
    /**
     * Constructor con ID y nombre
     * @param id ID del idioma
     * @param name Nombre del idioma
     */
    public Language(int id, String name) {
        super(id);
        this.name = name;
    }
    
    /**
     * Constructor completo
     * @param id ID del idioma
     * @param name Nombre del idioma
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado del idioma
     */
    public Language(int id, String name, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.name = name;
    }
    
    /**
     * Obtiene el nombre del idioma
     * @return Nombre del idioma
     */
    public String getName() {
        return name;
    }
    
    /**
     * Establece el nombre del idioma
     * @param name Nombre del idioma
     */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean validar() {
        return name != null && !name.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
