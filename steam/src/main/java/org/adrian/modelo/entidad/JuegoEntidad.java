package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.CATEGORIAJUEGO;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.PEGIJUEGO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class JuegoEntidad {

    private Long id;
    private String tituloJuego;
    private String descripcion;
    private String desarrollador;
    private LocalDateTime fechaLanzamiento;
    private Double precioBase;
    private int descuentoActual;
    private String[] idiomas;
    private ESTADOJUEGO estado;
    private PEGIJUEGO pegi;
    private CATEGORIAJUEGO categoria;

    public JuegoEntidad(Long id, String tituloJuego, String descripcion, String desarrollador,
                        LocalDateTime fechaLanzamiento, Double precioBase, int descuentoActual, String[] idiomas,
                        ESTADOJUEGO estado, PEGIJUEGO pegi, CATEGORIAJUEGO categoria) {
        this.id = id;
        this.tituloJuego = tituloJuego;
        this.descripcion = descripcion;
        this.desarrollador = desarrollador;
        this.fechaLanzamiento = fechaLanzamiento;
        this.precioBase = precioBase;
        this.descuentoActual = descuentoActual;
        this.idiomas = idiomas;
        this.estado = estado;
        this.pegi = pegi;
        this.categoria = categoria;
    }

    // Getters
    public Long getId() { return id; }
    public String getTituloJuego() { return tituloJuego; }
    public String getDescripcion() { return descripcion; }
    public String getDesarrollador() { return desarrollador; }
    public LocalDateTime getFechaLanzamiento() { return fechaLanzamiento; }
    public Double getPrecioBase() { return precioBase; }
    public int getDescuentoActual() { return descuentoActual; }
    public String[] getIdiomas() { return idiomas; }
    public ESTADOJUEGO getEstado() { return estado; }
    public PEGIJUEGO getPegi() { return pegi; }
    public CATEGORIAJUEGO getCategoria() { return categoria; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTituloJuego(String tituloJuego) { this.tituloJuego = tituloJuego; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setDesarrollador(String desarrollador) { this.desarrollador = desarrollador; }
    public void setFechaLanzamiento(LocalDateTime fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }
    public void setPrecioBase(Double precioBase) { this.precioBase = precioBase; }
    public void setDescuentoActual(int descuentoActual) { this.descuentoActual = descuentoActual; }
    public void setIdiomas(String[] idiomas) { this.idiomas = idiomas; }
    public void setEstado(ESTADOJUEGO estado) { this.estado = estado; }
    public void setPegi(PEGIJUEGO pegi) { this.pegi = pegi; }
    public void setCategoria(CATEGORIAJUEGO categoria) { this.categoria = categoria; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JuegoEntidad that = (JuegoEntidad) o;
        return descuentoActual == that.descuentoActual &&
               Objects.equals(id, that.id) &&
               Objects.equals(tituloJuego, that.tituloJuego) &&
               Objects.equals(descripcion, that.descripcion) &&
               Objects.equals(desarrollador, that.desarrollador) &&
               Objects.equals(fechaLanzamiento, that.fechaLanzamiento) &&
               Objects.equals(precioBase, that.precioBase) &&
               Arrays.equals(idiomas, that.idiomas) &&
               estado == that.estado &&
               pegi == that.pegi &&
               categoria == that.categoria;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, tituloJuego, descripcion, desarrollador, fechaLanzamiento, precioBase, descuentoActual, estado, pegi, categoria);
        result = 31 * result + Arrays.hashCode(idiomas);
        return result;
    }

    @Override
    public String toString() {
        return "JuegoEntidad{" +
               "id=" + id +
               ", tituloJuego='" + tituloJuego + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", desarrollador='" + desarrollador + '\'' +
               ", fechaLanzamiento=" + fechaLanzamiento +
               ", precioBase=" + precioBase +
               ", descuentoActual=" + descuentoActual +
               ", idiomas=" + Arrays.toString(idiomas) +
               ", estado=" + estado +
               ", pegi=" + pegi +
               ", categoria=" + categoria +
               '}';
    }
}
