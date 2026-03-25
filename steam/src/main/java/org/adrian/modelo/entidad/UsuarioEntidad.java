package org.adrian.modelo.entidad;

import org.adrian.modelo.enums.ESTADOCUENTA;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class UsuarioEntidad {

    private Long id;
    private String nombre;
    private String email;
    private String contrasenia;
    private String nombreReal;
    private String pais;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaRegistro;
    private String avatar;
    private Double saldoCartera;
    private ESTADOCUENTA estado;

    public UsuarioEntidad(Long id, String nombre, String email, String contrasenia,
                          String nombreReal, String pais, LocalDate fechaNacimiento,
                          LocalDateTime fechaRegistro, String avatar, Double saldoCartera, ESTADOCUENTA estado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.nombreReal = nombreReal;
        this.pais = pais;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.avatar = avatar;
        this.saldoCartera = saldoCartera;
        this.estado = estado;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getContrasenia() { return contrasenia; }
    public String getNombreReal() { return nombreReal; }
    public String getPais() { return pais; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public String getAvatar() { return avatar; }
    public Double getSaldoCartera() { return saldoCartera; }
    public ESTADOCUENTA getEstado() { return estado; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public void setNombreReal(String nombreReal) { this.nombreReal = nombreReal; }
    public void setPais(String pais) { this.pais = pais; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setSaldoCartera(Double saldoCartera) { this.saldoCartera = saldoCartera; }
    public void setEstado(ESTADOCUENTA estado) { this.estado = estado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntidad that = (UsuarioEntidad) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(nombre, that.nombre) &&
               Objects.equals(email, that.email) &&
               Objects.equals(contrasenia, that.contrasenia) &&
               Objects.equals(nombreReal, that.nombreReal) &&
               Objects.equals(pais, that.pais) &&
               Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
               Objects.equals(fechaRegistro, that.fechaRegistro) &&
               Objects.equals(avatar, that.avatar) &&
               Objects.equals(saldoCartera, that.saldoCartera) &&
               estado == that.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, email, contrasenia, nombreReal, pais, fechaNacimiento, fechaRegistro, avatar, saldoCartera, estado);
    }

    @Override
    public String toString() {
        return "UsuarioEntidad{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", email='" + email + '\'' +
               ", contrasenia='" + contrasenia + '\'' +
               ", nombreReal='" + nombreReal + '\'' +
               ", pais='" + pais + '\'' +
               ", fechaNacimiento=" + fechaNacimiento +
               ", fechaRegistro=" + fechaRegistro +
               ", avatar='" + avatar + '\'' +
               ", saldoCartera=" + saldoCartera +
               ", estado=" + estado +
               '}';
    }
}
