package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.BibliotecaDto;
import org.adrian.modelo.entidad.BibliotecaEntidad;
import org.adrian.modelo.form.BibliotecaForm;
import org.adrian.modelo.form.ErrorDto;
import org.adrian.modelo.form.ErrorType;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;
import org.adrian.repositorio.interfaces.IJuegoRepo;
import org.adrian.repositorio.interfaces.IUsuarioRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de la biblioteca personal de juegos de cada usuario.
 * Gestiona la adición, eliminación y consulta de juegos en la biblioteca,
 * así como el seguimiento del tiempo de juego.
 */
public class BibliotecaControlador {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IJuegoRepo juegoRepo;
    private final IUsuarioRepo usuarioRepo;

    /**
     * @param bibliotecaRepo repositorio de entradas de biblioteca
     * @param juegoRepo      repositorio de juegos
     * @param usuarioRepo    repositorio de usuarios
     */
    public BibliotecaControlador(IBibliotecaRepo bibliotecaRepo, IJuegoRepo juegoRepo, IUsuarioRepo usuarioRepo){this.bibliotecaRepo = bibliotecaRepo;
        this.juegoRepo = juegoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /**
     * Devuelve todos los juegos que forman parte de la biblioteca de un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return lista de DTOs con cada entrada de la biblioteca
     * @throws ValidationExcepcion si el usuario no existe o no tiene juegos en su biblioteca
     */
    public List<BibliotecaDto> verBibliotecaPersonal(Long idUsuario) throws ValidationExcepcion {

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        var usuarioOpt = usuarioRepo.obtenerPorId(idUsuario);

        if(usuarioOpt.isEmpty()){
            errores.add(new ErrorDto("usuarioOpt", ErrorType.NO_ENCONTRADO));
        }

        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(idUsuario));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var bibliotecas = bibliotecaRepo.obtenerTodos()
                .stream()
                .filter(b -> b.getIdUsuario().equals(idUsuario)).toList();


        var usuarioEntidad = usuarioOpt.get();

        var usuarioDto = Mapper.mapFrom(usuarioEntidad);

        return bibliotecas.stream().map(

                b -> {
                    var juegoentity = juegoRepo.obtenerPorId(b.getIdJuego());
                    var juegoDto = Mapper.mapFrom(juegoentity.orElse(null));
                    return Mapper.mapFrom(b, usuarioDto, juegoDto);
                }
        ).toList();


    }

    /**
     * Añade un juego a la biblioteca de un usuario, evitando duplicados.
     *
     * @param form datos de la nueva entrada de biblioteca
     * @return DTO de la entrada creada
     * @throws ValidationExcepcion si el usuario o el juego no existen, o el juego ya está en la biblioteca
     */
    public BibliotecaDto aniadirJuegoABiblioteca(BibliotecaForm form) throws ValidationExcepcion {

        List<ErrorDto> errores = form.validar();

        var usuario = usuarioRepo.obtenerPorId(form.idUsuario());
        var juego = juegoRepo.obtenerPorId(form.idJuego());

        if(usuario.isEmpty()){
            errores.add(new ErrorDto("Usuario", ErrorType.NO_ENCONTRADO));
        }

        if(juego.isEmpty()){
            errores.add(new ErrorDto("Juego", ErrorType.NO_ENCONTRADO));
        }

        boolean bibliotecaDuplicada = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(form.idUsuario()) && b.getIdJuego().equals(form.idJuego()));

        if(bibliotecaDuplicada){
            errores.add(new ErrorDto("biblioteca", ErrorType.DUPLICADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEntidad = usuario.get();
        var juegoEntidad = juego.get();

        Optional<BibliotecaEntidad> nuevaBiblioteca = bibliotecaRepo.crear(form);

        var usuarioDto = Mapper.mapFrom(usuarioEntidad);
        var juegoDto = Mapper.mapFrom(juegoEntidad);

        var biblioteca = nuevaBiblioteca.get();

        return Mapper.mapFrom(biblioteca, usuarioDto, juegoDto);

    }

    /**
     * Elimina un juego de la biblioteca de un usuario.
     *
     * @param idJuego   identificador del juego a eliminar
     * @param idUsuario identificador del usuario propietario
     * @return DTO de la entrada eliminada
     * @throws ValidationExcepcion si el usuario, el juego o la entrada de biblioteca no existen
     */
    public BibliotecaDto eliminarJuegoBiblioteca(Long idJuego, Long idUsuario) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();


        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(idUsuario));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdJuego().equals(idJuego));

        if(!juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
        }

        var biblioteca = bibliotecaRepo.obtenerPorIdUsuarioIdJuego(idUsuario, idJuego);

        if(biblioteca.isEmpty()){
            errores.add(new ErrorDto("biblioteca", ErrorType.NO_ENCONTRADO));
        }

        var usuario = usuarioRepo.obtenerPorId(idUsuario);
        var juego = juegoRepo.obtenerPorId(idJuego);

        if(usuario.isEmpty()){
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }
        if(juego.isEmpty()){
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEntidad = usuario.get();
        var juegoEntidad = juego.get();

        var usuarioDto = Mapper.mapFrom(usuarioEntidad);
        var juegoDto = Mapper.mapFrom(juegoEntidad);

        var bibliotecaEntidad = biblioteca.get();

        var idBiblioteca = bibliotecaEntidad.getId();

        bibliotecaRepo.eliminar(idBiblioteca);

        return Mapper.mapFrom(bibliotecaEntidad, usuarioDto, juegoDto);

    }

    /**
     * Actualiza el total de horas jugadas de un usuario en un juego concreto.
     *
     * @param idUsuario identificador del usuario
     * @param idJuego   identificador del juego
     * @param horas     nuevo total de horas jugadas (debe ser ≥ 0)
     * @return DTO de la entrada actualizada
     * @throws ValidationExcepcion si el usuario, el juego o la entrada de biblioteca no existen,
     *                             o las horas son negativas
     */
    public BibliotecaDto actualizarTiempoJuegoUsuario(Long idUsuario, Long idJuego, Double horas) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        boolean horasPositivas = horas >= 0;
        if(!horasPositivas){
            errores.add(new ErrorDto("horasAAniadir", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(idUsuario));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdJuego().equals(idJuego));

        if(!juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
        }

        var bibliotecaOpt = bibliotecaRepo.obtenerPorIdUsuarioIdJuego(idUsuario, idJuego);

        if(bibliotecaOpt.isEmpty()){
            errores.add(new ErrorDto("bibliotecaOpt", ErrorType.NO_ENCONTRADO));
        }

        var usuarioOpt = usuarioRepo.obtenerPorId(idUsuario);
        var juegoOpt = juegoRepo.obtenerPorId(idJuego);

        if(usuarioOpt.isEmpty()){
            errores.add(new ErrorDto("usuarioOpt", ErrorType.NO_ENCONTRADO));
        }
        if(juegoOpt.isEmpty()){
            errores.add(new ErrorDto("juegoOpt", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEntidad = usuarioOpt.get();
        var juegoEntidad = juegoOpt.get();
        
        var usuarioDto = Mapper.mapFrom(usuarioEntidad);
        var juegoDto = Mapper.mapFrom(juegoEntidad);

        var bibliotecaEncontrada = bibliotecaOpt.get();

        var bibliotecaActualizada = bibliotecaRepo.actualizar(bibliotecaEncontrada.getId(), new BibliotecaForm(bibliotecaEncontrada.getId(), idUsuario, idJuego,
                bibliotecaEncontrada.getFechaAdquisicion(), horas, Optional.ofNullable(bibliotecaEncontrada.getUltimaFechaJuego()),
                bibliotecaEncontrada.getEstadoInstalacion()));

        var bibliotecaActualizadaEntidad = bibliotecaActualizada.get();

        return Mapper.mapFrom(bibliotecaActualizadaEntidad, usuarioDto, juegoDto);

    }

    /**
     * Recupera la información de la última sesión de juego de un usuario en un juego concreto,
     * incluyendo la fecha de última conexión y las horas acumuladas.
     *
     * @param idUsuario identificador del usuario
     * @param idJuego   identificador del juego
     * @return DTO con los datos de la entrada de biblioteca (incluye {@code ultimaFechaJuego} y {@code horasJuego})
     * @throws ValidationExcepcion si el usuario, el juego o la entrada de biblioteca no existen
     */
    public BibliotecaDto consultarUltimaSesionJuego(Long idUsuario, Long idJuego) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();


        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(idUsuario));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdJuego().equals(idJuego));

        if(!juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
        }

        var bibliotecaOpt = bibliotecaRepo.obtenerPorIdUsuarioIdJuego(idUsuario, idJuego);

        if(bibliotecaOpt.isEmpty()){
            errores.add(new ErrorDto("bibliotecaOpt", ErrorType.NO_ENCONTRADO));
        }

        var usuarioOpt = usuarioRepo.obtenerPorId(idUsuario);
        var juegoOpt = juegoRepo.obtenerPorId(idJuego);

        if(usuarioOpt.isEmpty()){
            errores.add(new ErrorDto("usuarioOpt", ErrorType.NO_ENCONTRADO));
        }
        if(juegoOpt.isEmpty()){
            errores.add(new ErrorDto("juegoOpt", ErrorType.NO_ENCONTRADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        var usuarioEntidad = usuarioOpt.get();
        var juegoEntidad = juegoOpt.get();

        var usuarioDto = Mapper.mapFrom(usuarioEntidad);
        var juegoDto = Mapper.mapFrom(juegoEntidad);

        var bibliotecaEntidad = bibliotecaOpt.get();

        return  Mapper.mapFrom(bibliotecaEntidad, usuarioDto, juegoDto);
    }

}
