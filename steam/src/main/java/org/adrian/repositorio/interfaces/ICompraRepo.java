package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.CompraEntidad;
import org.adrian.modelo.form.CompraForm;

import java.util.List;

/**
 * Repositorio de transacciones de compra.
 * Extiende las operaciones CRUD básicas con consultas por usuario.
 */
public interface ICompraRepo extends ICrud<CompraEntidad, CompraForm, Long>{

    /**
     * Devuelve todas las compras realizadas por un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return lista de compras del usuario; vacía si no tiene ninguna
     */
    List<CompraEntidad> obtenerPorUsuario(Long idUsuario);
}
