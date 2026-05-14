package org.adrian.modelo.form;

/**
 * DTO que representa un error de validación concreto.
 *
 * @param campo   nombre del campo que generó el error
 * @param mensaje tipo de error producido ({@link ErrorType})
 */
public record ErrorDto(String campo, ErrorType mensaje) {
}
