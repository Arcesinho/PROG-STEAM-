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

/**
 * Controlador de reseñas de juegos.
 * Permite escribir, eliminar, ocultar y consultar reseñas de usuarios.
 */
public class ReseniaControlador {

    private final IBibliotecaRepo bibliotecaRepo;
    private final IReseniaRepo reseniaRepo;

    /**
     * @param bibliotecaRepo repositorio de biblioteca (para verificar que el usuario posee el juego)
     * @param reseniaRepo    repositorio de reseñas
     */
    public ReseniaControlador( IBibliotecaRepo bibliotecaRepo, IReseniaRepo reseniaRepo) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.reseniaRepo = reseniaRepo;
    }

    /**
     * Publica una nueva reseña de un juego. El usuario debe tener el juego en su biblioteca
     * y no haber escrito una reseña previa para ese juego.
     *
     * @param idUsuario    identificador del usuario autor
     * @param idJuego      identificador del juego reseñado
     * @param recomendado  {@code true} si el usuario recomienda el juego, {@code false} en caso contrario
     * @param textoResenia texto de la reseña (50–8000 caracteres)
     * @return DTO de la reseña publicada
     * @throws ValidationExcepcion si el usuario no posee el juego, ya ha escrito una reseña o el texto no es válido
     */
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

    /**
     * Marca una reseña como eliminada. Solo el autor puede eliminar su propia reseña.
     *
     * @param idResenia identificador de la reseña
     * @param idUsuario identificador del usuario que solicita la eliminación
     * @return DTO de la reseña con estado {@link ESTADORESENIA#ELIMINADA}
     * @throws ValidationExcepcion si la reseña no existe o no pertenece al usuario
     */
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

    /**
     * Oculta una reseña publicada. Solo el autor puede ocultar su propia reseña
     * y únicamente si está en estado {@link ESTADORESENIA#PUBLICADA}.
     *
     * @param idResenia identificador de la reseña
     * @param idUsuario identificador del usuario que solicita ocultarla
     * @return DTO de la reseña con estado {@link ESTADORESENIA#OCULTA}
     * @throws ValidationExcepcion si la reseña no existe, no pertenece al usuario o no está publicada
     */
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

    /**
     * Devuelve las reseñas publicadas de un juego con soporte de filtro y ordenación.
     *
     * @param idJuego identificador del juego
     * @param filtro  {@code "positivas"} muestra solo recomendadas, {@code "negativas"} solo no recomendadas,
     *                {@code null} muestra todas
     * @param orden   {@code "recientes"} ordena de más nueva a más antigua;
     *                cualquier otro valor ordena por horas jugadas de mayor a menor
     * @return lista de DTOs de reseñas publicadas que cumplen los criterios
     */
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

    /**
     * Devuelve todas las reseñas escritas por un usuario, con filtro opcional por estado.
     *
     * @param idUsuario    identificador del usuario
     * @param filtroEstado nombre del estado a filtrar (p. ej. {@code "PUBLICADA"}, {@code "OCULTA"},
     *                     {@code "ELIMINADA"}); {@code null} para obtener todas
     * @return lista de DTOs de reseñas del usuario
     */
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
