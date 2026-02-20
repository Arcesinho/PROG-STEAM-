package org.adrian.recursos;


public  class ComprobarDosDecimales {

    public static boolean tieneDosOMenosDecimales(double valor) {
        double redondeado = Math.round(valor * 100) / 100.0f;
        return Math.abs(valor - redondeado) < 1e-6;
    }
}
