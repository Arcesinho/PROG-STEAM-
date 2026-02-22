package org.adrian.modelo.form;

import org.adrian.modelo.enums.EstadoCompraEnum;
import org.adrian.modelo.enums.MetodoPagoCompraEnum;
import org.adrian.recursos.ComprobarDosDecimales;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record CompraForm (Long id, Long idUsuario, Long idJuego, Double precioSinDescuento, Optional<Integer> descuento, MetodoPagoCompraEnum.METODOPAGO metodopago, EstadoCompraEnum.ESTADO estado){

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
        if(d == null || d < 0 || d > 100){
            errores.add(new ErrorDto("descuento", ErrorType.FORMATO_INVALIDO));
        }
        if (d == null || d < 0){
            errores.add(new ErrorDto("descuento", ErrorType.VALOR_DEMASIADO_BAJO));
        }


        return errores;
    }

}
