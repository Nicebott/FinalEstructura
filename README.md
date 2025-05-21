### Sakila Database

## Descripción

Sakila Database es una aplicación Java que proporciona una interfaz orientada a objetos para gestionar la base de datos Sakila, un esquema que simula un sistema de alquiler de películas. 
Esta aplicación implementa una arquitectura en capas con un diseño orientado a objetos, facilitando operaciones CRUD (Crear, Leer) sobre las diferentes entidades del sistema.

## Características principales

- **Gestión completa de entidades**: Administración de actores, películas, clientes, inventario y ciudades
- **Consultas personalizadas**: Ejecución de consultas complejas como "Top películas más alquiladas" o "Ingresos por categoría"
- **Generación de reportes**: Exportación de resultados a formatos como CSV
- **Interfaz por consola**: Menú intuitivo para interactuar con todas las funcionalidades
- **Caché en memoria**: Optimización de rendimiento mediante listas en memoria
- **Validación de datos**: Cada entidad implementa su propio método de validación
- **Manejo de transacciones**: Soporte para operaciones transaccionales
- **Registro de actividades**: Sistema de logging para seguimiento y depuración


## Tecnologías utilizadas

- Java 8+
- JDBC para conexión a base de datos
- MySQL (Base de datos Sakila)
- Programación Orientada a Objetos
- Patrón DAO (Data Access Object)


## Estructura del proyecto

### Paquetes principales

com.sakila
├── controllers     # Controladores para cada entidad
├── data           # Interfaces y clases para acceso a datos
├── models         # Clases de modelo/entidades
└── utils          # Clases de utilidad (conexión, logging, reportes)

### Diagrama de clases simplificado
Entity (abstract)
  ├── Actor
  ├── Cliente
  ├── Pelicula
  ├── Inventario
  ├── City
  ├── Country
  ├── Address
  ├── Store
  ├── Staff
  └── Language

iDatapost<T> (interface)
  ├── ActorControlador
  ├── ClienteControlador
  ├── PeliculaControlador
  ├── InventarioControlador
  ├── CityControlador
  └── CountryControlador

Utilities
  ├── DatabaseConnection
  ├── Logger
  └── ReportGenerator

## Requisitos previos

- JDK 8 o superior
- MySQL Server 5.7 o superior
- Base de datos Sakila instalada
- Conector JDBC para MySQL


## Instalación

### 1. Clonar el repositorio

git clone https://github.com/Nicebott/FinalEstructura


### 2. Configurar la conexión a la base de datos

Edita el archivo com.sakila.utils.DatabaseConnection.java para configurar los parámetros de conexión:

java
private static final String URL = "jdbc:mysql://localhost:3306/sakila";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";

### 3. Compilar el proyecto

# Compilación manual
javac -d bin src/com/sakila/**/*.java

### 4. Ejecutar la aplicación

# Ejecución manual
java -cp bin:lib/* Main


## Guía de uso

### Menú principal

Al iniciar la aplicación, se muestra un menú con las siguientes opciones:

1. Gestionar Actores
2. Gestionar Clientes
3. Gestionar Películas
4. Gestionar Inventario
5. Ejecutar consultas personalizadas
6. Generar reportes
7. Gestionar Ciudades
8. Salir


### Ejemplos de consultas personalizadas

La aplicación permite ejecutar consultas predefinidas como:

- Top 5 películas más alquiladas
- Top 5 clientes con más alquileres
- Ingresos por categoría


### Generación de reportes

Los reportes se generan en formato CSV y se guardan en el directorio ./reports/ con un timestamp para identificarlos fácilmente.

## Arquitectura

### Capa de Modelo

La aplicación utiliza una jerarquía de clases basada en la clase abstracta (Entity), de la cual heredan todas las entidades del sistema. Cada entidad implementa el método `validar()` para garantizar la integridad de los datos.

### Capa de Controladores

Cada entidad tiene su controlador correspondiente que implementa la interfaz `iDatapost<T>`, estableciendo un contrato uniforme para las operaciones CRUD. Los controladores mantienen listas de entidades en memoria para optimizar el rendimiento.

### Capa de Utilidades

- `DatabaseConnection`: Gestiona la conexión a la base de datos
- `Logger`: Proporciona funcionalidad de registro
- `ReportGenerator`: Genera reportes a partir de consultas SQL


## Ejemplos de código

### Obtener un actor por ID

java
ActorControlador controlador = new ActorControlador(conexion);
Actor actor = controlador.get(1);
System.out.println(actor.getNombrePrimer() + " " + actor.getApellido());


### Crear un nuevo cliente

java
Cliente cliente = new Cliente();
cliente.setPrimerNombre("Juan");
cliente.setApellido("Pérez");
cliente.setCorreoElectronico("juan.perez@example.com");
cliente.setTienda(tienda);
cliente.setDireccion(direccion);

ClienteControlador controlador = new ClienteControlador(conexion);
boolean resultado = controlador.post(cliente);


### Ejecutar una consulta personalizada

java
String sql = "SELECT f.film_id, f.title, COUNT(r.rental_id) AS total_rentals " +
             "FROM film f " +
             "JOIN inventory i ON f.film_id = i.film_id " +
             "JOIN rental r ON i.inventory_id = r.inventory_id " +
             "GROUP BY f.film_id " +
             "ORDER BY total_rentals DESC " +
             "LIMIT 5";

try (Statement stmt = conexion.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {
    while (rs.next()) {
        System.out.println(rs.getString("title") + ": " + 
                           rs.getInt("total_rentals") + " alquileres");
    }
}


## Patrones de diseño implementados

### Patrón DAO (Data Access Object)

Los controladores implementan el patrón DAO a través de la interfaz `iDatapost<T>`, proporcionando una capa de abstracción para las operaciones de base de datos.

### Patrón Repository

Cada controlador actúa como un repositorio para su entidad correspondiente, manteniendo una colección en memoria y sincronizándola con la base de datos.

### Patrón Template Method

La clase abstracta `Entity` define la estructura común y comportamientos que deben implementar todas las entidades.

## Autor

Nicolas Zierow Fermin

## Capturas de pantalla

### Menú principal

===== SAKILA DATABASE =====
1. Gestionar Actores
2. Gestionar Clientes
3. Gestionar Películas
4. Gestionar Inventario
5. Ejecutar consultas personalizadas
6. Generar reportes
7. Gestionar Ciudades
0. Salir
Seleccione una opción:

### Lista de actores


  Lista de actores:
1 - PENELOPE GUINESS
2 - NICK WAHLBERG
3 - ED CHASE
4 - JENNIFER DAVIS
5 - JOHNNY LOLLOBRIGIDA
6 - BETTE NICHOLSON
7 - GRACE MOSTEL
8 - MATTHEW JOHANSSON
9 - JOE SWANK
10 - CHRISTIAN GABLE

### Consulta personalizada: Top películas


  Top 5 películas más alquiladas:
103 - BUCKET BROTHERHOOD - 34 alquileres
738 - ROCKETEER MOTHER - 33 alquileres
382 - GRIT CLOCKWORK - 32 alquileres
730 - RIDGEMONT SUBMARINE - 32 alquileres
331 - FORWARD TEMPLE - 32 alquileres
