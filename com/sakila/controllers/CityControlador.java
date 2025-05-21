package com.sakila.controllers;

import com.sakila.data.iDatapost;
import com.sakila.models.City;
import com.sakila.models.Country;
import com.sakila.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para manejar operaciones de City en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class CityControlador implements iDatapost<City> {
    private Connection conexion;
    private List<City> ciudades;
    private CountryControlador countryControlador;
    
    public CityControlador(Connection conexion) {
        this.conexion = conexion;
        this.ciudades = new ArrayList<>();
        // Inicializar el controlador de países solo si es necesario
        if (countryControlador == null) {
            this.countryControlador = new CountryControlador(conexion);
        }
        cargarCiudades();
    }
    
    /**
     * Obtiene el controlador de países
     * @return Controlador de países
     */
    public CountryControlador getCountryControlador() {
        return countryControlador;
    }
    
    /**
     * Carga las ciudades de la base de datos
     */
    private void cargarCiudades() {
        try {
            ciudades.clear();
            String sql = "SELECT c.city_id, c.city, c.country_id, c.last_update, " +
                         "co.country " +
                         "FROM city c " +
                         "JOIN country co ON c.country_id = co.country_id";
            
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    int cityId = rs.getInt("city_id");
                    String cityName = rs.getString("city");
                    int countryId = rs.getInt("country_id");
                    String countryName = rs.getString("country");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    Country country = new Country(countryId, countryName);
                    country.setUltimaActualizacion(lastUpdate);
                    
                    City city = new City(cityId, cityName, country);
                    city.setUltimaActualizacion(lastUpdate);
                    city.setFechaCreacion(lastUpdate); // No hay campo create_date en city
                    city.setActivo(true); // No hay campo active en city
                    
                    ciudades.add(city);
                }
            }
            
            Logger.info("Ciudades cargadas: " + ciudades.size());
        } catch (SQLException e) {
            Logger.error("Error al cargar ciudades: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean post(City city) {
        String sql = "INSERT INTO city (city, country_id) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, city.getCity());
            stmt.setInt(2, city.getCountry().getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        city.setId(generatedKeys.getInt(1));
                        city.setUltimaActualizacion(new Date(System.currentTimeMillis()));
                        ciudades.add(city);
                        Logger.info("Ciudad creada con ID: " + city.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al crear ciudad: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean put(City city) {
        String sql = "UPDATE city SET city = ?, country_id = ? WHERE city_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, city.getCity());
            stmt.setInt(2, city.getCountry().getId());
            stmt.setInt(3, city.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                city.actualizarFecha();
                
                // Actualizar en la lista local
                for (int i = 0; i < ciudades.size(); i++) {
                    if (ciudades.get(i).getId() == city.getId()) {
                        ciudades.set(i, city);
                        break;
                    }
                }
                
                Logger.info("Ciudad actualizada con ID: " + city.getId());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al actualizar ciudad: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        // En este caso, como la tabla city no tiene un campo 'active',
        // realmente eliminamos el registro en lugar de marcarlo como inactivo
        String sql = "DELETE FROM city WHERE city_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Eliminar de la lista local
                ciudades.removeIf(c -> c.getId() == id);
                
                Logger.info("Ciudad eliminada con ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al eliminar ciudad: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public City get(int id) {
        // Primero buscamos en la lista local
        for (City city : ciudades) {
            if (city.getId() == id) {
                return city;
            }
        }
        
        // Si no lo encontramos, buscamos en la base de datos
        String sql = "SELECT c.city_id, c.city, c.country_id, c.last_update, " +
                     "co.country " +
                     "FROM city c " +
                     "JOIN country co ON c.country_id = co.country_id " +
                     "WHERE c.city_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int cityId = rs.getInt("city_id");
                    String cityName = rs.getString("city");
                    int countryId = rs.getInt("country_id");
                    String countryName = rs.getString("country");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    Country country = new Country(countryId, countryName);
                    country.setUltimaActualizacion(lastUpdate);
                    
                    City city = new City(cityId, cityName, country);
                    city.setUltimaActualizacion(lastUpdate);
                    city.setFechaCreacion(lastUpdate); // No hay campo create_date en city
                    city.setActivo(true); // No hay campo active en city
                    
                    // Añadir a la lista local
                    ciudades.add(city);
                    
                    return city;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener ciudad por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<City> get() {
        // Si la lista ya está cargada, la devolvemos
        if (!ciudades.isEmpty()) {
            return new ArrayList<>(ciudades);
        }
        
        // Si no, cargamos las ciudades y devolvemos la lista
        cargarCiudades();
        return new ArrayList<>(ciudades);
    }
    
    @Override
    public List<City> get(String... criterios) {
        List<City> resultado = new ArrayList<>();
        String criterio = criterios[0].toLowerCase();
        
        // Primero buscamos en la lista local
        for (City city : ciudades) {
            if (city.getCity().toLowerCase().contains(criterio) || 
                city.getCountry().getCountry().toLowerCase().contains(criterio)) {
                resultado.add(city);
            }
        }
        
        // Si encontramos resultados, los devolvemos
        if (!resultado.isEmpty()) {
            return resultado;
        }
        
        // Si no, buscamos en la base de datos
        String sql = "SELECT c.city_id, c.city, c.country_id, c.last_update, " +
                     "co.country " +
                     "FROM city c " +
                     "JOIN country co ON c.country_id = co.country_id " +
                     "WHERE LOWER(c.city) LIKE ? OR LOWER(co.country) LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio + "%");
            stmt.setString(2, "%" + criterio + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int cityId = rs.getInt("city_id");
                    String cityName = rs.getString("city");
                    int countryId = rs.getInt("country_id");
                    String countryName = rs.getString("country");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    Country country = new Country(countryId, countryName);
                    country.setUltimaActualizacion(lastUpdate);
                    
                    City city = new City(cityId, cityName, country);
                    city.setUltimaActualizacion(lastUpdate);
                    city.setFechaCreacion(lastUpdate); // No hay campo create_date en city
                    city.setActivo(true); // No hay campo active en city
                    
                    resultado.add(city);
                    
                    // Añadir a la lista local si no existe
                    if (ciudades.stream().noneMatch(c -> c.getId() == city.getId())) {
                        ciudades.add(city);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al buscar ciudades: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Obtiene las ciudades por país
     * @param countryId ID del país
     * @return Lista de ciudades del país
     */
    public List<City> getCiudadesPorPais(int countryId) {
        List<City> resultado = new ArrayList<>();
        
        // Primero buscamos en la lista local
        for (City city : ciudades) {
            if (city.getCountry().getId() == countryId) {
                resultado.add(city);
            }
        }
        
        // Si encontramos resultados, los devolvemos
        if (!resultado.isEmpty()) {
            return resultado;
        }
        
        // Si no, buscamos en la base de datos
        String sql = "SELECT c.city_id, c.city, c.country_id, c.last_update, " +
                     "co.country " +
                     "FROM city c " +
                     "JOIN country co ON c.country_id = co.country_id " +
                     "WHERE c.country_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, countryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int cityId = rs.getInt("city_id");
                    String cityName = rs.getString("city");
                    String countryName = rs.getString("country");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    Country country = new Country(countryId, countryName);
                    country.setUltimaActualizacion(lastUpdate);
                    
                    City city = new City(cityId, cityName, country);
                    city.setUltimaActualizacion(lastUpdate);
                    city.setFechaCreacion(lastUpdate); // No hay campo create_date en city
                    city.setActivo(true); // No hay campo active en city
                    
                    resultado.add(city);
                    
                    // Añadir a la lista local si no existe
                    if (ciudades.stream().noneMatch(c -> c.getId() == city.getId())) {
                        ciudades.add(city);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener ciudades por país: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Obtiene estadísticas de ciudades
     * @return Mapa con estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        try {
            // Total de ciudades
            estadisticas.put("totalCiudades", ciudades.size());
            
            // Ciudades por país
            String sqlCiudadesPorPais = "SELECT co.country_id, co.country, COUNT(c.city_id) AS total " +
                                       "FROM country co " +
                                       "LEFT JOIN city c ON co.country_id = c.country_id " +
                                       "GROUP BY co.country_id " +
                                       "ORDER BY total DESC";
            
            Map<String, Integer> ciudadesPorPais = new HashMap<>();
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlCiudadesPorPais)) {
                while (rs.next()) {
                    String pais = rs.getString("country");
                    int total = rs.getInt("total");
                    ciudadesPorPais.put(pais, total);
                }
            }
            
            estadisticas.put("ciudadesPorPais", ciudadesPorPais);
            
        } catch (SQLException e) {
            Logger.error("Error al obtener estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return estadisticas;
    }
}
