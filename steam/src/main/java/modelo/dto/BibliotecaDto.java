package modelo.dto;

import modelo.enums.EstadoInstalacionBiblioteca;

public record BibliotecaDto (Long id, String fechaAdquisicion, Double horasJuego, String ultimaFechaJuego, UsuarioDto usuario, JuegoDto juego, EstadoInstalacionBiblioteca.ESTADO_INSTALACION estadoInstalacion)  {}
