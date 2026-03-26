package com.api.Scrims_Valorant.Facade.Utils;

import org.springframework.stereotype.Component;

import com.api.Scrims_Valorant.Model.CreadorScrim;
import com.api.Scrims_Valorant.Model.Division;
import com.api.Scrims_Valorant.Model.Jugador;
import com.api.Scrims_Valorant.Model.Rango;
import com.api.Scrims_Valorant.Model.Usuario;
import com.api.Scrims_Valorant.Repository.UsuarioRepository;

@Component
public class UserUtils {
    private UsuarioRepository usuarioRepository;

    public UserUtils(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario validarCredenciales(String username, String password) {
        // Implementar lógica de autenticación
        // Por ahora, búsqueda simple
        Usuario usuario = usuarioRepository.buscarPorUsername(username);
        if (usuario != null && usuario.getPasswordHash().equals(password)) {
            return usuario;
        }
        return null;
    }

    public boolean registrarNuevoUsuario(String username, String email, String password, boolean esCreador) {
        return registrarNuevoUsuario(username, email, password, esCreador, null);
    }

    public boolean registrarNuevoUsuario(String username, String email, String password, boolean esCreador, String rangoSeleccionado) {
        // Verificar si el usuario ya existe
        if (usuarioRepository.buscarPorUsername(username) != null) {
            System.out.println("El nombre de usuario ya existe");
            return false;
        }

        // Generar nuevo ID
        // Crear usuario según el tipo
        Usuario nuevoUsuario;
        if (esCreador) {
            nuevoUsuario = new CreadorScrim(username, email, password);
        } else {
            Division division = Division.HIERRO;
            if (rangoSeleccionado != null && !rangoSeleccionado.isBlank()) {
                try {
                    division = Division.valueOf(rangoSeleccionado.trim().toUpperCase());
                } catch (IllegalArgumentException ex) {
                    System.out.println("Rango invalido. Se asigna HIERRO por defecto.");
                }
            }
            nuevoUsuario = new Jugador(username, email, password, new Rango(division));
        }

        // Guardar usuario
        return usuarioRepository.guardarUsuario(nuevoUsuario);
    }
}