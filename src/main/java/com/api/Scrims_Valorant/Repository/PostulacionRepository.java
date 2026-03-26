package com.api.Scrims_Valorant.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.Scrims_Valorant.Model.Postulacion;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    List<Postulacion> findByScrim_IdScrim(Long scrimId);

    List<Postulacion> findByUsuario_IdUsuario(Long usuarioId);

    default void guardarPostulacion(Postulacion postulacion) {
        save(postulacion);
    }

    default Postulacion buscarPorId(int postulacionId) {
        return findById((long) postulacionId).orElse(null);
    }

    default List<Postulacion> buscarPorScrim(int scrimId) {
        return findByScrim_IdScrim((long) scrimId);
    }

    default List<Postulacion> buscarPorUsuario(int usuarioId) {
        return findByUsuario_IdUsuario((long) usuarioId);
    }

    default List<Postulacion> obtenerTodas() {
        return findAll();
    }

    default void eliminarPostulacion(int postulacionId) {
        deleteById((long) postulacionId);
    }

    // Compatibilidad con flujo legado que generaba IDs manualmente
    default int generarNuevoId() {
        return findAll().stream()
                .mapToInt(Postulacion::getIdPostulacion)
                .max()
                .orElse(0) + 1;
    }
}
