package org.adrian.repositorio.implementacionMemoria;

import org.adrian.modelo.entidad.UsuarioEntidad;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.interfaces.IUsuarioRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de usuarios.
 * Almacena las entidades en una lista estática y genera identificadores secuenciales.
 * Esta implementación es útil para pruebas y prototipos; no persiste datos entre ejecuciones.
 */
public class UsuarioRepoImplementacionMemoria implements IUsuarioRepo {


    private static final List<UsuarioEntidad> usuarios = new ArrayList<>();
    private static Long idCounter = 1L;

    /** @return la fecha y hora actuales usada como fecha de registro del usuario */
    private LocalDateTime fechaRegistro() {
        return LocalDateTime.now();
    }


    @Override
    public Optional<UsuarioEntidad> crear(UsuarioForm form) {

        var usuario = new UsuarioEntidad(idCounter++, form.nombreUsuario(), form.email(), form.contrasenia(), form.nombreReal(),
                form.pais(), form.fechaNacimiento(), fechaRegistro(), form.avatar().orElse(null), form.saldoCartera(), form.estado());
        usuarios.add(usuario);

        return Optional.of(usuario);
    }


    @Override
    public Optional<UsuarioEntidad> obtenerPorId(Long id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
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
        var usuarioActualizado = new UsuarioEntidad(id, form.nombreUsuario(), form.email(), form.contrasenia(), form.nombreReal(), form.pais(), form.fechaNacimiento(), usuarioExistente.getFechaRegistro(), form.avatar().orElse(null), form.saldoCartera(), form.estado());
        usuarios.removeIf(u -> u.getId().equals(id));
        usuarios.add(usuarioActualizado);

        return Optional.of(usuarioActualizado);
    }

    @Override
    public boolean eliminar(Long id) {
        return usuarios.removeIf(u -> u.getId().equals(id));
    }
}
