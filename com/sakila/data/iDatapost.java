package com.sakila.data;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD en la base de datos
 * @param <T> Tipo de entidad
 * @author Nicolas Zierow Fermin
 */
public interface iDatapost<T> {
    /**
     * Crea una nueva entidad en la base de datos
     * @param entity Entidad a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    boolean post(T entity);
    
    /**
     * Actualiza una entidad existente en la base de datos
     * @param entity Entidad a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean put(T entity);
    
    /**
     * Marca una entidad como inactiva en la base de datos
     * @param id ID de la entidad a eliminar
     * @return true si se marcó como inactiva correctamente, false en caso contrario
     */
    boolean delete(int id);
    
    /**
     * Obtiene una entidad por su ID
     * @param id ID de la entidad
     * @return Entidad encontrada o null si no existe
     */
    T get(int id);
    
    /**
     * Obtiene todas las entidades
     * @return Lista de entidades
     */
    List<T> get();
    
    /**
     * Busca entidades según criterios
     * @param criterios Criterios de búsqueda
     * @return Lista de entidades que cumplen los criterios
     */
    List<T> get(String... criterios);
}
