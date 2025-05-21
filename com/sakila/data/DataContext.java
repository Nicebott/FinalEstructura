package com.sakila.data;

import com.sakila.models.Entity;
import com.sakila.utils.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que implementa la interfaz iDatapost y proporciona métodos concretos
 * que los hijos no pueden sobrescribir
 * @param <T> Tipo de entidad
 * @author Nicolas Zierow Fermin
 */
public abstract class DataContext<T extends Entity> implements iDatapost<T> {
    protected Connection conexion;
    protected List<T> entidades;
    
    /**
     * Constructor
     * @param conexion Conexión a la base de datos
     */
    public DataContext(Connection conexion) {
        this.conexion = conexion;
        this.entidades = new ArrayList<>();
    }
    
    /**
     * Método final para crear una entidad
     * @param entity Entidad a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    @Override
    public final boolean post(T entity) {
        if (!entity.validar()) {
            Logger.error("Entidad no válida para crear");
            return false;
        }
        
        return postImplementation(entity);
    }
    
    /**
     * Método final para actualizar una entidad
     * @param entity Entidad a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    @Override
    public final boolean put(T entity) {
        if (!entity.validar()) {
            Logger.error("Entidad no válida para actualizar");
            return false;
        }
        
        return putImplementation(entity);
    }
    
    /**
     * Método final para eliminar una entidad
     * @param id ID de la entidad a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    @Override
    public final boolean delete(int id) {
        if (id <= 0) {
            Logger.error("ID no válido para eliminar");
            return false;
        }
        
        return deleteImplementation(id);
    }
    
    /**
     * Método final para obtener una entidad por su ID
     * @param id ID de la entidad
     * @return Entidad encontrada o null si no existe
     */
    @Override
    public final T get(int id) {
        if (id <= 0) {
            Logger.error("ID no válido para buscar");
            return null;
        }
        
        return getImplementation(id);
    }
    
    /**
     * Método final para obtener todas las entidades
     * @return Lista de entidades
     */
    @Override
    public final List<T> get() {
        return getImplementation();
    }
    
    /**
     * Método final para buscar entidades según criterios
     * @param criterios Criterios de búsqueda
     * @return Lista de entidades que cumplen los criterios
     */
    @Override
    public final List<T> get(String... criterios) {
        if (criterios == null || criterios.length == 0) {
            Logger.error("Criterios de búsqueda no válidos");
            return new ArrayList<>();
        }
        
        return getImplementation(criterios);
    }
    
    /**
     * Implementación del método post
     * @param entity Entidad a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    protected abstract boolean postImplementation(T entity);
    
    /**
     * Implementación del método put
     * @param entity Entidad a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    protected abstract boolean putImplementation(T entity);
    
    /**
     * Implementación del método delete
     * @param id ID de la entidad a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    protected abstract boolean deleteImplementation(int id);
    
    /**
     * Implementación del método get por ID
     * @param id ID de la entidad
     * @return Entidad encontrada o null si no existe
     */
    protected abstract T getImplementation(int id);
    
    /**
     * Implementación del método get para todas las entidades
     * @return Lista de entidades
     */
    protected abstract List<T> getImplementation();
    
    /**
     * Implementación del método get con criterios
     * @param criterios Criterios de búsqueda
     * @return Lista de entidades que cumplen los criterios
     */
    protected abstract List<T> getImplementation(String... criterios);
}
