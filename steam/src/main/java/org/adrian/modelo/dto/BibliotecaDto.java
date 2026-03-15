package org.adrian.modelo.dto;


import org.adrian.modelo.enums.ESTADOINSTALACIONBIBLIOTECA;

import java.time.LocalDateTime;

public record BibliotecaDto (Long id, Long idUsuario, Long idJuego, LocalDateTime fechaAdquisicion,
                             Double horasJuego, LocalDateTime ultimaFechaJuego,  ESTADOINSTALACIONBIBLIOTECA estadoInstalacion)  {}
