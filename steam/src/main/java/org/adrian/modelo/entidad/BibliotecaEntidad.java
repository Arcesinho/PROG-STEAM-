package org.adrian.modelo.entidad;


import org.adrian.modelo.enums.ESTADOINSTALACIONBIBLIOTECA;

import java.time.LocalDateTime;

public record BibliotecaEntidad (Long id, Long idUsuario, Long idJuego, LocalDateTime fechaAdquisicion, Double horasJuego, LocalDateTime ultimaFechaJuego, ESTADOINSTALACIONBIBLIOTECA estadoInstalacion)  {}
