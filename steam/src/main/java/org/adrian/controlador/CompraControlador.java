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
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;
import org.adrian.repositorio.interfaces.ICompraRepo;
import org.adrian.repositorio.interfaces.IJuegoRepo;
import org.adrian.repositorio.interfaces.IUsuarioRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Controlador de transacciones de compra de juegos.
 * Gestiona el flujo completo de una compra: creación, pago y reembolso.
 */
public class CompraControlador {

    private final ICompraRepo compraRepo;
    private final IJuegoRepo juegoRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IBibliotecaRepo bibliotecaRepo;

    /**
     * @param compraRepo    repositorio de compras
     * @param juegoRepo     repositorio de juegos
     * @param usuarioRepo   repositorio de usuarios
     * @param bibliotecaRepo repositorio de biblioteca (necesario para validar horas jugadas en reembolsos)
     */
    public CompraControlador(ICompraRepo compraRepo, IJuegoRepo juegoRepo, IUsuarioRepo usuarioRepo, IBibliotecaRepo bibliotecaRepo) {
        this.compraRepo = compraRepo;
        this.juegoRepo = juegoRepo;
        this.usuarioRepo = usuarioRepo;
        this.bibliotecaRepo = bibliotecaRepo;
    }

    /**
     * Inicia el proceso de compra de un juego creando una transacción en estado PENDIENTE.
     * Valida que el usuario esté activo, el juego disponible, no sea una compra duplicada
     * y, si el método de pago es la cartera Steam, que haya saldo suficiente.
     *
     * @param form datos de la compra a realizar
     * @return DTO de la compra creada con estado {@link ESTADOCOMPRA#PENDIENTE}
     * @throws ValidationExcepcion si alguna validación falla
     */
    public CompraDto realizarCompraJuego(CompraForm form) throws ValidationExcepcion{
        List<ErrorDto> errores = new ArrayList<>();
        
        if (form == null) {
            errores.add(new ErrorDto("formulario", ErrorType.REQUERIDO));
            throw new ValidationExcepcion(errores);
        }
        
        errores.addAll(form.validar());

        var usuarioOpt = usuarioRepo.obtenerPorId(form.idUsuario());
        var juegoOpt = juegoRepo.obtenerPorId(form.idJuego());

        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }
        if (juegoOpt.isEmpty()) {
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
        }

        // Verificar compra duplicada: usuario no puede comprar el mismo juego dos veces
        List<CompraEntidad> comprasUsuario = compraRepo.obtenerPorUsuario(form.idUsuario());
        boolean compraDuplicada = comprasUsuario.stream()
                .anyMatch(c -> c.getIdJuego().equals(form.idJuego()));

        if (compraDuplicada) {
            errores.add(new ErrorDto("compra", ErrorType.DUPLICADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        var usuarioEntidad = usuarioOpt.get();
        var juegoEntidad = juegoOpt.get();

        if (usuarioEntidad.getEstado() != ESTADOCUENTA.ACTIVA) {
            errores.add(new ErrorDto("usuario", ErrorType.USUARIO_INACTIVO));
        }

        if (juegoEntidad.getEstado() != ESTADOJUEGO.DISPONIBLE) {
            errores.add(new ErrorDto("juego", ErrorType.JUEGO_NO_DISPONIBLE));
        }

        // Validación de método de pago y saldo
        if (form.metodopago() == METODOPAGOCOMPRA.CARTERA_STEAM) {
            if (usuarioEntidad.getSaldoCartera() < juegoEntidad.getPrecioBase()) {
                errores.add(new ErrorDto("saldoCartera", ErrorType.SALDO_INSUFICIENTE));
            }
        } else if (form.metodopago() == null) {
            errores.add(new ErrorDto("metodoPago", ErrorType.REQUERIDO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        // Crear la compra con estado PENDIENTE
        Optional<CompraEntidad> nuevaCompra = compraRepo.crear(form);
        
        if (nuevaCompra.isEmpty()) {
            errores.add(new ErrorDto("compra", ErrorType.NO_ENCONTRADO));
            throw new ValidationExcepcion(errores);
        }

        var usuarioDto = Mapper.mapFrom(usuarioEntidad);
        var juegoDto = Mapper.mapFrom(juegoEntidad);
        var compra = nuevaCompra.get();

        return Mapper.mapFrom(compra, usuarioDto, juegoDto);

    }

    /**
     * Procesa el pago de una compra en estado PENDIENTE y la marca como COMPLETADA.
     * Si el método de pago es {@link METODOPAGOCOMPRA#CARTERA_STEAM} descuenta el importe
     * del saldo del usuario; para otros métodos requiere {@code datosPago} no nulo.
     *
     * @param idCompra  identificador de la compra a procesar
     * @param datosPago datos del medio de pago externo (requerido si no es cartera Steam)
     * @return DTO de la compra con estado {@link ESTADOCOMPRA#COMPLETADA}
     * @throws ValidationExcepcion si la compra no existe, no está pendiente o no hay saldo suficiente
     */
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
            throw new ValidationExcepcion(errores);
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
                usuario.getNombre(),
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
        if (juegoOpt.isEmpty()) {
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
            throw new ValidationExcepcion(errores);
        }
        var juegoDto = Mapper.mapFrom(juegoOpt.get());

        return Mapper.mapFrom(updatedCompraOpt.get(), usuarioDto, juegoDto);
    }

    /**
     * Ver información completa de una transacción
     * @param idCompra ID de la compra a consultar
     * @param idUsuario ID del usuario para verificar pertenencia
     * @return CompraDTO con los detalles de la compra
     * @throws ValidationExcepcion si la compra no existe o no pertenece al usuario
     */
    public CompraDto consultarDetallesCompra(Long idCompra, Long idUsuario) throws ValidationExcepcion {
        List<ErrorDto> errores = new ArrayList<>();

        var compraOpt = compraRepo.obtenerPorId(idCompra);
        if (compraOpt.isEmpty()) {
            errores.add(new ErrorDto("compra", ErrorType.NO_ENCONTRADO));
            throw new ValidationExcepcion(errores);
        }

        var compra = compraOpt.get();

        if (!compra.getIdUsuario().equals(idUsuario)) {
            errores.add(new ErrorDto("compra", ErrorType.NO_ENCONTRADO));
            throw new ValidationExcepcion(errores);
        }

        var usuarioOpt = usuarioRepo.obtenerPorId(idUsuario);
        var juegoOpt = juegoRepo.obtenerPorId(compra.getIdJuego());

        if (usuarioOpt.isEmpty() || juegoOpt.isEmpty()) {
            errores.add(new ErrorDto("compra", ErrorType.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }


        var usuarioDto = Mapper.mapFrom(usuarioOpt.orElse(null));
        var juegoDto = Mapper.mapFrom(juegoOpt.orElse(null));

        return Mapper.mapFrom(compraOpt.orElse(null), usuarioDto, juegoDto);
    }

    /**
     * Devolver una compra y reintegrar el dinero a la cartera
     * @param idCompra ID de la compra a reembolsar
     * @param motivoReembolso Motivo del reembolso
     * @return CompraDTO con el estado actualizado
     * @throws ValidationExcepcion si no cumple con las validaciones
     */
    public CompraDto solicitarReembolso(Long idCompra, String motivoReembolso) throws ValidationExcepcion {
        List<ErrorDto> errores = new ArrayList<>();
        final int PLAZO_REEMBOLSO_DIAS = 14;
        final int HORAS_JUGADAS_LIMITE = 2;

        var compraOpt = compraRepo.obtenerPorId(idCompra);
        if (compraOpt.isEmpty()) {
            errores.add(new ErrorDto("compra", ErrorType.NO_ENCONTRADO));
            throw new ValidationExcepcion(errores);
        }
        var compra = compraOpt.get();

        // Validar que la compra esté completada
        if (compra.getEstado() != ESTADOCOMPRA.COMPLETADA) {
            errores.add(new ErrorDto("compra", ErrorType.FORMATO_INVALIDO));
        }

        // Validar que esté dentro del plazo de reembolso
        if (compra.getFechaCompra() != null) {
            long diasTranscurridos = ChronoUnit.DAYS.between(compra.getFechaCompra(), LocalDateTime.now());
            if (diasTranscurridos > PLAZO_REEMBOLSO_DIAS) {
                errores.add(new ErrorDto("reembolso", ErrorType.FUERA_DE_PLAZO));
            }
        }

        var bibliotecaOpt = bibliotecaRepo.obtenerPorIdUsuarioIdJuego(compra.getIdUsuario(), compra.getIdJuego());
        if (bibliotecaOpt.isEmpty()) {
            errores.add(new ErrorDto("biblioteca", ErrorType.NO_ENCONTRADO));
        } else {
            var biblioteca = bibliotecaOpt.get();
            if (biblioteca.getHorasJuego() != null && biblioteca.getHorasJuego() > HORAS_JUGADAS_LIMITE) {
                errores.add(new ErrorDto("horasJuego", ErrorType.LIMITE_EXCEDIDO));
            }
        }

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        var usuarioOpt = usuarioRepo.obtenerPorId(compra.getIdUsuario());
        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
            throw new ValidationExcepcion(errores);
        }

        var usuario = usuarioOpt.get();
        double precioReembolso = compra.getPrecioSinDescuento() * (1 - compra.getDescuento() / 100.0);

        // Solo devolver saldo a la cartera si la compra se pagó con cartera Steam
        if (compra.getMetodopago() == METODOPAGOCOMPRA.CARTERA_STEAM) {
            var usuarioForm = new UsuarioForm(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getContrasenia(),
                usuario.getNombreReal(),
                usuario.getPais(),
                usuario.getFechaNacimiento(),
                Optional.ofNullable(usuario.getAvatar()),
                usuario.getSaldoCartera() + precioReembolso,
                usuario.getEstado()
            );
            usuarioRepo.actualizar(usuario.getId(), usuarioForm);
            usuario = usuarioRepo.obtenerPorId(usuario.getId()).get();
        }

        // Cambiar estado de la compra a reembolsada
        var compraForm = new CompraForm(
            compra.getId(),
            compra.getIdUsuario(),
            compra.getIdJuego(),
            compra.getPrecioSinDescuento(),
            Optional.of(compra.getDescuento()),
            compra.getMetodopago(),
            Optional.of(ESTADOCOMPRA.REEMBOLSADA)
        );
        var compraActualizada = compraRepo.actualizar(compra.getId(), compraForm);

        var usuarioDto = Mapper.mapFrom(usuario);
        var juegoOpt = juegoRepo.obtenerPorId(compra.getIdJuego());
        if (juegoOpt.isEmpty()) {
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
            throw new ValidationExcepcion(errores);
        }
        var juegoDto = Mapper.mapFrom(juegoOpt.get());

        return Mapper.mapFrom(compraActualizada.get(), usuarioDto, juegoDto);
    }

}
