package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.ReseniaEntidad;
import org.adrian.modelo.form.ReseniaForm;

import java.util.Optional;

/**
 * Repositorio de reseñas de juegos.
 * Extiende las operaciones CRUD básicas con búsqueda por usuario y juego.
 */
public interface IReseniaRepo extends ICrud<ReseniaEntidad, ReseniaForm, Long>{

    /**
     * Busca la reseña que un usuario ha escrito sobre un juego concreto.
     *
     * @param idUsuario identificador del usuario autor
     * @param idJuego   identificador del juego reseñado
     * @return Optional con la reseña encontrada, o vacío si no existe
     */
    Optional<ReseniaEntidad> obtenerPorUsuarioYJuego(Long idUsuario, Long idJuego);
}
