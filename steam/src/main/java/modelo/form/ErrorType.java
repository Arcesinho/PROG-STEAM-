package modelo.form;

public enum ErrorType {

    REQUERIDO("El campo es obligatorio"),
    FORMATO_INVALIDO("El formato es inválido"),
    NO_EMPEZAR_POR_NUMERO("El primer carácter no puede ser numérico"),
    VALOR_DEMASIADO_ALTO("El valor es demasiado alto"),
    VALOR_DEMASIADO_BAJO("El valor es demasiado bajo"),
    NO_ENCONTRADO("No se encontró el elemento"),
    DUPLICADO("El elemento está duplicado");

    private final String mensaje;

    private ErrorType(String mensaje) {
        this.mensaje = mensaje;
    }
}
