package org.adrian.modelo.dto;



import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.enums.METODOPAGOCOMPRA;

import java.time.LocalDateTime;

/**
 * DTO de solo lectura con los datos de una transacción de compra.
 *
 * @param id                 identificador único de la compra
 * @param idUsuario          identificador del usuario comprador
 * @param idJuego            identificador del juego comprado
 * @param usuario            DTO del usuario comprador
 * @param juego              DTO del juego comprado
 * @param fechaCompra        fecha y hora en que se realizó la compra
 * @param precioSinDescuento precio del juego antes de aplicar el descuento
 * @param descuento          porcentaje de descuento aplicado (0–100)
 * @param metodopago         método de pago utilizado ({@link METODOPAGOCOMPRA})
 * @param estado             estado actual de la transacción ({@link ESTADOCOMPRA})
 */
public record CompraDto (Long id, Long idUsuario, Long idJuego, UsuarioDto usuario, JuegoDto juego, LocalDateTime fechaCompra,
                         Double precioSinDescuento, int descuento, METODOPAGOCOMPRA metodopago, ESTADOCOMPRA estado){}