package org.adrian.transaction;

import org.adrian.excepcion.ValidationExcepcion;

public interface ExceptionSupplier<T> {

    T get() throws ValidationExcepcion;
}
