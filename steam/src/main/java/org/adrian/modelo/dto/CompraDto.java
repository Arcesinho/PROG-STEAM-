package org.adrian.modelo.dto;



import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.enums.METODOPAGOCOMPRA;

import java.time.LocalDateTime;

public record CompraDto (Long id, Long idUsuario, Long idJuego, UsuarioDto usuario, JuegoDto juego, LocalDateTime fechaCompra,
                         Double precioSinDescuento, int descuento, METODOPAGOCOMPRA metodopago, ESTADOCOMPRA estado){}