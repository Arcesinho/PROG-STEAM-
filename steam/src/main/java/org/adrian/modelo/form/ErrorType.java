package org.adrian.modelo.form;

/**
 * Enumeración de tipos de error de validación utilizados en los formularios y controladores.
 * Cada constante incluye un mensaje descriptivo accesible por reflexión o logging.
 */
public enum ErrorType {

    REQUERIDO("El campo es obligatorio"),
    FORMATO_INVALIDO("El formato es inválido"),
    NO_EMPEZAR_POR_NUMERO("El primer carácter no puede ser numérico"),
    VALOR_DEMASIADO_ALTO("El valor es demasiado alto"),
    VALOR_DEMASIADO_BAJO("El valor es demasiado bajo"),
    NO_ENCONTRADO("No se encontró el elemento"),
    DUPLICADO("El elemento está duplicado"),
    SALDO_INSUFICIENTE("Saldo insuficiente para completar la compra"),
    USUARIO_INACTIVO("El usuario no está en estado activo"),
    JUEGO_NO_DISPONIBLE("El juego no está disponible"),
    FECHA_NO_VALIDA("Fecha no válida"),
    FUERA_DE_PLAZO("La operación no se puede realizar fuera del plazo permitido para el reembolso"),
    LIMITE_EXCEDIDO("Se ha excedido el límite permitido de horas para reembolsar");
    

    private final String mensaje;

    private ErrorType(String mensaje) {
        this.mensaje = mensaje;
    }
}
