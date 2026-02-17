package modelo.dto;

import modelo.enums.EstadoCompraEnum;
import modelo.enums.MetodoPagoCompraEnum;

import java.time.LocalDateTime;

public record CompraDto (Long id, LocalDateTime fechaCompra, Double precioSinDescuento, int descuento, UsuarioDto usuario, JuegoDto juego, MetodoPagoCompraEnum.METODOPAGO metodopago, EstadoCompraEnum.ESTADO estado){}