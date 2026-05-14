package org.adrian.modelo.dto;

import org.adrian.modelo.enums.CATEGORIAJUEGO;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.PEGIJUEGO;

import java.time.LocalDateTime;

/**
 * DTO de solo lectura con los datos de un juego del catálogo.
 *
 * @param id               identificador único del juego
 * @param tituloJuego      título del juego
 * @param descripcion      descripción larga del juego (puede ser {@code null})
 * @param desarrollador    nombre del estudio desarrollador
 * @param fechaLanzamiento fecha y hora de lanzamiento oficial
 * @param precioBase       precio original sin descuento
 * @param descuentoActual  porcentaje de descuento activo (0–100)
 * @param idiomas          lista de idiomas disponibles
 * @param estado           estado de disponibilidad en el catálogo ({@link ESTADOJUEGO})
 * @param pegi             clasificación de edad PEGI ({@link PEGIJUEGO})
 * @param categoria        género del juego ({@link CATEGORIAJUEGO})
 */
public record JuegoDto (Long id, String tituloJuego, String descripcion, String desarrollador,
                        LocalDateTime fechaLanzamiento, Double precioBase, int descuentoActual,
                        String[] idiomas, ESTADOJUEGO estado, PEGIJUEGO pegi, CATEGORIAJUEGO categoria){}
