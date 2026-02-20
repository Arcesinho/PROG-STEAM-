package org.adrian.modelo.form;

import org.adrian.modelo.enums.CategoriaJuegoEnum;
import org.adrian.modelo.enums.EstadoJuegoEnum;
import org.adrian.modelo.enums.PegiJuegoEnum;
import org.adrian.recursos.ComprobarDosDecimales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JuegoForm(String tituloJuego, Optional<String> descripcion, String desarrollador, LocalDateTime fechaLanzamiento, Double precioBase, Optional<Integer> descuentoActual,  PegiJuegoEnum.PEGI pegi, Optional<String[]> idiomas ,EstadoJuegoEnum.ESTADO estado, CategoriaJuegoEnum.CATEGORIA categoria) {

    public List<ErrorDto> validar() {

        var errores = new ArrayList<ErrorDto>();

        //Validaciones Título

        if (tituloJuego == null || tituloJuego.isEmpty()) {
            errores.add(new ErrorDto("tituloJuego", ErrorType.REQUERIDO));
        }
        if(tituloJuego == null ||tituloJuego.length()<1){
            errores.add(new ErrorDto("tituloJuego", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(tituloJuego == null ||tituloJuego.length()>100){
            errores.add(new ErrorDto("tituloJuego", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validacones Descripcion

        var d = descripcion.orElse(null);
        if(d == null || d.length() > 2000){
            errores.add(new ErrorDto("descripcion", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones Desarrolador

        if (desarrollador == null || desarrollador.isEmpty()) {
            errores.add(new ErrorDto("desarrollador", ErrorType.REQUERIDO));
        }
        if(desarrollador == null ||desarrollador.length()<2){
            errores.add(new ErrorDto("desarrollador", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(desarrollador == null ||desarrollador.length()>100){
            errores.add(new ErrorDto("desarrollador", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones fechaLanzamiento

        if (fechaLanzamiento == null) {
            errores.add( new ErrorDto("fechaLanzamiento", ErrorType.REQUERIDO));
        }

        //Validaciones precio base

        if(precioBase == null){
            errores.add(new ErrorDto("precioBase", ErrorType.REQUERIDO));
        }
        if(precioBase == null || !(precioBase >= 0)){
            errores.add(new ErrorDto("precioBase", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(precioBase == null || !(ComprobarDosDecimales.tieneDosOMenosDecimales(precioBase))){
            errores.add(new ErrorDto("precioBase", ErrorType.FORMATO_INVALIDO));
        }
        if(precioBase == null || precioBase <0.00){
            errores.add(new ErrorDto("precioBase", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(precioBase == null || precioBase > 999.99){
            errores.add(new ErrorDto("precioBase", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones descuentoActual

        var dA = descuentoActual.orElse(null);
        if(dA == null || dA <0){
            errores.add(new ErrorDto("descuentoActual", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(dA == null || dA > 100){
            errores.add(new ErrorDto("descuentoActual", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones pegi

        if(pegi == null){
            errores.add(new ErrorDto("pegi", ErrorType.REQUERIDO));
        }

        //Validaciones Idiomas disponible

        var i = idiomas.orElse(null);
        if(i == null || i.length < 1){
            errores.add(new ErrorDto("idiomas", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(i == null || i.length > 200){
            errores.add(new ErrorDto("idiomas", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones estado

        if(estado == null){
            errores.add(new ErrorDto("estado", ErrorType.REQUERIDO));
        }



        return errores;
    }
}
