package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.form.BibliotecaForm;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface IBibliotecaRepo extends ICrud<BibliotecaEntidad, BibliotecaForm, Long> {

    public Optional<BibliotecaEntidad> obtenerHoras(Long idUsuario, Long idJuego);

    public Optional<BibliotecaEntidad> obtenerPorIdUsuarioIdJuego(Long idUsuario, Long idJuego);

}
