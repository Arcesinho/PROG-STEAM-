package org.adrian.repositorio.hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.adrian.modelo.entidad.UsuarioEntidad;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.transaction.ISesionManager;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.adrian.repositorio.interfaces.IUsuarioRepo;

public class UsuarioHibernate implements IUsuarioRepo {

    private ISesionManager sm;
    private static Long idCounter = 1L;


    public UsuarioHibernate(ISesionManager sm) {
        this.sm = sm;
    }

    /** @return la fecha y hora actuales usada como fecha de registro del usuario */
    private LocalDateTime fechaRegistro() {
        return LocalDateTime.now();
    }

    @Override
    public Optional<UsuarioEntidad> crear(UsuarioForm form) {
        var session = sm.getSession();

        var usuario = new UsuarioEntidad(idCounter++, form.nombreUsuario(), form.email(), form.contrasenia(), form.nombreReal(),
                form.pais(), form.fechaNacimiento(), fechaRegistro(), form.avatar().orElse(null), form.saldoCartera(),
                 form.estado());
        session.persist(usuario);

        return Optional.of(usuario);
    }

    @Override
    public Optional<UsuarioEntidad> obtenerPorId(Long id) {
        var session = sm.getSession();

        return Optional.of(session.find(UsuarioEntidad.class, id));
    }

    @Override
    public List<UsuarioEntidad> obtenerTodos() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntidad> cq = cb.createQuery(UsuarioEntidad.class);
        Root<UsuarioEntidad> root = cq.from(UsuarioEntidad.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<UsuarioEntidad> actualizar(Long id, UsuarioForm form) {
        var session = sm.getSession();
        var usuarioOpt = this.obtenerPorId(id);

        if (usuarioOpt.isEmpty())
            return Optional.empty();

        session.merge(new UsuarioEntidad(id, form.nombreUsuario(), form.email(), form.contrasenia(), form.nombreReal(),
                form.pais(), form.fechaNacimiento(),fechaRegistro(), form.avatar().orElse(null), form.saldoCartera(),
                form.estado()));

        return obtenerPorId(id);
    }

    @Override
    public boolean eliminar(Long id) {
        var session = sm.getSession();

        var usuario = this.obtenerPorId(id);
        if (usuario.isEmpty())
            return false;

        session.remove(usuario);

        return true;
    }



    
}
