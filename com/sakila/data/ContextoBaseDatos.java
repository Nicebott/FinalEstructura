package com.sakila.data;

import com.sakila.utils.DatabaseConnection;
import com.sakila.utils.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase para manejar el contexto de la base de datos
 * @author Nicolas Zierow Fermin
 */
public class ContextoBaseDatos {
    private static Connection conexion;
    
    /**
     * Obtiene la conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException Si ocurre un error al conectar
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DatabaseConnection.getConnection();
        }
        return conexion;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                Logger.info("Conexión a la base de datos cerrada");
            } catch (SQLException e) {
                Logger.error("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Inicia una transacción
     * @throws SQLException Si ocurre un error al iniciar la transacción
     */
    public static void iniciarTransaccion() throws SQLException {
        Connection conn = getConexion();
        conn.setAutoCommit(false);
        Logger.info("Transacción iniciada");
    }
    
    /**
     * Confirma una transacción
     * @throws SQLException Si ocurre un error al confirmar la transacción
     */
    public static void confirmarTransaccion() throws SQLException {
        Connection conn = getConexion();
        conn.commit();
        conn.setAutoCommit(true);
        Logger.info("Transacción confirmada");
    }
    
    /**
     * Revierte una transacción
     * @throws SQLException Si ocurre un error al revertir la transacción
     */
    public static void revertirTransaccion() throws SQLException {
        Connection conn = getConexion();
        conn.rollback();
        conn.setAutoCommit(true);
        Logger.info("Transacción revertida");
    }
}
