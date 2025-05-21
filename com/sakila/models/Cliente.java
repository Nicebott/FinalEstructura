package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa un cliente en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Cliente extends Entity {
    private Store tienda;
    private String primerNombre;
    private String apellido;
    private String correoElectronico;
    private Address direccion;
    
    /**
     * Constructor por defecto
     */
    public Cliente() {
        super();
    }
    
    /**
     * Constructor con campos básicos
     * @param tienda Tienda del cliente
     * @param primerNombre Nombre del cliente
     * @param apellido Apellido del cliente
     * @param correoElectronico Correo electrónico del cliente
     * @param direccion Dirección del cliente
     */
    public Cliente(Store tienda, String primerNombre, String apellido, String correoElectronico, Address direccion) {
        super();
        this.tienda = tienda;
        this.primerNombre = primerNombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
    }
    
    /**
     * Constructor con ID y campos básicos
     * @param id ID del cliente
     * @param tienda Tienda del cliente
     * @param primerNombre Nombre del cliente
     * @param apellido Apellido del cliente
     * @param correoElectronico Correo electrónico del cliente
     * @param direccion Dirección del cliente
     */
    public Cliente(int id, Store tienda, String primerNombre, String apellido, String correoElectronico, Address direccion) {
        super(id);
        this.tienda = tienda;
        this.primerNombre = primerNombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
    }
    
    /**
     * Constructor completo
     * @param id ID del cliente
     * @param tienda Tienda del cliente
     * @param primerNombre Nombre del cliente
     * @param apellido Apellido del cliente
     * @param correoElectronico Correo electrónico del cliente
     * @param direccion Dirección del cliente
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado del cliente
     */
    public Cliente(int id, Store tienda, String primerNombre, String apellido, String correoElectronico, 
                  Address direccion, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.tienda = tienda;
        this.primerNombre = primerNombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
    }
    
    /**
     * Obtiene la tienda del cliente
     * @return Tienda del cliente
     */
    public Store getTienda() {
        return tienda;
    }
    
    /**
     * Establece la tienda del cliente
     * @param tienda Tienda del cliente
     */
    public void setTienda(Store tienda) {
        this.tienda = tienda;
    }
    
    /**
     * Obtiene el nombre del cliente
     * @return Nombre del cliente
     */
    public String getPrimerNombre() {
        return primerNombre;
    }
    
    /**
     * Establece el nombre del cliente
     * @param primerNombre Nombre del cliente
     */
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    
    /**
     * Obtiene el apellido del cliente
     * @return Apellido del cliente
     */
    public String getApellido() {
        return apellido;
    }
    
    /**
     * Establece el apellido del cliente
     * @param apellido Apellido del cliente
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    /**
     * Obtiene el correo electrónico del cliente
     * @return Correo electrónico del cliente
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    /**
     * Establece el correo electrónico del cliente
     * @param correoElectronico Correo electrónico del cliente
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
    /**
     * Obtiene la dirección del cliente
     * @return Dirección del cliente
     */
    public Address getDireccion() {
        return direccion;
    }
    
    /**
     * Establece la dirección del cliente
     * @param direccion Dirección del cliente
     */
    public void setDireccion(Address direccion) {
        this.direccion = direccion;
    }
    
    @Override
    public boolean validar() {
        return tienda != null && tienda.validar() && 
               primerNombre != null && !primerNombre.isEmpty() && 
               apellido != null && !apellido.isEmpty() && 
               direccion != null && direccion.validar();
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", tienda=" + (tienda != null ? tienda.getId() : "null") +
                ", primerNombre='" + primerNombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", direccion=" + (direccion != null ? direccion.getAddress() : "null") +
                ", activo=" + activo +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
