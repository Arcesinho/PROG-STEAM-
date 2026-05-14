package org.adrian.modelo.enums;

/**
 * Estado de disponibilidad de un juego en el catálogo.
 * <ul>
 *   <li>{@link #DISPONIBLE} – el juego puede comprarse y jugarse sin restricciones.</li>
 *   <li>{@link #PREVENTA} – el juego puede reservarse pero aún no ha sido lanzado.</li>
 *   <li>{@link #ACCESO_ANTICIPADO} – disponible en fase de acceso anticipado (Early Access).</li>
 *   <li>{@link #NO_DISPONIBLE} – el juego no puede adquirirse actualmente.</li>
 * </ul>
 */
public enum ESTADOJUEGO{DISPONIBLE, PREVENTA, ACCESO_ANTICIPADO, NO_DISPONIBLE}

