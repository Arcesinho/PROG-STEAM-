package org.adrian.modelo.form;

import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.enums.METODOPAGOCOMPRA;
import org.adrian.recursos.ComprobarDosDecimales;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Formulario de entrada para crear o actualizar una compra.
 *
 * @param id                 identificador de la compra (puede ser {@code null} en creación)
 * @param idUsuario          identificador del usuario comprador
 * @param idJuego            identificador del juego a comprar
 * @param precioSinDescuento precio del juego antes de aplicar descuentos (≥ 0, máximo 2 decimales)
 * @param descuento          porcentaje de descuento (opcional, 0–100)
 * @param metodopago         método de pago a utilizar
 * @param estado             estado de la compra (opcional; útil para actualizaciones)
 */
public record CompraForm (Long id, Long idUsuario, Long idJuego, Double precioSinDescuento, Optional<Integer> descuento, METODOPAGOCOMPRA metodopago, Optional<ESTADOCOMPRA> estado){

    public static final int MIN_DESCUENTO = 0;
    public static final int MAX_DESCUENTO = 100;

    /**
     * Valida todos los campos del formulario según las reglas de negocio.
     *
     * @return lista de {@link ErrorDto} con los errores encontrados; vacía si todo es correcto
     */
    public List<ErrorDto> validar() {

        var errores = new ArrayList<ErrorDto>();


       //Validaciones de la referencia a Usuario

        if (idUsuario == null){
            errores.add(new ErrorDto("idUsuario", ErrorType.REQUERIDO));
        }

        //Validaciones de la referencia a juego

        if (idJuego == null){
            errores.add(new ErrorDto("idJuego", ErrorType.REQUERIDO));
        }

        //Validaciones de metodopago

        if(metodopago == null){
            errores.add(new ErrorDto("metodoPago", ErrorType.REQUERIDO));
        }

        //Validaciones de precioSinDescuento

        if(precioSinDescuento == null){
            errores.add(new ErrorDto("precioSinDescuento", ErrorType.REQUERIDO));
        }
        if(precioSinDescuento == null || precioSinDescuento < 0){
            errores.add(new ErrorDto("precioSinDescuento", ErrorType.FORMATO_INVALIDO));
        }
        if(precioSinDescuento == null || !(ComprobarDosDecimales.tieneDosOMenosDecimales(precioSinDescuento))){
            errores.add(new ErrorDto("precioSinDescuento", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones de descuento

        var d = descuento.orElse(null);
        if(d == null || d < MIN_DESCUENTO || d > MAX_DESCUENTO){
            errores.add(new ErrorDto("descuento", ErrorType.FORMATO_INVALIDO));
        }
        if (d == null || d < 0){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_DEMASIADO_BAJO));
        }


        return errores;
    }

}
