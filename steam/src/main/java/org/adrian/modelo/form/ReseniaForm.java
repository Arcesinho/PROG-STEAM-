package org.adrian.modelo.form;

import org.adrian.modelo.enums.ESTADORESENIA;

import java.util.ArrayList;
import java.util.List;

/**
 * Formulario de entrada para crear o actualizar una reseña.
 *
 * @param id          identificador de la reseña (puede ser {@code null} en creación)
 * @param idUsuario   identificador del usuario autor
 * @param idJuego     identificador del juego reseñado
 * @param recomendado {@code true} si el usuario recomienda el juego
 * @param textoResenia texto de la reseña (50–8000 caracteres)
 * @param estado      estado de visibilidad de la reseña
 */
public record ReseniaForm (Long id, Long idUsuario, Long idJuego, boolean recomendado, String textoResenia,  ESTADORESENIA estado){

    public static final int MIN_LENG_TEXTORESENIA = 50;
    public static final int MAX_LENG_TEXTORESENIA = 8000;

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

        //Validaciones de recomendado

        //Validaciones de textoResenia

        if (textoResenia == null){
            errores.add(new ErrorDto("textoResenia", ErrorType.REQUERIDO));
        }
        if(textoResenia == null || textoResenia.length() < MIN_LENG_TEXTORESENIA){
            errores.add(new ErrorDto("textoResenia", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(textoResenia == null || textoResenia.length() > MAX_LENG_TEXTORESENIA){
            errores.add(new ErrorDto("textoResenia", ErrorType.VALOR_DEMASIADO_ALTO));
        }


        return errores;
    }
}


