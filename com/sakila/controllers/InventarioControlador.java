package com.sakila.controllers;

import com.sakila.data.iDatapost;
import com.sakila.models.Inventario;
import com.sakila.models.Pelicula;
import com.sakila.models.Store;
import com.sakila.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para manejar operaciones de Inventario en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class InventarioControlador implements iDatapost<Inventario> {
    private Connection conexion;
    private List<Inventario> inventarios;
    private PeliculaControlador peliculaControlador;
    
    public InventarioControlador(Connection conexion) {
        this.conexion = conexion;
        this.inventarios = new ArrayList<>();
        this.peliculaControlador = new PeliculaControlador(conexion);
        cargarInventarios();
    }
    
    /**
     * Carga los inventarios de la base de datos
     */
    private void cargarInventarios() {
        try {
            inventarios.clear();
            String sql = "SELECT i.inventory_id, i.film_id, i.store_id, i.last_update " +
                        "FROM inventory i " +
                        "LIMIT 100"; // Limitamos para no cargar demasiados registros
            
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    int inventoryId = rs.getInt("inventory_id");
                    int filmId = rs.getInt("film_id");
                    int storeId = rs.getInt("store_id");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    // Obtenemos la película
                    Pelicula pelicula = peliculaControlador.get(filmId);
                    
                    // Creamos un Store simple (sin todos los detalles)
                    Store store = new Store();
                    store.setId(storeId);
                    
                    Inventario inventario = new Inventario();
                    inventario.setId(inventoryId);
                    inventario.setPelicula(pelicula);
                    inventario.setTienda(store);
                    inventario.setUltimaActualizacion(lastUpdate);
                    inventario.setFechaCreacion(lastUpdate); // No hay campo create_date en inventory
                    inventario.setActivo(true); // No hay campo active en inventory
                    
                    inventarios.add(inventario);
                }
            }
            
            Logger.info("Inventarios cargados: " + inventarios.size());
        } catch (SQLException e) {
            Logger.error("Error al cargar inventarios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean post(Inventario inventario) {
        String sql = "INSERT INTO inventory (film_id, store_id) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inventario.getPelicula().getId());
            stmt.setInt(2, inventario.getTienda().getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inventario.setId(generatedKeys.getInt(1));
                        inventario.setUltimaActualizacion(new Date(System.currentTimeMillis()));
                        inventarios.add(inventario);
                        Logger.info("Inventario creado con ID: " + inventario.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al crear inventario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean put(Inventario inventario) {
        String sql = "UPDATE inventory SET film_id = ?, store_id = ? WHERE inventory_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, inventario.getPelicula().getId());
            stmt.setInt(2, inventario.getTienda().getId());
            stmt.setInt(3, inventario.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                inventario.actualizarFecha();
                
                // Actualizar en la lista local
                for (int i = 0; i < inventarios.size(); i++) {
                    if (inventarios.get(i).getId() == inventario.getId()) {
                        inventarios.set(i, inventario);
                        break;
                    }
                }
                
                Logger.info("Inventario actualizado con ID: " + inventario.getId());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al actualizar inventario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        // En este caso, como la tabla inventory no tiene un campo 'active',
        // realmente eliminamos el registro en lugar de marcarlo como inactivo
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Eliminar de la lista local
                inventarios.removeIf(i -> i.getId() == id);
                
                Logger.info("Inventario eliminado con ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al eliminar inventario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Inventario get(int id) {
        // Primero buscamos en la lista local
        for (Inventario inventario : inventarios) {
            if (inventario.getId() == id) {
                return inventario;
            }
        }
        
        // Si no lo encontramos, buscamos en la base de datos
        String sql = "SELECT i.inventory_id, i.film_id, i.store_id, i.last_update " +
                    "FROM inventory i " +
                    "WHERE i.inventory_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int inventoryId = rs.getInt("inventory_id");
                    int filmId = rs.getInt("film_id");
                    int storeId = rs.getInt("store_id");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    // Obtenemos la película
                    Pelicula pelicula = peliculaControlador.get(filmId);
                    
                    // Creamos un Store simple (sin todos los detalles)
                    Store store = new Store();
                    store.setId(storeId);
                    
                    Inventario inventario = new Inventario();
                    inventario.setId(inventoryId);
                    inventario.setPelicula(pelicula);
                    inventario.setTienda(store);
                    inventario.setUltimaActualizacion(lastUpdate);
                    inventario.setFechaCreacion(lastUpdate); // No hay campo create_date en inventory
                    inventario.setActivo(true); // No hay campo active en inventory
                    
                    // Añadir a la lista local
                    inventarios.add(inventario);
                    
                    return inventario;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener inventario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<Inventario> get() {
        // Si la lista ya está cargada, la devolvemos
        if (!inventarios.isEmpty()) {
            return new ArrayList<>(inventarios);
        }
        
        // Si no, cargamos los inventarios y devolvemos la lista
        cargarInventarios();
        return new ArrayList<>(inventarios);
    }
    
    @Override
    public List<Inventario> get(String... criterios) {
        List<Inventario> resultado = new ArrayList<>();
        String criterio = criterios[0].toLowerCase();
        
        // Buscamos en la base de datos por título de película
        String sql = "SELECT i.inventory_id, i.film_id, i.store_id, i.last_update " +
                    "FROM inventory i " +
                    "JOIN film f ON i.film_id = f.film_id " +
                    "WHERE LOWER(f.title) LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int inventoryId = rs.getInt("inventory_id");
                    int filmId = rs.getInt("film_id");
                    int storeId = rs.getInt("store_id");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    // Obtenemos la película
                    Pelicula pelicula = peliculaControlador.get(filmId);
                    
                    // Creamos un Store simple (sin todos los detalles)
                    Store store = new Store();
                    store.setId(storeId);
                    
                    Inventario inventario = new Inventario();
                    inventario.setId(inventoryId);
                    inventario.setPelicula(pelicula);
                    inventario.setTienda(store);
                    inventario.setUltimaActualizacion(lastUpdate);
                    inventario.setFechaCreacion(lastUpdate); // No hay campo create_date en inventory
                    inventario.setActivo(true); // No hay campo active en inventory
                    
                    resultado.add(inventario);
                    
                    // Añadir a la lista local si no existe
                    if (inventarios.stream().noneMatch(i -> i.getId() == inventario.getId())) {
                        inventarios.add(inventario);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al buscar inventarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Obtiene el total de películas por tienda
     * @param idTienda ID de la tienda
     * @return Total de películas
     */
    public int obtenerTotalPeliculasPorTienda(int idTienda) {
        String sql = "SELECT COUNT(*) AS total FROM inventory WHERE store_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idTienda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener total de películas por tienda: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    // Métodos de compatibilidad con el código existente
    
    /**
     * @deprecated Use get(int id) instead
     */
    public Inventario obtenerPorId(int id) {
        return get(id);
    }
    
    /**
     * @deprecated Use get() instead
     */
    public List<Inventario> obtenerTodos() {
        return get();
    }
    
    /**
     * @deprecated Use get(String... criterios) instead
     */
    public List<Inventario> buscar(String... criterios) {
        return get(criterios);
    }
    
    /**
     * @deprecated Use post(Inventario inventario) instead
     */
    public boolean crear(Inventario inventario) {
        return post(inventario);
    }
    
    /**
     * @deprecated Use put(Inventario inventario) instead
     */
    public boolean actualizar(Inventario inventario) {
        return put(inventario);
    }
    
    /**
     * @deprecated Use delete(int id) instead
     */
    public boolean eliminar(int id) {
        return delete(id);
    }
}
