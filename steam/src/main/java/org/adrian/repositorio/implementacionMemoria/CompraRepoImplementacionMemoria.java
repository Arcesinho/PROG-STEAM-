package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.CompraEntidad;
import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.form.CompraForm;
import org.adrian.repositorio.interfaces.ICompraRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraRepoImplementacionMemoria implements ICompraRepo {

    private static final List<CompraEntidad> compras = new ArrayList<>();
    private static Long idCounter = 1L;
    private LocalDateTime fechaCompra() {
        LocalDateTime.now();
        return LocalDateTime.now();
    }


    @Override
    public Optional<CompraEntidad> crear(CompraForm form) {

        var juego = new CompraEntidad(idCounter++, form.idUsuario(), form.idJuego(), fechaCompra(), form.precioSinDescuento(), form.descuento().orElse(0), form.metodopago(), form.estado().orElse(ESTADOCOMPRA.PENDIENTE));
        compras.add(juego);

        return Optional.of(juego);
    }

    @Override
    public Optional<CompraEntidad> obtenerPorId(Long id) {
        return compras.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<CompraEntidad> obtenerTodos() {
        return new ArrayList<>(compras);
    }

    @Override
    public Optional<CompraEntidad> actualizar(Long id, CompraForm form) {
        var compraOpt = obtenerPorId(id);
        if (compraOpt.isEmpty()) {
            throw new IllegalArgumentException("Compra no encontrado");
        }

        var compraActualizada = new CompraEntidad(id, form.idUsuario(), form.idJuego(), fechaCompra(), form.precioSinDescuento(), form.descuento().orElse(null), form.metodopago(), ESTADOCOMPRA.COMPLETADA);
        compras.removeIf(u -> u.getId().equals(id));
        compras.add(compraActualizada);

        return Optional.of(compraActualizada);
    }

    @Override
    public boolean eliminar(Long id) {
        return compras.removeIf(u -> u.getId().equals(id));
    }

}
