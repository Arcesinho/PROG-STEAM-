package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.UsuarioEntidad;
import org.adrian.modelo.form.UsuarioForm;

/**
 * Repositorio de usuarios. Hereda todas las operaciones CRUD de {@link ICrud}.
 */
public interface IUsuarioRepo extends ICrud<UsuarioEntidad, UsuarioForm, Long>{
}
