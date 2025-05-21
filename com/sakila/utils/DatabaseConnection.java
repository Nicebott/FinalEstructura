package com.sakila.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos MySQL
 * @author Nicolas Zierow Fermin
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/sakila";
    private static final String USER = "root"; // Usuario de MySQL
    private static final String PASSWORD = "2121"; 
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        try {
            // Cargar el driver
            Class.forName(DRIVER);
            Logger.info("Driver de base de datos cargado: " + DRIVER);
        } catch (ClassNotFoundException e) {
            Logger.error("Error al cargar el driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException Si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        Logger.info("Conectando a la base de datos: " + URL);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Actualiza la configuración de la conexión
     * @param newUrl Nueva URL de conexión
     * @param newUser Nuevo usuario
     * @param newPassword Nueva contraseña
     */
    public static void updateConfig(String newUrl, String newUser, String newPassword) {
        // Este método ya no modifica las constantes, pero se mantiene por compatibilidad
        Logger.info("Solicitud de actualización de configuración recibida (no aplicada debido a valores constantes)");
    }
}
