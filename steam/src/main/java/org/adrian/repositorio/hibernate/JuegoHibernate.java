package org.adrian.repositorio.hibernate;

import java.util.List;
import java.util.Optional;

import org.adrian.modelo.entidad.JuegoEntidad;
import org.adrian.modelo.form.JuegoForm;
import org.adrian.repositorio.interfaces.IJuegoRepo;
import org.adrian.transaction.ISesionManager;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class JuegoHibernate implements IJuegoRepo {

    private ISesionManager sm;
    public JuegoHibernate(ISesionManager sm) {
        this.sm = sm;
    }
    private static Long idCounter = 1L;

    @Override
    public Optional<JuegoEntidad> crear(JuegoForm form) {
        var session = sm.getSession();

        var juego = new JuegoEntidad(idCounter++, form.tituloJuego(), form.descripcion().orElse(null), form.desarrollador(), form.fechaLanzamiento(), form.precioBase(), form.descuentoActual().orElse(null), form.idiomas().orElse(null), form.estado(), form.pegi(), form.categoria());
        session.persist(juego);

        return Optional.of(juego);
    }

    @Override
    public Optional<JuegoEntidad> obtenerPorId(Long id) {
        var session = sm.getSession();

        return Optional.of(session.find(JuegoEntidad.class, id));
    }
    
    @Override
    public List<JuegoEntidad> obtenerTodos() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<JuegoEntidad> cq = cb.createQuery(JuegoEntidad.class);
        Root<JuegoEntidad> root = cq.from(JuegoEntidad.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<JuegoEntidad> actualizar(Long id, JuegoForm form) {
        var session = sm.getSession();
        var juegoOpt = this.obtenerPorId(id);

        if (juegoOpt.isEmpty())
            return Optional.empty();

        session.merge(new JuegoEntidad(id, form.tituloJuego(), form.descripcion().orElse(null), form.desarrollador(), form.fechaLanzamiento(), form.precioBase(), form.descuentoActual().orElse(null), form.idiomas().orElse(null), form.estado(), form.pegi(), form.categoria()));

        return obtenerPorId(id);
    }
    
    @Override
    public boolean eliminar(Long id) {
        var session = sm.getSession();

        var juego = this.obtenerPorId(id);
        if (juego.isEmpty())
            return false;

        session.remove(juego);

        return true;
    }

}
