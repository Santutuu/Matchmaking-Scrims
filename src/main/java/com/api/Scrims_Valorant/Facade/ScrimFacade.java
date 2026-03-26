package com.api.Scrims_Valorant.Facade;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.api.Scrims_Valorant.Model.ConfiguracionScrim;
import com.api.Scrims_Valorant.Model.Confirmacion;
import com.api.Scrims_Valorant.Model.Estadistica;
import com.api.Scrims_Valorant.Model.Jugador;
import com.api.Scrims_Valorant.Model.Postulacion;
import com.api.Scrims_Valorant.Model.Scrim;
import com.api.Scrims_Valorant.Model.Usuario;
import com.api.Scrims_Valorant.Repository.PostulacionRepository;
import com.api.Scrims_Valorant.Repository.ScrimRepository;
import com.api.Scrims_Valorant.Repository.UsuarioRepository;

@Component
public class ScrimFacade {
    private final ScrimRepository scrimRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostulacionRepository postulacionRepository;

    public ScrimFacade(ScrimRepository scrimRepository,
                      UsuarioRepository usuarioRepository,
                      PostulacionRepository postulacionRepository) {
        this.scrimRepository = scrimRepository;
        this.usuarioRepository = usuarioRepository;
        this.postulacionRepository = postulacionRepository;
    }

    public Scrim CrearScrim(int organizationId, ConfiguracionScrim configuration) {
        // 1. Validar configuración
        if (!configuration.esConfiguracionValida()) {
            throw new RuntimeException("Configuración de scrim inválida");
        }

        // 2. Validar que el usuario es creador
        if (!usuarioRepository.esUsuarioCreador(organizationId)) {
            throw new RuntimeException("El usuario no tiene permisos para crear scrims");
        }

        Usuario creador = usuarioRepository.buscarPorId(organizationId);
        if (creador == null) {
            throw new RuntimeException("Usuario creador no encontrado");
        }

        // 3. Crear scrim con estado inicial
        Scrim nuevaScrim = new Scrim(configuration, creador);

        // 4. Guardar en repositorio
        scrimRepository.guardarScrim(nuevaScrim);

        System.out.println("✅ Scrim creada con ID: " + nuevaScrim.getIdScrim());
        return nuevaScrim;
    }

    public boolean postularseAScrim(int scrimId, int usuarioId, String rolDeseado) {
        Scrim scrim = scrimRepository.buscarPorId(scrimId);
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);

        if (scrim == null || usuario == null) {
            System.out.println("❌ Scrim o usuario no encontrado");
            return false;
        }

        if (scrim.cuposLlenos()) {
            System.out.println("❌ Scrim llena - no hay cupos disponibles");
            return false;
        }

        // Verificar si ya está postulado
        List<Postulacion> postulacionesExistentes = postulacionRepository.buscarPorScrim(scrimId);
        boolean yaPostulado = postulacionesExistentes.stream()
                .anyMatch(p -> p.getUsuario().getIdUsuario() == usuarioId);

        if (yaPostulado) {
            System.out.println("❌ Usuario ya está postulado a esta scrim");
            return false;
        }

        // Crear postulación
        Postulacion postulacion = new Postulacion();
        postulacion.setRolDeseado(rolDeseado);
        postulacion.setFecha(java.time.LocalDateTime.now());
        postulacion.setUsuario(usuario);
        postulacion.setScrim(scrim);
        boolean cumpleRequisitos = cumpleRequisitosScrim(scrim, usuario);
        if (cumpleRequisitos) {
            postulacion.aceptada();
        } else {
            postulacion.rechazada();
        }

        scrim.agregarPostulacion(postulacion);

        if (cumpleRequisitos) {
            boolean yaConfirmado = scrim.getConfirmaciones().stream()
                    .anyMatch(c -> c.getUsuario() != null && c.getUsuario().getIdUsuario() == usuarioId);

            if (!yaConfirmado) {
                Confirmacion confirmacion = new Confirmacion();
                confirmacion.setConfirmado(true);
                confirmacion.setFecha(LocalDate.now());
                confirmacion.setUsuario(usuario);
                confirmacion.setScrim(scrim);
                scrim.getConfirmaciones().add(confirmacion);
            }

            if (scrim.todasConfirmaciones()) {
                scrim.setEstadoNombre("CONFIRMADO");
            }
        }

        scrimRepository.guardarScrim(scrim);

        if (cumpleRequisitos) {
            System.out.println("✅ Postulación aceptada y confirmación creada para usuario: " + usuario.getUsername());
            return true;
        }

        System.out.println("❌ Postulación rechazada: el usuario no cumple requisitos de la scrim");
        return false;
    }

    private boolean cumpleRequisitosScrim(Scrim scrim, Usuario usuario) {
        if (!(usuario instanceof Jugador jugador)) {
            return false;
        }

        if (jugador.getRango() == null || jugador.getRango().getDivision() == null) {
            return false;
        }

        ConfiguracionScrim config = scrim.getConfig();
        if (config == null) {
            return false;
        }

        return config.esRangoValido(jugador.getRango().getDivision().name());
    }

    public List<Postulacion> obtenerPostulacionesDeScrim(int scrimId) {
        return postulacionRepository.buscarPorScrim(scrimId);
    }

    public List<Postulacion> obtenerPostulacionesDeUsuario(int usuarioId) {
        return postulacionRepository.buscarPorUsuario(usuarioId);
    }

    public void aceptarPostulacion(int postulacionId) {
        Postulacion postulacion = postulacionRepository.buscarPorId(postulacionId);
        if (postulacion != null) {
            postulacion.aceptada();
            postulacionRepository.guardarPostulacion(postulacion);
            System.out.println("✅ Postulación aceptada: " + postulacionId);
        } else {
            System.out.println("❌ Postulación no encontrada: " + postulacionId);
        }
    }

    public void rechazarPostulacion(int postulacionId) {
        Postulacion postulacion = postulacionRepository.buscarPorId(postulacionId);
        if (postulacion != null) {
            postulacion.rechazada();
            postulacionRepository.guardarPostulacion(postulacion);
            System.out.println("❌ Postulación rechazada: " + postulacionId);
        } else {
            System.out.println("❌ Postulación no encontrada: " + postulacionId);
        }
    }

    public Scrim ejecutarEmparejamiento(int scrimId) {
        Scrim scrim = scrimRepository.buscarPorId(scrimId);
        if (scrim != null && scrim.getConfig().getEstrategiaEmparejamiento() != null) {
            // Aquí iría la lógica de emparejamiento
            scrim.setEstadoNombre("LobbyArmado");
            scrimRepository.guardarScrim(scrim);
            System.out.println("🎯 Emparejamiento ejecutado para scrim: " + scrimId);
        }
        return scrim;
    }

    public boolean confirmarParticipacion(int scrimId, int usuarioId) {
        try {
            Scrim scrim = scrimRepository.buscarPorId(scrimId);
            if (scrim != null) {
                // Buscar postulación del usuario
                List<Postulacion> postulaciones = postulacionRepository.buscarPorScrim(scrimId);
                Postulacion postulacionUsuario = postulaciones.stream()
                        .filter(p -> p.getUsuario().getIdUsuario() == usuarioId)
                        .findFirst()
                        .orElse(null);

                if (postulacionUsuario != null && postulacionUsuario.isAceptada()) {
                    // Lógica de confirmación
                    // Aquí podrías crear una confirmación o marcar la postulación como confirmada

                    // Verificar si todos han confirmado
                    long confirmadosCount = postulaciones.stream()
                            .filter(Postulacion::isAceptada)
                            .count();

                    if (confirmadosCount >= scrim.getConfig().getMaxJugadores()) {
                        scrim.setEstadoNombre("Confirmado");
                        scrimRepository.guardarScrim(scrim);
                        System.out.println("✅ Todos confirmados - Scrim lista para iniciar");
                    }
                    return true;
                } else {
                    System.out.println("❌ Usuario no tiene postulación aceptada");
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("❌ Error confirmando participación: " + e.getMessage());
            return false;
        }
    }

    public void finalizarScrim(int scrimId, Estadistica estadisticas) {
        Scrim scrim = scrimRepository.buscarPorId(scrimId);
        if (scrim != null) {
            scrim.setEstadoNombre("Finalizado");
            scrimRepository.guardarScrim(scrim);
            System.out.println("🏁 Scrim finalizada: " + scrimId);
        } else {
            System.out.println("❌ Scrim no encontrada: " + scrimId);
        }
    }

    public void cancelarScrim(int scrimId, int usuarioId) {
        Scrim scrim = scrimRepository.buscarPorId(scrimId);
        if (scrim != null) {
            scrim.setEstadoNombre("Cancelado");
            scrimRepository.guardarScrim(scrim);

            // Opcional: también cancelar todas las postulaciones
            List<Postulacion> postulaciones = postulacionRepository.buscarPorScrim(scrimId);
            for (Postulacion postulacion : postulaciones) {
                postulacion.rechazada();
                postulacionRepository.guardarPostulacion(postulacion);
            }

            System.out.println("🛑 Scrim cancelada: " + scrimId);
        } else {
            System.out.println("❌ Scrim no encontrada: " + scrimId);
        }
    }

    public void RegistrarEstadisticas(Object request) {
        // Implementación pendiente
        System.out.println("📊 Registrando estadísticas...");
    }

    public void Reporte(Object request) {
        // Implementación pendiente
        System.out.println("📋 Generando reporte...");
    }

    // Método adicional para obtener información de una scrim con sus postulaciones
    public Scrim obtenerScrimConPostulaciones(int scrimId) {
        Scrim scrim = scrimRepository.buscarPorId(scrimId);
        if (scrim != null) {
            List<Postulacion> postulaciones = postulacionRepository.buscarPorScrim(scrimId);
            scrim.setPostulaciones(postulaciones);
        }
        return scrim;
    }
}