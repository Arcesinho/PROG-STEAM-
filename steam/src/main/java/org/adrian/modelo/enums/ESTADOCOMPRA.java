package org.adrian.modelo.enums;

/**
 * Estado del ciclo de vida de una transacción de compra.
 * <ul>
 *   <li>{@link #PENDIENTE} – compra iniciada pero el pago aún no se ha procesado.</li>
 *   <li>{@link #COMPLETADA} – pago procesado correctamente.</li>
 *   <li>{@link #REEMBOLSADA} – se ha realizado el reembolso al usuario.</li>
 * </ul>
 */
public enum ESTADOCOMPRA{PENDIENTE, COMPLETADA, REEMBOLSADA}

