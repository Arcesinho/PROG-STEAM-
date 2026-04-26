package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.ReseniaEntidad;
import org.adrian.modelo.form.ReseniaForm;

import java.util.Optional;

public interface IReseniaRepo extends ICrud<ReseniaEntidad, ReseniaForm, Long>{

    Optional<ReseniaEntidad> obtenerPorUsuarioYJuego(Long idUsuario, Long idJuego);
}
