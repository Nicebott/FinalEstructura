// No se debe declarar paquete aquí si el archivo está en la raíz
// Si quieres usar paquetes, debes mover este archivo a la carpeta com/sakila/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.sakila.controllers.ActorControlador;
import com.sakila.controllers.ClienteControlador;
import com.sakila.controllers.PeliculaControlador;
import com.sakila.controllers.InventarioControlador;
import com.sakila.controllers.CityControlador;
import com.sakila.controllers.CountryControlador;
import com.sakila.models.Actor;
import com.sakila.models.Cliente;
import com.sakila.models.Pelicula;
import com.sakila.models.Inventario;
import com.sakila.models.City;
import com.sakila.models.Country;
import com.sakila.utils.DatabaseConnection;
import com.sakila.utils.Logger;
import com.sakila.utils.ReportGenerator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
* Clase principal para la aplicación Sakila Database 
* @author Nicolas Zierow Fermin
*/
public class Main {
   public static void main(String[] args) {
       Logger.info("Iniciando aplicación Sakila Database");
       
       try (Connection conexion = DatabaseConnection.getConnection()) {
           Logger.info("Conexión a la base de datos establecida correctamente");
           
           // Crear controladores
           ActorControlador actorControlador = new ActorControlador(conexion);
           ClienteControlador clienteControlador = new ClienteControlador(conexion);
           PeliculaControlador peliculaControlador = new PeliculaControlador(conexion);
           InventarioControlador inventarioControlador = new InventarioControlador(conexion);
           CityControlador cityControlador = new CityControlador(conexion);
           CountryControlador countryControlador = new CountryControlador(conexion);
           
           // Iniciar la interfaz de usuario
           iniciarInterfazUsuario(conexion, actorControlador, clienteControlador, peliculaControlador, 
                                 inventarioControlador, cityControlador, countryControlador);
           
       } catch (Exception e) {
           Logger.error("Error de conexión a la base de datos: " + e.getMessage());
           e.printStackTrace();
       }
   }
   
   /**
    * Método para iniciar la interfaz de usuario por consola
    */
   private static void iniciarInterfazUsuario(
           Connection conexion,
           ActorControlador actorControlador,
           ClienteControlador clienteControlador,
           PeliculaControlador peliculaControlador,
           InventarioControlador inventarioControlador,
           CityControlador cityControlador,
           CountryControlador countryControlador) {
       
       Scanner scanner = new Scanner(System.in);
       boolean salir = false;
       
       while (!salir) {
           System.out.println("\n===== SAKILA DATABASE =====");
           System.out.println("1. Gestionar Actores");
           System.out.println("2. Gestionar Clientes");
           System.out.println("3. Gestionar Películas");
           System.out.println("4. Gestionar Inventario");
           System.out.println("5. Ejecutar consultas personalizadas");
           System.out.println("6. Generar reportes");
           System.out.println("7. Gestionar Ciudades");
           System.out.println("0. Salir");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); 
           
           try {
               switch (opcion) {
                   case 1:
                       gestionarActores(conexion, actorControlador, scanner);
                       break;
                   case 2:
                       gestionarClientes(conexion, clienteControlador, scanner);
                       break;
                   case 3:
                       gestionarPeliculas(conexion, peliculaControlador, scanner);
                       break;
                   case 4:
                       gestionarInventario(conexion, inventarioControlador, peliculaControlador, scanner);
                       break;
                   case 5:
                       ejecutarConsultasPersonalizadas(conexion, scanner);
                       break;
                   case 6:
                       generarReportes(conexion, scanner);
                       break;
                   case 7:
                       gestionarCiudades(conexion, cityControlador, countryControlador, scanner);
                       break;
                   case 0:
                       salir = true;
                       Logger.info("Cerrando aplicación");
                       break;
                   default:
                       System.out.println("Opción no válida. Intente de nuevo.");
               }
           } catch (SQLException e) {
               Logger.error("Error al ejecutar operación: " + e.getMessage());
               e.printStackTrace();
           }
       }
       
       scanner.close();
   }
   
   /**
    * Método para gestionar actores
    */
   private static void gestionarActores(Connection conexion, ActorControlador actorControlador, Scanner scanner) throws SQLException {
       boolean volver = false;
       
       while (!volver) {
           System.out.println("\n=== GESTIÓN DE ACTORES ===");
           System.out.println("1. Listar todos los actores");
           System.out.println("2. Buscar actor por ID");
           System.out.println("3. Agregar nuevo actor");
           System.out.println("4. Actualizar actor");
           System.out.println("5. Eliminar actor");
           System.out.println("6. Volver al menú principal");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); 
           
           switch (opcion) {
               case 1:
                   System.out.println("\n Lista de actores:");
                   for (Actor actor : actorControlador.get()) {
                       System.out.println(actor.getId() + " - " + actor.getNombrePrimer() + " " + actor.getApellido());
                   }
                   break;
               case 2:
                   System.out.print("Ingrese el ID del actor: ");
                   int actorId = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Actor actor = actorControlador.get(actorId);
                   if (actor != null) {
                       System.out.println("\nActor encontrado:");
                       System.out.println("ID: " + actor.getId());
                       System.out.println("Nombre: " + actor.getNombrePrimer());
                       System.out.println("Apellido: " + actor.getApellido());
                       System.out.println("Última actualización: " + actor.getUltimaActualizacion());
                   } else {
                       System.out.println("Actor no encontrado.");
                   }
                   break;
               case 3:
                   System.out.println("\nAgregar nuevo actor:");
                   System.out.print("Nombre: ");
                   String nombre = scanner.nextLine();
                   System.out.print("Apellido: ");
                   String apellido = scanner.nextLine();
                   
                   Actor nuevoActor = new Actor(nombre, apellido);
                   if (actorControlador.post(nuevoActor)) {
                       System.out.println("Actor agregado correctamente con ID: " + nuevoActor.getId());
                   } else {
                       System.out.println("Error al agregar actor.");
                   }
                   break;
               case 4:
                   System.out.print("Ingrese el ID del actor a actualizar: ");
                   int idActualizar = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Actor actorActualizar = actorControlador.get(idActualizar);
                   if (actorActualizar != null) {
                       System.out.println("Actor actual: " + actorActualizar.getNombrePrimer() + " " + actorActualizar.getApellido());
                       System.out.print("Nuevo nombre (deje en blanco para mantener el actual): ");
                       String nuevoNombre = scanner.nextLine();
                       System.out.print("Nuevo apellido (deje en blanco para mantener el actual): ");
                       String nuevoApellido = scanner.nextLine();
                       
                       if (!nuevoNombre.isEmpty()) {
                           actorActualizar.setNombrePrimer(nuevoNombre);
                       }
                       
                       if (!nuevoApellido.isEmpty()) {
                           actorActualizar.setApellido(nuevoApellido);
                       }
                       
                       if (actorControlador.put(actorActualizar)) {
                           System.out.println("Actor actualizado correctamente.");
                       } else {
                           System.out.println("Error al actualizar actor.");
                       }
                   } else {
                       System.out.println("Actor no encontrado.");
                   }
                   break;
               case 5:
                   System.out.print("Ingrese el ID del actor a eliminar: ");
                   int idEliminar = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Actor actorEliminar = actorControlador.get(idEliminar);
                   if (actorEliminar != null) {
                       System.out.println("¿Está seguro de eliminar al actor " + actorEliminar.getNombrePrimer() + " " + actorEliminar.getApellido() + "? (S/N)");
                       String confirmacion = scanner.nextLine();
                       
                       if (confirmacion.equalsIgnoreCase("S")) {
                           if (actorControlador.delete(idEliminar)) {
                               System.out.println("Actor eliminado correctamente.");
                           } else {
                               System.out.println("Error al eliminar actor.");
                           }
                       } else {
                           System.out.println("Operación cancelada.");
                       }
                   } else {
                       System.out.println("Actor no encontrado.");
                   }
                   break;
               case 6:
                   volver = true;
                   break;
               default:
                   System.out.println("Opción no válida. Intente de nuevo.");
           }
       }
   }
   
   /**
    * Método para gestionar clientes
    */
   private static void gestionarClientes(Connection conexion, ClienteControlador clienteControlador, Scanner scanner) throws SQLException {
       boolean volver = false;
       
       while (!volver) {
           System.out.println("\n=== GESTIÓN DE CLIENTES ===");
           System.out.println("1. Listar todos los clientes");
           System.out.println("2. Buscar cliente por ID");
           System.out.println("3. Agregar nuevo cliente");
           System.out.println("4. Actualizar cliente");
           System.out.println("5. Eliminar cliente");
           System.out.println("6. Ver alquileres de cliente");
           System.out.println("7. Volver al menú principal");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); // Consumir el salto de línea
           
           switch (opcion) {
               case 1:
                   System.out.println("\n Lista de clientes:");
                   for (Cliente cliente : clienteControlador.get()) {
                       System.out.println(cliente.getId() + " - " + cliente.getPrimerNombre() + " " + 
                                         cliente.getApellido() + " - " + cliente.getCorreoElectronico());
                   }
                   break;
               case 2:
                   System.out.print("Ingrese el ID del cliente: ");
                   int clienteId = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Cliente cliente = clienteControlador.get(clienteId);
                   if (cliente != null) {
                       System.out.println("\nCliente encontrado:");
                       System.out.println("ID: " + cliente.getId());
                       System.out.println("Nombre: " + cliente.getPrimerNombre() + " " + cliente.getApellido());
                       System.out.println("Email: " + cliente.getCorreoElectronico());
                       System.out.println("Dirección: " + cliente.getDireccion().getAddress());
                       System.out.println("Activo: " + (cliente.isActivo() ? "Sí" : "No"));
                   } else {
                       System.out.println("Cliente no encontrado.");
                   }
                   break;
               case 3:
                   // Implementación simplificada - en un caso real se necesitaría más información
                   System.out.println("\nPara agregar un nuevo cliente, se requiere información adicional como tienda y dirección.");
                   System.out.println("Esta funcionalidad requiere implementación adicional.");
                   break;
               case 4:
                   System.out.print("Ingrese el ID del cliente a actualizar: ");
                   int idActualizar = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Cliente clienteActualizar = clienteControlador.get(idActualizar);
                   if (clienteActualizar != null) {
                       System.out.println("Cliente actual: " + clienteActualizar.getPrimerNombre() + " " + clienteActualizar.getApellido());
                       System.out.print("Nuevo nombre (deje en blanco para mantener el actual): ");
                       String nuevoNombre = scanner.nextLine();
                       System.out.print("Nuevo apellido (deje en blanco para mantener el actual): ");
                       String nuevoApellido = scanner.nextLine();
                       System.out.print("Nuevo email (deje en blanco para mantener el actual): ");
                       String nuevoEmail = scanner.nextLine();
                       
                       if (!nuevoNombre.isEmpty()) {
                           clienteActualizar.setPrimerNombre(nuevoNombre);
                       }
                       
                       if (!nuevoApellido.isEmpty()) {
                           clienteActualizar.setApellido(nuevoApellido);
                       }
                       
                       if (!nuevoEmail.isEmpty()) {
                           clienteActualizar.setCorreoElectronico(nuevoEmail);
                       }
                       
                       if (clienteControlador.put(clienteActualizar)) {
                           System.out.println("Cliente actualizado correctamente.");
                       } else {
                           System.out.println("Error al actualizar cliente.");
                       }
                   } else {
                       System.out.println("Cliente no encontrado.");
                   }
                   break;
               case 5:
                   System.out.print("Ingrese el ID del cliente a eliminar: ");
                   int idEliminar = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Cliente clienteEliminar = clienteControlador.get(idEliminar);
                   if (clienteEliminar != null) {
                       System.out.println("¿Está seguro de eliminar al cliente " + clienteEliminar.getPrimerNombre() + " " + 
                                         clienteEliminar.getApellido() + "? (S/N)");
                       String confirmacion = scanner.nextLine();
                       
                       if (confirmacion.equalsIgnoreCase("S")) {
                           if (clienteControlador.delete(idEliminar)) {
                               System.out.println("Cliente eliminado correctamente.");
                           } else {
                               System.out.println("Error al eliminar cliente.");
                           }
                       } else {
                           System.out.println("Operación cancelada.");
                       }
                   } else {
                       System.out.println("Cliente no encontrado.");
                   }
                   break;
               case 6:
                   System.out.print("Ingrese el ID del cliente para ver sus alquileres: ");
                   int idAlquileres = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Cliente clienteAlquileres = clienteControlador.get(idAlquileres);
                   if (clienteAlquileres != null) {
                       System.out.println("\nAlquileres del cliente " + clienteAlquileres.getPrimerNombre() + " " + 
                                         clienteAlquileres.getApellido() + ":");
                       
                       var alquileres = clienteControlador.obtenerAlquileresDeCliente(idAlquileres);
                       if (alquileres.isEmpty()) {
                           System.out.println("El cliente no tiene alquileres registrados.");
                       } else {
                           for (var alquiler : alquileres) {
                               System.out.println("ID: " + alquiler.get("id") + 
                                                 " - Película: " + alquiler.get("tituloPelicula") + 
                                                 " - Fecha: " + alquiler.get("fechaAlquiler") + 
                                                 " - Devolución: " + alquiler.get("fechaDevolucion") + 
                                                 " - Monto: $" + alquiler.get("monto"));
                           }
                       }
                   } else {
                       System.out.println("Cliente no encontrado.");
                   }
                   break;
               case 7:
                   volver = true;
                   break;
               default:
                   System.out.println("Opción no válida. Intente de nuevo.");
           }
       }
   }
   
   /**
    * Método para gestionar películas
    */
   private static void gestionarPeliculas(Connection conexion, PeliculaControlador peliculaControlador, Scanner scanner) throws SQLException {
       boolean volver = false;
       
       while (!volver) {
           System.out.println("\n=== GESTIÓN DE PELÍCULAS ===");
           System.out.println("1. Listar todas las películas");
           System.out.println("2. Buscar película por ID");
           System.out.println("3. Agregar nueva película");
           System.out.println("4. Actualizar película");
           System.out.println("5. Eliminar película");
           System.out.println("6. Ver actores de una película");
           System.out.println("7. Volver al menú principal");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); // Consumir el salto de línea
           
           switch (opcion) {
               case 1:
                   System.out.println("\n Lista de películas:");
                   for (Pelicula pelicula : peliculaControlador.get()) {
                       System.out.println(pelicula.getId() + " - " + pelicula.getTitulo() + " (" + 
                                         pelicula.getAnioLanzamiento() + ") - " + pelicula.getClasificacion());
                   }
                   break;
               case 2:
                   System.out.print("Ingrese el ID de la película: ");
                   int peliculaId = scanner.nextInt();
                   scanner.nextLine(); // Consumir el salto de línea
                   
                   Pelicula pelicula = peliculaControlador.get(peliculaId);
                   if (pelicula != null) {
                       System.out.println("\nPelícula encontrada:");
                       System.out.println("ID: " + pelicula.getId());
                       System.out.println("Título: " + pelicula.getTitulo());
                       System.out.println("Descripción: " + pelicula.getDescripcion());
                       System.out.println("Año: " + pelicula.getAnioLanzamiento());
                       System.out.println("Idioma: " + pelicula.getIdioma().getName());
                       System.out.println("Duración: " + pelicula.getDuracion() + " minutos");
                       System.out.println("Clasificación: " + pelicula.getClasificacion());
                       System.out.println("Tarifa de renta: $" + pelicula.getTarifaRenta());
                   } else {
                       System.out.println("Película no encontrada.");
                   }
                   break;
               case 3:
                   // Implementación simplificada - en un caso real se necesitaría más información
                   System.out.println("\nPara agregar una nueva película, se requiere información adicional como idioma.");
                   System.out.println("Esta funcionalidad requiere implementación adicional.");
                   break;
               case 4:
                   System.out.print("Ingrese el ID de la película a actualizar: ");
                   int idActualizar = scanner.nextInt();
                   scanner.nextLine(); // Consumir el salto de línea
                   
                   Pelicula peliculaActualizar = peliculaControlador.get(idActualizar);
                   if (peliculaActualizar != null) {
                       System.out.println("Película actual: " + peliculaActualizar.getTitulo());
                       System.out.print("Nuevo título (deje en blanco para mantener el actual): ");
                       String nuevoTitulo = scanner.nextLine();
                       System.out.print("Nueva descripción (deje en blanco para mantener la actual): ");
                       String nuevaDescripcion = scanner.nextLine();
                       
                       if (!nuevoTitulo.isEmpty()) {
                           peliculaActualizar.setTitulo(nuevoTitulo);
                       }
                       
                       if (!nuevaDescripcion.isEmpty()) {
                           peliculaActualizar.setDescripcion(nuevaDescripcion);
                       }
                       
                       if (peliculaControlador.put(peliculaActualizar)) {
                           System.out.println("Película actualizada correctamente.");
                       } else {
                           System.out.println("Error al actualizar película.");
                       }
                   } else {
                       System.out.println("Película no encontrada.");
                   }
                   break;
               case 5:
                   System.out.print("Ingrese el ID de la película a eliminar: ");
                   int idEliminar = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Pelicula peliculaEliminar = peliculaControlador.get(idEliminar);
                   if (peliculaEliminar != null) {
                       System.out.println("¿Está seguro de eliminar la película " + peliculaEliminar.getTitulo() + "? (S/N)");
                       String confirmacion = scanner.nextLine();
                       
                       if (confirmacion.equalsIgnoreCase("S")) {
                           if (peliculaControlador.delete(idEliminar)) {
                               System.out.println("Película eliminada correctamente.");
                           } else {
                               System.out.println("Error al eliminar película.");
                           }
                       } else {
                           System.out.println("Operación cancelada.");
                       }
                   } else {
                       System.out.println("Película no encontrada.");
                   }
                   break;
               case 6:
                   System.out.print("Ingrese el ID de la película para ver sus actores: ");
                   int idActores = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Pelicula peliculaActores = peliculaControlador.get(idActores);
                   if (peliculaActores != null) {
                       System.out.println("\nActores de la película " + peliculaActores.getTitulo() + ":");
                       
                       var actores = peliculaControlador.obtenerActoresDePelicula(idActores);
                       if (actores.isEmpty()) {
                           System.out.println("La película no tiene actores registrados.");
                       } else {
                           for (var actor : actores) {
                               System.out.println(actor.get("id") + " - " + actor.get("nombre") + " " + actor.get("apellido"));
                           }
                       }
                   } else {
                       System.out.println("Película no encontrada.");
                   }
                   break;
               case 7:
                   volver = true;
                   break;
               default:
                   System.out.println("Opción no válida. Intente de nuevo.");
           }
       }
   }
   
   /**
    * Método para gestionar inventario
    */
   private static void gestionarInventario(Connection conexion, InventarioControlador inventarioControlador, 
                                         PeliculaControlador peliculaControlador, Scanner scanner) throws SQLException {
       boolean volver = false;
       
       while (!volver) {
           System.out.println("\n=== GESTIÓN DE INVENTARIO ===");
           System.out.println("1. Listar inventario");
           System.out.println("2. Buscar inventario por ID");
           System.out.println("3. Agregar nuevo inventario");
           System.out.println("4. Eliminar inventario");
           System.out.println("5. Buscar inventario por título de película");
           System.out.println("6. Volver al menú principal");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); 
           
           switch (opcion) {
               case 1:
                   System.out.println("\n Lista de inventario:");
                   for (Inventario inventario : inventarioControlador.get()) {
                       System.out.println(inventario.getId() + " - Película: " + 
                                         (inventario.getPelicula() != null ? inventario.getPelicula().getTitulo() : "N/A") + 
                                         " - Tienda: " + inventario.getTienda().getId());
                   }
                   break;
               case 2:
                   System.out.print("Ingrese el ID del inventario: ");
                   int inventarioId = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Inventario inventario = inventarioControlador.get(inventarioId);
                   if (inventario != null) {
                       System.out.println("\nInventario encontrado:");
                       System.out.println("ID: " + inventario.getId());
                       System.out.println("Película: " + (inventario.getPelicula() != null ? inventario.getPelicula().getTitulo() : "N/A"));
                       System.out.println("Tienda: " + inventario.getTienda().getId());
                   } else {
                       System.out.println("Inventario no encontrado.");
                   }
                   break;
               case 3:
                   // Implementación simplificada - en un caso real se necesitaría más información
                   System.out.println("\nPara agregar un nuevo inventario, se requiere seleccionar una película y una tienda.");
                   System.out.println("Esta funcionalidad requiere implementación adicional.");
                   break;
               case 4:
                   System.out.print("Ingrese el ID del inventario a eliminar: ");
                   int idEliminar = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   Inventario inventarioEliminar = inventarioControlador.get(idEliminar);
                   if (inventarioEliminar != null) {
                       System.out.println("¿Está seguro de eliminar el inventario ID " + inventarioEliminar.getId() + 
                                         " de la película " + 
                                         (inventarioEliminar.getPelicula() != null ? inventarioEliminar.getPelicula().getTitulo() : "N/A") + 
                                         "? (S/N)");
                       String confirmacion = scanner.nextLine();
                       
                       if (confirmacion.equalsIgnoreCase("S")) {
                           if (inventarioControlador.delete(idEliminar)) {
                               System.out.println("Inventario eliminado correctamente.");
                           } else {
                               System.out.println("Error al eliminar inventario.");
                           }
                       } else {
                           System.out.println("Operación cancelada.");
                       }
                   } else {
                       System.out.println("Inventario no encontrado.");
                   }
                   break;
               case 5:
                   System.out.print("Ingrese el título de la película a buscar: ");
                   String tituloBuscar = scanner.nextLine();
                   
                   List<Inventario> inventariosPorTitulo = inventarioControlador.get(tituloBuscar);
                   if (!inventariosPorTitulo.isEmpty()) {
                       System.out.println("\nInventarios encontrados para la película \"" + tituloBuscar + "\":");
                       for (Inventario inv : inventariosPorTitulo) {
                           System.out.println(inv.getId() + " - Película: " + 
                                             (inv.getPelicula() != null ? inv.getPelicula().getTitulo() : "N/A") + 
                                             " - Tienda: " + inv.getTienda().getId());
                       }
                   } else {
                       System.out.println("No se encontraron inventarios para la película \"" + tituloBuscar + "\".");
                   }
                   break;
               case 6:
                   volver = true;
                   break;
               default:
                   System.out.println("Opción no válida. Intente de nuevo.");
           }
       }
   }
   
   /**
    * Método para gestionar ciudades
    */
   private static void gestionarCiudades(Connection conexion, CityControlador cityControlador, 
                                       CountryControlador countryControlador, Scanner scanner) throws SQLException {
       boolean volver = false;
       
       while (!volver) {
           System.out.println("\n=== GESTIÓN DE CIUDADES ===");
           System.out.println("1. Listar ciudades");
           System.out.println("2. Buscar ciudad por ID");
           System.out.println("3. Agregar nueva ciudad");
           System.out.println("4. Actualizar ciudad");
           System.out.println("5. Eliminar ciudad");
           System.out.println("6. Listar ciudades por país");
           System.out.println("7. Volver al menú principal");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); 
           
           switch (opcion) {
               case 1:
                   System.out.println("\n Lista de ciudades:");
                   System.out.printf("%-5s %-25s %-25s\n", "ID", "Ciudad", "País");
                   System.out.println("------------------------------------------------------");
                   for (City ciudad : cityControlador.get()) {
                       System.out.printf("%-5d %-25s %-25s\n", 
                                        ciudad.getId(), 
                                        ciudad.getCity(), 
                                        ciudad.getCountry().getCountry());
                   }
                   break;
               case 2:
                   System.out.print("Ingrese el ID de la ciudad: ");
                   int ciudadId = scanner.nextInt();
                   scanner.nextLine(); // Consumir el salto de línea
                   
                   City ciudad = cityControlador.get(ciudadId);
                   if (ciudad != null) {
                       System.out.println("\nCiudad encontrada:");
                       System.out.println("ID: " + ciudad.getId());
                       System.out.println("Nombre: " + ciudad.getCity());
                       System.out.println("País: " + ciudad.getCountry().getCountry());
                   } else {
                       System.out.println("Ciudad no encontrada.");
                   }
                   break;
               case 3:
                   System.out.println("\nAgregar nueva ciudad:");
                   System.out.print("Nombre de la ciudad: ");
                   String nombreCiudad = scanner.nextLine();
                   
                   System.out.println("\nSeleccione un país:");
                   List<Country> paises = countryControlador.get();
                   for (int i = 0; i < paises.size(); i++) {
                       System.out.println((i + 1) + ". " + paises.get(i).getCountry());
                   }
                   System.out.print("Número de país: ");
                   int numPais = scanner.nextInt();
                   scanner.nextLine(); // Consumir el salto de línea
                   
                   if (numPais > 0 && numPais <= paises.size()) {
                       Country paisSeleccionado = paises.get(numPais - 1);
                       City nuevaCiudad = new City(nombreCiudad, paisSeleccionado);
                       
                       if (cityControlador.post(nuevaCiudad)) {
                           System.out.println("Ciudad agregada correctamente con ID: " + nuevaCiudad.getId());
                       } else {
                           System.out.println("Error al agregar ciudad.");
                       }
                   } else {
                       System.out.println("Selección de país inválida.");
                   }
                   break;
               case 4:
                   System.out.print("Ingrese el ID de la ciudad a actualizar: ");
                   int idActualizar = scanner.nextInt();
                   scanner.nextLine(); // Consumir el salto de línea
                   
                   City ciudadActualizar = cityControlador.get(idActualizar);
                   if (ciudadActualizar != null) {
                       System.out.println("Ciudad actual: " + ciudadActualizar.getCity() + ", " + 
                                         ciudadActualizar.getCountry().getCountry());
                       System.out.print("Nuevo nombre (deje en blanco para mantener el actual): ");
                       String nuevoNombre = scanner.nextLine();
                       
                       if (!nuevoNombre.isEmpty()) {
                           ciudadActualizar.setCity(nuevoNombre);
                       }
                       
                       System.out.println("¿Desea cambiar el país? (S/N)");
                       String cambiarPais = scanner.nextLine();
                       
                       if (cambiarPais.equalsIgnoreCase("S")) {
                           System.out.println("\nSeleccione un nuevo país:");
                           List<Country> listaPaises = countryControlador.get();
                           for (int i = 0; i < listaPaises.size(); i++) {
                               System.out.println((i + 1) + ". " + listaPaises.get(i).getCountry());
                           }
                           System.out.print("Número de país: ");
                           int nuevoNumPais = scanner.nextInt();
                           scanner.nextLine(); 
                           
                           if (nuevoNumPais > 0 && nuevoNumPais <= listaPaises.size()) {
                               Country nuevoPaisSeleccionado = listaPaises.get(nuevoNumPais - 1);
                               ciudadActualizar.setCountry(nuevoPaisSeleccionado);
                           } else {
                               System.out.println("Selección de país inválida.");
                           }
                       }
                       
                       if (cityControlador.put(ciudadActualizar)) {
                           System.out.println("Ciudad actualizada correctamente.");
                       } else {
                           System.out.println("Error al actualizar ciudad.");
                       }
                   } else {
                       System.out.println("Ciudad no encontrada.");
                   }
                   break;
               case 5:
                   System.out.print("Ingrese el ID de la ciudad a eliminar: ");
                   int idEliminar = scanner.nextInt();
                   scanner.nextLine(); // Consumir el salto de línea
                   
                   City ciudadEliminar = cityControlador.get(idEliminar);
                   if (ciudadEliminar != null) {
                       System.out.println("¿Está seguro de eliminar la ciudad " + ciudadEliminar.getCity() + ", " + 
                                         ciudadEliminar.getCountry().getCountry() + "? (S/N)");
                       String confirmacion = scanner.nextLine();
                       
                       if (confirmacion.equalsIgnoreCase("S")) {
                           if (cityControlador.delete(idEliminar)) {
                               System.out.println("Ciudad eliminada correctamente.");
                           } else {
                               System.out.println("Error al eliminar ciudad.");
                           }
                       } else {
                           System.out.println("Operación cancelada.");
                       }
                   } else {
                       System.out.println("Ciudad no encontrada.");
                   }
                   break;
               case 6:
                   System.out.println("\nSeleccione un país para ver sus ciudades:");
                   List<Country> listaPaises = countryControlador.get();
                   for (int i = 0; i < listaPaises.size(); i++) {
                       System.out.println((i + 1) + ". " + listaPaises.get(i).getCountry());
                   }
                   System.out.print("Número de país: ");
                   int paisNum = scanner.nextInt();
                   scanner.nextLine(); // Consumir el salto de línea
                   
                   if (paisNum > 0 && paisNum <= listaPaises.size()) {
                       Country paisSeleccionado = listaPaises.get(paisNum - 1);
                       List<City> ciudadesPorPais = cityControlador.getCiudadesPorPais(paisSeleccionado.getId());
                       
                       System.out.println("\nCiudades de " + paisSeleccionado.getCountry() + ":");
                       if (ciudadesPorPais.isEmpty()) {
                           System.out.println("No hay ciudades registradas para este país.");
                       } else {
                           for (City c : ciudadesPorPais) {
                               System.out.println(c.getId() + " - " + c.getCity());
                           }
                       }
                   } else {
                       System.out.println("Selección de país inválida.");
                   }
                   break;
               case 7:
                   volver = true;
                   break;
               default:
                   System.out.println("Opción no válida. Intente de nuevo.");
           }
       }
   }
   
   /**
    * Método para ejecutar consultas personalizadas
    */
   private static void ejecutarConsultasPersonalizadas(Connection conexion, Scanner scanner) throws SQLException {
       boolean volver = false;
       
       while (!volver) {
           System.out.println("\n=== CONSULTAS PERSONALIZADAS ===");
           System.out.println("1. Top 5 películas más alquiladas");
           System.out.println("2. Top 5 clientes con más alquileres");
           System.out.println("3. Ingresos por categoría");
           System.out.println("4. Películas por actor");
           System.out.println("5. Clientes por país");
           System.out.println("6. Volver al menú principal");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); 
           
           Statement stmt = conexion.createStatement();
           
           switch (opcion) {
               case 1:
                   String topPeliculas = "SELECT f.film_id, f.title, COUNT(r.rental_id) AS total_rentals " +
                                        "FROM film f " +
                                        "JOIN inventory i ON f.film_id = i.film_id " +
                                        "JOIN rental r ON i.inventory_id = r.inventory_id " +
                                        "GROUP BY f.film_id " +
                                        "ORDER BY total_rentals DESC " +
                                        "LIMIT 5";
                   ResultSet rsTopPeliculas = stmt.executeQuery(topPeliculas);
                   System.out.println("\n Top 5 películas más alquiladas:");
                   while (rsTopPeliculas.next()) {
                       System.out.println(rsTopPeliculas.getInt("film_id") + " - " +
                                          rsTopPeliculas.getString("title") + " - " +
                                          rsTopPeliculas.getInt("total_rentals") + " alquileres");
                   }
                   rsTopPeliculas.close();
                   break;
               case 2:
                   String topClientes = "SELECT c.customer_id, c.first_name, c.last_name, COUNT(r.rental_id) AS total_rentals " +
                                       "FROM customer c " +
                                       "JOIN rental r ON c.customer_id = r.customer_id " +
                                       "GROUP BY c.customer_id " +
                                       "ORDER BY total_rentals DESC " +
                                       "LIMIT 5";
                   ResultSet rsTopClientes = stmt.executeQuery(topClientes);
                   System.out.println("\n Top 5 clientes con más alquileres:");
                   while (rsTopClientes.next()) {
                       System.out.println(rsTopClientes.getInt("customer_id") + " - " +
                                          rsTopClientes.getString("first_name") + " " +
                                          rsTopClientes.getString("last_name") + " - " +
                                          rsTopClientes.getInt("total_rentals") + " alquileres");
                   }
                   rsTopClientes.close();
                   break;
               case 3:
                   String ingresosPorCategoria = "SELECT c.name AS category, SUM(p.amount) AS total_revenue " +
                                                "FROM category c " +
                                                "JOIN film_category fc ON c.category_id = fc.category_id " +
                                                "JOIN film f ON fc.film_id = f.film_id " +
                                                "JOIN inventory i ON f.film_id = i.film_id " +
                                                "JOIN rental r ON i.inventory_id = r.inventory_id " +
                                                "JOIN payment p ON r.rental_id = p.rental_id " +
                                                "GROUP BY c.name " +
                                                "ORDER BY total_revenue DESC";
                   ResultSet rsIngresos = stmt.executeQuery(ingresosPorCategoria);
                   System.out.println("\n Ingresos por categoría:");
                   while (rsIngresos.next()) {
                       System.out.printf("%-15s $%.2f\n", 
                                        rsIngresos.getString("category"),
                                        rsIngresos.getDouble("total_revenue"));
                   }
                   rsIngresos.close();
                   break;
               case 4:
                   System.out.print("Ingrese el ID del actor: ");
                   int actorId = scanner.nextInt();
                   scanner.nextLine(); 
                   
                   String peliculasPorActor = "SELECT a.actor_id, a.first_name, a.last_name, f.film_id, f.title, f.release_year " +
                                             "FROM actor a " +
                                             "JOIN film_actor fa ON a.actor_id = fa.actor_id " +
                                             "JOIN film f ON fa.film_id = f.film_id " +
                                             "WHERE a.actor_id = ? " +
                                             "ORDER BY f.release_year DESC, f.title";
                   
                   PreparedStatement pstmtActor = conexion.prepareStatement(peliculasPorActor);
                   pstmtActor.setInt(1, actorId);
                   ResultSet rsPeliculasActor = pstmtActor.executeQuery();
                   
                   boolean primerRegistro = true;
                   String nombreActor = "";
                   
                   System.out.println("\n Películas del actor:");
                   while (rsPeliculasActor.next()) {
                       if (primerRegistro) {
                           nombreActor = rsPeliculasActor.getString("first_name") + " " + 
                                        rsPeliculasActor.getString("last_name");
                           System.out.println("Actor: " + nombreActor);
                           primerRegistro = false;
                       }
                       
                       System.out.println(rsPeliculasActor.getInt("film_id") + " - " +
                                          rsPeliculasActor.getString("title") + " (" +
                                          rsPeliculasActor.getInt("release_year") + ")");
                   }
                   
                   if (primerRegistro) {
                       System.out.println("No se encontró el actor o no tiene películas asociadas.");
                   }
                   
                   rsPeliculasActor.close();
                   pstmtActor.close();
                   break;
               case 5:
                   String clientesPorPais = "SELECT co.country, COUNT(cu.customer_id) AS total " +
                                           "FROM customer cu " +
                                           "JOIN address a ON cu.address_id = a.address_id " +
                                           "JOIN city ci ON a.city_id = ci.city_id " +
                                           "JOIN country co ON ci.country_id = co.country_id " +
                                           "GROUP BY co.country " +
                                           "ORDER BY total DESC";
                   ResultSet rsClientesPais = stmt.executeQuery(clientesPorPais);
                   System.out.println("\n👥 Clientes por país:");
                   while (rsClientesPais.next()) {
                       System.out.printf("%-20s: %d clientes\n", 
                                        rsClientesPais.getString("country"),
                                        rsClientesPais.getInt("total"));
                   }
                   rsClientesPais.close();
                   break;
               case 6:
                   volver = true;
                   break;
               default:
                   System.out.println("Opción no válida. Intente de nuevo.");
           }
           
           stmt.close();
       }
   }
   
   /**
    * Método para generar reportes
    */
   private static void generarReportes(Connection conexion, Scanner scanner) throws SQLException {
       boolean volver = false;
       
       while (!volver) {
           System.out.println("\n=== GENERACIÓN DE REPORTES ===");
           System.out.println("1. Películas por categoría");
           System.out.println("2. Clientes por país");
           System.out.println("3. Ingresos por mes");
           System.out.println("4. Inventario por tienda");
           System.out.println("5. Volver al menú principal");
           System.out.print("Seleccione una opción: ");
           
           int opcion = scanner.nextInt();
           scanner.nextLine(); 
           
           Statement stmt = conexion.createStatement();
           
           switch (opcion) {
               case 1:
                   String peliculasPorCategoria = "SELECT c.name, COUNT(f.film_id) AS total " +
                                                 "FROM category c " +
                                                 "JOIN film_category fc ON c.category_id = fc.category_id " +
                                                 "JOIN film f ON fc.film_id = f.film_id " +
                                                 "GROUP BY c.name " +
                                                 "ORDER BY total DESC";
                   ResultSet rsCategorias = stmt.executeQuery(peliculasPorCategoria);
                   System.out.println("\n Películas por categoría:");
                   while (rsCategorias.next()) {
                       System.out.printf("%-15s: %d películas\n", 
                                        rsCategorias.getString("name"),
                                        rsCategorias.getInt("total"));
                   }
                   rsCategorias.close();
                   
                   System.out.println("\n¿Desea exportar este reporte a CSV? (S/N)");
                   String exportarCSV = scanner.nextLine();
                   
                   if (exportarCSV.equalsIgnoreCase("S")) {
                       try {
                           ResultSet rsExport = stmt.executeQuery(peliculasPorCategoria);
                           String fileName = ReportGenerator.generateFileName("peliculas_por_categoria", "csv");
                           ReportGenerator.generateCSVReport(rsExport, fileName);
                           System.out.println("Reporte exportado a: " + fileName);
                           rsExport.close();
                       } catch (Exception e) {
                           System.out.println("Error al exportar reporte: " + e.getMessage());
                       }
                   }
                   break;
               case 2:
                   String clientesPorPais = "SELECT co.country, COUNT(cu.customer_id) AS total " +
                                           "FROM customer cu " +
                                           "JOIN address a ON cu.address_id = a.address_id " +
                                           "JOIN city ci ON a.city_id = ci.city_id " +
                                           "JOIN country co ON ci.country_id = co.country_id " +
                                           "GROUP BY co.country " +
                                           "ORDER BY total DESC";
                   ResultSet rsPaises = stmt.executeQuery(clientesPorPais);
                   System.out.println("\n Clientes por país:");
                   while (rsPaises.next()) {
                       System.out.printf("%-20s: %d clientes\n", 
                                        rsPaises.getString("country"),
                                        rsPaises.getInt("total"));
                   }
                   rsPaises.close();
                   
                   System.out.println("\n¿Desea exportar este reporte a CSV? (S/N)");
                   String exportarPaisesCSV = scanner.nextLine();
                   
                   if (exportarPaisesCSV.equalsIgnoreCase("S")) {
                       try {
                           ResultSet rsExport = stmt.executeQuery(clientesPorPais);
                           String fileName = ReportGenerator.generateFileName("clientes_por_pais", "csv");
                           ReportGenerator.generateCSVReport(rsExport, fileName);
                           System.out.println("Reporte exportado a: " + fileName);
                           rsExport.close();
                       } catch (Exception e) {
                           System.out.println("Error al exportar reporte: " + e.getMessage());
                       }
                   }
                   break;
               case 3:
                   String ingresosPorMes = "SELECT YEAR(p.payment_date) AS year, MONTH(p.payment_date) AS month, " +
                                          "SUM(p.amount) AS total_revenue " +
                                          "FROM payment p " +
                                          "GROUP BY YEAR(p.payment_date), MONTH(p.payment_date) " +
                                          "ORDER BY year, month";
                   ResultSet rsIngresos = stmt.executeQuery(ingresosPorMes);
                   System.out.println("\n Ingresos por mes:");
                   System.out.printf("%-6s %-6s %-15s\n", "Año", "Mes", "Ingresos");
                   System.out.println("---------------------------");
                   while (rsIngresos.next()) {
                       System.out.printf("%-6d %-6d $%-15.2f\n", 
                                        rsIngresos.getInt("year"),
                                        rsIngresos.getInt("month"),
                                        rsIngresos.getDouble("total_revenue"));
                   }
                   rsIngresos.close();
                   
                   System.out.println("\n¿Desea exportar este reporte a CSV? (S/N)");
                   String exportarIngresosCSV = scanner.nextLine();
                   
                   if (exportarIngresosCSV.equalsIgnoreCase("S")) {
                       try {
                           ResultSet rsExport = stmt.executeQuery(ingresosPorMes);
                           String fileName = ReportGenerator.generateFileName("ingresos_por_mes", "csv");
                           ReportGenerator.generateCSVReport(rsExport, fileName);
                           System.out.println("Reporte exportado a: " + fileName);
                           rsExport.close();
                       } catch (Exception e) {
                           System.out.println("Error al exportar reporte: " + e.getMessage());
                       }
                   }
                   break;
               case 4:
                   String inventarioPorTienda = "SELECT s.store_id, COUNT(i.inventory_id) AS total_inventory, " +
                                               "COUNT(DISTINCT f.film_id) AS unique_films " +
                                               "FROM store s " +
                                               "JOIN inventory i ON s.store_id = i.store_id " +
                                               "JOIN film f ON i.film_id = f.film_id " +
                                               "GROUP BY s.store_id";
                   ResultSet rsInventario = stmt.executeQuery(inventarioPorTienda);
                   System.out.println("\n📦 Inventario por tienda:");
                   System.out.printf("%-10s %-20s %-20s\n", "Tienda", "Total Inventario", "Películas Únicas");
                   System.out.println("--------------------------------------------------");
                   while (rsInventario.next()) {
                       System.out.printf("%-10d %-20d %-20d\n", 
                                        rsInventario.getInt("store_id"),
                                        rsInventario.getInt("total_inventory"),
                                        rsInventario.getInt("unique_films"));
                   }
                   rsInventario.close();
                   
                   System.out.println("\n¿Desea exportar este reporte a CSV? (S/N)");
                   String exportarInventarioCSV = scanner.nextLine();
                   
                   if (exportarInventarioCSV.equalsIgnoreCase("S")) {
                       try {
                           ResultSet rsExport = stmt.executeQuery(inventarioPorTienda);
                           String fileName = ReportGenerator.generateFileName("inventario_por_tienda", "csv");
                           ReportGenerator.generateCSVReport(rsExport, fileName);
                           System.out.println("Reporte exportado a: " + fileName);
                           rsExport.close();
                       } catch (Exception e) {
                           System.out.println("Error al exportar reporte: " + e.getMessage());
                       }
                   }
                   break;
               case 5:
                   volver = true;
                   break;
               default:
                   System.out.println("Opción no válida. Intente de nuevo.");
           }
           
           stmt.close();
       }
   }
}
