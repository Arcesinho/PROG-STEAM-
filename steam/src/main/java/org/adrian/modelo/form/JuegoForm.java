package org.adrian.modelo.form;

import org.adrian.modelo.enums.CATEGORIAJUEGO;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.PEGIJUEGO;
import org.adrian.recursos.ComprobarDosDecimales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JuegoForm(String tituloJuego, Optional<String> descripcion, String desarrollador, LocalDateTime fechaLanzamiento,
                        Double precioBase, Optional<Integer> descuentoActual, PEGIJUEGO pegi, Optional<String[]> idiomas ,
                        ESTADOJUEGO estado, CATEGORIAJUEGO categoria) {

    public static final int MIN_LENG_TITULO = 1;
    public static final int MAX_LENG_TITULO = 100;
    public static final int MAX_LENG_DESCRIPCION = 2000;
    public static final int MIN_LENG_DESARROLLADOR = 2;
    public static final int MAX_LENG_DESARROLADOR = 100;
    public static final double MAX_PRECIOBASE = 999.99;
    public static final double MIN_PRECIOBASE = 0.00;
    public static final int MIN_DESCUENTOACTUAL = 0;
    public static final int MAX_DESCUENTOACTUAL = 100;
    public static final int MAX_LENG_IDIOMAS = 200;

    public List<ErrorDto> validar() {

        var errores = new ArrayList<ErrorDto>();

        //Validaciones Título

        if (tituloJuego == null || tituloJuego.isEmpty()) {
            errores.add(new ErrorDto("tituloJuego", ErrorType.REQUERIDO));
        }
        if(tituloJuego == null ||tituloJuego.length()< MIN_LENG_TITULO){
            errores.add(new ErrorDto("tituloJuego", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(tituloJuego == null ||tituloJuego.length()> MAX_LENG_TITULO){
            errores.add(new ErrorDto("tituloJuego", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validacones Descripcion

        var d = descripcion.orElse(null);
        if(d == null || d.length() > MAX_LENG_DESCRIPCION){
            errores.add(new ErrorDto("descripcion", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones Desarrolador

        if (desarrollador == null || desarrollador.isEmpty()) {
            errores.add(new ErrorDto("desarrollador", ErrorType.REQUERIDO));
        }
        if(desarrollador == null ||desarrollador.length()< MIN_LENG_DESARROLLADOR){
            errores.add(new ErrorDto("desarrollador", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(desarrollador == null ||desarrollador.length()> MAX_LENG_DESARROLADOR){
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
        if(precioBase == null || precioBase < MIN_PRECIOBASE){
            errores.add(new ErrorDto("precioBase", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(precioBase == null || precioBase > MAX_PRECIOBASE){
            errores.add(new ErrorDto("precioBase", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones descuentoActual

        var dA = descuentoActual.orElse(null);
        if(dA == null || dA < MIN_DESCUENTOACTUAL){
            errores.add(new ErrorDto("descuentoActual", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(dA == null || dA > MAX_DESCUENTOACTUAL){
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
        if(i == null || i.length > MAX_LENG_IDIOMAS){
            errores.add(new ErrorDto("idiomas", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones estado

        if(estado == null){
            errores.add(new ErrorDto("estado", ErrorType.REQUERIDO));
        }


        return errores;
    }
}
