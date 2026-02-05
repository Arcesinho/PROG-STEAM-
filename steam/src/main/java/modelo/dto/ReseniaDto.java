package modelo.dto;

import modelo.enums.EstadoReseniaEnum;

import java.time.LocalDateTime;

public class ReseniaDto {

    //Variables
    private Long id;
    private boolean recomendado;
    private String textoResenia;
    private Double horasHastaResenia;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaUltimaEdicion;
    private EstadoReseniaEnum.ESTADO estado;
    private UsuarioDto usuario;
    private JuegoDto juego;

    //Constructor

    public ReseniaDto(Long id, boolean recomendado, String textoResenia, Double horasHastaResenia, LocalDateTime fechaPublicacion, LocalDateTime fechaUltimaEdicion, EstadoReseniaEnum.ESTADO estado, UsuarioDto usuario, JuegoDto juego) {
        this.id = id;
        this.recomendado = recomendado;
        this.textoResenia = textoResenia;
        this.horasHastaResenia = horasHastaResenia;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaUltimaEdicion = fechaUltimaEdicion;
        this.estado = estado;
        this.usuario = usuario;
        this.juego = juego;
    }

    //Getters


    public Long getId() {
        return id;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public String getTextoResenia() {
        return textoResenia;
    }

    public Double getHorasHastaResenia() {
        return horasHastaResenia;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public LocalDateTime getFechaUltimaEdicion() {
        return fechaUltimaEdicion;
    }

    public EstadoReseniaEnum.ESTADO getEstado() {
        return estado;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public JuegoDto getJuego() {
        return juego;
    }
}
