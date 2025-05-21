package com.sakila.controllers;

import com.sakila.data.iDatapost;
import com.sakila.models.Pelicula;
import com.sakila.models.Language;
import com.sakila.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para manejar operaciones de Película en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class PeliculaControlador implements iDatapost<Pelicula> {
    private Connection conexion;
    private List<Pelicula> peliculas;
    
    public PeliculaControlador(Connection conexion) {
        this.conexion = conexion;
        this.peliculas = new ArrayList<>();
        cargarPeliculas();
    }
    
    /**
     * Carga las películas de la base de datos
     */
    private void cargarPeliculas() {
        try {
            peliculas.clear();
            String sql = "SELECT f.*, l.name as language_name, ol.name as original_language_name " +
                        "FROM film f " +
                        "JOIN language l ON f.language_id = l.language_id " +
                        "LEFT JOIN language ol ON f.original_language_id = ol.language_id";
            
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    int filmId = rs.getInt("film_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    int releaseYear = rs.getInt("release_year");
                    int languageId = rs.getInt("language_id");
                    String languageName = rs.getString("language_name");
                    int originalLanguageId = rs.getInt("original_language_id");
                    String originalLanguageName = rs.getString("original_language_name");
                    int rentalDuration = rs.getInt("rental_duration");
                    double rentalRate = rs.getDouble("rental_rate");
                    int length = rs.getInt("length");
                    double replacementCost = rs.getDouble("replacement_cost");
                    String rating = rs.getString("rating");
                    String specialFeatures = rs.getString("special_features");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    Language language = new Language(languageId, languageName);
                    language.setUltimaActualizacion(lastUpdate);
                    
                    Language originalLanguage = null;
                    if (originalLanguageId > 0) {
                        originalLanguage = new Language(originalLanguageId, originalLanguageName);
                        originalLanguage.setUltimaActualizacion(lastUpdate);
                    }
                    
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(filmId);
                    pelicula.setTitulo(title);
                    pelicula.setDescripcion(description);
                    pelicula.setAnioLanzamiento(releaseYear);
                    pelicula.setIdioma(language);
                    pelicula.setIdiomaOriginal(originalLanguage);
                    pelicula.setDuracionRenta(rentalDuration);
                    pelicula.setTarifaRenta(rentalRate);
                    pelicula.setDuracion(length);
                    pelicula.setCostoReemplazo(replacementCost);
                    pelicula.setClasificacion(rating);
                    pelicula.setCaracteristicasEspeciales(specialFeatures);
                    pelicula.setUltimaActualizacion(lastUpdate);
                    pelicula.setFechaCreacion(lastUpdate); // No hay campo create_date en film
                    pelicula.setActivo(true); // No hay campo active en film
                    
                    peliculas.add(pelicula);
                }
            }
            
            Logger.info("Películas cargadas: " + peliculas.size());
        } catch (SQLException e) {
            Logger.error("Error al cargar películas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean post(Pelicula pelicula) {
        String sql = "INSERT INTO film (title, description, release_year, language_id, original_language_id, " +
                    "rental_duration, rental_rate, length, replacement_cost, rating, special_features) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pelicula.getTitulo());
            stmt.setString(2, pelicula.getDescripcion());
            stmt.setInt(3, pelicula.getAnioLanzamiento());
            stmt.setInt(4, pelicula.getIdioma().getId());
            
            if (pelicula.getIdiomaOriginal() != null) {
                stmt.setInt(5, pelicula.getIdiomaOriginal().getId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(6, pelicula.getDuracionRenta());
            stmt.setDouble(7, pelicula.getTarifaRenta());
            stmt.setInt(8, pelicula.getDuracion());
            stmt.setDouble(9, pelicula.getCostoReemplazo());
            stmt.setString(10, pelicula.getClasificacion());
            stmt.setString(11, pelicula.getCaracteristicasEspeciales());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pelicula.setId(generatedKeys.getInt(1));
                        pelicula.setUltimaActualizacion(new Date(System.currentTimeMillis()));
                        peliculas.add(pelicula);
                        Logger.info("Película creada con ID: " + pelicula.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al crear película: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean put(Pelicula pelicula) {
        String sql = "UPDATE film SET title = ?, description = ?, release_year = ?, language_id = ?, " +
                    "original_language_id = ?, rental_duration = ?, rental_rate = ?, length = ?, " +
                    "replacement_cost = ?, rating = ?, special_features = ? " +
                    "WHERE film_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, pelicula.getTitulo());
            stmt.setString(2, pelicula.getDescripcion());
            stmt.setInt(3, pelicula.getAnioLanzamiento());
            stmt.setInt(4, pelicula.getIdioma().getId());
            
            if (pelicula.getIdiomaOriginal() != null) {
                stmt.setInt(5, pelicula.getIdiomaOriginal().getId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(6, pelicula.getDuracionRenta());
            stmt.setDouble(7, pelicula.getTarifaRenta());
            stmt.setInt(8, pelicula.getDuracion());
            stmt.setDouble(9, pelicula.getCostoReemplazo());
            stmt.setString(10, pelicula.getClasificacion());
            stmt.setString(11, pelicula.getCaracteristicasEspeciales());
            stmt.setInt(12, pelicula.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                pelicula.actualizarFecha();
                
                // Actualizar en la lista local
                for (int i = 0; i < peliculas.size(); i++) {
                    if (peliculas.get(i).getId() == pelicula.getId()) {
                        peliculas.set(i, pelicula);
                        break;
                    }
                }
                
                Logger.info("Película actualizada con ID: " + pelicula.getId());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al actualizar película: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        // En este caso, como la tabla film no tiene un campo 'active',
        // realmente eliminamos el registro en lugar de marcarlo como inactivo
        String sql = "DELETE FROM film WHERE film_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Eliminar de la lista local
                peliculas.removeIf(p -> p.getId() == id);
                
                Logger.info("Película eliminada con ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al eliminar película: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Pelicula get(int id) {
        // Primero buscamos en la lista local
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId() == id) {
                return pelicula;
            }
        }
        
        // Si no lo encontramos, buscamos en la base de datos
        String sql = "SELECT f.*, l.name as language_name, ol.name as original_language_name " +
                    "FROM film f " +
                    "JOIN language l ON f.language_id = l.language_id " +
                    "LEFT JOIN language ol ON f.original_language_id = ol.language_id " +
                    "WHERE f.film_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int filmId = rs.getInt("film_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    int releaseYear = rs.getInt("release_year");
                    int languageId = rs.getInt("language_id");
                    String languageName = rs.getString("language_name");
                    int originalLanguageId = rs.getInt("original_language_id");
                    String originalLanguageName = rs.getString("original_language_name");
                    int rentalDuration = rs.getInt("rental_duration");
                    double rentalRate = rs.getDouble("rental_rate");
                    int length = rs.getInt("length");
                    double replacementCost = rs.getDouble("replacement_cost");
                    String rating = rs.getString("rating");
                    String specialFeatures = rs.getString("special_features");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    Language language = new Language(languageId, languageName);
                    language.setUltimaActualizacion(lastUpdate);
                    
                    Language originalLanguage = null;
                    if (originalLanguageId > 0) {
                        originalLanguage = new Language(originalLanguageId, originalLanguageName);
                        originalLanguage.setUltimaActualizacion(lastUpdate);
                    }
                    
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(filmId);
                    pelicula.setTitulo(title);
                    pelicula.setDescripcion(description);
                    pelicula.setAnioLanzamiento(releaseYear);
                    pelicula.setIdioma(language);
                    pelicula.setIdiomaOriginal(originalLanguage);
                    pelicula.setDuracionRenta(rentalDuration);
                    pelicula.setTarifaRenta(rentalRate);
                    pelicula.setDuracion(length);
                    pelicula.setCostoReemplazo(replacementCost);
                    pelicula.setClasificacion(rating);
                    pelicula.setCaracteristicasEspeciales(specialFeatures);
                    pelicula.setUltimaActualizacion(lastUpdate);
                    pelicula.setFechaCreacion(lastUpdate); // No hay campo create_date en film
                    pelicula.setActivo(true); // No hay campo active en film
                    
                    // Añadir a la lista local
                    peliculas.add(pelicula);
                    
                    return pelicula;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener película por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<Pelicula> get() {
        // Si la lista ya está cargada, la devolvemos
        if (!peliculas.isEmpty()) {
            return new ArrayList<>(peliculas);
        }
        
        // Si no, cargamos las películas y devolvemos la lista
        cargarPeliculas();
        return new ArrayList<>(peliculas);
    }
    
    @Override
    public List<Pelicula> get(String... criterios) {
        List<Pelicula> resultado = new ArrayList<>();
        String criterio = criterios[0].toLowerCase();
        
        // Primero buscamos en la lista local
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getTitulo().toLowerCase().contains(criterio) || 
                pelicula.getDescripcion().toLowerCase().contains(criterio)) {
                resultado.add(pelicula);
            }
        }
        
        // Si encontramos resultados, los devolvemos
        if (!resultado.isEmpty()) {
            return resultado;
        }
        
        // Si no, buscamos en la base de datos
        String sql = "SELECT f.*, l.name as language_name, ol.name as original_language_name " +
                    "FROM film f " +
                    "JOIN language l ON f.language_id = l.language_id " +
                    "LEFT JOIN language ol ON f.original_language_id = ol.language_id " +
                    "WHERE LOWER(f.title) LIKE ? OR LOWER(f.description) LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio + "%");
            stmt.setString(2, "%" + criterio + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int filmId = rs.getInt("film_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    int releaseYear = rs.getInt("release_year");
                    int languageId = rs.getInt("language_id");
                    String languageName = rs.getString("language_name");
                    int originalLanguageId = rs.getInt("original_language_id");
                    String originalLanguageName = rs.getString("original_language_name");
                    int rentalDuration = rs.getInt("rental_duration");
                    double rentalRate = rs.getDouble("rental_rate");
                    int length = rs.getInt("length");
                    double replacementCost = rs.getDouble("replacement_cost");
                    String rating = rs.getString("rating");
                    String specialFeatures = rs.getString("special_features");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    Language language = new Language(languageId, languageName);
                    language.setUltimaActualizacion(lastUpdate);
                    
                    Language originalLanguage = null;
                    if (originalLanguageId > 0) {
                        originalLanguage = new Language(originalLanguageId, originalLanguageName);
                        originalLanguage.setUltimaActualizacion(lastUpdate);
                    }
                    
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(filmId);
                    pelicula.setTitulo(title);
                    pelicula.setDescripcion(description);
                    pelicula.setAnioLanzamiento(releaseYear);
                    pelicula.setIdioma(language);
                    pelicula.setIdiomaOriginal(originalLanguage);
                    pelicula.setDuracionRenta(rentalDuration);
                    pelicula.setTarifaRenta(rentalRate);
                    pelicula.setDuracion(length);
                    pelicula.setCostoReemplazo(replacementCost);
                    pelicula.setClasificacion(rating);
                    pelicula.setCaracteristicasEspeciales(specialFeatures);
                    pelicula.setUltimaActualizacion(lastUpdate);
                    pelicula.setFechaCreacion(lastUpdate); // No hay campo create_date en film
                    pelicula.setActivo(true); // No hay campo active en film
                    
                    resultado.add(pelicula);
                    
                    // Añadir a la lista local si no existe
                    if (peliculas.stream().noneMatch(p -> p.getId() == pelicula.getId())) {
                        peliculas.add(pelicula);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al buscar películas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Obtiene los actores de una película
     * @param idPelicula ID de la película
     * @return Lista de actores
     */
    public List<Map<String, Object>> obtenerActoresDePelicula(int idPelicula) {
        List<Map<String, Object>> actores = new ArrayList<>();
        
        String sql = "SELECT a.actor_id, a.first_name, a.last_name " +
                    "FROM actor a " +
                    "JOIN film_actor fa ON a.actor_id = fa.actor_id " +
                    "WHERE fa.film_id = ? " +
                    "ORDER BY a.last_name, a.first_name";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idPelicula);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> actor = new HashMap<>();
                    actor.put("id", rs.getInt("actor_id"));
                    actor.put("nombre", rs.getString("first_name"));
                    actor.put("apellido", rs.getString("last_name"));
                    
                    actores.add(actor);
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener actores de la película: " + e.getMessage());
            e.printStackTrace();
        }
        
        return actores;
    }
    
    /**
     * Obtiene estadísticas de películas
     * @return Mapa con estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        try {
            // Total de películas
            estadisticas.put("totalPeliculas", peliculas.size());
            
            // Películas por clasificación
            String sqlClasificacion = "SELECT rating, COUNT(*) AS total FROM film GROUP BY rating";
            Map<String, Integer> peliculasPorClasificacion = new HashMap<>();
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlClasificacion)) {
                while (rs.next()) {
                    String clasificacion = rs.getString("rating");
                    int total = rs.getInt("total");
                    peliculasPorClasificacion.put(clasificacion, total);
                }
            }
            estadisticas.put("peliculasPorClasificacion", peliculasPorClasificacion);
            
            // Películas por categoría
            String sqlCategoria = "SELECT c.name, COUNT(fc.film_id) AS total " +
                                 "FROM category c " +
                                 "JOIN film_category fc ON c.category_id = fc.category_id " +
                                 "GROUP BY c.name";
            Map<String, Integer> peliculasPorCategoria = new HashMap<>();
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlCategoria)) {
                while (rs.next()) {
                    String categoria = rs.getString("name");
                    int total = rs.getInt("total");
                    peliculasPorCategoria.put(categoria, total);
                }
            }
            estadisticas.put("peliculasPorCategoria", peliculasPorCategoria);
            
        } catch (SQLException e) {
            Logger.error("Error al obtener estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return estadisticas;
    }
    
    // Métodos de compatibilidad con el código existente
    
    /**
     * @deprecated Use get(int id) instead
     */
    public Pelicula obtenerPorId(int id) {
        return get(id);
    }
    
    /**
     * @deprecated Use get() instead
     */
    public List<Pelicula> obtenerTodos() {
        return get();
    }
    
    /**
     * @deprecated Use get(String... criterios) instead
     */
    public List<Pelicula> buscar(String... criterios) {
        return get(criterios);
    }
    
    /**
     * @deprecated Use post(Pelicula pelicula) instead
     */
    public boolean crear(Pelicula pelicula) {
        return post(pelicula);
    }
    
    /**
     * @deprecated Use put(Pelicula pelicula) instead
     */
    public boolean actualizar(Pelicula pelicula) {
        return put(pelicula);
    }
    
    /**
     * @deprecated Use delete(int id) instead
     */
    public boolean eliminar(int id) {
        return delete(id);
    }
}
