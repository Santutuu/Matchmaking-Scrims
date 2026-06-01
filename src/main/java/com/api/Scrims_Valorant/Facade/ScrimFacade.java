package com.api.Scrims_Valorant.Facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.api.Scrims_Valorant.Model.ConfiguracionScrim;
import com.api.Scrims_Valorant.Model.Emparejador;
import com.api.Scrims_Valorant.Model.Estadistica;
import com.api.Scrims_Valorant.Model.EstadoScrimTipo;
import com.api.Scrims_Valorant.Model.Postulacion;
import com.api.Scrims_Valorant.Model.Scrim;
import com.api.Scrims_Valorant.Model.Usuario;
import com.api.Scrims_Valorant.Repository.PostulacionRepository;
import com.api.Scrims_Valorant.Repository.ScrimRepository;
import com.api.Scrims_Valorant.Repository.UsuarioRepository;
import com.api.Scrims_Valorant.State.ContextoScrim;
import com.api.Scrims_Valorant.Strategy.EstrategiaEmparejamiento;

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
        if (configuration == null || !configuration.esConfiguracionValida()) {
            throw new RuntimeException("Configuración de scrim inválida");
        }

        if (!usuarioRepository.esUsuarioCreador(organizationId)) {
            throw new RuntimeException("El usuario no tiene permisos para crear scrims");
        }

        Usuario creador = usuarioRepository.buscarPorId(organizationId);
        if (creador == null) {
            throw new RuntimeException("Usuario creador no encontrado");
        }

        Scrim nuevaScrim = new Scrim(configuration, creador);
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

        if (scrim.getEstadoActual() != EstadoScrimTipo.BUSCANDO) {
            System.out.println("❌ La scrim no está aceptando postulaciones");
            return false;
        }

        if (scrim.cuposCompletosConAceptadas()) {
            System.out.println("❌ Scrim llena - no hay cupos disponibles");
            return false;
        }

        List<Postulacion> postulacionesExistentes = postulacionRepository.buscarPorScrim(scrimId);
        boolean yaPostulado = postulacionesExistentes.stream()
                .anyMatch(p -> p.getUsuario() != null && p.getUsuario().getIdUsuario() == usuarioId);

        if (yaPostulado) {
            System.out.println("❌ Usuario ya está postulado a esta scrim");
            return false;
        }

        boolean cumpleRequisitos = cumpleRequisitosScrim(scrim, usuario);
        if (!cumpleRequisitos) {
            System.out.println("❌ El usuario no cumple los requisitos de la scrim");
            return false;
        }

        Postulacion postulacion = new Postulacion();
        postulacion.setRolDeseado(rolDeseado);
        postulacion.setFecha(LocalDateTime.now());
        postulacion.setUsuario(usuario);
        postulacion.setScrim(scrim);
        postulacion.aceptada();

        scrim.agregarPostulacion(postulacion);
        scrimRepository.guardarScrim(scrim);

        System.out.println("✅ Postulación aceptada para usuario: " + usuario.getUsername());

        if (scrim.cuposCompletosConAceptadas()) {
            System.out.println("🚀 Cupos completos con postulaciones aceptadas. Ejecutando emparejamiento...");
            ejecutarEmparejamiento(scrimId);
        }

        return true;
    }

    private boolean cumpleRequisitosScrim(Scrim scrim, Usuario usuario) {
        if (scrim == null || scrim.getConfig() == null) {
            return false;
        }

        EstrategiaEmparejamiento estrategia = scrim.getConfig().getEstrategiaEmparejamiento();
        if (estrategia == null) {
            return false;
        }

        return estrategia.validarElegibilidad(usuario, scrim);
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
        if (scrim == null) {
            System.out.println("❌ Scrim no encontrada: " + scrimId);
            return null;
        }

        if (scrim.getEstadoActual() != EstadoScrimTipo.BUSCANDO) {
            System.out.println("⚠️ Emparejamiento omitido: la scrim no está en estado BUSCANDO");
            return scrim;
        }

        if (!scrim.cuposCompletosConAceptadas()) {
            System.out.println("⚠️ Emparejamiento omitido: no se completaron los cupos con aceptadas");
            return scrim;
        }

        EstrategiaEmparejamiento estrategia = scrim.getConfig() != null
                ? scrim.getConfig().getEstrategiaEmparejamiento()
                : null;

        if (estrategia == null) {
            System.out.println("⚠️ Emparejamiento omitido: la scrim no tiene estrategia configurada");
            return scrim;
        }

        Emparejador emparejador = new Emparejador(estrategia);
        List<Postulacion> seleccionadas = emparejador.seleccionarPostulaciones(scrim);

        if (seleccionadas.size() < scrim.getConfig().getMaxJugadores()) {
            System.out.println("⚠️ Emparejamiento omitido: no hay suficientes postulaciones elegibles");
            return scrim;
        }

        ContextoScrim contexto = new ContextoScrim(scrim);
        contexto.armarLobby();

        scrimRepository.guardarScrim(scrim);
        System.out.println("✅ Emparejamiento ejecutado. Nuevo estado: " + scrim.getEstadoNombre());

        return scrim;
    }

    public boolean confirmarParticipacion(int scrimId, int usuarioId) {
        System.out.println("ℹ️ Confirmación no se usa en este flujo actual");
        return false;
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
        System.out.println("📊 Registrando estadísticas...");
    }

    public void Reporte(Object request) {
        System.out.println("📋 Generando reporte...");
    }

    public Scrim obtenerScrimConPostulaciones(int scrimId) {
        Scrim scrim = scrimRepository.buscarPorId(scrimId);
        if (scrim != null) {
            List<Postulacion> postulaciones = postulacionRepository.buscarPorScrim(scrimId);
            scrim.setPostulaciones(postulaciones);
        }
        return scrim;
    }
}