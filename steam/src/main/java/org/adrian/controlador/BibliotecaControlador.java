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

    public BibliotecaDto aniadirJuegoABiblioteca(BibliotecaForm form) throws ValidationExcepcion {

        List<ErrorDto> errores = form.validar();

        var idJuego = form.idJuego();
        var idUsuairo = form.idUsuario();

        var usuario = usuarioRepo.obtenerPorId(idUsuairo);
        var Juego = juegoRepo.obtenerPorId(idJuego);

        if(usuario.isEmpty()){
            errores.add(new ErrorDto("Usuario", ErrorType.NO_ENCONTRADO));
        }

        if(Juego.isEmpty()){
            errores.add(new ErrorDto("Juego", ErrorType.NO_ENCONTRADO));
        }

        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.idUsuario().equals(form.idUsuario()));

        if(usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.DUPLICADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.idJuego().equals(form.idJuego()));

        if(juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.DUPLICADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        Optional<BibliotecaEntidad> nuevaBiblioteca = bibliotecaRepo.crear(form);

        var biblioteca = nuevaBiblioteca.get();

        return Mapper.mapFrom(biblioteca);//Meter en el map from Dto y añadir el usuario y el juego directamente en el dto de biblioteca

    }

    public BibliotecaDto eliminarJuegoBiblioteca(Long idJuego, Long idUsuario) throws ValidationExcepcion{

        List<ErrorDto> errores = new ArrayList<ErrorDto>();

        boolean usuarioExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.idUsuario().equals(idUsuario));

        if(usuarioExiste){
            errores.add(new ErrorDto("usuario", ErrorType.DUPLICADO));
        }

        boolean juegoExiste = bibliotecaRepo.obtenerTodos().stream()
                .anyMatch(b -> b.idJuego().equals(idJuego));

        if(juegoExiste){
            errores.add(new ErrorDto("juego", ErrorType.DUPLICADO));
        }

        if(!errores.isEmpty()){
            throw new ValidationExcepcion(errores);
        }

        //Falta acabar

    }

}
