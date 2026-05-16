package org.adrian;

import org.adrian.controlador.JuegoControlador;
import org.adrian.controlador.UsuarioControlador;
import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.modelo.form.JuegoForm;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.PEGIJUEGO;
import org.adrian.modelo.enums.CATEGORIAJUEGO;
import org.adrian.modelo.enums.ESTADOCUENTA;
import org.adrian.repositorio.implementacionMemoria.JuegoRepoImplementacionMemoria;
import org.adrian.repositorio.implementacionMemoria.UsuarioRepoImplementacionMemoria;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class App {

    public static void main( String[] args) {

        var juegoRepo = new JuegoRepoImplementacionMemoria();
        var usuarioRepo = new UsuarioRepoImplementacionMemoria();

        var juegoCtrl = new JuegoControlador(juegoRepo);
        var usuarioCtrl = new UsuarioControlador(usuarioRepo);

        try {
            var juegoForm = new JuegoForm("MiJuegoPrueba", Optional.of("Descripcion"), "MiEstudio", LocalDateTime.now(), 9.99, Optional.of(0), PEGIJUEGO.PEGI_3, Optional.of(new String[]{"ES","EN"}), ESTADOJUEGO.DISPONIBLE, CATEGORIAJUEGO.ACCION);
            var juegoDto = juegoCtrl.aniadirNuevoJuego(juegoForm);
            System.out.println("Juego creado: " + juegoDto.tituloJuego());

            var catalogo = juegoCtrl.consultarCatalogoCompleto();
            System.out.println("Catalogo tamaño: " + catalogo.size());

            var usuarioForm = new UsuarioForm("usuarioPrueba", "usuario@ejemplo.com", "Password1", "Nombre Real", "Spain", LocalDate.of(1990,1,1), Optional.empty(), 100.0, ESTADOCUENTA.ACTIVA);
            var usuarioDto = usuarioCtrl.registrarNuevoUsuario(usuarioForm);
            System.out.println("Usuario creado: " + usuarioDto.nombreUsuario());

            var perfil = usuarioCtrl.consultarPerfilUsuarioPorId(usuarioDto.id());
            System.out.println("Perfil obtenido: " + perfil.nombreUsuario());

            var detalle = juegoCtrl.consultarDetalleJuegoPorId(juegoDto.id());
            System.out.println("Detalle juego: " + detalle.tituloJuego());

        } catch (ValidationExcepcion e) {
            System.out.println("Errores de validación:");
            e.getErrores().forEach(er -> System.out.println(er.getCampo() + " -> " + er.getTipo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
