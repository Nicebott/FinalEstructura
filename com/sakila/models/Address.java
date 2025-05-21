package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa una dirección en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Address extends Entity {
    private String address;
    private String address2;
    private String district;
    private City city;
    private String postalCode;
    private String phone;
    
    /**
     * Constructor por defecto
     */
    public Address() {
        super();
    }
    
    /**
     * Constructor con campos básicos
     * @param address Dirección
     * @param district Distrito
     * @param city Ciudad
     * @param phone Teléfono
     */
    public Address(String address, String district, City city, String phone) {
        super();
        this.address = address;
        this.district = district;
        this.city = city;
        this.phone = phone;
    }
    
    /**
     * Constructor con ID y campos básicos
     * @param id ID de la dirección
     * @param address Dirección
     * @param district Distrito
     * @param city Ciudad
     * @param phone Teléfono
     */
    public Address(int id, String address, String district, City city, String phone) {
        super(id);
        this.address = address;
        this.district = district;
        this.city = city;
        this.phone = phone;
    }
    
    /**
     * Constructor completo
     * @param id ID de la dirección
     * @param address Dirección
     * @param address2 Dirección secundaria
     * @param district Distrito
     * @param city Ciudad
     * @param postalCode Código postal
     * @param phone Teléfono
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado de la dirección
     */
    public Address(int id, String address, String address2, String district, City city, 
                  String postalCode, String phone, Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.address = address;
        this.address2 = address2;
        this.district = district;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
    }
    
    /**
     * Obtiene la dirección
     * @return Dirección
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Establece la dirección
     * @param address Dirección
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Obtiene la dirección secundaria
     * @return Dirección secundaria
     */
    public String getAddress2() {
        return address2;
    }
    
    /**
     * Establece la dirección secundaria
     * @param address2 Dirección secundaria
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    /**
     * Obtiene el distrito
     * @return Distrito
     */
    public String getDistrict() {
        return district;
    }
    
    /**
     * Establece el distrito
     * @param district Distrito
     */
    public void setDistrict(String district) {
        this.district = district;
    }
    
    /**
     * Obtiene la ciudad
     * @return Ciudad
     */
    public City getCity() {
        return city;
    }
    
    /**
     * Establece la ciudad
     * @param city Ciudad
     */
    public void setCity(City city) {
        this.city = city;
    }
    
    /**
     * Obtiene el código postal
     * @return Código postal
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    /**
     * Establece el código postal
     * @param postalCode Código postal
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    /**
     * Obtiene el teléfono
     * @return Teléfono
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * Establece el teléfono
     * @param phone Teléfono
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public boolean validar() {
        return address != null && !address.isEmpty() && 
               district != null && !district.isEmpty() && 
               city != null && city.validar() && 
               phone != null && !phone.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", district='" + district + '\'' +
                ", city=" + (city != null ? city.getCity() : "null") +
                ", phone='" + phone + '\'' +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
