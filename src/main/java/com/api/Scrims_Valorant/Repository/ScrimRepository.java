package com.api.Scrims_Valorant.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.Scrims_Valorant.Model.Scrim;

@Repository
public interface ScrimRepository extends JpaRepository<Scrim, Long> {

    default void guardarScrim(Scrim scrim) {
        save(scrim);
    }

    default Scrim buscarPorId(int scrimId) {
        return findById((long) scrimId).orElse(null);
    }

    default List<Scrim> obtenerTodas() {
        return findAll();
    }

    default void eliminarScrim(int scrimId) {
        deleteById((long) scrimId);
    }
}
