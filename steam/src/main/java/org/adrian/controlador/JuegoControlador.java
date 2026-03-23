package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.JuegoDto;
import org.adrian.modelo.entidad.JuegoEntidad;
import org.adrian.modelo.enums.ESTADOJUEGO;
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

    public JuegoDto aniadirNuevoJuego(JuegoForm form) throws ValidationExcepcion{

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

    public boolean consultarCatalogoCompleto() throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var listadeJuegos = juegoRepo.obtenerTodos();

        if(listadeJuegos.isEmpty()){
            errores.add(new ErrorDto("listaJuegos", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var listaDeJuegosDTO = listadeJuegos.stream().toList();

        return false;
        //Falta acabar la devolucioón de una lista en este caso

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

    public JuegoDto aplicarDescuentoJuegoPorId(Long id, Integer descuento) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var JuegoABuscarOpt = juegoRepo.obtenerPorId(id);

        if(JuegoABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idJuego", ErrorType.NO_ENCONTRADO));
        }
        if(descuento<0 || descuento>100){
            errores.add(new ErrorDto("descuento", ErrorType.FORMATO_INVALIDO));
        }

        var JuegoEncontrado = JuegoABuscarOpt.get();

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        Double nuevoPrecio = JuegoEncontrado.precioBase() - (JuegoEncontrado.precioBase()*0.01*descuento);

        var JuegoActualizado = juegoRepo.actualizar(id, new JuegoForm(JuegoEncontrado.tituloJuego(), Optional.ofNullable(JuegoEncontrado.descripcion()), JuegoEncontrado.desarrollador(), JuegoEncontrado.fechaLanzamiento(),
                nuevoPrecio, Optional.of(descuento), JuegoEncontrado.pegi(), Optional.ofNullable(JuegoEncontrado.idiomas()), JuegoEncontrado.estado(), JuegoEncontrado.categoria()));

        var JuegoADevolver = JuegoActualizado.get();

        return Mapper.mapFrom(JuegoADevolver);
    }

    public JuegoDto cambiarEstadoJuegoPorId(Long id, ESTADOJUEGO estadojuego) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var JuegoABuscarOpt = juegoRepo.obtenerPorId(id);

        if(JuegoABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idJuego", ErrorType.NO_ENCONTRADO));
        }
        if(!(estadojuego.equals(ESTADOJUEGO.ACCESO_ANTICIPADO)|| estadojuego.equals(ESTADOJUEGO.DISPONIBLE)|| estadojuego.equals(ESTADOJUEGO.NO_DISPONIBLE)||estadojuego.equals(ESTADOJUEGO.PREVENTA))){
            errores.add(new ErrorDto("estadojuego", ErrorType.FORMATO_INVALIDO));
        }

        var JuegoEncontrado = JuegoABuscarOpt.get();

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var JuegoActualizado = juegoRepo.actualizar(id, new JuegoForm(JuegoEncontrado.tituloJuego(), Optional.ofNullable(JuegoEncontrado.descripcion()), JuegoEncontrado.desarrollador(), JuegoEncontrado.fechaLanzamiento(),
                JuegoEncontrado.precioBase(), Optional.of(JuegoEncontrado.descuentoActual()), JuegoEncontrado.pegi(), Optional.ofNullable(JuegoEncontrado.idiomas()), estadojuego, JuegoEncontrado.categoria()));

        var JuegoADevolver = JuegoActualizado.get();

        return Mapper.mapFrom(JuegoADevolver);

    }
}
