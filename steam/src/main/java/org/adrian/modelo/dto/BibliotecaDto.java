package org.adrian.modelo.dto;


import org.adrian.modelo.enums.ESTADOINSTALACIONBIBLIOTECA;

import java.time.LocalDateTime;

/**
 * DTO de solo lectura que representa la entrada de un juego en la biblioteca de un usuario.
 *
 * @param id                identificador único de la entrada
 * @param idUsuario         identificador del usuario propietario
 * @param idJuego           identificador del juego
 * @param usuarioDto        DTO del usuario propietario
 * @param JuegoDto          DTO del juego asociado
 * @param fechaAdquisicion  fecha y hora en que se añadió el juego a la biblioteca
 * @param horasJuego        total de horas jugadas (puede ser {@code null})
 * @param ultimaFechaJuego  fecha y hora de la última sesión de juego (puede ser {@code null})
 * @param estadoInstalacion estado de instalación del juego ({@link ESTADOINSTALACIONBIBLIOTECA})
 */
public record BibliotecaDto (Long id, Long idUsuario, Long idJuego, UsuarioDto usuarioDto, JuegoDto JuegoDto,
                             LocalDateTime fechaAdquisicion,
                             Double horasJuego, LocalDateTime ultimaFechaJuego,
                             ESTADOINSTALACIONBIBLIOTECA estadoInstalacion)  {}
