package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa una tienda en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Store extends Entity {
    private Address address;
    private Staff manager;
    
    /**
     * Constructor por defecto
     */
    public Store() {
        super();
    }
    
    /**
     * Constructor con dirección y gerente
     * @param address Dirección de la tienda
     * @param manager Gerente de la tienda
     */
    public Store(Address address, Staff manager) {
        super();
        this.address = address;
        this.manager = manager;
    }
    
    /**
     * Constructor con ID, dirección y gerente
     * @param id ID de la tienda
     * @param address Dirección de la tienda
     * @param manager Gerente de la tienda
     */
    public Store(int id, Address address, Staff manager) {
        super(id);
        this.address = address;
        this.manager = manager;
    }
    
    /**
     * Constructor completo
     * @param id ID de la tienda
     * @param address Dirección de la tienda
     * @param manager Gerente de la tienda
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado de la tienda
     */
    public Store(int id, Address address, Staff manager, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.address = address;
        this.manager = manager;
    }
    
    /**
     * Obtiene la dirección de la tienda
     * @return Dirección de la tienda
     */
    public Address getAddress() {
        return address;
    }
    
    /**
     * Establece la dirección de la tienda
     * @param address Dirección de la tienda
     */
    public void setAddress(Address address) {
        this.address = address;
    }
    
    /**
     * Obtiene el gerente de la tienda
     * @return Gerente de la tienda
     */
    public Staff getManager() {
        return manager;
    }
    
    /**
     * Establece el gerente de la tienda
     * @param manager Gerente de la tienda
     */
    public void setManager(Staff manager) {
        this.manager = manager;
    }
    
    @Override
    public boolean validar() {
        return address != null && address.validar() && 
               manager != null && manager.validar();
    }
    
    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", address=" + (address != null ? address.getAddress() : "null") +
                ", manager=" + (manager != null ? manager.getFirstName() + " " + manager.getLastName() : "null") +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
