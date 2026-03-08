package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.ESTADORESENIA;

import java.time.LocalDateTime;

public record ReseniaEntidad (Long id, Long idUsuario, Long idJuego, boolean recomendado, String textoResenia, Double horasHastaResenia, LocalDateTime fechaPublicacion, LocalDateTime fechaUltimaEdicion, ESTADORESENIA estado){}
