package org.adrian.modelo.dto;

import org.adrian.modelo.enums.ESTADOCUENTA;

import java.time.LocalDate;
import java.time.LocalDateTime;



public record UsuarioDto (Long id, String nombre, String email,  String nombreReal, String pais, LocalDate fechaNacimiento, LocalDateTime fechaRegistro, String avatar, Double saldoCartera, ESTADOCUENTA estado){}


