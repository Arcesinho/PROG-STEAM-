package modelo.dto;

import modelo.enums.CategoriaJuegoEnum;
import modelo.enums.EstadoJuegoEnum;
import modelo.enums.PegiJuegoEnum;

import java.time.LocalDateTime;

public class JuegoDto {

    //Variables
    private Long id;
    private String tituloJuego;
    private String descripcion;
    private String desarrollador;
    private LocalDateTime fechaLanzamiento;
    private Double precioBase;
    private int descuentoActual;
    private String[] idiomas;
    private EstadoJuegoEnum.ESTADO estado;
    private PegiJuegoEnum.PEGI pegi;
    private CategoriaJuegoEnum.CATEGORIA categoria;

    //Constructor

    public JuegoDto(Long id, String tituloJuego, String descripcion, String desarrollador, LocalDateTime fechaLanzamiento, Double precioBase, int descuentoActual, String[] idiomas, EstadoJuegoEnum.ESTADO estado, PegiJuegoEnum.PEGI pegi, CategoriaJuegoEnum.CATEGORIA categoria) {
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

    //Getters


    public Long getId() {
        return id;
    }

    public String getTituloJuego() {
        return tituloJuego;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public LocalDateTime getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public int getDescuentoActual() {
        return descuentoActual;
    }

    public String[] getIdiomas() {
        return idiomas;
    }
    public EstadoJuegoEnum.ESTADO getEstado() {
        return estado;
    }

    public PegiJuegoEnum.PEGI getPegi() {
        return pegi;
    }

    public CategoriaJuegoEnum.CATEGORIA getCategoria() {
        return categoria;
    }
}
