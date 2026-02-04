package modelo.dto;

import modelo.enums.EstadoCuentaEnum;
import java.time.LocalDateTime;

    //Variables

public class UsuarioDto {

        private int id;
        private String nombre;
        private String email;
        private String contrasenia;
        private String nombreReal;
        private String pais;
        private LocalDateTime fechaNacimiento;
        private LocalDateTime fechaRegistro;
        private String avatar;
        private Double saldoCartera;
        private EstadoCuentaEnum.ESTADOCUENTA estado;

    //Constructor

    public UsuarioDto(int id, String nombre, String email, String contrasenia, String nombreReal, String pais, LocalDateTime fechaNacimiento, LocalDateTime fechaRegistro, String avatar, Double saldoCartera, EstadoCuentaEnum.ESTADOCUENTA estado) {
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

    //Getters


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getNombreReal() {
        return nombreReal;
    }

    public String getPais() {
        return pais;
    }

    public LocalDateTime getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public String getAvatar() {
        return avatar;
    }

    public Double getSaldoCartera() {
        return saldoCartera;
    }

    public EstadoCuentaEnum.ESTADOCUENTA getEstado() {
        return estado;
    }
}


