package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.enums.METODOPAGOCOMPRA;

import java.time.LocalDateTime;
import java.util.Objects;

public class CompraEntidad {

    private Long id;
    private Long idUsuario;
    private Long idJuego;
    private LocalDateTime fechaCompra;
    private Double precioSinDescuento;
    private int descuento;
    private METODOPAGOCOMPRA metodopago;
    private ESTADOCOMPRA estado;

    public CompraEntidad(Long id, Long idUsuario, Long idJuego, LocalDateTime fechaCompra,
                         Double precioSinDescuento, int descuento, METODOPAGOCOMPRA metodopago, ESTADOCOMPRA estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaCompra = fechaCompra;
        this.precioSinDescuento = precioSinDescuento;
        this.descuento = descuento;
        this.metodopago = metodopago;
        this.estado = estado;
    }

    // Getters
    public Long getId() { return id; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdJuego() { return idJuego; }
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public Double getPrecioSinDescuento() { return precioSinDescuento; }
    public int getDescuento() { return descuento; }
    public METODOPAGOCOMPRA getMetodopago() { return metodopago; }
    public ESTADOCOMPRA getEstado() { return estado; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public void setIdJuego(Long idJuego) { this.idJuego = idJuego; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
    public void setPrecioSinDescuento(Double precioSinDescuento) { this.precioSinDescuento = precioSinDescuento; }
    public void setDescuento(int descuento) { this.descuento = descuento; }
    public void setMetodopago(METODOPAGOCOMPRA metodopago) { this.metodopago = metodopago; }
    public void setEstado(ESTADOCOMPRA estado) { this.estado = estado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompraEntidad that = (CompraEntidad) o;
        return descuento == that.descuento &&
               Objects.equals(id, that.id) &&
               Objects.equals(idUsuario, that.idUsuario) &&
               Objects.equals(idJuego, that.idJuego) &&
               Objects.equals(fechaCompra, that.fechaCompra) &&
               Objects.equals(precioSinDescuento, that.precioSinDescuento) &&
               metodopago == that.metodopago &&
               estado == that.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, idJuego, fechaCompra, precioSinDescuento, descuento, metodopago, estado);
    }

    @Override
    public String toString() {
        return "CompraEntidad{" +
               "id=" + id +
               ", idUsuario=" + idUsuario +
               ", idJuego=" + idJuego +
               ", fechaCompra=" + fechaCompra +
               ", precioSinDescuento=" + precioSinDescuento +
               ", descuento=" + descuento +
               ", metodopago=" + metodopago +
               ", estado=" + estado +
               '}';
    }
}