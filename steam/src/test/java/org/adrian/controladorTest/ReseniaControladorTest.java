package org.adrian.controladorTest;

import org.adrian.controlador.ReseniaControlador;
import org.adrian.controlador.UsuarioControlador;
import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.modelo.dto.ReseniaDto;
import org.adrian.modelo.enums.ESTADORESENIA;
import org.adrian.modelo.enums.ESTADOINSTALACIONBIBLIOTECA;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.CATEGORIAJUEGO;
import org.adrian.modelo.enums.PEGIJUEGO;
import org.adrian.modelo.enums.ESTADOCUENTA;
import org.adrian.modelo.form.BibliotecaForm;
import org.adrian.modelo.form.JuegoForm;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.implementacionMemoria.BibliotecaRepoImplementacionMemoria;
import org.adrian.repositorio.implementacionMemoria.ReseniaRepoImplementacionMemoria;
import org.adrian.repositorio.implementacionMemoria.JuegoRepoImplementacionMemoria;
import org.adrian.repositorio.implementacionMemoria.UsuarioRepoImplementacionMemoria;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;
import org.adrian.repositorio.interfaces.IReseniaRepo;
import org.adrian.repositorio.interfaces.IJuegoRepo;
import org.adrian.repositorio.interfaces.IUsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReseniaControladorTest {

    private IReseniaRepo reseniaRepo;
    private IBibliotecaRepo bibliotecaRepo;
    private IUsuarioRepo usuarioRepo;
    private IJuegoRepo juegoRepo;
    private ReseniaControlador controlador;

    @BeforeEach
    void setUp() {
        reseniaRepo = new ReseniaRepoImplementacionMemoria();
        bibliotecaRepo = new BibliotecaRepoImplementacionMemoria();
        usuarioRepo = new UsuarioRepoImplementacionMemoria();
        juegoRepo = new JuegoRepoImplementacionMemoria();

        controlador = new ReseniaControlador(bibliotecaRepo, reseniaRepo);
    }

    // Helper methods to create test data
    private Long crearUsuarioActivo() throws ValidationExcepcion {
        var usuarioControlador = new UsuarioControlador(usuarioRepo);
        var usuarioDto = usuarioControlador.registrarNuevoUsuario(
                new UsuarioForm(
                        "user1",
                        "user1@example.com",
                        "Pass1234",
                        "User 1",
                        "Spain",
                        LocalDate.of(1990, 1, 1),
                        Optional.of("avatar.png"),
                        100.0,
                        ESTADOCUENTA.ACTIVA
                )
        );
        assertNotNull(usuarioDto);
        return usuarioDto.id();
    }

    private Long crearJuegoDisponible() {
        var juegoOpt = juegoRepo.crear(new JuegoForm(
                "TestGame",
                Optional.of("Game description"),
                "DevCompany",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                20.0,
                Optional.of(0),
                PEGIJUEGO.PEGI_3,
                Optional.of(new String[]{"EN"}),
                ESTADOJUEGO.DISPONIBLE,
                CATEGORIAJUEGO.ACCION
        ));

        assertTrue(juegoOpt.isPresent());
        return juegoOpt.get().getId();
    }

    private void agregarJuegoABiblioteca(Long idUsuario, Long idJuego) {
        bibliotecaRepo.crear(new BibliotecaForm(null, idUsuario, idJuego, LocalDateTime.now(), 10.0, Optional.empty(), ESTADOINSTALACIONBIBLIOTECA.INSTALADO));
    }

    @Test
    void testEscribirReseñaExitoso() throws ValidationExcepcion {
        Long idUsuario = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario, idJuego);

        ReseniaDto result = controlador.escribirResenia(idUsuario, idJuego, true, "Este es un texto de reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres para ser válido.");

        assertNotNull(result);
        assertEquals(idUsuario, result.idUsuario());
        assertEquals(idJuego, result.idJuego());
        assertTrue(result.recomendado());
        assertEquals("Este es un texto de reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres para ser válido.", result.textoResenia());
        assertEquals(ESTADORESENIA.PUBLICADA, result.estado());
    }

    @Test
    void testEscribirReseniaUsuarioNoPropietario() {
        Long idUsuario = 1L;
        Long idJuego = 1L;

        ValidationExcepcion exception = assertThrows(ValidationExcepcion.class, () ->
            controlador.escribirResenia(idUsuario, idJuego, true, "Este es un texto de reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres para ser válido."));

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.campo().equals("juego")));
    }

    @Test
    void testEscribirReseniaDuplicada() throws ValidationExcepcion {
        Long idUsuario = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario, idJuego);

        controlador.escribirResenia(idUsuario, idJuego, true, "Primera reseña con texto lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.");

        ValidationExcepcion exception = assertThrows(ValidationExcepcion.class, () ->
            controlador.escribirResenia(idUsuario, idJuego, false, "Segunda reseña con texto lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres."));

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.campo().equals("resenia")));
    }

    @Test
    void testEliminarReseniaExitoso() throws ValidationExcepcion {
        // Setup: crear reseña
        Long idUsuario = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario, idJuego);
        ReseniaDto reseña = controlador.escribirResenia(idUsuario, idJuego, true, "Texto de reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.");

        ReseniaDto result = controlador.eliminarResenia(reseña.id(), idUsuario);

        assertNotNull(result);
        assertEquals(ESTADORESENIA.ELIMINADA, result.estado());
    }

    @Test
    void testEliminarReseniaNoExiste() {
        ValidationExcepcion exception = assertThrows(ValidationExcepcion.class, () ->
            controlador.eliminarResenia(999L, 1L));

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.campo().equals("resenia")));
    }

    @Test
    void testEliminarReseniaNoPertenece() throws ValidationExcepcion {
        Long idUsuario1 = crearUsuarioActivo();
        Long idUsuario2 = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario1, idJuego);
        ReseniaDto reseña = controlador.escribirResenia(idUsuario1, idJuego, true, "Texto de reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.");

        ValidationExcepcion exception = assertThrows(ValidationExcepcion.class, () ->
            controlador.eliminarResenia(reseña.id(), idUsuario2));

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.campo().equals("usuario")));
    }

    @Test
    void testOcultarReseniaExitoso() throws ValidationExcepcion {
        Long idUsuario = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario, idJuego);
        ReseniaDto reseña = controlador.escribirResenia(idUsuario, idJuego, true, "Texto de reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.");

        ReseniaDto result = controlador.ocultarResenia(reseña.id(), idUsuario);

        assertNotNull(result);
        assertEquals(ESTADORESENIA.OCULTA, result.estado());
    }

    @Test
    void testOcultarReseniaYaOculta() throws ValidationExcepcion {
        Long idUsuario = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario, idJuego);
        ReseniaDto reseña = controlador.escribirResenia(idUsuario, idJuego, true, "Texto");
        controlador.ocultarResenia(reseña.id(), idUsuario); // Ocultar primero

        ValidationExcepcion exception = assertThrows(ValidationExcepcion.class, () ->
            controlador.ocultarResenia(reseña.id(), idUsuario));

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.campo().equals("estado")));
    }

    @Test
    void testVerReseniasJuego() throws ValidationExcepcion {
        Long idUsuario = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario, idJuego);
        controlador.escribirResenia(idUsuario, idJuego, true, "Buena reseña con texto lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.");

        List<ReseniaDto> result = controlador.verReseniasJuego(idJuego, null, "recientes");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Buena reseña con texto lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.", result.get(0).textoResenia());
    }

    @Test
    void testVerReseniasUsuario() throws ValidationExcepcion {
        Long idUsuario = crearUsuarioActivo();
        Long idJuego = crearJuegoDisponible();
        agregarJuegoABiblioteca(idUsuario, idJuego);
        controlador.escribirResenia(idUsuario, idJuego, true, "Texto de reseña lo suficientemente largo para cumplir con los requisitos mínimos de longitud establecidos en el sistema. Debe tener al menos 50 caracteres.");

        List<ReseniaDto> result = controlador.verReseniasUsuario(idUsuario, null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}