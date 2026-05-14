package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.entidad.ReseniaEntidad;
import org.adrian.modelo.form.ReseniaForm;
import org.adrian.repositorio.interfaces.IReseniaRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de reseñas.
 * Almacena las entidades en una lista estática y genera identificadores secuenciales.
 * Al crear o actualizar una reseña, captura automáticamente las horas jugadas del usuario
 * consultando el repositorio de biblioteca.
 * Esta implementación es útil para pruebas y prototipos; no persiste datos entre ejecuciones.
 */
public class ReseniaRepoImplementacionMemoria implements IReseniaRepo {

    private static final List<ReseniaEntidad> resenias = new ArrayList<>();
    private static Long idCounter = 1L;
    private final BibliotecaRepoImplementacionMemoria bibliotecaRepo = new BibliotecaRepoImplementacionMemoria();


    /** @return la fecha y hora actuales usada como fecha de publicación */
    private LocalDateTime fechaPublicacion() {
        LocalDateTime.now();
        return LocalDateTime.now();
    }

    /** @return la fecha y hora actuales usada como fecha de última edición */
    private LocalDateTime fechaUltimaEdicion() {
        LocalDateTime.now();
        return LocalDateTime.now();
    }


    @Override
    public Optional<ReseniaEntidad> crear(ReseniaForm form) {

        Double horasEnEsteMomento = bibliotecaRepo.obtenerHoras(form.idUsuario(), form.idJuego())
                .map(BibliotecaEntidad::getHorasJuego)
                .orElse(0.0);

        var resenia = new ReseniaEntidad(idCounter++, form.idUsuario(), form.idJuego(), form.recomendado(), form.textoResenia(),  horasEnEsteMomento, fechaPublicacion(), fechaUltimaEdicion(), form.estado());
        resenias.add(resenia);

        return Optional.of(resenia);
    }

    @Override
    public Optional<ReseniaEntidad> obtenerPorId(Long id) {
        return resenias.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ReseniaEntidad> obtenerTodos() {
        return new ArrayList<>(resenias);
    }

    @Override
    public Optional<ReseniaEntidad> actualizar(Long id, ReseniaForm form) {
        var reseniaOpt = obtenerPorId(id);
        if (reseniaOpt.isEmpty()) {
            throw new IllegalArgumentException("Reseña no encontrado");
        }
        Double horasActualizadas = bibliotecaRepo.obtenerHoras(form.idUsuario(), form.idJuego())
                .map(BibliotecaEntidad::getHorasJuego)
                .orElse(reseniaOpt.get().getHorasHastaResenia());

        var reseniaActualizada = new ReseniaEntidad(id,  form.idUsuario(), form.idJuego(), form.recomendado(), form.textoResenia(),  horasActualizadas, fechaPublicacion(), fechaUltimaEdicion(), form.estado());
        resenias.removeIf(u -> u.getId().equals(id));
        resenias.add(reseniaActualizada);

        return Optional.of(reseniaActualizada);
    }

    @Override
    public boolean eliminar(Long id) {
        return resenias.removeIf(u -> u.getId().equals(id));
    }

    /**
     * Busca la reseña de un usuario sobre un juego concreto.
     *
     * @param idUsuario identificador del usuario autor
     * @param idJuego   identificador del juego reseñado
     * @return Optional con la reseña encontrada, o vacío si no existe
     */
    @Override
    public Optional<ReseniaEntidad> obtenerPorUsuarioYJuego(Long idUsuario, Long idJuego) {
        return resenias.stream()
                .filter(r -> r.getIdUsuario().equals(idUsuario) && r.getIdJuego().equals(idJuego))
                .findFirst();
    }
}
