package org.adrian.repositorio.hibernate;

import java.util.List;
import java.util.Optional;

import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.form.BibliotecaForm;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;
import org.adrian.transaction.ISesionManager;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class BibliotecaHibernate implements IBibliotecaRepo {

    private ISesionManager sm;
    private static Long idCounter = 1L;

    public BibliotecaHibernate(ISesionManager sm) {
        this.sm = sm;
    }

    @Override
    public Optional<BibliotecaEntidad> crear(BibliotecaForm form) {
        var session = sm.getSession();

        var biblioteca = new BibliotecaEntidad(idCounter++, form.idUsuario(), form.idJuego(), form.fechaAdquisicion(), form.horasJuego(), form.ultimaFechaJuego().orElse(null), form.estadoInstalacion());
        session.persist(biblioteca);

        return Optional.of(biblioteca);
    }

    @Override
    public Optional<BibliotecaEntidad> obtenerPorId(Long id) {
        var session = sm.getSession();

        return Optional.of(session.find(BibliotecaEntidad.class, id));
    }

    @Override
    public List<BibliotecaEntidad> obtenerTodos() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntidad> cq = cb.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = cq.from(BibliotecaEntidad.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<BibliotecaEntidad> actualizar(Long id, BibliotecaForm form) {
        var session = sm.getSession();
        var bibliotecaOpt = this.obtenerPorId(id);

        if (bibliotecaOpt.isEmpty())
            throw new IllegalArgumentException("Biblioteca no encontrado");

        session.merge(new BibliotecaEntidad(id, form.idUsuario(), form.idJuego(), form.fechaAdquisicion(), form.horasJuego(), form.ultimaFechaJuego().orElse(null), form.estadoInstalacion()));

        return obtenerPorId(id);
    }

    @Override
    public Optional<BibliotecaEntidad> obtenerHoras(Long idUsuario, Long idJuego) {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntidad> cq = cb.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = cq.from(BibliotecaEntidad.class);

        cq.select(root).where(cb.and(cb.equal(root.get("idUsuario"), idUsuario), cb.equal(root.get("idJuego"), idJuego)));

        var result = session.createQuery(cq).getResultList();
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(result.get(0));
    }

    @Override
    public Optional<BibliotecaEntidad> obtenerPorIdUsuarioIdJuego(Long idUsuario, Long idJuego) {
        return obtenerHoras(idUsuario, idJuego);
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sm.getSession();

        var biblioteca = this.obtenerPorId(id);
        if (biblioteca.isEmpty())
            return false;

        session.remove(biblioteca.get());

        return true;
    }

}
