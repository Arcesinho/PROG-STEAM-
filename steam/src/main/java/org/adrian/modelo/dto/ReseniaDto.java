package org.adrian.modelo.dto;

import org.adrian.modelo.enums.EstadoReseniaEnum;

import java.time.LocalDateTime;

public record ReseniaDto (Long id, Long idUsuario, Long idJuego, boolean recomendado, String textoResenia, Double horasHastaResenia, LocalDateTime fechaPublicacion, LocalDateTime fechaUltimaEdicion, EstadoReseniaEnum.ESTADO estado){}
