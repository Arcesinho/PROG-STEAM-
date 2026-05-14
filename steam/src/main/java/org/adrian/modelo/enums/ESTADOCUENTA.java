package org.adrian.modelo.enums;


/**
 * Estado de la cuenta de un usuario en la plataforma.
 * <ul>
 *   <li>{@link #ACTIVA} – cuenta operativa; el usuario puede comprar, reseñar y jugar.</li>
 *   <li>{@link #SUSPENDIDA} – cuenta temporalmente restringida.</li>
 *   <li>{@link #BANEADA} – cuenta permanentemente inhabilitada.</li>
 * </ul>
 */
public enum ESTADOCUENTA{ACTIVA, SUSPENDIDA, BANEADA}
