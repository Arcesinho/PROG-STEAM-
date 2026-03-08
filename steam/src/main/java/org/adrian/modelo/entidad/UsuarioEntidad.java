package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.ESTADOCUENTA;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioEntidad (Long id, String nombre, String email, String contrasenia, String nombreReal, String pais, LocalDate fechaNacimiento, LocalDateTime fechaRegistro, String avatar, Double saldoCartera, ESTADOCUENTA estado){}
