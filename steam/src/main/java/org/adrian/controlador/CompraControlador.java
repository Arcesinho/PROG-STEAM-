package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.CompraDto;
import org.adrian.modelo.entidad.CompraEntidad;
import org.adrian.modelo.enums.ESTADOCOMPRA;
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

        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }
        if (juegoOpt.isEmpty()) {
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
        }

        List<CompraEntidad> compras = compraRepo.obtenerTodos();
        boolean compraDuplicada = compras.stream()
                .anyMatch(c -> c.getIdUsuario().equals(form.idUsuario()) && c.getIdJuego().equals(form.idJuego()));

        if (compraDuplicada) {
            errores.add(new ErrorDto("compra", ErrorType.DUPLICADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        var usuarioEntidad = usuarioOpt.get();
        var juegoEntidad = juegoOpt.get();

        if (!(usuarioEntidad.getEstado() == ESTADOCUENTA.ACTIVA)) {
            errores.add(new ErrorDto("usuario", ErrorType.USUARIO_INACTIVO));
        }

        if (!(juegoEntidad.getEstado() == ESTADOJUEGO.DISPONIBLE)) {
            errores.add(new ErrorDto("juego", ErrorType.JUEGO_NO_DISPONIBLE));
        }

        if (form.metodopago() == METODOPAGOCOMPRA.CARTERA_STEAM) {
            if (usuarioEntidad.getSaldoCartera() < juegoEntidad.getPrecioBase()) {
                errores.add(new ErrorDto("saldoCartera", ErrorType.SALDO_INSUFICIENTE));
            }
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        Optional<CompraEntidad> nuevaCompra = compraRepo.crear(form);

        var usuarioDto = Mapper.mapFrom(usuarioEntidad);
        var juegoDto = Mapper.mapFrom(juegoEntidad);

        var compra = nuevaCompra.get();

        return Mapper.mapFrom(compra, usuarioDto, juegoDto);

    }

    public CompraDto procesarPago(Long idCompra, String datosPago) throws ValidationExcepcion {
        List<ErrorDto> errores = new ArrayList<>();

        var compraOpt = compraRepo.obtenerPorId(idCompra);
        if (compraOpt.isEmpty()) {
            errores.add(new ErrorDto("compra", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        var compra = compraOpt.get();

        if (compra.getEstado() != ESTADOCOMPRA.PENDIENTE) {
            errores.add(new ErrorDto("compra", ErrorType.FORMATO_INVALIDO)); // or add ESTADO_INVALIDO
        }

        var usuarioOpt = usuarioRepo.obtenerPorId(compra.getIdUsuario());
        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }

        var usuario = usuarioOpt.get();

        double precioFinal = compra.getPrecioSinDescuento() * (1 - compra.getDescuento() / 100.0);

        if (compra.getMetodopago() == METODOPAGOCOMPRA.CARTERA_STEAM) {
            if (usuario.getSaldoCartera() < precioFinal) {
                errores.add(new ErrorDto("saldoCartera", ErrorType.SALDO_INSUFICIENTE));
            }
        } else {
            // for other methods, assume datosPago is required
            if (datosPago == null || datosPago.isEmpty()) {
                errores.add(new ErrorDto("datosPago", ErrorType.REQUERIDO));
            }
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        if (compra.getMetodopago() == METODOPAGOCOMPRA.CARTERA_STEAM) {
            var usuarioForm = new UsuarioForm(
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                usuario.getContrasenia(),
                usuario.getNombreReal(),
                usuario.getPais(),
                usuario.getFechaNacimiento(),
                Optional.ofNullable(usuario.getAvatar()),
                usuario.getSaldoCartera() - precioFinal,
                usuario.getEstado()
            );
            usuarioRepo.actualizar(usuario.getId(), usuarioForm);
            usuario = usuarioRepo.obtenerPorId(usuario.getId()).get();
        }

        var compraForm = new CompraForm(
            compra.getId(),
            compra.getIdUsuario(),
            compra.getIdJuego(),
            compra.getPrecioSinDescuento(),
            Optional.of(compra.getDescuento()),
            compra.getMetodopago(),
            Optional.of(ESTADOCOMPRA.COMPLETADA)
        );
        var updatedCompraOpt = compraRepo.actualizar(compra.getId(), compraForm);

        var usuarioDto = Mapper.mapFrom(usuario);
        var juegoOpt = juegoRepo.obtenerPorId(compra.getIdJuego());
        var juegoDto = Mapper.mapFrom(juegoOpt.get());

        return Mapper.mapFrom(updatedCompraOpt.get(), usuarioDto, juegoDto);
    }

}
