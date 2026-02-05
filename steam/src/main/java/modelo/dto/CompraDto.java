package modelo.dto;

import modelo.enums.EstadoCompraEnum;
import modelo.enums.MetodoPagoCompraEnum;

import java.time.LocalDateTime;

public class CompraDto {

    //Variables

    private Long id;
    private LocalDateTime fechaCompra;
    private Double precioSinDescuento;
    private int descuento;
    private UsuarioDto usuario;
    private JuegoDto juego;
    private MetodoPagoCompraEnum.METODOPAGO metodopago;
    private EstadoCompraEnum.ESTADO estado;

    //Constructor

    public CompraDto(Long id, LocalDateTime fechaCompra, Double precioSinDescuento, int descuento, UsuarioDto usuario, JuegoDto juego, MetodoPagoCompraEnum.METODOPAGO metodopago, EstadoCompraEnum.ESTADO estado) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.precioSinDescuento = precioSinDescuento;
        this.descuento = descuento;
        this.usuario = usuario;
        this.juego = juego;
        this.metodopago = metodopago;
        this.estado = estado;
    }

    //Getters


    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public Double getPrecioSinDescuento() {
        return precioSinDescuento;
    }

    public int getDescuento() {
        return descuento;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public JuegoDto getJuego() {
        return juego;
    }

    public MetodoPagoCompraEnum.METODOPAGO getMetodopago() {
        return metodopago;
    }

    public EstadoCompraEnum.ESTADO getEstado() {
        return estado;
    }
}
