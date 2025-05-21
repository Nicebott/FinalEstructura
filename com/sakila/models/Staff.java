package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa un miembro del personal en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Staff extends Entity {
    private String firstName;
    private String lastName;
    private Address address;
    private String email;
    private Store store;
    private boolean active;
    private String username;
    private String password;
    private byte[] picture;
    
    /**
     * Constructor por defecto
     */
    public Staff() {
        super();
        this.active = true;
    }
    
    /**
     * Constructor con campos básicos
     * @param firstName Nombre
     * @param lastName Apellido
     * @param address Dirección
     * @param email Correo electrónico
     * @param username Nombre de usuario
     */
    public Staff(String firstName, String lastName, Address address, String email, String username) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.username = username;
        this.active = true;
    }
    
    /**
     * Constructor con ID y campos básicos
     * @param id ID del miembro del personal
     * @param firstName Nombre
     * @param lastName Apellido
     * @param address Dirección
     * @param email Correo electrónico
     * @param username Nombre de usuario
     */
    public Staff(int id, String firstName, String lastName, Address address, String email, String username) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.username = username;
        this.active = true;
    }
    
    /**
     * Constructor completo
     * @param id ID del miembro del personal
     * @param firstName Nombre
     * @param lastName Apellido
     * @param address Dirección
     * @param email Correo electrónico
     * @param store Tienda
     * @param active Estado activo
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param picture Imagen
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     */
    public Staff(int id, String firstName, String lastName, Address address, String email, 
                Store store, boolean active, String username, String password, byte[] picture,
                Date fechaCreacion, Date ultimaActualizacion) {
        super(id, fechaCreacion, ultimaActualizacion, active);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.store = store;
        this.active = active;
        this.username = username;
        this.password = password;
        this.picture = picture;
    }
    
    /**
     * Obtiene el nombre
     * @return Nombre
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Establece el nombre
     * @param firstName Nombre
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Obtiene el apellido
     * @return Apellido
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Establece el apellido
     * @param lastName Apellido
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Obtiene la dirección
     * @return Dirección
     */
    public Address getAddress() {
        return address;
    }
    
    /**
     * Establece la dirección
     * @param address Dirección
     */
    public void setAddress(Address address) {
        this.address = address;
    }
    
    /**
     * Obtiene el correo electrónico
     * @return Correo electrónico
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el correo electrónico
     * @param email Correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Obtiene la tienda
     * @return Tienda
     */
    public Store getStore() {
        return store;
    }
    
    /**
     * Establece la tienda
     * @param store Tienda
     */
    public void setStore(Store store) {
        this.store = store;
    }
    
    /**
     * Verifica si está activo
     * @return true si está activo, false en caso contrario
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Establece el estado activo
     * @param active Estado activo
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Obtiene el nombre de usuario
     * @return Nombre de usuario
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Establece el nombre de usuario
     * @param username Nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Obtiene la contraseña
     * @return Contraseña
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Establece la contraseña
     * @param password Contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Obtiene la imagen
     * @return Imagen
     */
    public byte[] getPicture() {
        return picture;
    }
    
    /**
     * Establece la imagen
     * @param picture Imagen
     */
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
    
    @Override
    public boolean validar() {
        return firstName != null && !firstName.isEmpty() && 
               lastName != null && !lastName.isEmpty() && 
               address != null && address.validar() && 
               username != null && !username.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", active=" + active +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
