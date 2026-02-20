package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.EstadoInstalacionBiblioteca;

import java.time.LocalDateTime;

public record BibliotecaEntidad (Long id, Long idUsuario, Long idJuego, LocalDateTime fechaAdquisicion, Double horasJuego, LocalDateTime ultimaFechaJuego, EstadoInstalacionBiblioteca.ESTADO_INSTALACION estadoInstalacion)  {}
