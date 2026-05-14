package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.ESTADOINSTALACIONBIBLIOTECA;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad que representa la entrada de un juego en la biblioteca personal de un usuario.
 * Registra cuándo se adquirió el juego, las horas jugadas y el estado de instalación.
 */
public class BibliotecaEntidad {

    private Long id;
    private Long idUsuario;
    private Long idJuego;
    private LocalDateTime fechaAdquisicion;
    private Double horasJuego;
    private LocalDateTime ultimaFechaJuego;
    private ESTADOINSTALACIONBIBLIOTECA estadoInstalacion;

    public BibliotecaEntidad(Long id, Long idUsuario, Long idJuego, LocalDateTime fechaAdquisicion,
                             Double horasJuego, LocalDateTime ultimaFechaJuego, ESTADOINSTALACIONBIBLIOTECA estadoInstalacion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaAdquisicion = fechaAdquisicion;
        this.horasJuego = horasJuego;
        this.ultimaFechaJuego = ultimaFechaJuego;
        this.estadoInstalacion = estadoInstalacion;
    }

    // Getters
    public Long getId() { return id; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdJuego() { return idJuego; }
    public LocalDateTime getFechaAdquisicion() { return fechaAdquisicion; }
    public Double getHorasJuego() { return horasJuego; }
    public LocalDateTime getUltimaFechaJuego() { return ultimaFechaJuego; }
    public ESTADOINSTALACIONBIBLIOTECA getEstadoInstalacion() { return estadoInstalacion; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public void setIdJuego(Long idJuego) { this.idJuego = idJuego; }
    public void setFechaAdquisicion(LocalDateTime fechaAdquisicion) { this.fechaAdquisicion = fechaAdquisicion; }
    public void setHorasJuego(Double horasJuego) { this.horasJuego = horasJuego; }
    public void setUltimaFechaJuego(LocalDateTime ultimaFechaJuego) { this.ultimaFechaJuego = ultimaFechaJuego; }
    public void setEstadoInstalacion(ESTADOINSTALACIONBIBLIOTECA estadoInstalacion) { this.estadoInstalacion = estadoInstalacion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BibliotecaEntidad that = (BibliotecaEntidad) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(idUsuario, that.idUsuario) &&
               Objects.equals(idJuego, that.idJuego) &&
               Objects.equals(fechaAdquisicion, that.fechaAdquisicion) &&
               Objects.equals(horasJuego, that.horasJuego) &&
               Objects.equals(ultimaFechaJuego, that.ultimaFechaJuego) &&
               estadoInstalacion == that.estadoInstalacion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, idJuego, fechaAdquisicion, horasJuego, ultimaFechaJuego, estadoInstalacion);
    }

    @Override
    public String toString() {
        return "BibliotecaEntidad{" +
               "id=" + id +
               ", idUsuario=" + idUsuario +
               ", idJuego=" + idJuego +
               ", fechaAdquisicion=" + fechaAdquisicion +
               ", horasJuego=" + horasJuego +
               ", ultimaFechaJuego=" + ultimaFechaJuego +
               ", estadoInstalacion=" + estadoInstalacion +
               '}';
    }
}
