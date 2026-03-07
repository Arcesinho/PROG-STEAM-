package org.adrian.controladorTest;

import org.adrian.controlador.UsuarioControlador;
import org.adrian.excepcion.ValidationExcepcion;
import org.adrian.modelo.dto.UsuarioDto;
import org.adrian.modelo.entidad.UsuarioEntidad;
import org.adrian.modelo.enums.EstadoCuentaEnum;
import org.adrian.modelo.form.UsuarioForm;
import org.adrian.repositorio.interfaces.IUsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class UsuarioControladorTest {

    private IUsuarioRepo repo = new LocalUsuarioRepo() {
        @Override
        public Optional<UsuarioEntidad> obtenerPorId(Long aLong) {
            return Optional.empty();
        }

        @Override
        public Optional<UsuarioEntidad> actualizar(Long aLong, UsuarioForm form) {
            return Optional.empty();
        }

        @Override
        public boolean eliminar(Long aLong) {
            return false;
        }
    };
    private UsuarioControlador controlador;

    private static abstract class LocalUsuarioRepo implements IUsuarioRepo {
        private final List<UsuarioEntidad> usuarios = new ArrayList<>();

        @Override
        public List<UsuarioEntidad> obtenerTodos() {
            return new ArrayList<>(usuarios);
        }

        @Override
        public Optional<UsuarioEntidad> crear(UsuarioForm form) {
            UsuarioEntidad user = new UsuarioEntidad(
                    1L, form.nombreUsuario(), form.email(), form.contrasenia(),
                    form.nombreReal(), form.pais(), form.fechaNacimiento(),
                    null, null, null , EstadoCuentaEnum.ESTADOCUENTA.ACTIVA
            );
            usuarios.add(user);
            return Optional.of(user);
        }
    }

    @BeforeEach
    void setUp() {
        this.controlador = new UsuarioControlador(repo);
    }

    @Test
    public void testRegistroExitoso() throws ValidationExcepcion {

        UsuarioForm form = new UsuarioForm(
                "Test","adrian@gmail.com",
                "Password123",
                "Adrian",
                "Spain",
                LocalDate.of(2000, 1, 1),
                Optional.of("Adrian"),
                100.0,
                EstadoCuentaEnum.ESTADOCUENTA.ACTIVA);

        UsuarioDto resultado = controlador.registrarNuevoUsuario(form);

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1, repo.obtenerTodos().size(), "Debería haber un usuario en el repo");
    }

    @Test
    void testErrorPorEmailDuplicado() {
        UsuarioForm primerForm = new UsuarioForm(
                "UserUno", "duplicado@gmail.com", "Pass1", "Real", "Spain",
                LocalDate.of(1990, 1, 1), Optional.empty(), 0.0, EstadoCuentaEnum.ESTADOCUENTA.ACTIVA
        );
        try { controlador.registrarNuevoUsuario(primerForm); } catch (Exception ignored) {}

        UsuarioForm segundoForm = new UsuarioForm(
                "UserDos", "duplicado@gmail.com", "Pass2", "Real2", "Spain",
                LocalDate.of(1995, 5, 5), Optional.empty(), 0.0, EstadoCuentaEnum.ESTADOCUENTA.ACTIVA
        );

        // Verificamos que lance la excepción por el anyMatch del controlador
        assertThrows(ValidationExcepcion.class, () -> {
            controlador.registrarNuevoUsuario(segundoForm);
        }, "Debería lanzar ValidationExcepcion por email duplicado");
    }
}