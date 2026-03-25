package org.adrian.mapper;


import org.adrian.modelo.dto.*;
import org.adrian.modelo.entidad.*;


public class Mapper {

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

    public static CompraDto mapFrom (CompraEntidad entidad){
        if(entidad == null)
            return null;

        return new CompraDto(
                entidad.getId(),
                entidad.getIdUsuario(),
                entidad.getIdJuego(),
                entidad.getFechaCompra(),
                entidad.getPrecioSinDescuento(),
                entidad.getDescuento(),
                entidad.getMetodopago(),
                entidad.getEstado());

    }

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
