package modelo.dto;

import modelo.dto.JuegoDto;
import modelo.dto.UsuarioDto;

public class BibliotecaDto {

    //Variables

    private int id;
    private String fechaAdquisicion;
    private int horasJuego;
    private String ultimaFechaJuego;
    private enum ESTADO_INSTALACION{
        INSTALADO, NO_INSTALADO}
    private UsuarioDto usuario;
    private JuegoDto juego;

    //Constructor

    public BibliotecaDto(int id, String fechaAdquisicion, int horasJuego, String ultimaFechaJuego, UsuarioDto usuario, JuegoDto juego) {
        this.id = id;
        this.fechaAdquisicion = fechaAdquisicion;
        this.horasJuego = horasJuego;
        this.ultimaFechaJuego = ultimaFechaJuego;
        this.usuario = usuario;
        this.juego = juego;
    }

    //Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(String fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public int getHorasJuego() {
        return horasJuego;
    }

    public void setHorasJuego(int horasJuego) {
        this.horasJuego = horasJuego;
    }

    public String getUltimaFechaJuego() {
        return ultimaFechaJuego;
    }

    public void setUltimaFechaJuego(String ultimaFechaJuego) {
        this.ultimaFechaJuego = ultimaFechaJuego;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }

    public JuegoDto getJuego() {
        return juego;
    }

    public void setJuego(JuegoDto juego) {
        this.juego = juego;
    }
}
