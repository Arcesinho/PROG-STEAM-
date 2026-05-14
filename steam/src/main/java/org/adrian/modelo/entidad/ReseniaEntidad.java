package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.ESTADORESENIA;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad que representa la reseña de un usuario sobre un juego.
 * Recoge si el juego es recomendado, el texto de la reseña, las horas jugadas
 * en el momento de escribirla y las fechas de publicación y última edición.
 */
public class ReseniaEntidad {

    private Long id;
    private Long idUsuario;
    private Long idJuego;
    private boolean recomendado;
    private String textoResenia;
    private Double horasHastaResenia;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaUltimaEdicion;
    private ESTADORESENIA estado;

    public ReseniaEntidad(Long id, Long idUsuario, Long idJuego, boolean recomendado,
                          String textoResenia, Double horasHastaResenia, LocalDateTime fechaPublicacion,
                          LocalDateTime fechaUltimaEdicion, ESTADORESENIA estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.recomendado = recomendado;
        this.textoResenia = textoResenia;
        this.horasHastaResenia = horasHastaResenia;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaUltimaEdicion = fechaUltimaEdicion;
        this.estado = estado;
    }

    // Getters
    public Long getId() { return id; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdJuego() { return idJuego; }
    public boolean isRecomendado() { return recomendado; }
    public String getTextoResenia() { return textoResenia; }
    public Double getHorasHastaResenia() { return horasHastaResenia; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public LocalDateTime getFechaUltimaEdicion() { return fechaUltimaEdicion; }
    public ESTADORESENIA getEstado() { return estado; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public void setIdJuego(Long idJuego) { this.idJuego = idJuego; }
    public void setRecomendado(boolean recomendado) { this.recomendado = recomendado; }
    public void setTextoResenia(String textoResenia) { this.textoResenia = textoResenia; }
    public void setHorasHastaResenia(Double horasHastaResenia) { this.horasHastaResenia = horasHastaResenia; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public void setFechaUltimaEdicion(LocalDateTime fechaUltimaEdicion) { this.fechaUltimaEdicion = fechaUltimaEdicion; }
    public void setEstado(ESTADORESENIA estado) { this.estado = estado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReseniaEntidad that = (ReseniaEntidad) o;
        return recomendado == that.recomendado &&
               Objects.equals(id, that.id) &&
               Objects.equals(idUsuario, that.idUsuario) &&
               Objects.equals(idJuego, that.idJuego) &&
               Objects.equals(textoResenia, that.textoResenia) &&
               Objects.equals(horasHastaResenia, that.horasHastaResenia) &&
               Objects.equals(fechaPublicacion, that.fechaPublicacion) &&
               Objects.equals(fechaUltimaEdicion, that.fechaUltimaEdicion) &&
               estado == that.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, idJuego, recomendado, textoResenia, horasHastaResenia, fechaPublicacion, fechaUltimaEdicion, estado);
    }

    @Override
    public String toString() {
        return "ReseniaEntidad{" +
               "id=" + id +
               ", idUsuario=" + idUsuario +
               ", idJuego=" + idJuego +
               ", recomendado=" + recomendado +
               ", textoResenia='" + textoResenia + '\'' +
               ", horasHastaResenia=" + horasHastaResenia +
               ", fechaPublicacion=" + fechaPublicacion +
               ", fechaUltimaEdicion=" + fechaUltimaEdicion +
               ", estado=" + estado +
               '}';
    }
}
