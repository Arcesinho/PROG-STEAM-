package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.form.BibliotecaForm;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BibliotecaRepoImplementacionMemoria implements IBibliotecaRepo {

    private static final List<BibliotecaEntidad> bibliotecas = new ArrayList<>();
    private static Long idCounter = 1L;


    @Override
    public Optional<BibliotecaEntidad> crear(BibliotecaForm form) {

        var biblioteca = new BibliotecaEntidad(idCounter++, form.idUsuario(), form.idJuego(), form.fechaAdquisicion(), form.horasJuego(), form.ultimaFechaJuego().orElse(null), form.estadoInstalacion());
        bibliotecas.add(biblioteca);

        return Optional.of(biblioteca);
    }

    @Override
    public Optional<BibliotecaEntidad> obtenerPorId(Long id) {
        return bibliotecas.stream()
                .filter(u -> u.id().equals(id))
                .findFirst();
    }

    @Override
    public List<BibliotecaEntidad> obtenerTodos() {
        return new ArrayList<>(bibliotecas);
    }

    @Override
    public Optional<BibliotecaEntidad> actualizar(Long id, BibliotecaForm form) {
        var bibliotecaOpt = obtenerPorId(id);
        if (bibliotecaOpt.isEmpty()) {
            throw new IllegalArgumentException("Biblioteca no encontrado");
        }

        var bibliotecaActualizada = new BibliotecaEntidad(id, form.idUsuario(), form.idJuego(), form.fechaAdquisicion(), form.horasJuego(), form.ultimaFechaJuego().orElse(null), form.estadoInstalacion());
        bibliotecas.removeIf(u -> u.id().equals(id));
        bibliotecas.add(bibliotecaActualizada);

        return Optional.of(bibliotecaActualizada);
    }

    @Override
    public Optional<BibliotecaEntidad> obtenerHoras(Long idUsuario, Long idJuego) {
        return bibliotecas.stream()
                .filter(b -> b.idUsuario().equals(idUsuario) && b.idJuego().equals(idJuego))
                .findFirst();
    }

    @Override
    public boolean eliminar(Long id) {
        return bibliotecas.removeIf(u -> u.id().equals(id));
    }
}
