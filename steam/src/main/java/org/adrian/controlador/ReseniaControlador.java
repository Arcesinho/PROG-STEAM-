package org.adrian.controlador;

import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.mapper.Mapper;
import org.adrian.modelo.dto.ReseniaDto;
import org.adrian.modelo.entidad.ReseniaEntidad;
import org.adrian.modelo.enums.ESTADORESENIA;
import org.adrian.modelo.form.ErrorDto;
import org.adrian.modelo.form.ErrorType;
import org.adrian.modelo.form.ReseniaForm;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;
import org.adrian.repositorio.interfaces.IReseniaRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReseniaControlador {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IReseniaRepo reseniaRepo;

    public ReseniaControlador( IBibliotecaRepo bibliotecaRepo, IReseniaRepo reseniaRepo) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.reseniaRepo = reseniaRepo;
    }

    public ReseniaDto escribirResenia(Long idUsuario, Long idJuego, boolean recomendado, String textoResenia) throws ValidationExcepcion {
        var form = new ReseniaForm(null, idUsuario, idJuego, recomendado, textoResenia, ESTADORESENIA.PUBLICADA);
        
        var errores = form.validar();
        
        if (bibliotecaRepo.obtenerHoras(idUsuario, idJuego).isEmpty()) {
            errores.add(new ErrorDto("juego", ErrorType.NO_ENCONTRADO));
        }
        
        if (reseniaRepo.obtenerPorUsuarioYJuego(idUsuario, idJuego).isPresent()) {
            errores.add(new ErrorDto("resenia", ErrorType.DUPLICADO));
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }
        
        var entidad = reseniaRepo.crear(form).orElseThrow();
        
        return Mapper.mapFrom(entidad);
    }

    public ReseniaDto eliminarResenia(Long idResenia, Long idUsuario) throws ValidationExcepcion {
        var errores = new ArrayList<ErrorDto>();
        
        var entidadOpt = reseniaRepo.obtenerPorId(idResenia);
        if (entidadOpt.isEmpty()) {
            errores.add(new ErrorDto("resenia", ErrorType.NO_ENCONTRADO));
        } else {
            var entidad = entidadOpt.get();
            if (!entidad.getIdUsuario().equals(idUsuario)) {
                errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
            }
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }
        
        var entidad = entidadOpt.get();
        var form = new ReseniaForm(entidad.getId(), entidad.getIdUsuario(), entidad.getIdJuego(), 
                                   entidad.isRecomendado(), entidad.getTextoResenia(), ESTADORESENIA.ELIMINADA);
        
        var entidadActualizada = reseniaRepo.actualizar(idResenia, form).orElseThrow();


        return Mapper.mapFrom(entidadActualizada);
    }

    public ReseniaDto ocultarResenia(Long idResenia, Long idUsuario) throws ValidationExcepcion {
        var errores = new ArrayList<ErrorDto>();
        
        var entidadOpt = reseniaRepo.obtenerPorId(idResenia);
        if (entidadOpt.isEmpty()) {
            errores.add(new ErrorDto("resenia", ErrorType.NO_ENCONTRADO));

        } else {
            var entidad = entidadOpt.get();
            if (!entidad.getIdUsuario().equals(idUsuario)) {
                errores.add(new ErrorDto("usuario", ErrorType.NO_ENCONTRADO));
            }
            if (entidad.getEstado() != ESTADORESENIA.PUBLICADA) {
                errores.add(new ErrorDto("estado", ErrorType.FORMATO_INVALIDO));
            }
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationExcepcion(errores);
        }
        
        var entidad = entidadOpt.get();
        var form = new ReseniaForm(entidad.getId(), entidad.getIdUsuario(), entidad.getIdJuego(), 
                                   entidad.isRecomendado(), 
                                   entidad.getTextoResenia(), ESTADORESENIA.OCULTA);
        
        var entidadActualizada = reseniaRepo.actualizar(idResenia, form).orElseThrow();


        return Mapper.mapFrom(entidadActualizada);
    }

    public List<ReseniaDto> verReseniasJuego(Long idJuego, String filtro, String orden) {
        var todasResenias = reseniaRepo.obtenerTodos();
        
        //Primero filtramos por juego, estado y tipo de reseña (positiva/negativa)
        var reseniasFiltradas = todasResenias.stream()
                .filter(r -> r.getIdJuego().equals(idJuego))
                .filter(r -> r.getEstado() == ESTADORESENIA.PUBLICADA)
                .filter(r -> filtro == null || 
                        (filtro.equals("positivas") && r.isRecomendado()) ||
                        (filtro.equals("negativas") && !r.isRecomendado()))
                .collect(Collectors.toList());
        
        Comparator<ReseniaEntidad> comparator;
        if ("recientes".equals(orden)) {

            comparator = Comparator.comparing(ReseniaEntidad::getFechaPublicacion).reversed();
        } else {

            comparator = Comparator.comparing(ReseniaEntidad::getHorasHastaResenia).reversed();
        }
        
        reseniasFiltradas.sort(comparator);
        
        return reseniasFiltradas.stream()
                .map(Mapper::mapFrom)
                .collect(Collectors.toList());
    }

    public List<ReseniaDto> verReseniasUsuario(Long idUsuario, String filtroEstado) {
        var todasResenias = reseniaRepo.obtenerTodos();
        
        var reseniasFiltradas = todasResenias.stream()
                .filter(r -> r.getIdUsuario().equals(idUsuario))
                .filter(r -> filtroEstado == null || r.getEstado().name().equalsIgnoreCase(filtroEstado))
                .collect(Collectors.toList());
        
        return reseniasFiltradas.stream()
                .map(Mapper::mapFrom)
                .collect(Collectors.toList());
    }

}
