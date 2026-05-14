package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.JuegoEntidad;
import org.adrian.modelo.form.JuegoForm;
import org.adrian.repositorio.interfaces.IJuegoRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de juegos.
 * Almacena las entidades en una lista estática y genera identificadores secuenciales.
 * Esta implementación es útil para pruebas y prototipos; no persiste datos entre ejecuciones.
 */
public class JuegoRepoImplementacionMemoria implements IJuegoRepo {

    private static final List<JuegoEntidad> juegos = new ArrayList<>();
    private static Long idCounter = 1L;


    @Override
    public Optional<JuegoEntidad> crear(JuegoForm form) {

        var juego = new JuegoEntidad(idCounter++, form.tituloJuego(), form.descripcion().orElse(null), form.desarrollador(), form.fechaLanzamiento(), form.precioBase(), form.descuentoActual().orElse(null), form.idiomas().orElse(null), form.estado(), form.pegi(), form.categoria());
        juegos.add(juego);

        return Optional.of(juego);
    }



    @Override
    public Optional<JuegoEntidad> obtenerPorId(Long id) {
        return juegos.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<JuegoEntidad> obtenerTodos() {
        return new ArrayList<>(juegos);
    }

    @Override
    public Optional<JuegoEntidad> actualizar(Long id, JuegoForm form) {
        var juegoOpt = obtenerPorId(id);
        if (juegoOpt.isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado");
        }

        var juegoActualizado = new JuegoEntidad(id, form.tituloJuego(), form.descripcion().orElse(null), form.desarrollador(), form.fechaLanzamiento(), form.precioBase(), form.descuentoActual().orElse(null), form.idiomas().orElse(null), form.estado(), form.pegi(), form.categoria());
        juegos.removeIf(u -> u.getId().equals(id));
        juegos.add(juegoActualizado);

        return Optional.of(juegoActualizado);
    }

    @Override
    public boolean eliminar(Long id) {
        return juegos.removeIf(u -> u.getId().equals(id));
    }
}
