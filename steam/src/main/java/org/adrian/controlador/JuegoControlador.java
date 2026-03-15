package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.JuegoDto;
import org.adrian.modelo.entidad.JuegoEntidad;
import org.adrian.modelo.form.ErrorDto;
import org.adrian.modelo.form.ErrorType;
import org.adrian.modelo.form.JuegoForm;
import org.adrian.repositorio.interfaces.IJuegoRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JuegoControlador {

    private final IJuegoRepo juegoRepo;

    public JuegoControlador(IJuegoRepo juegoRepo){ this.juegoRepo = juegoRepo;}

    public JuegoDto registrarNuevoJuego(JuegoForm form) throws ValidationExcepcion{

        List<ErrorDto> errores = form.validar();

        boolean tituloExiste = juegoRepo.obtenerTodos().stream()
                .anyMatch(u -> u.tituloJuego().equalsIgnoreCase(form.tituloJuego()));
        if (tituloExiste) {
            errores.add(new ErrorDto("tituloJuego", ErrorType.DUPLICADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        Optional<JuegoEntidad> nuevoJuego = juegoRepo.crear(form);

        var juegoEntidad = nuevoJuego.orElseThrow(()-> new ValidationExcepcion(errores));

        return Mapper.mapFrom(juegoEntidad);

    }

    public JuegoDto consultarDetalleJuegoPorId(Long id) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var JuegoABuscarOpt = juegoRepo.obtenerPorId(id);

        if(JuegoABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idJuego", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var JuegoEncontrado = JuegoABuscarOpt.get();

        return Mapper.mapFrom(JuegoEncontrado); //Falta estadisticas y reseñas destacadas
    }

}
