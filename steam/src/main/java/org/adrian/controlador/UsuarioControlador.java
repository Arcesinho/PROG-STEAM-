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


/**
 * Controlador de operaciones sobre cuentas de usuario.
 * Gestiona el registro, consulta y actualización del saldo de la cartera.
 */
public class UsuarioControlador {

    private final IUsuarioRepo usuarioRepo;


    /**
     * @param usuarioRepo repositorio de usuarios que se usará para las operaciones de persistencia
     */
    public UsuarioControlador(IUsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    /**
     * Registra un nuevo usuario validando que el email y el nombre de usuario sean únicos.
     *
     * @param form datos del nuevo usuario
     * @return DTO con la información del usuario creado
     * @throws ValidationExcepcion si el formulario contiene errores o el email/nombre ya existe
     */
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


        var usuarioEntidad = nuevoUsuario.orElseThrow(() -> new ValidationExcepcion(errores));

        return Mapper.mapFrom(usuarioEntidad);

    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return DTO con el perfil del usuario encontrado
     * @throws ValidationExcepcion si no existe ningún usuario con ese id
     */
    public UsuarioDto consultarPerfilUsuarioPorId(Long id) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<>();

        var usuarioABuscarOpt = usuarioRepo.obtenerPorId(id);

        if(!(usuarioABuscarOpt.isPresent())){
            errores.add(new ErrorDto("idUsuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }


        return Mapper.mapFrom(usuarioABuscarOpt.orElse(null));

    }

    /**
     * Busca un usuario por su nombre de usuario (case-sensitive).
     *
     * @param nombre nombre de usuario a buscar
     * @return DTO con el perfil del usuario encontrado
     * @throws ValidationExcepcion si no existe ningún usuario con ese nombre
     */
    public UsuarioDto consultarPerfilUsuarioPorNombre(String nombre) throws ValidationExcepcion{

        var errores = new ArrayList<ErrorDto>();

        var usuarioABuscarOpt = usuarioRepo.obtenerTodos().stream().filter(U -> Objects.equals(U.getNombre(), nombre)).toList();


        if(usuarioABuscarOpt.isEmpty()){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }


        return Mapper.mapFrom(usuarioABuscarOpt.stream().findFirst().orElse(null));
    }

    /**
     * Añade saldo a la cartera del usuario. La cantidad debe estar entre 5,00 € y 500,00 €
     * y el usuario debe tener la cuenta activa.
     *
     * @param id             identificador del usuario
     * @param cantidadAniadir importe a añadir (rango válido: 5,00 – 500,00)
     * @return DTO del usuario con el saldo actualizado
     * @throws ValidationExcepcion si el usuario no existe, está inactivo o la cantidad está fuera del rango permitido
     */
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

        if (cantidadAniadir < 5.00){
            errores.add(new ErrorDto("cantidadAniadir", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        if (cantidadAniadir > 500.00){
            errores.add(new ErrorDto("cantidadAniadir", ErrorType.VALOR_DEMASIADO_ALTO));
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

    /**
     * Devuelve el saldo actual de la cartera de un usuario.
     *
     * @param id identificador del usuario
     * @return saldo actual de la cartera
     * @throws ValidationExcepcion si no existe ningún usuario con ese id
     */
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





