package com.sakila.controllers;

import com.sakila.data.iDatapost;
import com.sakila.models.Country;
import com.sakila.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para manejar operaciones de Country en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class CountryControlador implements iDatapost<Country> {
    private Connection conexion;
    private List<Country> paises;
    
    public CountryControlador(Connection conexion) {
        this.conexion = conexion;
        this.paises = new ArrayList<>();
        cargarPaises();
    }
    
    /**
     * Carga los países de la base de datos
     */
    private void cargarPaises() {
        try {
            paises.clear();
            String sql = "SELECT * FROM country";
            
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    Country country = new Country();
                    country.setId(rs.getInt("country_id"));
                    country.setCountry(rs.getString("country"));
                    country.setUltimaActualizacion(rs.getTimestamp("last_update"));
                    country.setFechaCreacion(rs.getTimestamp("last_update")); // No hay campo create_date en country
                    country.setActivo(true); // No hay campo active en country
                    
                    paises.add(country);
                }
            }
            
            Logger.info("Países cargados: " + paises.size());
        } catch (SQLException e) {
            Logger.error("Error al cargar países: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean post(Country country) {
        String sql = "INSERT INTO country (country) VALUES (?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, country.getCountry());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        country.setId(generatedKeys.getInt(1));
                        country.setUltimaActualizacion(new Date(System.currentTimeMillis()));
                        paises.add(country);
                        Logger.info("País creado con ID: " + country.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al crear país: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean put(Country country) {
        String sql = "UPDATE country SET country = ? WHERE country_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, country.getCountry());
            stmt.setInt(2, country.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                country.actualizarFecha();
                
                // Actualizar en la lista local
                for (int i = 0; i < paises.size(); i++) {
                    if (paises.get(i).getId() == country.getId()) {
                        paises.set(i, country);
                        break;
                    }
                }
                
                Logger.info("País actualizado con ID: " + country.getId());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al actualizar país: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        // En este caso, como la tabla country no tiene un campo 'active',
        // realmente eliminamos el registro en lugar de marcarlo como inactivo
        String sql = "DELETE FROM country WHERE country_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Eliminar de la lista local
                paises.removeIf(p -> p.getId() == id);
                
                Logger.info("País eliminado con ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al eliminar país: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Country get(int id) {
        // Primero buscamos en la lista local
        for (Country country : paises) {
            if (country.getId() == id) {
                return country;
            }
        }
        
        // Si no lo encontramos, buscamos en la base de datos
        String sql = "SELECT * FROM country WHERE country_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Country country = new Country();
                    country.setId(rs.getInt("country_id"));
                    country.setCountry(rs.getString("country"));
                    country.setUltimaActualizacion(rs.getTimestamp("last_update"));
                    country.setFechaCreacion(rs.getTimestamp("last_update")); // No hay campo create_date en country
                    country.setActivo(true); // No hay campo active en country
                    
                    // Añadir a la lista local
                    paises.add(country);
                    
                    return country;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener país por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<Country> get() {
        // Si la lista ya está cargada, la devolvemos
        if (!paises.isEmpty()) {
            return new ArrayList<>(paises);
        }
        
        // Si no, cargamos los países y devolvemos la lista
        cargarPaises();
        return new ArrayList<>(paises);
    }
    
    @Override
    public List<Country> get(String... criterios) {
        List<Country> resultado = new ArrayList<>();
        String criterio = criterios[0].toLowerCase();
        
        // Primero buscamos en la lista local
        for (Country country : paises) {
            if (country.getCountry().toLowerCase().contains(criterio)) {
                resultado.add(country);
            }
        }
        
        // Si encontramos resultados, los devolvemos
        if (!resultado.isEmpty()) {
            return resultado;
        }
        
        // Si no, buscamos en la base de datos
        String sql = "SELECT * FROM country WHERE LOWER(country) LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Country country = new Country();
                    country.setId(rs.getInt("country_id"));
                    country.setCountry(rs.getString("country"));
                    country.setUltimaActualizacion(rs.getTimestamp("last_update"));
                    country.setFechaCreacion(rs.getTimestamp("last_update")); // No hay campo create_date en country
                    country.setActivo(true); // No hay campo active en country
                    
                    resultado.add(country);
                    
                    // Añadir a la lista local si no existe
                    if (paises.stream().noneMatch(p -> p.getId() == country.getId())) {
                        paises.add(country);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al buscar países: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
}
