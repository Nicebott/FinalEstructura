# Sakila Database


Una aplicación Java para gestionar la base de datos Sakila de MySQL, que proporciona una interfaz de consola para administrar actores, clientes, películas, inventario y ciudades, así como ejecutar consultas personalizadas y generar reportes.

##  Tabla de Contenidos

- [Requisitos]
- [Instalación]
- [Estructura del Proyecto]
- [Funcionalidades]
- [Uso]
- [Tecnologías]

##  Requisitos

- Java 11 o superior
- MySQL 8.0 o superior
- Base de datos Sakila instalada en MySQL
- Conector JDBC para MySQL

##  Instalación

1. Clona este repositorio:
 
   git clone https://github.com/Nicebott/FinalEstructura

2. Configura la conexión a la base de datos en `com/sakila/utils/DatabaseConnection.java`:
   
   private static final String URL = "jdbc:mysql://localhost:3306/sakila";
   private static final String USER = "tu usuario";
   private static final String PASSWORD = "tu contraseña";
   

3. Compila el proyecto:
   javac -d bin -cp lib/mysql-connector-java-8.0.28.jar Main.java com/sakila/**/*.java
  

4. Ejecuta la aplicación:

   java -cp bin:lib/mysql-connector-java-8.0.28.jar Main
  

##  Estructura del Proyecto


sakila-database/
├── Main.java                           # Punto de entrada de la aplicación
├── com/
│   └── sakila/
│       ├── controllers/                # Controladores para las entidades
│       │   ├── ActorControlador.java
│       │   ├── CityControlador.java
│       │   ├── ClienteControlador.java
│       │   ├── CountryControlador.java
│       │   ├── InventarioControlador.java
│       │   └── PeliculaControlador.java
│       ├── data/                       # Capa de acceso a datos
│       │   ├── ContextoBaseDatos.java
│       │   ├── DataContext.java
│       │   └── iDatapost.java
│       ├── models/                     # Modelos de datos
│       │   ├── Actor.java
│       │   ├── Address.java
│       │   ├── City.java
│       │   ├── Cliente.java
│       │   ├── Country.java
│       │   ├── Entity.java
│       │   ├── Inventario.java
│       │   ├── Language.java
│       │   ├── Pelicula.java
│       │   ├── Staff.java
│       │   └── Store.java
│       └── utils/                      # Utilidades
│           ├── DatabaseConnection.java
│           ├── Logger.java
│           ├── ReportGenerator.java
│           └── Validator.java
├── lib/                                # Bibliotecas externas
│   └── mysql-connector-java-8.0.28.jar
└── config.properties                   # Archivo de configuración

##  Funcionalidades

### Gestión de Entidades
- **Actores**: Listar, buscar, agregar, actualizar y eliminar actores.
- **Clientes**: Listar, buscar, agregar, actualizar y eliminar clientes, ver alquileres.
- **Películas**: Listar, buscar, agregar, actualizar y eliminar películas, ver actores.
- **Inventario**: Listar, buscar, agregar y eliminar inventario.
- **Ciudades**: Listar, buscar, agregar, actualizar y eliminar ciudades, listar por país.

### Consultas Personalizadas
- Top 5 películas más alquiladas
- Top 5 clientes con más alquileres
- Ingresos por categoría
- Películas por actor
- Clientes por país

### Generación de Reportes
- Películas por categoría
- Clientes por país
- Ingresos por mes
- Inventario por tienda
- Exportación a CSV y JSON

##  Uso

La aplicación proporciona una interfaz de consola con un menú principal que permite acceder a las diferentes funcionalidades:

===== SAKILA DATABASE =====
1. Gestionar Actores
2. Gestionar Clientes
3. Gestionar Películas
4. Gestionar Inventario
5. Ejecutar consultas personalizadas
6. Generar reportes
7. Gestionar Ciudades
0. Salir

Cada opción lleva a un submenú con operaciones específicas para cada entidad o funcionalidad.

### Ejemplo: Listar Actores

1. Selecciona la opción 1 en el menú principal
2. Selecciona la opción 1 en el submenú de Gestión de Actores
3. Se mostrará una lista de todos los actores en la base de datos

### Ejemplo: Generar un Reporte

1. Selecciona la opción 6 en el menú principal
2. Selecciona el tipo de reporte que deseas generar
3. Visualiza el reporte en la consola
4. Opcionalmente, exporta el reporte a CSV

##  Tecnologías

- **Java**: Lenguaje de programación principal
- **MySQL**: Sistema de gestión de base de datos
- **JDBC**: API para conectar Java con MySQL
- **Patrón MVC**: Arquitectura del proyecto (Modelo-Vista-Controlador)
- **Patrón DAO**: Para el acceso a datos (Data Access Object)

---

Desarrollado por [Nicolas Zierow Fermin]
