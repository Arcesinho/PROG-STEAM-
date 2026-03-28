package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.UsuarioDto;
import org.adrian.modelo.entidad.UsuarioEntidad;
import org.adrian.modelo.enums.ESTADOCUENTA;
import org.adrian.modelo.form.ErrorDto;
import org.adrian.modelo.form.ErrorType;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(form.email()));
        if (emailExiste) {
            errores.add(new ErrorDto("email", ErrorType.DUPLICADO));
        }

        //Validaciones si el nombreUsuario ya existe

        boolean nombreExiste = usuarioRepo.obtenerTodos().stream()
                .anyMatch(u -> u.getNombre().equalsIgnoreCase(form.nombreUsuario()));
        if (nombreExiste) {
            errores.add(new ErrorDto("nombreUsuario", ErrorType.DUPLICADO));
        }

        //Si hay errores los devolvemos

        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }

        Optional<UsuarioEntidad> nuevoUsuario = usuarioRepo.crear(form);

        //Esto es para que el paquete deje de serlo, ya que no se puede pasar un Optional al mapper

        var usuarioEntidad = nuevoUsuario.orElseThrow(() -> new ValidationExcepcion(errores));

        return Mapper.mapFrom(usuarioEntidad);

    }

    public UsuarioDto consultarPerfilUsuarioPorId(Long id) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var usuarioABuscarOpt = usuarioRepo.obtenerPorId(id);

        if(usuarioABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idUsuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEncontrado = usuarioABuscarOpt.get();

        return Mapper.mapFrom(usuarioEncontrado);

    }

    public UsuarioDto consultarPerfilUsuarioPorNombre(String nombre) throws ValidationExcepcion{

        var errores = new ArrayList<ErrorDto>();

        var usuarioABuscarOpt = usuarioRepo.obtenerTodos().stream().filter(U -> Objects.equals(U.getNombre(), nombre)).toList();


        if(usuarioABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEncontrado = usuarioABuscarOpt.getFirst();

        return Mapper.mapFrom(usuarioEncontrado);
    }

    public UsuarioDto aniadirSaldoCarteraUsuario(Long id, Double cantidadAniadir) throws ValidationExcepcion{

        var errores = new ArrayList<ErrorDto>();

        var usuarioABuscarOpt = usuarioRepo.obtenerPorId(id);

        if(usuarioABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idUsuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEncontrado = usuarioABuscarOpt.get();

        if(usuarioEncontrado.getEstado() != ESTADOCUENTA.ACTIVA){
            errores.add(new ErrorDto("estado", ErrorType.USUARIO_INACTIVO));
        }

        if (cantidadAniadir < 0){
            errores.add(new ErrorDto("cantidadAniadir", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        if (cantidadAniadir < 5.00 || cantidadAniadir > 500.00){
            errores.add(new ErrorDto("cantidadAniadir", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var nuevoSaldo = usuarioEncontrado.getSaldoCartera() + cantidadAniadir;

        var usuarioActualizado = usuarioRepo.actualizar(id,new UsuarioForm(usuarioEncontrado.getNombre(),usuarioEncontrado.getEmail(),
                usuarioEncontrado.getContrasenia(),usuarioEncontrado.getNombreReal(),usuarioEncontrado.getPais(),
                usuarioEncontrado.getFechaNacimiento(), Optional.ofNullable(usuarioEncontrado.getAvatar()), nuevoSaldo, usuarioEncontrado.getEstado()));

        var usuarioADevolver = usuarioActualizado.get();

        return Mapper.mapFrom(usuarioADevolver);
    }

    public Double consultarSaldoCarteraUsuario(Long id) throws ValidationExcepcion{

        var errores = new ArrayList<ErrorDto>();

        var usuarioABuscarOpt = usuarioRepo.obtenerPorId(id);

        if(usuarioABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idUsuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEncontrado = usuarioABuscarOpt.get();

        return usuarioEncontrado.getSaldoCartera();

    }
}





