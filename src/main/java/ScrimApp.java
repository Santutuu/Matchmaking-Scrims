import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.api.Scrims_Valorant.Facade.ScrimFacade;
import com.api.Scrims_Valorant.Facade.UserFacade;
import com.api.Scrims_Valorant.Model.ConfiguracionScrim;
import com.api.Scrims_Valorant.Model.Division;
import com.api.Scrims_Valorant.Model.Scrim;
import com.api.Scrims_Valorant.Model.Usuario;
import com.api.Scrims_Valorant.Repository.ScrimRepository;
import com.api.Scrims_Valorant.Repository.UsuarioRepository;
import com.api.Scrims_Valorant.ScrimsValorantApplication;
import com.api.Scrims_Valorant.Strategy.EmparejamientoPorHistorial;
import com.api.Scrims_Valorant.Strategy.EmparejamientoPorLatencia;
import com.api.Scrims_Valorant.Strategy.EmparejamientoPorRango;
import com.api.Scrims_Valorant.Strategy.EstrategiaEmparejamiento;

public class ScrimApp {
    private static ScrimFacade scrimFacade;
    private static UserFacade userFacade;
    private static ScrimRepository scrimRepository;
    private static UsuarioRepository usuarioRepository;
    private static Scanner scanner = new Scanner(System.in);
    private static Usuario usuarioLogueado = null;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ScrimsValorantApplication.class, args);
        scrimFacade = context.getBean(ScrimFacade.class);
        userFacade = context.getBean(UserFacade.class);
        scrimRepository = context.getBean(ScrimRepository.class);
        usuarioRepository = context.getBean(UsuarioRepository.class);

        System.out.println("eScrims - Sistema de Gestion de Scrims");
        System.out.println("=======================================");

        while (true) {
            mostrarMenuInicial();
            int opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    if (iniciarSesion()) {
                        menuPrincipal();
                    }
                    break;
                case 2:
                    registrarUsuario();
                    break;
                case 3:
                    System.out.println("Hasta pronto!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }

            System.out.println("\n" + "=".repeat(50) + "\n");
            System.out.print("Presione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private static void mostrarMenuInicial() {
        System.out.println("\nMENU INICIAL");
        System.out.println("1. Iniciar Sesion");
        System.out.println("2. Registrar Usuario");
        System.out.println("3. Salir");
    }

    private static boolean iniciarSesion() {
        System.out.println("\nINICIO DE SESION");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        usuarioLogueado = userFacade.autenticarUsuario(username, password);

        if (usuarioLogueado != null) {
            System.out.println("Bienvenido, " + usuarioLogueado.getUsername() + "!");
            System.out.println("Tipo: " + (usuarioLogueado.puedeCrearScrim() ? "Creador" : "Jugador"));
            return true;
        } else {
            System.out.println("Credenciales incorrectas");
            return false;
        }
    }

    private static void registrarUsuario() {
        System.out.println("\nREGISTRO DE USUARIO");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Es creador? (s/n): ");
        String tipo = scanner.nextLine();
        boolean esCreador = tipo.equalsIgnoreCase("s");

        String rango = null;
        if (!esCreador) {
            rango = seleccionarRangoRegistro();
        }

        boolean exito = userFacade.registrarUsuario(username, email, password, esCreador, rango);

        if (exito) {
            System.out.println("Usuario registrado exitosamente!");
        } else {
            System.out.println("Error al registrar usuario");
        }
    }

    private static String seleccionarRangoRegistro() {
        Division[] divisiones = Division.values();

        System.out.println("Seleccione rango del jugador:");
        for (int i = 0; i < divisiones.length; i++) {
            System.out.println((i + 1) + ". " + divisiones[i].name());
        }

        while (true) {
            int opcion = leerEntero("Opcion de rango: ");
            if (opcion >= 1 && opcion <= divisiones.length) {
                return divisiones[opcion - 1].name();
            }
            System.out.println("Opcion invalida. Seleccione un numero entre 1 y " + divisiones.length + ".");
        }
    }

    private static void menuPrincipal() {
        while (usuarioLogueado != null) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    if (usuarioLogueado.puedeCrearScrim()) {
                        crearScrim();
                    } else {
                        System.out.println("No tienes permisos para crear scrims");
                    }
                    break;
                case 2:
                    listarScrims();
                    break;
                case 3:
                    buscarScrimPorId();
                    break;
                case 4:
                    if (!usuarioLogueado.puedeCrearScrim()) {
                        postularseAScrim();
                    } else {
                        System.out.println("Los creadores no pueden postularse a scrims");
                    }
                    break;
                case 5:
                    if (usuarioLogueado.puedeCrearScrim()) {
                        ejecutarEmparejamiento();
                    } else {
                        System.out.println("Solo los creadores pueden ejecutar emparejamiento");
                    }
                    break;
                case 6:
                    confirmarParticipacion();
                    break;
                case 7:
                    if (usuarioLogueado.puedeCrearScrim()) {
                        eliminarScrim();
                    } else {
                        System.out.println("No tienes permisos para eliminar scrims");
                    }
                    break;
                case 8:
                    gestionarPerfil();
                    break;
                case 9:
                    System.out.println("Cerrando sesion...");
                    usuarioLogueado = null;
                    return;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }

            System.out.println("\n" + "=".repeat(50) + "\n");
            if (usuarioLogueado != null) {
                System.out.print("Presione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\nMENU PRINCIPAL - Usuario: " + usuarioLogueado.getUsername());
        System.out.println("1. Crear Scrim" + (usuarioLogueado.puedeCrearScrim() ? "" : " (No disponible)"));
        System.out.println("2. Listar Scrims");
        System.out.println("3. Buscar Scrim por ID");
        System.out.println("4. Postularse a Scrim" + (usuarioLogueado.puedeCrearScrim() ? " (No disponible)" : ""));
        System.out.println("5. Ejecutar Emparejamiento" + (usuarioLogueado.puedeCrearScrim() ? "" : " (No disponible)"));
        System.out.println("6. Confirmar Participacion");
        System.out.println("7. Eliminar Scrim" + (usuarioLogueado.puedeCrearScrim() ? "" : " (No disponible)"));
        System.out.println("8. Gestionar Perfil");
        System.out.println("9. Cerrar Sesion");
    }

    private static void crearScrim() {
        System.out.println("\nCREAR NUEVA SCRIM");

        try {
            System.out.print("Region (ej: LATAM, NA, EU): ");
            String region = scanner.nextLine();

            System.out.print("Rango Minimo (HIERRO, PLATA, ORO, PLATINO, MAESTRO, GRANMAESTRO): ");
            String rangeMin = scanner.nextLine().toUpperCase();

            System.out.print("Rango Maximo (HIERRO, PLATA, ORO, PLATINO, MAESTRO, GRANMAESTRO): ");
            String rangeMax = scanner.nextLine().toUpperCase();

            System.out.print("Latencia maxima (ms): ");
            int latenciaMax = leerEntero("", 100);

            System.out.print("Duracion (minutos): ");
            int duration = leerEntero("", 60);

            System.out.print("Maximo de jugadores: ");
            int maxJugadores = leerEntero("", 10);

            LocalDateTime fechaHoraInicio = LocalDateTime.now().plusHours(1);
            System.out.println("Fecha/hora de inicio (por defecto: " + fechaHoraInicio + ")");
            System.out.print("Cambiar? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Ingrese fecha/hora (YYYY-MM-DDTHH:MM ej: 2024-01-15T20:30): ");
                String fechaInput = scanner.nextLine();
                try {
                    fechaHoraInicio = LocalDateTime.parse(fechaInput);
                } catch (Exception e) {
                    System.out.println("Formato invalido. Usando fecha por defecto.");
                }
            }

            System.out.println("\nEstrategia de emparejamiento:");
            System.out.println("1. Por Rango");
            System.out.println("2. Por Latencia");
            System.out.println("3. Por Historial");
            System.out.println("4. Sin estrategia");
            int estrOpc = leerEntero("Opcion: ", 4);

            EstrategiaEmparejamiento estrategia = null;
            switch (estrOpc) {
                case 1:
                    estrategia = new EmparejamientoPorRango();
                    break;
                case 2:
                    estrategia = new EmparejamientoPorLatencia();
                    break;
                case 3:
                    estrategia = new EmparejamientoPorHistorial();
                    break;
                default:
                    estrategia = null;
            }

            ConfiguracionScrim config = new ConfiguracionScrim(
                    region, rangeMin, rangeMax, latenciaMax,
                    fechaHoraInicio, duration, maxJugadores,
                    estrategia
            );

            System.out.println("\nCreando scrim...");
            Scrim scrimCreada = (Scrim) userFacade.crearScrim(usuarioLogueado.getIdUsuario(), config);

            if (scrimCreada != null) {
                System.out.println("Scrim creada exitosamente!");
                System.out.println("ID: " + scrimCreada.getIdScrim());
                System.out.println("Estado: " + scrimCreada.getEstadoNombre());
                System.out.println("Region: " + scrimCreada.getConfig().getRegion());
                System.out.println("Jugadores: " + scrimCreada.getConfig().getMaxJugadores());
                System.out.println("Inicio: " + scrimCreada.getConfig().getFechaHoraInicio());
                System.out.println("Rango: " + scrimCreada.getConfig().getRangeMin() + " - " + scrimCreada.getConfig().getRangeMax());
            } else {
                System.out.println("Error: No se pudo crear la scrim");
            }

        } catch (Exception e) {
            System.out.println("Error al crear scrim: " + e.getMessage());
        }
    }

    private static void listarScrims() {
        System.out.println("\nLISTA DE SCRIMS");

        List<Scrim> scrims = scrimRepository.obtenerTodas();

        if (scrims.isEmpty()) {
            System.out.println("No hay scrims creadas.");
            return;
        }

        System.out.println("Encontradas " + scrims.size() + " scrim(s):");
        System.out.println("-".repeat(100));

        for (Scrim scrim : scrims) {
            String estrategia = scrim.getConfig().getEstrategiaEmparejamiento() != null ?
                    scrim.getConfig().getEstrategiaEmparejamiento().getClass().getSimpleName() : "Sin estrategia";

            System.out.printf("ID: %-3d | %-6s | %-8s | %2d/%-2d | %-18s | %s | %s\n",
                    scrim.getIdScrim(),
                    scrim.getConfig().getRegion(),
                    scrim.getConfig().getRangeMin() + "-" + scrim.getConfig().getRangeMax(),
                    scrim.getPostulaciones() != null ? scrim.getPostulaciones().size() : 0,
                    scrim.getConfig().getMaxJugadores(),
                    scrim.getEstadoNombre(),
                    scrim.getConfig().getFechaHoraInicio().toString().substring(0, 16).replace("T", " "),
                    estrategia
            );
        }
    }

    private static void buscarScrimPorId() {
        System.out.println("\nBUSCAR SCRIM POR ID");

        int scrimId = leerEntero("Ingrese ID de la scrim: ");
        Scrim scrim = scrimRepository.buscarPorId(scrimId);

        if (scrim != null) {
            System.out.println("Scrim encontrada:");
            System.out.println("   ID: " + scrim.getIdScrim());
            System.out.println("   Estado: " + scrim.getEstadoNombre());
            System.out.println("   Region: " + scrim.getConfig().getRegion());
            System.out.println("   Rango: " + scrim.getConfig().getRangeMin() + " - " + scrim.getConfig().getRangeMax());
            System.out.println("   Jugadores: " + (scrim.getPostulaciones() != null ? scrim.getPostulaciones().size() : 0) +
                    "/" + scrim.getConfig().getMaxJugadores());
            System.out.println("   Inicio: " + scrim.getConfig().getFechaHoraInicio());
            System.out.println("   Duracion: " + scrim.getConfig().getDuration() + " min");
            System.out.println("   Latencia max: " + scrim.getConfig().getLatenciaMax() + "ms");
            System.out.println("   Estrategia: " +
                    (scrim.getConfig().getEstrategiaEmparejamiento() != null ?
                            scrim.getConfig().getEstrategiaEmparejamiento().getClass().getSimpleName() : "Sin estrategia"));
        } else {
            System.out.println("No se encontro scrim con ID: " + scrimId);
        }
    }

    private static void postularseAScrim() {
        System.out.println("\nPOSTULARSE A SCRIM");

        int scrimId = leerEntero("ID de la scrim: ");
        System.out.print("Rol deseado: ");
        String rolDeseado = scanner.nextLine();

        boolean exito = userFacade.postularseAScrim(scrimId, usuarioLogueado.getIdUsuario(), rolDeseado);

        if (exito) {
            System.out.println("Postulacion enviada exitosamente");
        } else {
            System.out.println("Error al postularse a la scrim");
        }
    }

    private static void ejecutarEmparejamiento() {
        System.out.println("\nEJECUTAR EMPAREJAMIENTO");

        int scrimId = leerEntero("ID de la scrim: ");
        Scrim scrim = scrimFacade.ejecutarEmparejamiento(scrimId);

        if (scrim != null) {
            System.out.println("Emparejamiento ejecutado para scrim ID: " + scrim.getIdScrim());
            System.out.println("Nuevo estado: " + scrim.getEstadoNombre());
        } else {
            System.out.println("Error en emparejamiento o scrim no encontrada");
        }
    }

    private static void confirmarParticipacion() {
        System.out.println("\nCONFIRMAR PARTICIPACION");

        int scrimId = leerEntero("ID de la scrim: ");
        boolean exito = userFacade.confirmarParticipacion(scrimId, usuarioLogueado.getIdUsuario());

        if (exito) {
            System.out.println("Confirmacion procesada exitosamente");
        } else {
            System.out.println("Error al confirmar participacion");
        }
    }

    private static void eliminarScrim() {
        System.out.println("\nELIMINAR SCRIM");

        int scrimId = leerEntero("ID de la scrim a eliminar: ");

        System.out.print("Esta seguro? (s/n): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("s")) {
            scrimRepository.eliminarScrim(scrimId);
        } else {
            System.out.println("Eliminacion cancelada");
        }
    }

    private static void gestionarPerfil() {
        System.out.println("\nPERFIL DE USUARIO");
        System.out.println("ID: " + usuarioLogueado.getIdUsuario());
        System.out.println("Username: " + usuarioLogueado.getUsername());
        System.out.println("Email: " + usuarioLogueado.getEmail());
        System.out.println("Tipo: " + (usuarioLogueado.puedeCrearScrim() ? "Creador" : "Jugador"));

        if (!usuarioLogueado.puedeCrearScrim()) {
            System.out.println("\nTus postulaciones activas:");
            // Aquí se podría implementar la lógica para mostrar las postulaciones del usuario
        }
    }

    private static int leerEntero(String mensaje) {
        return leerEntero(mensaje, -1);
    }

    private static int leerEntero(String mensaje, int valorPorDefecto) {
        System.out.print(mensaje);
        try {
            String input = scanner.nextLine();
            if (input.isEmpty() && valorPorDefecto != -1) {
                return valorPorDefecto;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (valorPorDefecto != -1) {
                System.out.println("Entrada invalida. Usando valor por defecto: " + valorPorDefecto);
                return valorPorDefecto;
            } else {
                System.out.println("Entrada invalida. Intente nuevamente.");
                return leerEntero(mensaje, valorPorDefecto);
            }
        }
    }
}