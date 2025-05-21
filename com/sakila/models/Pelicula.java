package com.sakila.models;

import java.util.Date;

/**
 * Clase que representa una película en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class Pelicula extends Entity {
    private String titulo;
    private String descripcion;
    private int anioLanzamiento;
    private Language idioma;
    private Language idiomaOriginal;
    private int duracionRenta;
    private double tarifaRenta;
    private int duracion;
    private double costoReemplazo;
    private String clasificacion;
    private String caracteristicasEspeciales;
    
    /**
     * Constructor por defecto
     */
    public Pelicula() {
        super();
        this.duracionRenta = 3; // Valor por defecto
        this.tarifaRenta = 4.99; // Valor por defecto
        this.costoReemplazo = 19.99; // Valor por defecto
        this.clasificacion = "G"; // Valor por defecto
    }
    
    /**
     * Constructor con campos básicos
     * @param titulo Título de la película
     * @param descripcion Descripción de la película
     * @param anioLanzamiento Año de lanzamiento
     * @param idioma Idioma de la película
     */
    public Pelicula(String titulo, String descripcion, int anioLanzamiento, Language idioma) {
        super();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.anioLanzamiento = anioLanzamiento;
        this.idioma = idioma;
        this.duracionRenta = 3; // Valor por defecto
        this.tarifaRenta = 4.99; // Valor por defecto
        this.costoReemplazo = 19.99; // Valor por defecto
        this.clasificacion = "G"; // Valor por defecto
    }
    
    /**
     * Constructor con ID y campos básicos
     * @param id ID de la película
     * @param titulo Título de la película
     * @param descripcion Descripción de la película
     * @param anioLanzamiento Año de lanzamiento
     * @param idioma Idioma de la película
     */
    public Pelicula(int id, String titulo, String descripcion, int anioLanzamiento, Language idioma) {
        super(id);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.anioLanzamiento = anioLanzamiento;
        this.idioma = idioma;
        this.duracionRenta = 3; // Valor por defecto
        this.tarifaRenta = 4.99; // Valor por defecto
        this.costoReemplazo = 19.99; // Valor por defecto
        this.clasificacion = "G"; // Valor por defecto
    }
    
    /**
     * Constructor completo
     * @param id ID de la película
     * @param titulo Título de la película
     * @param descripcion Descripción de la película
     * @param anioLanzamiento Año de lanzamiento
     * @param idioma Idioma de la película
     * @param idiomaOriginal Idioma original de la película
     * @param duracionRenta Duración de la renta en días
     * @param tarifaRenta Tarifa de renta
     * @param duracion Duración en minutos
     * @param costoReemplazo Costo de reemplazo
     * @param clasificacion Clasificación
     * @param caracteristicasEspeciales Características especiales
     * @param fechaCreacion Fecha de creación
     * @param ultimaActualizacion Fecha de última actualización
     * @param activo Estado de la película
     */
    public Pelicula(int id, String titulo, String descripcion, int anioLanzamiento, 
                   Language idioma, Language idiomaOriginal, int duracionRenta, 
                   double tarifaRenta, int duracion, double costoReemplazo, 
                   String clasificacion, String caracteristicasEspeciales,
                   Date fechaCreacion, Date ultimaActualizacion, boolean activo) {
        super(id, fechaCreacion, ultimaActualizacion, activo);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.anioLanzamiento = anioLanzamiento;
        this.idioma = idioma;
        this.idiomaOriginal = idiomaOriginal;
        this.duracionRenta = duracionRenta;
        this.tarifaRenta = tarifaRenta;
        this.duracion = duracion;
        this.costoReemplazo = costoReemplazo;
        this.clasificacion = clasificacion;
        this.caracteristicasEspeciales = caracteristicasEspeciales;
    }
    
    /**
     * Obtiene el título de la película
     * @return Título de la película
     */
    public String getTitulo() {
        return titulo;
    }
    
    /**
     * Establece el título de la película
     * @param titulo Título de la película
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    /**
     * Obtiene la descripción de la película
     * @return Descripción de la película
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Establece la descripción de la película
     * @param descripcion Descripción de la película
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Obtiene el año de lanzamiento de la película
     * @return Año de lanzamiento de la película
     */
    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }
    
    /**
     * Establece el año de lanzamiento de la película
     * @param anioLanzamiento Año de lanzamiento de la película
     */
    public void setAnioLanzamiento(int anioLanzamiento) {
        this.anioLanzamiento = anioLanzamiento;
    }
    
    /**
     * Obtiene el idioma de la película
     * @return Idioma de la película
     */
    public Language getIdioma() {
        return idioma;
    }
    
    /**
     * Establece el idioma de la película
     * @param idioma Idioma de la película
     */
    public void setIdioma(Language idioma) {
        this.idioma = idioma;
    }
    
    /**
     * Obtiene el idioma original de la película
     * @return Idioma original de la película
     */
    public Language getIdiomaOriginal() {
        return idiomaOriginal;
    }
    
    /**
     * Establece el idioma original de la película
     * @param idiomaOriginal Idioma original de la película
     */
    public void setIdiomaOriginal(Language idiomaOriginal) {
        this.idiomaOriginal = idiomaOriginal;
    }
    
    /**
     * Obtiene la duración de la renta en días
     * @return Duración de la renta en días
     */
    public int getDuracionRenta() {
        return duracionRenta;
    }
    
    /**
     * Establece la duración de la renta en días
     * @param duracionRenta Duración de la renta en días
     */
    public void setDuracionRenta(int duracionRenta) {
        this.duracionRenta = duracionRenta;
    }
    
    /**
     * Obtiene la tarifa de renta
     * @return Tarifa de renta
     */
    public double getTarifaRenta() {
        return tarifaRenta;
    }
    
    /**
     * Establece la tarifa de renta
     * @param tarifaRenta Tarifa de renta
     */
    public void setTarifaRenta(double tarifaRenta) {
        this.tarifaRenta = tarifaRenta;
    }
    
    /**
     * Obtiene la duración en minutos
     * @return Duración en minutos
     */
    public int getDuracion() {
        return duracion;
    }
    
    /**
     * Establece la duración en minutos
     * @param duracion Duración en minutos
     */
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    
    /**
     * Obtiene el costo de reemplazo
     * @return Costo de reemplazo
     */
    public double getCostoReemplazo() {
        return costoReemplazo;
    }
    
    /**
     * Establece el costo de reemplazo
     * @param costoReemplazo Costo de reemplazo
     */
    public void setCostoReemplazo(double costoReemplazo) {
        this.costoReemplazo = costoReemplazo;
    }
    
    /**
     * Obtiene la clasificación
     * @return Clasificación
     */
    public String getClasificacion() {
        return clasificacion;
    }
    
    /**
     * Establece la clasificación
     * @param clasificacion Clasificación
     */
    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
    
    /**
     * Obtiene las características especiales
     * @return Características especiales
     */
    public String getCaracteristicasEspeciales() {
        return caracteristicasEspeciales;
    }
    
    /**
     * Establece las características especiales
     * @param caracteristicasEspeciales Características especiales
     */
    public void setCaracteristicasEspeciales(String caracteristicasEspeciales) {
        this.caracteristicasEspeciales = caracteristicasEspeciales;
    }
    
    @Override
    public boolean validar() {
        return titulo != null && !titulo.isEmpty() && 
               descripcion != null && !descripcion.isEmpty() && 
               anioLanzamiento > 0 && 
               idioma != null && idioma.validar();
    }
    
    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", anioLanzamiento=" + anioLanzamiento +
                ", idioma=" + (idioma != null ? idioma.getName() : "null") +
                ", duracion=" + duracion +
                ", clasificacion='" + clasificacion + '\'' +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
