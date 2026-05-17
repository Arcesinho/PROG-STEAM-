package org.adrian.transaction;

import java.util.Optional;

import org.adrian.excepcion.ValidationExcepcion;


/**
 * Implementaci�n no-op de {@link ITransactionManager}.
 * Se usa con repositorios en memoria donde no existe el concepto de
 * transacci�n.
 */
public class NoOpTransactionManager implements ITransactionManager {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T inTransaction(ExceptionSupplier<T> work) throws ValidationExcepcion {
        try {
            return work.get();
        }catch(ValidationExcepcion e){
            throw e;
        } 
        catch (Exception e) {
            try {
                return (T) Optional.empty();
            } catch (ClassCastException ex) {
                return null;
            }
        }
    }
}
