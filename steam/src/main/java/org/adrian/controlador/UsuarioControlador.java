package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.UsuarioDto;
import org.adrian.modelo.entidad.UsuarioEntidad;
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

        var usuarioEntidad = nuevoUsuario.orElseThrow(() -> new ValidationExcepcion(errores));

        return Mapper.mapFrom(usuarioEntidad);

    }

    public UsuarioDto consultarPerfilUsuarioPorId(Long id) throws ValidationExcepcion{

        var errores = new ArrayList<ErrorDto>();

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

        var usuarioABuscarOpt = usuarioRepo.obtenerTodos().stream().filter(U -> Objects.equals(U.nombre(), nombre)).toList();


        if(usuarioABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEncontrado = usuarioABuscarOpt.getFirst();

        return Mapper.mapFrom(usuarioEncontrado);
    }

    public Double aniadirSaldoCarteraUsuario(Long id, Double cantidadAniadir) throws ValidationExcepcion{

        var errores = new ArrayList<ErrorDto>();

        var usuarioABuscarOpt = usuarioRepo.obtenerPorId(id);

        if(usuarioABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("idUsuario", ErrorType.NO_ENCONTRADO));
        }

        var usuarioEncontrado = usuarioABuscarOpt.get();

        if (cantidadAniadir < 0){
            errores.add(new ErrorDto("cantidadAniadir", ErrorType.NO_ENCONTRADO));
        }

        if(!(cantidadAniadir < 5.00 || cantidadAniadir > 500.00)){
            errores.add(new ErrorDto("cantidadAniadir", ErrorType.FORMATO_INVALIDO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var nuevoSaldo = usuarioEncontrado.saldoCartera() + cantidadAniadir;

        usuarioRepo.actualizar(id,new UsuarioForm(usuarioEncontrado.nombre(),usuarioEncontrado.email(),
                usuarioEncontrado.contrasenia(),usuarioEncontrado.nombreReal(),usuarioEncontrado.pais(),
                usuarioEncontrado.fechaNacimiento(), Optional.ofNullable(usuarioEncontrado.avatar()), nuevoSaldo, usuarioEncontrado.estado()));

        return nuevoSaldo; //Cambiar a dto
    }


}





