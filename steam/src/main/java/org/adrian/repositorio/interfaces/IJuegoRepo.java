package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.JuegoEntidad;
import org.adrian.modelo.form.JuegoForm;

/**
 * Repositorio de juegos del catálogo. Hereda todas las operaciones CRUD de {@link ICrud}.
 */
public interface IJuegoRepo extends ICrud<JuegoEntidad, JuegoForm, Long>{
}
