package com.sakila.controllers;

import com.sakila.data.iDatapost;
import com.sakila.models.Cliente;
import com.sakila.models.Store;
import com.sakila.models.Address;
import com.sakila.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para manejar operaciones de Cliente en la base de datos
 * @author Nicolas Zierow Fermin
 */
public class ClienteControlador implements iDatapost<Cliente> {
    private Connection conexion;
    private List<Cliente> clientes;
    
    public ClienteControlador(Connection conexion) {
        this.conexion = conexion;
        this.clientes = new ArrayList<>();
        cargarClientes();
    }
    
    /**
     * Carga los clientes de la base de datos
     */
    private void cargarClientes() {
        try {
            clientes.clear();
            String sql = "SELECT c.customer_id, c.store_id, c.first_name, c.last_name, c.email, " +
                        "c.address_id, c.active, c.create_date, c.last_update, " +
                        "a.address " +
                        "FROM customer c " +
                        "JOIN address a ON c.address_id = a.address_id " +
                        "LIMIT 100"; // Limitamos para no cargar demasiados registros
            
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    int storeId = rs.getInt("store_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    int addressId = rs.getInt("address_id");
                    String addressText = rs.getString("address");
                    boolean active = rs.getBoolean("active");
                    Timestamp createDate = rs.getTimestamp("create_date");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    // Creamos un Store simple (sin todos los detalles)
                    Store store = new Store();
                    store.setId(storeId);
                    
                    // Creamos un Address simple (sin todos los detalles)
                    Address address = new Address();
                    address.setId(addressId);
                    address.setAddress(addressText);
                    
                    Cliente cliente = new Cliente();
                    cliente.setId(customerId);
                    cliente.setTienda(store);
                    cliente.setPrimerNombre(firstName);
                    cliente.setApellido(lastName);
                    cliente.setCorreoElectronico(email);
                    cliente.setDireccion(address);
                    cliente.setActivo(active);
                    cliente.setFechaCreacion(createDate);
                    cliente.setUltimaActualizacion(lastUpdate);
                    
                    clientes.add(cliente);
                }
            }
            
            Logger.info("Clientes cargados: " + clientes.size());
        } catch (SQLException e) {
            Logger.error("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean post(Cliente cliente) {
        String sql = "INSERT INTO customer (store_id, first_name, last_name, email, address_id, active, create_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cliente.getTienda().getId());
            stmt.setString(2, cliente.getPrimerNombre());
            stmt.setString(3, cliente.getApellido());
            stmt.setString(4, cliente.getCorreoElectronico());
            stmt.setInt(5, cliente.getDireccion().getId());
            stmt.setBoolean(6, cliente.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getInt(1));
                        cliente.setFechaCreacion(new Date(System.currentTimeMillis()));
                        cliente.setUltimaActualizacion(new Date(System.currentTimeMillis()));
                        clientes.add(cliente);
                        Logger.info("Cliente creado con ID: " + cliente.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al crear cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean put(Cliente cliente) {
        String sql = "UPDATE customer SET store_id = ?, first_name = ?, last_name = ?, email = ?, " +
                    "address_id = ?, active = ? " +
                    "WHERE customer_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getTienda().getId());
            stmt.setString(2, cliente.getPrimerNombre());
            stmt.setString(3, cliente.getApellido());
            stmt.setString(4, cliente.getCorreoElectronico());
            stmt.setInt(5, cliente.getDireccion().getId());
            stmt.setBoolean(6, cliente.isActivo());
            stmt.setInt(7, cliente.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                cliente.actualizarFecha();
                
                // Actualizar en la lista local
                for (int i = 0; i < clientes.size(); i++) {
                    if (clientes.get(i).getId() == cliente.getId()) {
                        clientes.set(i, cliente);
                        break;
                    }
                }
                
                Logger.info("Cliente actualizado con ID: " + cliente.getId());
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        // En este caso, como la tabla customer tiene un campo 'active',
        // marcamos el cliente como inactivo en lugar de eliminarlo
        String sql = "UPDATE customer SET active = 0 WHERE customer_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Actualizar en la lista local
                for (Cliente cliente : clientes) {
                    if (cliente.getId() == id) {
                        cliente.setActivo(false);
                        cliente.actualizarFecha();
                        break;
                    }
                }
                
                Logger.info("Cliente marcado como inactivo con ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Error al marcar cliente como inactivo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Cliente get(int id) {
        // Primero buscamos en la lista local
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        
        // Si no lo encontramos, buscamos en la base de datos
        String sql = "SELECT c.customer_id, c.store_id, c.first_name, c.last_name, c.email, " +
                    "c.address_id, c.active, c.create_date, c.last_update, " +
                    "a.address " +
                    "FROM customer c " +
                    "JOIN address a ON c.address_id = a.address_id " +
                    "WHERE c.customer_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    int storeId = rs.getInt("store_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    int addressId = rs.getInt("address_id");
                    String addressText = rs.getString("address");
                    boolean active = rs.getBoolean("active");
                    Timestamp createDate = rs.getTimestamp("create_date");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    // Creamos un Store simple (sin todos los detalles)
                    Store store = new Store();
                    store.setId(storeId);
                    
                    // Creamos un Address simple (sin todos los detalles)
                    Address address = new Address();
                    address.setId(addressId);
                    address.setAddress(addressText);
                    
                    Cliente cliente = new Cliente();
                    cliente.setId(customerId);
                    cliente.setTienda(store);
                    cliente.setPrimerNombre(firstName);
                    cliente.setApellido(lastName);
                    cliente.setCorreoElectronico(email);
                    cliente.setDireccion(address);
                    cliente.setActivo(active);
                    cliente.setFechaCreacion(createDate);
                    cliente.setUltimaActualizacion(lastUpdate);
                    
                    // Añadir a la lista local
                    clientes.add(cliente);
                    
                    return cliente;
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener cliente por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<Cliente> get() {
        // Si la lista ya está cargada, la devolvemos
        if (!clientes.isEmpty()) {
            return new ArrayList<>(clientes);
        }
        
        // Si no, cargamos los clientes y devolvemos la lista
        cargarClientes();
        return new ArrayList<>(clientes);
    }
    
    @Override
    public List<Cliente> get(String... criterios) {
        List<Cliente> resultado = new ArrayList<>();
        String criterio = criterios[0].toLowerCase();
        
        // Primero buscamos en la lista local
        for (Cliente cliente : clientes) {
            if (cliente.getPrimerNombre().toLowerCase().contains(criterio) || 
                cliente.getApellido().toLowerCase().contains(criterio) || 
                cliente.getCorreoElectronico().toLowerCase().contains(criterio)) {
                resultado.add(cliente);
            }
        }
        
        // Si encontramos resultados, los devolvemos
        if (!resultado.isEmpty()) {
            return resultado;
        }
        
        // Si no, buscamos en la base de datos
        String sql = "SELECT c.customer_id, c.store_id, c.first_name, c.last_name, c.email, " +
                    "c.address_id, c.active, c.create_date, c.last_update, " +
                    "a.address " +
                    "FROM customer c " +
                    "JOIN address a ON c.address_id = a.address_id " +
                    "WHERE LOWER(c.first_name) LIKE ? OR LOWER(c.last_name) LIKE ? OR LOWER(c.email) LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio + "%");
            stmt.setString(2, "%" + criterio + "%");
            stmt.setString(3, "%" + criterio + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    int storeId = rs.getInt("store_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    int addressId = rs.getInt("address_id");
                    String addressText = rs.getString("address");
                    boolean active = rs.getBoolean("active");
                    Timestamp createDate = rs.getTimestamp("create_date");
                    Timestamp lastUpdate = rs.getTimestamp("last_update");
                    
                    // Creamos un Store simple (sin todos los detalles)
                    Store store = new Store();
                    store.setId(storeId);
                    
                    // Creamos un Address simple (sin todos los detalles)
                    Address address = new Address();
                    address.setId(addressId);
                    address.setAddress(addressText);
                    
                    Cliente cliente = new Cliente();
                    cliente.setId(customerId);
                    cliente.setTienda(store);
                    cliente.setPrimerNombre(firstName);
                    cliente.setApellido(lastName);
                    cliente.setCorreoElectronico(email);
                    cliente.setDireccion(address);
                    cliente.setActivo(active);
                    cliente.setFechaCreacion(createDate);
                    cliente.setUltimaActualizacion(lastUpdate);
                    
                    resultado.add(cliente);
                    
                    // Añadir a la lista local si no existe
                    if (clientes.stream().noneMatch(c -> c.getId() == cliente.getId())) {
                        clientes.add(cliente);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al buscar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Obtiene los alquileres de un cliente
     * @param idCliente ID del cliente
     * @return Lista de alquileres
     */
    public List<Map<String, Object>> obtenerAlquileresDeCliente(int idCliente) {
        List<Map<String, Object>> alquileres = new ArrayList<>();
        
        String sql = "SELECT r.rental_id, r.rental_date, r.return_date, f.title, p.amount " +
                    "FROM rental r " +
                    "JOIN inventory i ON r.inventory_id = i.inventory_id " +
                    "JOIN film f ON i.film_id = f.film_id " +
                    "LEFT JOIN payment p ON r.rental_id = p.rental_id " +
                    "WHERE r.customer_id = ? " +
                    "ORDER BY r.rental_date DESC";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> alquiler = new HashMap<>();
                    alquiler.put("id", rs.getInt("rental_id"));
                    alquiler.put("fechaAlquiler", rs.getTimestamp("rental_date"));
                    alquiler.put("fechaDevolucion", rs.getTimestamp("return_date"));
                    alquiler.put("tituloPelicula", rs.getString("title"));
                    alquiler.put("monto", rs.getDouble("amount"));
                    
                    alquileres.add(alquiler);
                }
            }
        } catch (SQLException e) {
            Logger.error("Error al obtener alquileres del cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return alquileres;
    }
    
    /**
     * Obtiene estadísticas de clientes
     * @return Mapa con estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        try {
            // Total de clientes
            String sqlTotal = "SELECT COUNT(*) AS total FROM customer";
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlTotal)) {
                if (rs.next()) {
                    estadisticas.put("totalClientes", rs.getInt("total"));
                }
            }
            
            // Clientes activos vs inactivos
            String sqlActivos = "SELECT active, COUNT(*) AS total FROM customer GROUP BY active";
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlActivos)) {
                while (rs.next()) {
                    boolean activo = rs.getBoolean("active");
                    int total = rs.getInt("total");
                    
                    if (activo) {
                        estadisticas.put("clientesActivos", total);
                    } else {
                        estadisticas.put("clientesInactivos", total);
                    }
                }
            }
            
            // Clientes por tienda
            String sqlTiendas = "SELECT store_id, COUNT(*) AS total FROM customer GROUP BY store_id";
            Map<Integer, Integer> clientesPorTienda = new HashMap<>();
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlTiendas)) {
                while (rs.next()) {
                    int idTienda = rs.getInt("store_id");
                    int total = rs.getInt("total");
                    clientesPorTienda.put(idTienda, total);
                }
            }
            estadisticas.put("clientesPorTienda", clientesPorTienda);
            
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
    public Cliente obtenerPorId(int id) {
        return get(id);
    }
    
    /**
     * @deprecated Use get() instead
     */
    public List<Cliente> obtenerTodos() {
        return get();
    }
    
    /**
     * @deprecated Use get(String... criterios) instead
     */
    public List<Cliente> buscar(String... criterios) {
        return get(criterios);
    }
    
    /**
     * @deprecated Use post(Cliente cliente) instead
     */
    public boolean crear(Cliente cliente) {
        return post(cliente);
    }
    
    /**
     * @deprecated Use put(Cliente cliente) instead
     */
    public boolean actualizar(Cliente cliente) {
        return put(cliente);
    }
    
    /**
     * @deprecated Use delete(int id) instead
     */
    public boolean eliminar(int id) {
        return delete(id);
    }
}
