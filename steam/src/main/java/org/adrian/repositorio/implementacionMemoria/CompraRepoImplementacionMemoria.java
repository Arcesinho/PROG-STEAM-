package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.CompraEntidad;
import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.form.CompraForm;
import org.adrian.repositorio.interfaces.ICompraRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de compras.
 * Almacena las entidades en una lista estática y genera identificadores secuenciales.
 * Al crear una compra, la fecha se registra automáticamente con el instante actual
 * y el estado inicial es {@link ESTADOCOMPRA#PENDIENTE} si no se especifica otro.
 * Esta implementación es útil para pruebas y prototipos; no persiste datos entre ejecuciones.
 */
public class CompraRepoImplementacionMemoria implements ICompraRepo {

    private static final List<CompraEntidad> compras = new ArrayList<>();
    private static Long idCounter = 1L;

    /** @return la fecha y hora actuales usada como fecha de la compra */
    private LocalDateTime fechaCompra() {
        return LocalDateTime.now();
    }


    @Override
    public Optional<CompraEntidad> crear(CompraForm form) {

        var compra = new CompraEntidad(idCounter++, form.idUsuario(), form.idJuego(), fechaCompra(), form.precioSinDescuento(), form.descuento().orElse(0), form.metodopago(), form.estado().orElse(ESTADOCOMPRA.PENDIENTE));
        compras.add(compra);

        return Optional.of(compra);
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

        var compraExistente = compraOpt.get();
        var compraActualizada = new CompraEntidad(id, form.idUsuario(), form.idJuego(), compraExistente.getFechaCompra(), form.precioSinDescuento(), form.descuento().orElse(0), form.metodopago(), form.estado().orElse(ESTADOCOMPRA.COMPLETADA));
        compras.removeIf(u -> u.getId().equals(id));
        compras.add(compraActualizada);

        return Optional.of(compraActualizada);
    }

    /**
     * Devuelve todas las compras realizadas por un usuario concreto.
     *
     * @param idUsuario identificador del usuario
     * @return lista de compras del usuario; vacía si no tiene ninguna
     */
    @Override
    public List<CompraEntidad> obtenerPorUsuario(Long idUsuario) {
        return compras.stream()
                .filter(c -> c.getIdUsuario().equals(idUsuario))
                .toList();
    }

    @Override
    public boolean eliminar(Long id) {
        return compras.removeIf(u -> u.getId().equals(id));
    }

}
