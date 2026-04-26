package org.adrian.controladorTest;

import org.adrian.controlador.CompraControlador;
import org.adrian.controlador.UsuarioControlador;
import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.modelo.enums.CATEGORIAJUEGO;
import org.adrian.modelo.enums.ESTADOCUENTA;
import org.adrian.modelo.enums.ESTADOJUEGO;
import org.adrian.modelo.enums.ESTADOCOMPRA;
import org.adrian.modelo.enums.METODOPAGOCOMPRA;
import org.adrian.modelo.enums.PEGIJUEGO;
import org.adrian.modelo.form.CompraForm;
import org.adrian.modelo.form.JuegoForm;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.implementacionMemoria.BibliotecaRepoImplementacionMemoria;
import org.adrian.repositorio.implementacionMemoria.CompraRepoImplementacionMemoria;
import org.adrian.repositorio.implementacionMemoria.JuegoRepoImplementacionMemoria;
import org.adrian.repositorio.implementacionMemoria.UsuarioRepoImplementacionMemoria;
import org.adrian.repositorio.interfaces.IBibliotecaRepo;
import org.adrian.repositorio.interfaces.ICompraRepo;
import org.adrian.repositorio.interfaces.IJuegoRepo;
import org.adrian.repositorio.interfaces.IUsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CompraControladorTest {

    private ICompraRepo compraRepo;
    private IJuegoRepo juegoRepo;
    private IUsuarioRepo usuarioRepo;
    private IBibliotecaRepo bibliotecaRepo;
    private CompraControlador controlador;

    @BeforeEach
    void setUp() {
        compraRepo = new CompraRepoImplementacionMemoria();
        juegoRepo = new JuegoRepoImplementacionMemoria();
        usuarioRepo = new UsuarioRepoImplementacionMemoria();
        bibliotecaRepo = new BibliotecaRepoImplementacionMemoria();

        controlador = new CompraControlador(compraRepo, juegoRepo, usuarioRepo, bibliotecaRepo);
    }

    private Long crearUsuarioActivo() throws ValidationExcepcion {
        var usuarioDto = new UsuarioControlador(usuarioRepo).registrarNuevoUsuario(
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

    @Test
    void testRealizarCompraExitosa() throws ValidationExcepcion {
        Long usuarioId = crearUsuarioActivo();
        Long juegoId = crearJuegoDisponible();

        var compraResult = controlador.realizarCompraJuego(
                new CompraForm(null, usuarioId, juegoId, 20.0, Optional.of(0), METODOPAGOCOMPRA.CARTERA_STEAM, Optional.of(ESTADOCOMPRA.COMPLETADA))
        );

        assertNotNull(compraResult);
        assertEquals(usuarioId, compraResult.usuario().id());
        assertEquals(juegoId, compraResult.juego().id());
        assertEquals(1, compraRepo.obtenerTodos().size());
    }

    @Test
    void testRealizarCompraDuplicadaLanzaExcepcion() throws ValidationExcepcion {
        Long usuarioId = crearUsuarioActivo();
        Long juegoId = crearJuegoDisponible();

        controlador.realizarCompraJuego(
                new CompraForm(null, usuarioId, juegoId, 20.0, Optional.of(0), METODOPAGOCOMPRA.CARTERA_STEAM, Optional.of(ESTADOCOMPRA.COMPLETADA))
        );

        assertThrows(ValidationExcepcion.class, () -> {
            controlador.realizarCompraJuego(
                    new CompraForm(null, usuarioId, juegoId, 20.0, Optional.of(0), METODOPAGOCOMPRA.CARTERA_STEAM, Optional.of(ESTADOCOMPRA.COMPLETADA))
            );
        });
    }

    @Test
    void testRealizarCompraUsuarioNoExistenteLanzaExcepcion() {
        Long juegoId = crearJuegoDisponible();

        assertThrows(ValidationExcepcion.class, () -> {
            controlador.realizarCompraJuego(
                    new CompraForm(null, 9999L, juegoId, 20.0, Optional.of(0), METODOPAGOCOMPRA.CARTERA_STEAM, Optional.of(ESTADOCOMPRA.COMPLETADA))
            );
        });
    }

    @Test
    void testRealizarCompraSaldoInsuficienteLanzaExcepcion() throws ValidationExcepcion {
        // usuario con saldo de 5.0 < precio de 20.0
        var usuarioDto = new UsuarioControlador(usuarioRepo).registrarNuevoUsuario(
                new UsuarioForm(
                        "user2",
                        "user2@example.com",
                        "Pass1234",
                        "User 2",
                        "Spain",
                        LocalDate.of(1990, 1, 1),
                        Optional.empty(),
                        5.0,
                        ESTADOCUENTA.ACTIVA
                )
        );

        Long juegoId = crearJuegoDisponible();

        assertThrows(ValidationExcepcion.class, () -> {
            controlador.realizarCompraJuego(
                    new CompraForm(null, usuarioDto.id(), juegoId, 20.0, Optional.of(0), METODOPAGOCOMPRA.CARTERA_STEAM, Optional.of(ESTADOCOMPRA.COMPLETADA))
            );
        });
    }

    @Test
    void testRealizarCompraUsuarioInactivoLanzaExcepcion() throws ValidationExcepcion {
        var usuarioDto = new UsuarioControlador(usuarioRepo).registrarNuevoUsuario(
                new UsuarioForm(
                        "user3",
                        "user3@example.com",
                        "Pass1234",
                        "User 3",
                        "Spain",
                        LocalDate.of(1990, 1, 1),
                        Optional.empty(),
                        100.0,
                        ESTADOCUENTA.SUSPENDIDA
                )
        );

        Long juegoId = crearJuegoDisponible();

        assertThrows(ValidationExcepcion.class, () -> {
            controlador.realizarCompraJuego(
                    new CompraForm(null, usuarioDto.id(), juegoId, 20.0, Optional.of(0), METODOPAGOCOMPRA.CARTERA_STEAM, Optional.of(ESTADOCOMPRA.COMPLETADA))
            );
        });
    }

    @Test
    void testRealizarCompraJuegoNoDisponibleLanzaExcepcion() throws ValidationExcepcion {
        Long usuarioId = crearUsuarioActivo();

        var juegoOpt = juegoRepo.crear(new JuegoForm(
                "TestGame2",
                Optional.of("Game description"),
                "DevCompany",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                20.0,
                Optional.of(0),
                PEGIJUEGO.PEGI_3,
                Optional.of(new String[]{"EN"}),
                ESTADOJUEGO.NO_DISPONIBLE,
                CATEGORIAJUEGO.ACCION
        ));

        assertTrue(juegoOpt.isPresent());
        Long juegoId = juegoOpt.get().getId();

        assertThrows(ValidationExcepcion.class, () -> {
            controlador.realizarCompraJuego(
                    new CompraForm(null, usuarioId, juegoId, 20.0, Optional.of(0), METODOPAGOCOMPRA.CARTERA_STEAM, Optional.of(ESTADOCOMPRA.COMPLETADA))
            );
        });
    }
}
