package modelo.dto;

public class UsuarioDto {

    //Variables

    private int id;
    private String nombreUsuario;
    private String email;
    private String contrasenia;
    private String nombreReal;
    private String pais;
    private String fechaNacimiento;
    private String fechaRegistro;
    private String avatar;
    private Double saldoCartera;
    private enum ESTADOCUENTA {
        ACTIVA, SUSPENDIDA, BANEADA
    }

    //Constructor

    public UsuarioDto(int id, String nombreUsuario, String email, String contrasenia, String nombreReal, String pais, String fechaNacimiento, String fechaRegistro, String avatar, Double saldoCartera) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasenia = contrasenia;
        this.nombreReal = nombreReal;
        this.pais = pais;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.avatar = avatar;
        this.saldoCartera = saldoCartera;
    }

    //Getters
    //Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombreReal() {
        return nombreReal;
    }

    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Double getSaldoCartera() {
        return saldoCartera;
    }

    public void setSaldoCartera(Double saldoCartera) {
        this.saldoCartera = saldoCartera;
    }
}


