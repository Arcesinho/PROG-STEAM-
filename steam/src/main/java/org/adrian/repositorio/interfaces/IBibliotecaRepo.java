package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.form.BibliotecaForm;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Repositorio de entradas de biblioteca de usuario.
 * Extiende las operaciones CRUD básicas con consultas específicas por usuario y juego.
 */
public interface IBibliotecaRepo extends ICrud<BibliotecaEntidad, BibliotecaForm, Long> {

    /**
     * Obtiene la entrada de biblioteca de un usuario para un juego concreto,
     * usada principalmente para recuperar las horas jugadas.
     *
     * @param idUsuario identificador del usuario
     * @param idJuego   identificador del juego
     * @return Optional con la entrada encontrada, o vacío si no existe
     */
    Optional<BibliotecaEntidad> obtenerHoras(Long idUsuario, Long idJuego);

    /**
     * Obtiene la entrada de biblioteca que corresponde a la combinación usuario-juego.
     *
     * @param idUsuario identificador del usuario
     * @param idJuego   identificador del juego
     * @return Optional con la entrada encontrada, o vacío si no existe
     */
    Optional<BibliotecaEntidad> obtenerPorIdUsuarioIdJuego(Long idUsuario, Long idJuego);

}
