package org.adrian.modelo.form;

import org.adrian.modelo.enums.EstadoInstalacionBiblioteca;
import org.adrian.recursos.ComprobarDosDecimales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record BibliotecaForm(Long id, Long idUsuario, Long idJuego, LocalDateTime fechaAdquisicion, Double horasJuego,
                             Optional<LocalDateTime> ultimaFechaJuego,
                             EstadoInstalacionBiblioteca.ESTADO_INSTALACION estadoInstalacion) {

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

        var ufj = ultimaFechaJuego.orElse(null);
        if (ufj == null || ufj.isAfter(LocalDateTime.now())) {
            errores.add(new ErrorDto("ultimaFechaJuego", ErrorType.FECHA_NO_VALIDA));
        }
        if (ufj == null || ufj.isBefore(fechaAdquisicion)) {
            errores.add(new ErrorDto("ultimaFechaJuego", ErrorType.FECHA_NO_VALIDA));
        }

        return errores;
    }
}
