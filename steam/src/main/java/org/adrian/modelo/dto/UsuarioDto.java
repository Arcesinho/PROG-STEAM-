package org.adrian.modelo.dto;

import org.adrian.modelo.enums.ESTADOCUENTA;

import java.time.LocalDate;
import java.time.LocalDateTime;



/**
 * DTO de solo lectura con los datos públicos de un usuario.
 * No expone la contraseña ni información sensible de autenticación.
 *
 * @param id             identificador único del usuario
 * @param nombre         nombre de usuario (alias)
 * @param email          dirección de correo electrónico
 * @param nombreReal     nombre real del usuario
 * @param pais           país de residencia
 * @param fechaNacimiento fecha de nacimiento
 * @param fechaRegistro  fecha y hora en que se creó la cuenta
 * @param avatar         URL o ruta del avatar (puede ser {@code null})
 * @param saldoCartera   saldo disponible en la cartera Steam
 * @param estado         estado de la cuenta ({@link ESTADOCUENTA})
 */
public record UsuarioDto (Long id, String nombre, String email,  String nombreReal, String pais, LocalDate fechaNacimiento,
                          LocalDateTime fechaRegistro, String avatar, Double saldoCartera, ESTADOCUENTA estado){}


