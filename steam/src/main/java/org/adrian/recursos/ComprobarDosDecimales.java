package org.adrian.recursos;


/**
 * Utilidad para validar la precisión decimal de valores numéricos.
 */
public  class ComprobarDosDecimales {

    /**
     * Comprueba que un número tiene como máximo 2 decimales.
     * Utiliza una tolerancia de 1e-6 para evitar errores de representación en coma flotante.
     *
     * @param valor número a comprobar
     * @return {@code true} si el valor tiene 0, 1 o 2 decimales significativos; {@code false} en caso contrario
     */
    public static boolean tieneDosOMenosDecimales(double valor) {
        double redondeado = Math.round(valor * 100) / 100.0f;
        return Math.abs(valor - redondeado) < 1e-6;
    }
}
