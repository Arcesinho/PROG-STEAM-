package org.adrian.repositorio.hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.adrian.modelo.entidad.CompraEntidad;
import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.form.CompraForm;
import org.adrian.repositorio.interfaces.ICompraRepo;
import org.adrian.transaction.ISesionManager;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CompraHibernate implements ICompraRepo {

    private ISesionManager sm;
    private static Long idCounter = 1L;

    public CompraHibernate(ISesionManager sm) {
        this.sm = sm;
    }

    private LocalDateTime fechaCompra() {
        return LocalDateTime.now();
    }

    @Override
    public Optional<CompraEntidad> crear(CompraForm form) {
        var session = sm.getSession();

        var compra = new CompraEntidad(idCounter++, form.idUsuario(), form.idJuego(), fechaCompra(), form.precioSinDescuento(), form.descuento().orElse(0), form.metodopago(), form.estado().orElse(ESTADOCOMPRA.PENDIENTE));
        session.persist(compra);

        return Optional.of(compra);
    }

    @Override
    public Optional<CompraEntidad> obtenerPorId(Long id) {
        var session = sm.getSession();

        return Optional.of(session.find(CompraEntidad.class, id));
    }

    @Override
    public List<CompraEntidad> obtenerTodos() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CompraEntidad> cq = cb.createQuery(CompraEntidad.class);
        Root<CompraEntidad> root = cq.from(CompraEntidad.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<CompraEntidad> actualizar(Long id, CompraForm form) {
        var session = sm.getSession();
        var compraOpt = this.obtenerPorId(id);
        if (compraOpt.isEmpty()) {
            throw new IllegalArgumentException("Compra no encontrado");
        }

        var compraExistente = compraOpt.get();
        var compraActualizada = new CompraEntidad(id, form.idUsuario(), form.idJuego(), compraExistente.getFechaCompra(), form.precioSinDescuento(), form.descuento().orElse(0), form.metodopago(), form.estado().orElse(ESTADOCOMPRA.COMPLETADA));
        session.merge(compraActualizada);

        return Optional.of(compraActualizada);
    }

    @Override
    public List<CompraEntidad> obtenerPorUsuario(Long idUsuario) {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CompraEntidad> cq = cb.createQuery(CompraEntidad.class);
        Root<CompraEntidad> root = cq.from(CompraEntidad.class);

        cq.select(root).where(cb.equal(root.get("idUsuario"), idUsuario));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sm.getSession();

        var compra = this.obtenerPorId(id);
        if (compra.isEmpty())
            return false;

        session.remove(compra.get());

        return true;
    }

}
