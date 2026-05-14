package org.adrian.modelo.enums;

/**
 * Método de pago utilizado en una compra.
 * <ul>
 *   <li>{@link #CARTERA_STEAM} – descuenta el importe directamente del saldo de la cartera del usuario.</li>
 *   <li>{@link #TARJETA_CREDITO}, {@link #PAYPAL}, {@link #TRANSFERENCIA}, {@link #OTROS} – métodos externos.</li>
 * </ul>
 */
public enum METODOPAGOCOMPRA{TARJETA_CREDITO, PAYPAL, CARTERA_STEAM, TRANSFERENCIA, OTROS}
