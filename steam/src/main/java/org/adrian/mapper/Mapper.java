package org.adrian.mapper;


import org.adrian.modelo.dto.*;
import org.adrian.modelo.entidad.*;

import java.util.Optional;

public class Mapper {

    public static UsuarioDto mapFrom(UsuarioEntidad entidad){

        if(entidad == null)
            return null;

        return new UsuarioDto(
                entidad.id(),
                entidad.nombre(),
                entidad.email(),
                entidad.nombreReal(),
                entidad.pais(),
                entidad.fechaNacimiento(),
                entidad.fechaRegistro(),
                entidad.avatar(),
                entidad.saldoCartera(),
                entidad.estado());
    }

    public static JuegoDto mapFrom(JuegoEntidad entidad){

        if(entidad == null)
            return null;

        return new JuegoDto(
                entidad.id(),
                entidad.tituloJuego(),
                entidad.descripcion(),
                entidad.desarrollador(),
                entidad.fechaLanzamiento(),
                entidad.precioBase(),
                entidad.descuentoActual(),
                entidad.idiomas(),
                entidad.estado(),
                entidad.pegi(),
                entidad.categoria());
    }

    public static BibliotecaDto mapFrom(BibliotecaEntidad entidad){

        if(entidad == null)
            return null;

        return new BibliotecaDto(
                entidad.id(),
                entidad.idUsuario(),
                entidad.idJuego(),
                entidad.fechaAdquisicion(),
                entidad.horasJuego(),
                entidad.ultimaFechaJuego(),
                entidad.estadoInstalacion());

    }

    public static CompraDto mapFrom (CompraEntidad entidad){
        if(entidad == null)
            return null;

        return new CompraDto(
                entidad.id(),
                entidad.idUsuario(),
                entidad.idJuego(),
                entidad.fechaCompra(),
                entidad.precioSinDescuento(),
                entidad.descuento(),
                entidad.metodopago(),
                entidad.estado());

    }

    public static ReseniaDto mapFrom (ReseniaEntidad entidad){
        if(entidad == null)
            return null;

        return new ReseniaDto(
                entidad.id(),
                entidad.idUsuario(),
                entidad.idJuego(),
                entidad.recomendado(),
                entidad.textoResenia(),
                entidad.horasHastaResenia(),
                entidad.fechaPublicacion(),
                entidad.fechaUltimaEdicion(),
                entidad.estado());

    }

}
