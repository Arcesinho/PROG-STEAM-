package org.adrian.excepcion;

import org.adrian.modelo.form.ErrorDto;

import java.util.List;

/**
 * Excepción que encapsula una lista de errores de validación producidos durante el procesamiento
 * de un formulario o una operación de negocio.
 * Los errores se representan como instancias de {@link ErrorDto}.
 */
public class ValidationExcepcion extends Exception{

    private final List<ErrorDto> errores;

    /**
     * @param errores lista de errores de validación que causaron la excepción
     */
    public ValidationExcepcion(List<ErrorDto> errores) {
        super("Errores de validaci�n");
        this.errores = errores;
    }

    /**
     * @return lista inmutable de errores de validación
     */
    public List<ErrorDto> getErrores() {
        return errores;
    }
}
