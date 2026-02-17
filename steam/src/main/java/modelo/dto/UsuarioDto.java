package modelo.dto;

import modelo.enums.EstadoCuentaEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;



public record UsuarioDto (Long id, String nombre, String email, String contrasenia, String nombreReal, String pais, LocalDate fechaNacimiento, LocalDateTime fechaRegistro, String avatar, Double saldoCartera, EstadoCuentaEnum.ESTADOCUENTA estado){}


