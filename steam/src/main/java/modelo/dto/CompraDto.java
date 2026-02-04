package modelo.dto;

public class CompraDto {

    //Variables

    private int id;
    private String fechaCompra;
    private Double precioSinDescuento;
    private int descuento;
    private enum METODOPAGO{
        TARJETA_CREDITO, PAYPAL, CARTERA_STEAM, TRANSFERENCIA, OTROS}
    private enum ESTADO{
        COMPLETADA, REEMBOLSADA}
    private UsuarioDto usuario;
    private JuegoDto juego;

    //Constructor


    public CompraDto(int id, String fechaCompra, Double precioSinDescuento, int descuento, UsuarioDto usuario, JuegoDto juego) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.precioSinDescuento = precioSinDescuento;
        this.descuento = descuento;
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

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Double getPrecioSinDescuento() {
        return precioSinDescuento;
    }

    public void setPrecioSinDescuento(Double precioSinDescuento) {
        this.precioSinDescuento = precioSinDescuento;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
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
