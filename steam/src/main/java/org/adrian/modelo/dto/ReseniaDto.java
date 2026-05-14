package org.adrian.modelo.dto;


import org.adrian.modelo.enums.ESTADORESENIA;

import java.time.LocalDateTime;

/**
 * DTO de solo lectura con los datos de una reseña de usuario sobre un juego.
 *
 * @param id                identificador único de la reseña
 * @param idUsuario         identificador del usuario autor
 * @param idJuego           identificador del juego reseñado
 * @param recomendado       {@code true} si el usuario recomienda el juego
 * @param textoResenia      texto completo de la reseña
 * @param horasHastaResenia horas jugadas en el momento de escribir la reseña
 * @param fechaPublicacion  fecha y hora de publicación original
 * @param fechaUltimaEdicion fecha y hora de la última edición (puede ser {@code null})
 * @param estado            estado de visibilidad de la reseña ({@link ESTADORESENIA})
 */
public record ReseniaDto (Long id, Long idUsuario, Long idJuego, boolean recomendado,
                          String textoResenia, Double horasHastaResenia, LocalDateTime fechaPublicacion,
                          LocalDateTime fechaUltimaEdicion, ESTADORESENIA estado){}
