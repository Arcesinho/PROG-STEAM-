package org.adrian.repositorio.hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.entidad.ReseniaEntidad;
import org.adrian.modelo.form.ReseniaForm;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;
import org.adrian.repositorio.interfaces.IReseniaRepo;
import org.adrian.transaction.ISesionManager;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ReseniaHibernate implements IReseniaRepo {

    private ISesionManager sm;
    private static Long idCounter = 1L;

    public ReseniaHibernate(ISesionManager sm) {
        this.sm = sm;
    }

    private LocalDateTime fechaPublicacion() {
        return LocalDateTime.now();
    }

    private LocalDateTime fechaUltimaEdicion() {
        return LocalDateTime.now();
    }

    @Override
    public Optional<ReseniaEntidad> crear(ReseniaForm form) {
        var session = sm.getSession();

        IBibliotecaRepo biblioteca = new BibliotecaHibernate(sm);
        Double horasEnEsteMomento = biblioteca.obtenerHoras(form.idUsuario(), form.idJuego())
                .map(BibliotecaEntidad::getHorasJuego)
                .orElse(0.0);

        var resenia = new ReseniaEntidad(idCounter++, form.idUsuario(), form.idJuego(), form.recomendado(), form.textoResenia(), horasEnEsteMomento, fechaPublicacion(), fechaUltimaEdicion(), form.estado());
        session.persist(resenia);

        return Optional.of(resenia);
    }

    @Override
    public Optional<ReseniaEntidad> obtenerPorId(Long id) {
        var session = sm.getSession();

        return Optional.of(session.find(ReseniaEntidad.class, id));
    }

    @Override
    public List<ReseniaEntidad> obtenerTodos() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReseniaEntidad> cq = cb.createQuery(ReseniaEntidad.class);
        Root<ReseniaEntidad> root = cq.from(ReseniaEntidad.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<ReseniaEntidad> actualizar(Long id, ReseniaForm form) {
        var session = sm.getSession();
        var reseniaOpt = this.obtenerPorId(id);

        if (reseniaOpt.isEmpty())
            return Optional.empty();

        IBibliotecaRepo biblioteca = new BibliotecaHibernate(sm);
        Double horasActualizadas = biblioteca.obtenerHoras(form.idUsuario(), form.idJuego())
                .map(BibliotecaEntidad::getHorasJuego)
                .orElse(reseniaOpt.get().getHorasHastaResenia());

        session.merge(new ReseniaEntidad(id, form.idUsuario(), form.idJuego(), form.recomendado(), form.textoResenia(), horasActualizadas, fechaPublicacion(), fechaUltimaEdicion(), form.estado()));

        return obtenerPorId(id);
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sm.getSession();

        var resenia = this.obtenerPorId(id);
        if (resenia.isEmpty())
            return false;

        session.remove(resenia.get());

        return true;
    }

    @Override
    public Optional<ReseniaEntidad> obtenerPorUsuarioYJuego(Long idUsuario, Long idJuego) {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReseniaEntidad> cq = cb.createQuery(ReseniaEntidad.class);
        Root<ReseniaEntidad> root = cq.from(ReseniaEntidad.class);

        cq.select(root).where(cb.and(cb.equal(root.get("idUsuario"), idUsuario), cb.equal(root.get("idJuego"), idJuego)));

        var result = session.createQuery(cq).getResultList();
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(result.get(0));
    }

}
