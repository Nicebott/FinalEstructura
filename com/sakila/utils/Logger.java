package com.sakila.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Clase para manejar el logging de la aplicación
 * @author Nicolas Zierow Fermin
 */
public class Logger {
    private static final String LOG_FILE = "sakila.log";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static LogLevel logLevel = LogLevel.INFO;
    
    /**
     * Niveles de log
     */
    public enum LogLevel {
        DEBUG(1),
        INFO(2),
        WARNING(3),
        ERROR(4);
        
        private final int level;
        
        LogLevel(int level) {
            this.level = level;
        }
        
        public int getLevel() {
            return level;
        }
    }
    
    /**
     * Inicializa el logger con la configuración del archivo properties
     */
    public static void init() {
        try {
            Properties props = new Properties();
            props.load(new java.io.FileInputStream("config.properties"));
            
            String level = props.getProperty("log.level", "INFO");
            setLogLevel(LogLevel.valueOf(level));
            
            info("Logger inicializado con nivel: " + logLevel);
        } catch (Exception e) {
            error("Error al inicializar el logger: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Establece el nivel de log
     * @param level Nivel de log
     */
    public static void setLogLevel(LogLevel level) {
        logLevel = level;
    }
    
    /**
     * Registra un mensaje de debug
     * @param message Mensaje a registrar
     */
    public static void debug(String message) {
        if (logLevel.getLevel() <= LogLevel.DEBUG.getLevel()) {
            log("DEBUG", message);
        }
    }
    
    /**
     * Registra un mensaje de información
     * @param message Mensaje a registrar
     */
    public static void info(String message) {
        if (logLevel.getLevel() <= LogLevel.INFO.getLevel()) {
            log("INFO", message);
        }
    }
    
    /**
     * Registra un mensaje de advertencia
     * @param message Mensaje a registrar
     */
    public static void warning(String message) {
        if (logLevel.getLevel() <= LogLevel.WARNING.getLevel()) {
            log("WARNING", message);
        }
    }
    
    /**
     * Registra un mensaje de error
     * @param message Mensaje a registrar
     */
    public static void error(String message) {
        if (logLevel.getLevel() <= LogLevel.ERROR.getLevel()) {
            log("ERROR", message);
        }
    }
    
    /**
     * Registra un mensaje en el archivo de log y en la consola
     * @param level Nivel de log
     * @param message Mensaje a registrar
     */
    private static void log(String level, String message) {
        String timestamp = DATE_FORMAT.format(new Date());
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Imprimir en consola
        System.out.println(logMessage);
        
        // Escribir en archivo
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(logMessage);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
