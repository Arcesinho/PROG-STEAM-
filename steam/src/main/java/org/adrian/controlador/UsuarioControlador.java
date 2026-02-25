package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.UsuarioDto;
import org.adrian.modelo.entidad.UsuarioEntidad;
import org.adrian.modelo.form.ErrorDto;
import org.adrian.modelo.form.ErrorType;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.interfaces.*;

import java.util.List;
import java.util.Optional;


public class UsuarioControlador {

    private final IUsuarioRepo usuarioRepo;


    public UsuarioControlador(IUsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public UsuarioDto registrarNuevoUsuario(UsuarioForm form) throws ValidationExcepcion {

        List<ErrorDto> errores = form.validar();

        //Validaciones si el email ya existe

        boolean emailExiste = usuarioRepo.obtenerTodos().stream()
                .anyMatch(u -> u.email().equalsIgnoreCase(form.email()));
        if (emailExiste) {
            errores.add(new ErrorDto("email", ErrorType.DUPLICADO));
        }

        //Validaciones si el nombreUsuario ya existe

        boolean nombreExiste = usuarioRepo.obtenerTodos().stream()
                .anyMatch(u -> u.nombre().equalsIgnoreCase(form.nombreUsuario()));
        if (nombreExiste) {
            errores.add(new ErrorDto("nombreUsuario", ErrorType.DUPLICADO));
        }

        //Si hay errores los devolvemos

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        Optional<UsuarioEntidad> nuevoUsuario = usuarioRepo.crear(form);

        //Esto es para que el paquete deje de serlo, ya que no se puede pasar un Optional al mapper

        UsuarioEntidad usuarioEntidad = nuevoUsuario.orElseThrow(() -> new RuntimeException("Fallo al desencapsular la Entidad Usuario"));

        return Mapper.mapFrom(usuarioEntidad);

    }

}





