package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.UsuarioEntidad;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.interfaces.IUsuarioRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepoImplementacionMemoria implements IUsuarioRepo {


    private static final List<UsuarioEntidad> usuarios = new ArrayList<>();
    private static Long idCounter = 1L;

    private LocalDateTime fechaRegistro() {
        return LocalDateTime.now();
    }


    @Override
    public Optional<UsuarioEntidad> crear(UsuarioForm form) {

        var usuario = new UsuarioEntidad(idCounter++, form.nombreUsuario(), form.email(), form.contrasenia(), form.nombreReal(), form.pais(), form.fechaNacimiento(), fechaRegistro(), form.avatar().orElse(null), form.saldoCartera(), form.estado());
        usuarios.add(usuario);

        return Optional.of(usuario);
    }


    @Override
    public Optional<UsuarioEntidad> obtenerPorId(Long id) {
        return usuarios.stream()
                .filter(u -> u.id().equals(id))
                .findFirst();
    }

    @Override
    public List<UsuarioEntidad> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public Optional<UsuarioEntidad> actualizar(Long id, UsuarioForm form) {
        var usuarioOpt = obtenerPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        UsuarioEntidad usuarioExistente = usuarioOpt.get();
        var usuarioActualizado = new UsuarioEntidad(id, form.nombreUsuario(), form.email(), form.contrasenia(), form.nombreReal(), form.pais(), form.fechaNacimiento(), usuarioExistente.fechaRegistro(), form.avatar().orElse(null), form.saldoCartera(), form.estado());
        usuarios.removeIf(u -> u.id().equals(id));
        usuarios.add(usuarioActualizado);

        return Optional.of(usuarioActualizado);
    }

    @Override
    public boolean eliminar(Long id) {
        return usuarios.removeIf(u -> u.id().equals(id));
    }
}
