package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.EstadoCompraEnum;
import org.adrian.modelo.enums.MetodoPagoCompraEnum;

import java.time.LocalDateTime;

public record CompraEntidad (Long id, Long idUsuario, Long idJuego, LocalDateTime fechaCompra, Double precioSinDescuento, int descuento, MetodoPagoCompraEnum.METODOPAGO metodopago, EstadoCompraEnum.ESTADO estado){}