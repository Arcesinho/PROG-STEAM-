package modelo.dto;

public class ReseniaDto {

    //Variables

    private int id;
    private boolean recomendado;
    private String textoResenia;
    private int horasHastaResenia;
    private String fechaPublicacion;
    private String fechaUltimaEdicion;
    public enum ESTADO{
        PUBLICADA, OCULTA, ELIMINADA}
    public ESTADO estado;
    private UsuarioDto usuario;
    private JuegoDto juego;


    public ReseniaDto(int id, boolean recomendado, String textoResenia, int horasHastaResenia, String fechaPublicacion, String fechaUltimaEdicion, ESTADO estado, UsuarioDto usuario, JuegoDto juego) {
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

    public int getId() {
        return id;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public String getTextoResenia() {
        return textoResenia;
    }

    public int getHorasHastaResenia() {
        return horasHastaResenia;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public String getFechaUltimaEdicion() {
        return fechaUltimaEdicion;
    }

    public ESTADO getEstado() {
        return estado;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public JuegoDto getJuego() {
        return juego;
    }
}
