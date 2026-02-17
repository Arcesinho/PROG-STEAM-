package modelo.dto;

import modelo.enums.EstadoReseniaEnum;

import java.time.LocalDateTime;

public record ReseniaDto (Long id, boolean recomendado, String textoResenia, Double horasHastaResenia, LocalDateTime fechaPublicacion, LocalDateTime fechaUltimaEdicion, EstadoReseniaEnum.ESTADO estado, UsuarioDto usuario, JuegoDto juego){}
