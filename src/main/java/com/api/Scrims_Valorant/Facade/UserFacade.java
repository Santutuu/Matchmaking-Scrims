package com.api.Scrims_Valorant.Facade;

import org.springframework.stereotype.Component;

import com.api.Scrims_Valorant.Facade.Utils.UserUtils;
import com.api.Scrims_Valorant.Model.ConfiguracionScrim;
import com.api.Scrims_Valorant.Model.Scrim;
import com.api.Scrims_Valorant.Model.Usuario;
import com.api.Scrims_Valorant.Repository.UsuarioRepository;

@Component
public class UserFacade {
    private UsuarioRepository usuarioRepository;
    private ScrimFacade scrimFacade;
    private UserUtils userUtils;

    public UserFacade(UsuarioRepository usuarioRepository, ScrimFacade scrimFacade, UserUtils userUtils) {
        this.usuarioRepository = usuarioRepository;
        this.scrimFacade = scrimFacade;
        this.userUtils = userUtils;
    }

    // Autenticación
    public Usuario autenticarUsuario(String username, String password) {
        return userUtils.validarCredenciales(username, password);
    }

    public boolean registrarUsuario(String username, String email, String password, boolean esCreador) {
        return registrarUsuario(username, email, password, esCreador, null);
    }

    public boolean registrarUsuario(String username, String email, String password, boolean esCreador, String rango) {
        return userUtils.registrarNuevoUsuario(username, email, password, esCreador, rango);
    }

    // Operaciones de usuario
    public Usuario obtenerUsuarioPorId(int userId) {
        return usuarioRepository.buscarPorId(userId);
    }

    public boolean puedeCrearScrim(int userId) {
        return usuarioRepository.esUsuarioCreador(userId);
    }

    // Operaciones de scrim para usuarios
    public boolean postularseAScrim(int scrimId, int usuarioId, String rolDeseado) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            System.out.println("Usuario no encontrado");
            return false;
        }

        if (usuario.puedeCrearScrim()) {
            System.out.println("Los creadores no pueden postularse a scrims");
            return false;
        }

        return scrimFacade.postularseAScrim(scrimId, usuarioId, rolDeseado);
    }

    public boolean confirmarParticipacion(int scrimId, int usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            System.out.println("Usuario no encontrado");
            return false;
        }

        return scrimFacade.confirmarParticipacion(scrimId, usuarioId);
    }

    // Crear scrim (solo para creadores)
    public Scrim crearScrim(int usuarioCreadorId, ConfiguracionScrim configuracionScrim) {
        if (!puedeCrearScrim(usuarioCreadorId)) {
            System.out.println("El usuario no tiene permisos para crear scrims");
            return null;
        }

        return scrimFacade.CrearScrim(usuarioCreadorId, configuracionScrim);
    }
}