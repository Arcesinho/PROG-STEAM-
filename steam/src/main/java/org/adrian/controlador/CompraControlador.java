package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.CompraDto;
import org.adrian.modelo.entidad.CompraEntidad;
import org.adrian.modelo.enums.ESTADOCUENTA;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.METODOPAGOCOMPRA;
import org.adrian.modelo.form.CompraForm;
import org.adrian.modelo.form.ErrorDto;
import org.adrian.modelo.form.ErrorType;
import org.adrian.repositorio.interfaces.ICompraRepo;
import org.adrian.repositorio.interfaces.IJuegoRepo;
import org.adrian.repositorio.interfaces.IUsuarioRepo;

import java.util.List;
import java.util.Optional;

public class CompraControlador {

    private final ICompraRepo compraRepo;
    private final IJuegoRepo juegoRepo;
    private final IUsuarioRepo usuarioRepo;

    public CompraControlador(ICompraRepo compraRepo, IJuegoRepo juegoRepo, IUsuarioRepo usuarioRepo) {
        this.compraRepo = compraRepo;
        this.juegoRepo = juegoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public CompraDto realizarCompraJuego(CompraForm form) throws ValidationExcepcion{

        List<ErrorDto> errores = form.validar();

        var usuarioOpt = usuarioRepo.obtenerPorId(form.idUsuario());
        var juegoOpt = juegoRepo.obtenerPorId(form.idJuego());

        if(usuarioOpt.isEmpty()){
            errores.add(new ErrorDto("usuarioOpt", ErrorType.NO_ENCONTRADO));
        }
        if(juegoOpt.isEmpty()){
            errores.add(new ErrorDto("juegoOpt", ErrorType.NO_ENCONTRADO));
        }

        boolean usuarioExiste = compraRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(form.idUsuario()));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.DUPLICADO));
        }

        boolean juegoExiste = compraRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdJuego().equals(form.idJuego()));

        if(!juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.DUPLICADO));
        }

        var usuarioEntidad = usuarioOpt.get();
        var juegoEntidad = juegoOpt.get();

        if(!(usuarioEntidad.getEstado() == ESTADOCUENTA.ACTIVA)){
            errores.add(new ErrorDto("usuarioEntidad", ErrorType.REQUERIDO));
        }

        if(!(juegoEntidad.getEstado() == ESTADOJUEGO.DISPONIBLE)){
            errores.add(new ErrorDto("juegoEntidad", ErrorType.REQUERIDO));
        }

        if(form.metodopago() == METODOPAGOCOMPRA.CARTERA_STEAM){
            if(usuarioEntidad.getSaldoCartera() <= juegoEntidad.getPrecioBase()){
                errores.add(new ErrorDto("saldoCartera", ErrorType.VALOR_DEMASIADO_BAJO));
            }
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        Optional<CompraEntidad> nuevaCompra = compraRepo.crear(form);

        var usuarioDto = Mapper.mapFrom(usuarioEntidad);
        var juegoDto = Mapper.mapFrom(juegoEntidad);

        var compra = nuevaCompra.get();

        return Mapper.mapFrom(compra, usuarioDto, juegoDto);

    }

}
