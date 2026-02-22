package org.adrian.modelo.form;

import org.adrian.modelo.enums.EstadoReseniaEnum;

import java.util.ArrayList;
import java.util.List;

public record ReseniaForm (Long id, Long idUsuario, Long idJuego, boolean recomendado, String textoResenia,  EstadoReseniaEnum.ESTADO estado){

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

        //Validaciones de recomendado

        if (recomendado){
            errores.add(new ErrorDto("recomendado", ErrorType.REQUERIDO));
        }

        //Validaciones de textoResenia

        if (textoResenia == null){
            errores.add(new ErrorDto("textoResenia", ErrorType.REQUERIDO));
        }
        if(textoResenia == null || textoResenia.length() < 50){
            errores.add(new ErrorDto("textoResenia", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(textoResenia == null || textoResenia.length() > 8000){
            errores.add(new ErrorDto("textoResenia", ErrorType.VALOR_DEMASIADO_ALTO));
        }


        return errores;
    }
}


