package com.sakila.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase para generar reportes a partir de consultas SQL
 * @author Nicolas Zierow Fermin
 */
public class ReportGenerator {
    private static final String REPORT_PATH = "C:\\Users\\nicop\\OneDrive\\Escritorio\\V3\\";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Genera un reporte CSV a partir de un ResultSet
     * @param rs ResultSet con los datos
     * @param fileName Nombre del archivo
     * @throws SQLException Si ocurre un error al leer los datos
     */
    public static void generateCSVReport(ResultSet rs, String fileName) throws SQLException {
        String filePath = REPORT_PATH + fileName;
        
        try (FileWriter writer = new FileWriter(filePath)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Escribir encabezados
            for (int i = 1; i <= columnCount; i++) {
                writer.append(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.append(",");
                }
            }
            writer.append("\n");
            
            // Escribir datos
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null) {
                        // Escapar comillas y comas
                        value = value.replace("\"", "\"\"");
                        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
                            value = "\"" + value + "\"";
                        }
                    } else {
                        value = "";
                    }
                    writer.append(value);
                    
                    if (i < columnCount) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            
            Logger.info("Reporte CSV generado: " + filePath);
        } catch (IOException e) {
            Logger.error("Error al generar reporte CSV: " + e.getMessage());
            throw new SQLException("Error al generar reporte CSV", e);
        }
    }
    
    /**
     * Genera un reporte JSON a partir de un ResultSet
     * @param rs ResultSet con los datos
     * @param fileName Nombre del archivo
     * @throws SQLException Si ocurre un error al leer los datos
     */
    public static void generateJSONReport(ResultSet rs, String fileName) throws SQLException {
        String filePath = REPORT_PATH + fileName;
        
        try (FileWriter writer = new FileWriter(filePath)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Inicio del array JSON
            writer.write("[\n");
            
            boolean firstRow = true;
            // Escribir datos
            while (rs.next()) {
                if (!firstRow) {
                    writer.write(",\n");
                } else {
                    firstRow = false;
                }
                
                writer.write("  {\n");
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    
                    writer.write("    \"" + columnName + "\": ");
                    
                    if (value == null) {
                        writer.write("null");
                    } else {
                        // Escapar comillas en el valor
                        value = value.replace("\"", "\\\"");
                        writer.write("\"" + value + "\"");
                    }
                    
                    if (i < columnCount) {
                        writer.write(",\n");
                    } else {
                        writer.write("\n");
                    }
                }
                
                writer.write("  }");
            }
            
            // Fin del array JSON
            writer.write("\n]");
            
            Logger.info("Reporte JSON generado: " + filePath);
        } catch (IOException e) {
            Logger.error("Error al generar reporte JSON: " + e.getMessage());
            throw new SQLException("Error al generar reporte JSON", e);
        }
    }
    
    /**
     * Genera un nombre de archivo con timestamp
     * @param baseName Nombre base del archivo
     * @param extension Extensión del archivo
     * @return Nombre de archivo con timestamp
     */
    public static String generateFileName(String baseName, String extension) {
        String timestamp = DATE_FORMAT.format(new Date());
        return baseName + "_" + timestamp + "." + extension;
    }
}
