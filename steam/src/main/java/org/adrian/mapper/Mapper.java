package org.adrian.mapper;


import org.adrian.modelo.dto.*;
import org.adrian.modelo.entidad.*;


/**
 * Clase de utilidad estática para convertir entidades de dominio a objetos de transferencia de datos (DTO).
 * Devuelve {@code null} si la entidad recibida es {@code null}.
 */
public class Mapper {

    /**
     * Convierte una {@link UsuarioEntidad} en un {@link UsuarioDto}.
     *
     * @param entidad entidad a convertir
     * @return DTO equivalente, o {@code null} si {@code entidad} es {@code null}
     */
    public static UsuarioDto mapFrom(UsuarioEntidad entidad){

        if(entidad == null)
            return null;

        return new UsuarioDto(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getEmail(),
                entidad.getNombreReal(),
                entidad.getPais(),
                entidad.getFechaNacimiento(),
                entidad.getFechaRegistro(),
                entidad.getAvatar(),
                entidad.getSaldoCartera(),
                entidad.getEstado());
    }

    /**
     * Convierte una {@link JuegoEntidad} en un {@link JuegoDto}.
     *
     * @param entidad entidad a convertir
     * @return DTO equivalente, o {@code null} si {@code entidad} es {@code null}
     */
    public static JuegoDto mapFrom(JuegoEntidad entidad){

        if(entidad == null)
            return null;

        return new JuegoDto(
                entidad.getId(),
                entidad.getTituloJuego(),
                entidad.getDescripcion(),
                entidad.getDesarrollador(),
                entidad.getFechaLanzamiento(),
                entidad.getPrecioBase(),
                entidad.getDescuentoActual(),
                entidad.getIdiomas(),
                entidad.getEstado(),
                entidad.getPegi(),
                entidad.getCategoria());
    }

    /**
     * Convierte una {@link BibliotecaEntidad} en un {@link BibliotecaDto} enriquecido con los DTOs
     * del usuario y del juego correspondientes.
     *
     * @param entidad entidad de biblioteca a convertir
     * @param usuario DTO del usuario propietario
     * @param juego   DTO del juego asociado
     * @return DTO equivalente, o {@code null} si {@code entidad} es {@code null}
     */
    public static BibliotecaDto mapFrom(BibliotecaEntidad entidad, UsuarioDto usuario, JuegoDto juego){

        if(entidad == null)
            return null;

        return new BibliotecaDto(
                entidad.getId(),
                entidad.getIdUsuario(),
                entidad.getIdJuego(),
                usuario,
                juego,
                entidad.getFechaAdquisicion(),
                entidad.getHorasJuego(),
                entidad.getUltimaFechaJuego(),
                entidad.getEstadoInstalacion());

    }

    /**
     * Convierte una {@link CompraEntidad} en un {@link CompraDto} enriquecido con los DTOs
     * del usuario y del juego.
     *
     * @param entidad entidad de compra a convertir
     * @param usuario DTO del usuario comprador
     * @param juego   DTO del juego comprado
     * @return DTO equivalente, o {@code null} si {@code entidad} es {@code null}
     */
    public static CompraDto mapFrom (CompraEntidad entidad, UsuarioDto usuario, JuegoDto juego){
        if(entidad == null)
            return null;

        return new CompraDto(
                entidad.getId(),
                entidad.getIdUsuario(),
                entidad.getIdJuego(),
                usuario,
                juego,
                entidad.getFechaCompra(),
                entidad.getPrecioSinDescuento(),
                entidad.getDescuento(),
                entidad.getMetodopago(),
                entidad.getEstado());

    }

    /**
     * Convierte una {@link ReseniaEntidad} en un {@link ReseniaDto}.
     *
     * @param entidad entidad de reseña a convertir
     * @return DTO equivalente, o {@code null} si {@code entidad} es {@code null}
     */
    public static ReseniaDto mapFrom (ReseniaEntidad entidad){
        if(entidad == null)
            return null;

        return new ReseniaDto(
                entidad.getId(),
                entidad.getIdUsuario(),
                entidad.getIdJuego(),
                entidad.isRecomendado(),
                entidad.getTextoResenia(),
                entidad.getHorasHastaResenia(),
                entidad.getFechaPublicacion(),
                entidad.getFechaUltimaEdicion(),
                entidad.getEstado());

    }

}
