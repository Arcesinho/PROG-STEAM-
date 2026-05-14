package org.adrian.modelo.form;

import org.adrian.modelo.enums.ESTADOINSTALACIONBIBLIOTECA;
import org.adrian.recursos.ComprobarDosDecimales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Formulario de entrada para crear o actualizar una entrada en la biblioteca de un usuario.
 *
 * @param id                 identificador de la entrada (puede ser {@code null} en creación)
 * @param idUsuario          identificador del usuario propietario
 * @param idJuego            identificador del juego
 * @param fechaAdquisicion   fecha y hora de adquisición (no puede ser futura)
 * @param horasJuego         horas totales jugadas (≥ 0, máximo 2 decimales)
 * @param ultimaFechaJuego   fecha y hora de la última sesión (opcional; entre adquisición y ahora)
 * @param estadoInstalacion  estado de instalación del juego
 */
public record BibliotecaForm(Long id, Long idUsuario, Long idJuego, LocalDateTime fechaAdquisicion, Double horasJuego,
                             Optional<LocalDateTime> ultimaFechaJuego,
                             ESTADOINSTALACIONBIBLIOTECA estadoInstalacion) {

    /**
     * Valida todos los campos del formulario según las reglas de negocio.
     *
     * @return lista de {@link ErrorDto} con los errores encontrados; vacía si todo es correcto
     */
    public List<ErrorDto> validar() {

        var errores = new ArrayList<ErrorDto>();

        //Validaciones de la referencia a Usuario

        if (idUsuario == null) {
            errores.add(new ErrorDto("idUsuario", ErrorType.REQUERIDO));
        }

        //Validaciones de la referencia a juego

        if (idJuego == null) {
            errores.add(new ErrorDto("idJuego", ErrorType.REQUERIDO));
        }

        //Validaciones de la fecha de adquisición

        if (fechaAdquisicion == null) {
            errores.add(new ErrorDto("fechaAdquisicion", ErrorType.REQUERIDO));
        }
        if (fechaAdquisicion == null || fechaAdquisicion.isAfter(LocalDateTime.now())) {
            errores.add(new ErrorDto("fechaAdquisicion", ErrorType.FECHA_NO_VALIDA));
        }

        //Validaciones de horasJuego

        if (!(horasJuego >= 0)) {
            errores.add(new ErrorDto("horasJuego", ErrorType.FORMATO_INVALIDO));
        }
        if (horasJuego == null || !(ComprobarDosDecimales.tieneDosOMenosDecimales(horasJuego))) {
            errores.add(new ErrorDto("horasJuego", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones de última fecha de juego

        if(ultimaFechaJuego.isPresent()){
            var ufj = ultimaFechaJuego.get();

            if (ufj.isAfter(LocalDateTime.now())) {
                errores.add(new ErrorDto("ultimaFechaJuego", ErrorType.FECHA_NO_VALIDA));
            }
            if (ufj.isBefore(fechaAdquisicion)) {
                errores.add(new ErrorDto("ultimaFechaJuego", ErrorType.FECHA_NO_VALIDA));
            }
        }

        return errores;
    }
}
