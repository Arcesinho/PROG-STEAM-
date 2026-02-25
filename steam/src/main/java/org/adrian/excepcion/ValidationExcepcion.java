package org.adrian.excepcion;

import org.adrian.modelo.form.ErrorDto;

import java.util.List;

public class ValidationExcepcion extends Exception{

    private final List<ErrorDto> errores;

    public ValidationExcepcion(List<ErrorDto> errores) {
        super("Errores de validaci�n");
        this.errores = errores;
    }
    public List<ErrorDto> getErrores() {
        return errores;
    }
}
