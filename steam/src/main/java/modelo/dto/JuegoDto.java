package modelo.dto;

public class JuegoDto {

    //Variables

    private int id;
    private String tituloJ;
    private String descripcion;
    private String desarrollador;
    private String fechaLanzamiento;
    private Double precioBase;
    private int descuentoActual;
    private enum CATEGORIA {
        ACCION, AVENTURA, RPG, SHOOTER}
    private enum pegi{
        PEGI_3, PEGI_7, PEGI_12, PEGI_16, PEGI_18}
    private String[] idiomas;
    private enum ESTADO{
        DISPONIBLE, PREVENTA, ACCESO_ANTICIPADO, NO_DISPONIBLE}

    //Constructor

    public JuegoDto(int id, String tituloJ, String descripcion, String desarrollador, String fechaLanzamiento, Double precioBase, int descuentoActual, String[] idiomas) {
        this.id = id;
        this.tituloJ = tituloJ;
        this.descripcion = descripcion;
        this.desarrollador = desarrollador;
        this.fechaLanzamiento = fechaLanzamiento;
        this.precioBase = precioBase;
        this.descuentoActual = descuentoActual;
        this.idiomas = idiomas;
    }

    //Getters
    //Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTituloJ() {
        return tituloJ;
    }

    public void setTituloJ(String tituloJ) {
        this.tituloJ = tituloJ;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public int getDescuentoActual() {
        return descuentoActual;
    }

    public void setDescuentoActual(int descuentoActual) {
        this.descuentoActual = descuentoActual;
    }

    public String[] getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String[] idiomas) {
        this.idiomas = idiomas;
    }
}
