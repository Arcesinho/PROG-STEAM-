package modelo.dto;

import modelo.dto.JuegoDto;
import modelo.dto.UsuarioDto;

public class BibliotecaDto {

    //Variables

    public enum ESTADO_INSTALACION{INSTALADO, NO_INSTALADO}
    private Long id;
    private String fechaAdquisicion;
    private Double horasJuego;
    private String ultimaFechaJuego;
    private UsuarioDto usuario;
    private JuegoDto juego;
    private ESTADO_INSTALACION estadoInstalacion;

    //Constructor

    public BibliotecaDto(Long id, String fechaAdquisicion, Double horasJuego, String ultimaFechaJuego, UsuarioDto usuario, JuegoDto juego, ESTADO_INSTALACION estadoInstalacion) {
        this.id = id;
        this.fechaAdquisicion = fechaAdquisicion;
        this.horasJuego = horasJuego;
        this.ultimaFechaJuego = ultimaFechaJuego;
        this.usuario = usuario;
        this.juego = juego;
        this.estadoInstalacion = estadoInstalacion;
    }


    //Getters y setters


    public Long getId() {
        return id;
    }

    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public Double getHorasJuego() {
        return horasJuego;
    }

    public String getUltimaFechaJuego() {
        return ultimaFechaJuego;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public JuegoDto getJuego() {
        return juego;
    }

    public ESTADO_INSTALACION getEstadoInstalacion() {
        return estadoInstalacion;
    }
}
