package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.CATEGORIAJUEGO;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.PEGIJUEGO;

import java.time.LocalDateTime;

public record JuegoEntidad (Long id, String tituloJuego, String descripcion, String desarrollador,
                            LocalDateTime fechaLanzamiento, Double precioBase, int descuentoActual, String[] idiomas,
                            ESTADOJUEGO estado, PEGIJUEGO pegi, CATEGORIAJUEGO categoria){}
