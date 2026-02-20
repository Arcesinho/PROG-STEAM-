package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.CategoriaJuegoEnum;
import org.adrian.modelo.enums.EstadoJuegoEnum;
import org.adrian.modelo.enums.PegiJuegoEnum;

import java.time.LocalDateTime;

public record JuegoEntidad (Long id, String tituloJuego, String descripcion, String desarrollador, LocalDateTime fechaLanzamiento, Double precioBase, int descuentoActual, String[] idiomas, EstadoJuegoEnum.ESTADO estado, PegiJuegoEnum.PEGI pegi, CategoriaJuegoEnum.CATEGORIA categoria){}
