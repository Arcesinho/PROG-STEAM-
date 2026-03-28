package org.adrian.modelo.dto;

import java.util.List;
import java.util.Optional;

public record EstadisticasBibliotecaDto(Long idUsuario, Long idJuego, UsuarioDto usuarioDto, JuegoDto JuegoDto,
                                        List<JuegoDto> juegosTotales, Double horas,
                                        List<JuegoDto> juegosInstalados, Optional<JuegoDto> juegoMasJugado,
                                        Double valorTotal, List<JuegoDto> juegosNoJugados){}
