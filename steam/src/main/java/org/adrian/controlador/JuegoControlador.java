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

/**
 * Controlador del catálogo de juegos.
 * Permite añadir juegos, consultar el catálogo, aplicar descuentos y cambiar el estado de disponibilidad.
 */
public class JuegoControlador {

    private final IJuegoRepo juegoRepo;

    /**
     * @param juegoRepo repositorio de juegos que se usará para las operaciones de persistencia
     */
    public JuegoControlador(IJuegoRepo juegoRepo){ this.juegoRepo = juegoRepo;}

    /**
     * Añade un nuevo juego al catálogo validando que el título sea único.
     *
     * @param form datos del juego a registrar
     * @return DTO con la información del juego creado
     * @throws ValidationExcepcion si el formulario contiene errores o el título ya existe
     */
    public JuegoDto aniadirNuevoJuego(JuegoForm form) throws ValidationExcepcion{

        List<ErrorDto> errores = form.validar();

        boolean tituloExiste = juegoRepo.obtenerTodos().stream()
                .anyMatch(u -> u.getTituloJuego().equalsIgnoreCase(form.tituloJuego()));
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

    /**
     * Consulta todos los juegos disponibles en el catálogo.
     *
     * @return {@code true} si la operación se completó (pendiente de implementar la devolución de la lista)
     * @throws ValidationExcepcion si el catálogo está vacío
     */
    public List<JuegoDto> consultarCatalogoCompleto() throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var listadeJuegos = juegoRepo.obtenerTodos();

        if(listadeJuegos.isEmpty()){
            errores.add(new ErrorDto("listaJuegos", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        return listadeJuegos.stream()
                .map(Mapper::mapFrom)
                .toList();

    }

    /**
     * Recupera el detalle completo de un juego por su identificador.
     *
     * @param id identificador del juego
     * @return DTO con la información del juego
     * @throws ValidationExcepcion si no existe ningún juego con ese id
     */
    public JuegoDto consultarDetalleJuegoPorId(Long id) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var JuegoABuscarOpt = juegoRepo.obtenerPorId(id);

        if(JuegoABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idJuego", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        return Mapper.mapFrom(JuegoABuscarOpt.orElse(null)); //Falta estadisticas y reseñas destacadas
    }

    /**
     * Aplica un porcentaje de descuento al precio base de un juego.
     * El precio se recalcula como {@code precioBase * (1 - descuento/100)}.
     *
     * @param id       identificador del juego
     * @param descuento porcentaje de descuento a aplicar (0–100)
     * @return DTO del juego con el precio actualizado
     * @throws ValidationExcepcion si el juego no existe o el descuento está fuera del rango permitido
     */
    public JuegoDto aplicarDescuentoJuegoPorId(Long id, Integer descuento) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var JuegoABuscarOpt = juegoRepo.obtenerPorId(id);

        if(JuegoABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idJuego", ErrorType.NO_ENCONTRADO));
        }
        if(descuento<0 || descuento>100){
            errores.add(new ErrorDto("descuento", ErrorType.FORMATO_INVALIDO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var JuegoEncontrado = JuegoABuscarOpt.get();

        Double nuevoPrecio = JuegoEncontrado.getPrecioBase() - (JuegoEncontrado.getPrecioBase()*0.01*descuento);

        var JuegoActualizado = juegoRepo.actualizar(id, new JuegoForm(JuegoEncontrado.getTituloJuego(), Optional.ofNullable(JuegoEncontrado.getDescripcion()), JuegoEncontrado.getDesarrollador(), JuegoEncontrado.getFechaLanzamiento(),
                nuevoPrecio, Optional.of(descuento), JuegoEncontrado.getPegi(), Optional.ofNullable(JuegoEncontrado.getIdiomas()), JuegoEncontrado.getEstado(), JuegoEncontrado.getCategoria()));

        var JuegoADevolver = JuegoActualizado.orElseThrow(() -> new ValidationExcepcion(List.of(new ErrorDto("juego", ErrorType.NO_ENCONTRADO))));

        return Mapper.mapFrom(JuegoADevolver);
    }

    /**
     * Cambia el estado de disponibilidad de un juego.
     *
     * @param id          identificador del juego
     * @param estadojuego nuevo estado ({@link ESTADOJUEGO#DISPONIBLE}, {@link ESTADOJUEGO#NO_DISPONIBLE},
     *                    {@link ESTADOJUEGO#PREVENTA} o {@link ESTADOJUEGO#ACCESO_ANTICIPADO})
     * @return DTO del juego con el estado actualizado
     * @throws ValidationExcepcion si el juego no existe o el estado no es válido
     */
    public JuegoDto cambiarEstadoJuegoPorId(Long id, ESTADOJUEGO estadojuego) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var JuegoABuscarOpt = juegoRepo.obtenerPorId(id);

        if(JuegoABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idJuego", ErrorType.NO_ENCONTRADO));
        }
        if(!(estadojuego.equals(ESTADOJUEGO.ACCESO_ANTICIPADO)|| estadojuego.equals(ESTADOJUEGO.DISPONIBLE)|| estadojuego.equals(ESTADOJUEGO.NO_DISPONIBLE)||estadojuego.equals(ESTADOJUEGO.PREVENTA))){
            errores.add(new ErrorDto("estadojuego", ErrorType.FORMATO_INVALIDO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var JuegoEncontrado = JuegoABuscarOpt.get();

        var JuegoActualizado = juegoRepo.actualizar(id, new JuegoForm(JuegoEncontrado.getTituloJuego(), Optional.ofNullable(JuegoEncontrado.getDescripcion()), JuegoEncontrado.getDesarrollador(), JuegoEncontrado.getFechaLanzamiento(),
                JuegoEncontrado.getPrecioBase(), Optional.of(JuegoEncontrado.getDescuentoActual()), JuegoEncontrado.getPegi(), Optional.ofNullable(JuegoEncontrado.getIdiomas()), estadojuego, JuegoEncontrado.getCategoria()));

        var JuegoADevolver = JuegoActualizado.orElseThrow(() -> new ValidationExcepcion(List.of(new ErrorDto("juego", ErrorType.NO_ENCONTRADO))));

        return Mapper.mapFrom(JuegoADevolver);

    }
}
