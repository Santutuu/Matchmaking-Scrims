package com.api.Scrims_Valorant.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.Scrims_Valorant.Model.CreadorScrim;
import com.api.Scrims_Valorant.Model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    default Usuario buscarPorId(int userId) {
        return findById((long) userId).orElse(null);
    }

    default Usuario buscarPorUsername(String username) {
        return findByUsername(username).orElse(null);
    }

    default boolean guardarUsuario(Usuario usuario) {
        save(usuario);
        return true;
    }

    default boolean esUsuarioCreador(int userId) {
        Usuario usuario = buscarPorId(userId);
        return usuario instanceof CreadorScrim;
    }

}