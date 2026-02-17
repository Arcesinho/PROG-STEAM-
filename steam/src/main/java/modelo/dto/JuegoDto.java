package modelo.dto;

import modelo.enums.CategoriaJuegoEnum;
import modelo.enums.EstadoJuegoEnum;
import modelo.enums.PegiJuegoEnum;

import java.time.LocalDateTime;

public record JuegoDto (Long id, String tituloJuego, String descripcion, String desarrollador, LocalDateTime fechaLanzamiento, Double precioBase, int descuentoActual, String[] idiomas, EstadoJuegoEnum.ESTADO estado, PegiJuegoEnum.PEGI pegi, CategoriaJuegoEnum.CATEGORIA categoria){}
