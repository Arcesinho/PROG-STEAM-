package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.enums.METODOPAGOCOMPRA;

import java.time.LocalDateTime;

public record CompraEntidad (Long id, Long idUsuario, Long idJuego, LocalDateTime fechaCompra, Double precioSinDescuento, int descuento, METODOPAGOCOMPRA metodopago, ESTADOCOMPRA estado){}