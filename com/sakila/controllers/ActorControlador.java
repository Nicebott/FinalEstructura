package com.sakila.controllers;

import com.sakila.data.iDatapost;
import com.sakila.models.Actor;
import com.sakila.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para manejar operaciones de Actor en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class ActorControlador implements iDatapost<Actor> {
    private Connection conexion;
    private List<Actor> actores;
    
    public ActorControlador(Connection conexion) {
        this.conexion = conexion;
        this.actores = new ArrayList<>();
        cargarActores();
    }
    
    /**
     * Carga los actores de la base de datos
     */
    private void cargarActores() {
        try {
            actores.clear();
            String sql = "SELECT * FROM actor";
            
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getInt("actor_id"));
                    actor.setNombrePrimer(rs.getString("first_name"));
                    actor.setApellido(rs.getString("last_name"));
                    actor.setUltimaActualizacion(rs.getTimestamp("last_update"));
                    actor.setFechaCreacion(rs.getTimestamp("last_update")); // No hay campo create_date en actor
                    actor.setActivo(true); // No hay campo active en actor
                    
                    actores.add(actor);
                }
            }
            
            Logger.info("Actores cargados: " + actores.size());
        } catch (SQLException e) {
            Logger.error("Error al cargar actores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean post(Actor actor) {
        String sql = "INSERT INTO actor (first_name, last_name) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, actor.getNombrePrimer());
            stmt.setString(2, actor.getApellido());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        actor.setId(generatedKeys.getInt(1));
                        actor.setUltimaActualizacion(new Date(System.currentTimeMillis()));
                        actores.add(actor);
                        Logger.info("Actor creado con ID: " + actor.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al crear actor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean put(Actor actor) {
        String sql = "UPDATE actor SET first_name = ?, last_name = ? WHERE actor_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, actor.getNombrePrimer());
            stmt.setString(2, actor.getApellido());
            stmt.setInt(3, actor.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                actor.actualizarFecha();
                
                // Actualizar en la lista local
                for (int i = 0; i < actores.size(); i++) {
                    if (actores.get(i).getId() == actor.getId()) {
                        actores.set(i, actor);
                        break;
                    }
                }
                
                Logger.info("Actor actualizado con ID: " + actor.getId());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al actualizar actor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        // En este caso, como la tabla actor no tiene un campo 'active',
        // realmente eliminamos el registro en lugar de marcarlo como inactivo
        String sql = "DELETE FROM actor WHERE actor_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Eliminar de la lista local
                actores.removeIf(a -> a.getId() == id);
                
                Logger.info("Actor eliminado con ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al eliminar actor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Actor get(int id) {
        // Primero buscamos en la lista local
        for (Actor actor : actores) {
            if (actor.getId() == id) {
                return actor;
            }
        }
        
        // Si no lo encontramos, buscamos en la base de datos
        String sql = "SELECT * FROM actor WHERE actor_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getInt("actor_id"));
                    actor.setNombrePrimer(rs.getString("first_name"));
                    actor.setApellido(rs.getString("last_name"));
                    actor.setUltimaActualizacion(rs.getTimestamp("last_update"));
                    actor.setFechaCreacion(rs.getTimestamp("last_update")); // No hay campo create_date en actor
                    actor.setActivo(true); // No hay campo active en actor
                    
                    // Añadir a la lista local
                    actores.add(actor);
                    
                    return actor;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener actor por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<Actor> get() {
        // Si la lista ya está cargada, la devolvemos
        if (!actores.isEmpty()) {
            return new ArrayList<>(actores);
        }
        
        // Si no, cargamos los actores y devolvemos la lista
        cargarActores();
        return new ArrayList<>(actores);
    }
    
    @Override
    public List<Actor> get(String... criterios) {
        List<Actor> resultado = new ArrayList<>();
        String criterio = criterios[0].toLowerCase();
        
        // Primero buscamos en la lista local
        for (Actor actor : actores) {
            if (actor.getNombrePrimer().toLowerCase().contains(criterio) || 
                actor.getApellido().toLowerCase().contains(criterio)) {
                resultado.add(actor);
            }
        }
        
        // Si encontramos resultados, los devolvemos
        if (!resultado.isEmpty()) {
            return resultado;
        }
        
        // Si no, buscamos en la base de datos
        String sql = "SELECT * FROM actor WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio + "%");
            stmt.setString(2, "%" + criterio + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getInt("actor_id"));
                    actor.setNombrePrimer(rs.getString("first_name"));
                    actor.setApellido(rs.getString("last_name"));
                    actor.setUltimaActualizacion(rs.getTimestamp("last_update"));
                    actor.setFechaCreacion(rs.getTimestamp("last_update")); // No hay campo create_date en actor
                    actor.setActivo(true); // No hay campo active en actor
                    
                    resultado.add(actor);
                    
                    // Añadir a la lista local si no existe
                    if (actores.stream().noneMatch(a -> a.getId() == actor.getId())) {
                        actores.add(actor);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al buscar actores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Método para obtener estadísticas de actores
     * @return Mapa con estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        try {
            // Total de actores
            estadisticas.put("totalActores", actores.size());
            
            // Actores con más películas
            String sqlTopActores = "SELECT a.actor_id, a.first_name, a.last_name, COUNT(fa.film_id) AS total_peliculas " +
                                  "FROM actor a " +
                                  "JOIN film_actor fa ON a.actor_id = fa.actor_id " +
                                  "GROUP BY a.actor_id " +
                                  "ORDER BY total_peliculas DESC " +
                                  "LIMIT 5";
            
            List<Map<String, Object>> topActores = new ArrayList<>();
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlTopActores)) {
                while (rs.next()) {
                    Map<String, Object> actor = new HashMap<>();
                    actor.put("id", rs.getInt("actor_id"));
                    actor.put("nombre", rs.getString("first_name"));
                    actor.put("apellido", rs.getString("last_name"));
                    actor.put("totalPeliculas", rs.getInt("total_peliculas"));
                    topActores.add(actor);
                }
            }
            
            estadisticas.put("topActores", topActores);
            
        } catch (SQLException e) {
            Logger.error("Error al obtener estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return estadisticas;
    }
}
