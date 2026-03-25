package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.entidad.ReseniaEntidad;
import org.adrian.modelo.form.ReseniaForm;
import org.adrian.repositorio.interfaces.IReseniaRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReseniaRepoImplementacionMemoria implements IReseniaRepo {

    private static final List<ReseniaEntidad> resenias = new ArrayList<>();
    private static Long idCounter = 1L;
    private final BibliotecaRepoImplementacionMemoria bibliotecaRepo = new BibliotecaRepoImplementacionMemoria();


    private LocalDateTime fechaPublicacion() {
        LocalDateTime.now();
        return LocalDateTime.now();
    }

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
}
