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

public class BibliotecaControlador {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IJuegoRepo juegoRepo;
    private final IUsuarioRepo usuarioRepo;

    public BibliotecaControlador(IBibliotecaRepo bibliotecaRepo, IJuegoRepo juegoRepo, IUsuarioRepo usuarioRepo){this.bibliotecaRepo = bibliotecaRepo;
        this.juegoRepo = juegoRepo;
        this.usuarioRepo = usuarioRepo;
    }

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

    public BibliotecaDto aniadirJuegoABiblioteca(BibliotecaForm form) throws ValidationExcepcion {

        List<ErrorDto> errores = form.validar();

        var idJuego = form.idJuego();
        var idUsuairo = form.idUsuario();

        var usuario = usuarioRepo.obtenerPorId(idUsuairo);
        var juego = juegoRepo.obtenerPorId(idJuego);

        if(usuario.isEmpty()){
            errores.add(new ErrorDto("Usuario", ErrorType.NO_ENCONTRADO));
        }

        if(juego.isEmpty()){
            errores.add(new ErrorDto("Juego", ErrorType.NO_ENCONTRADO));
        }

        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(form.idUsuario()));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.DUPLICADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdJuego().equals(form.idJuego()));

        if(!juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.DUPLICADO));
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

    public BibliotecaDto eliminarJuegoBiblioteca(Long idJuego, Long idUsuario) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();


        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(idUsuario));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.DUPLICADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdJuego().equals(idJuego));

        if(!juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.DUPLICADO));
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

    public BibliotecaDto actualizarTiempoJuegoUsuario(Long idUsuario, Long idJuego, Double horas) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        boolean horasPositivas = horas >= 0;
        if(!horasPositivas){
            errores.add(new ErrorDto("horasAAniadir", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdUsuario().equals(idUsuario));

        if(!usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.DUPLICADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.getIdJuego().equals(idJuego));

        if(!juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.DUPLICADO));
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

}
