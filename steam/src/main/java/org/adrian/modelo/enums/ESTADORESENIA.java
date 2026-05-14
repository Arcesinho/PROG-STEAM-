package org.adrian.modelo.enums;

/**
 * Estado de visibilidad de una reseña.
 * <ul>
 *   <li>{@link #PUBLICADA} – visible para todos los usuarios.</li>
 *   <li>{@link #OCULTA} – ocultada por el autor; no aparece en el listado público.</li>
 *   <li>{@link #ELIMINADA} – marcada como eliminada; no se muestra ni se puede editar.</li>
 * </ul>
 */
public enum ESTADORESENIA{PUBLICADA, OCULTA, ELIMINADA}
