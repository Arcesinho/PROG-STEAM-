package org.adrian.repositorio.interfaces;

import org.adrian.modelo.entidad.CompraEntidad;
import org.adrian.modelo.form.CompraForm;

import java.util.List;

public interface ICompraRepo extends ICrud<CompraEntidad, CompraForm, Long>{

    List<CompraEntidad> obtenerPorUsuario(Long idUsuario);
}
