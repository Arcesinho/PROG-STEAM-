package org.adrian.modelo.form;

import org.adrian.modelo.enums.*;
import org.adrian.recursos.ComprobarDosDecimales;

import java.time.Period;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;

public record UsuarioForm(String nombreUsuario, String email, String contrasenia, String nombreReal, String pais, LocalDate fechaNacimiento,
                          Optional<String> avatar, Double saldoCartera, ESTADOCUENTA estado) {

    public static final int MIN_LENG_NOMBREUSUARIO = 3;
    public static final int MAX_LENG_NOMBREUSUARIO = 20;
    public static final int MIN_LENG_CONTRASENIA = 8;
    public static final int MIN_LENG_NOMBREREAL = 2;
    public static final int MAX_LENG_NOMBREREAL = 50;
    public static final int MIN_EDADNACIMIENTO = 13;
    public static final int MAX_LENG_AVATAR = 100;

    public List<ErrorDto> validar() {

        var errores = new ArrayList<ErrorDto>();

        //Seteamos variables para comparar nuestro email contra ellas

        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@"+"[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        //Variables necesarias para validar la contrasenia

        int mayusculas = 0, minusculas = 0, digitos = 0;
        for (char c : contrasenia.toCharArray()) {
            if (Character.isUpperCase(c)) mayusculas++;
            else if (Character.isLowerCase(c)) minusculas++;
            else if (Character.isDigit(c)) digitos++;
        }

        //Lista de paises válidos

        List<String> paisesValidos = new ArrayList<>();

        String[] codigosPaises = Locale.getISOCountries();

        for (String codigo : codigosPaises) {
            Locale locale = new Locale("", codigo);
            paisesValidos.add(locale.getDisplayCountry());
        }
        Collections.sort(paisesValidos);

        //Calculo de años de la edad del usuario

        Period edadEnAnios = Period.between(fechaNacimiento, LocalDate.now());


        //Validaciones del form para el nombreUsuario

        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            errores.add(new ErrorDto("nombreUsuario", ErrorType.REQUERIDO));
        }
        if (nombreUsuario == null || Character.isDigit(nombreUsuario.charAt(0))) {
            errores.add(new ErrorDto("nombreUsuario", ErrorType.NO_EMPEZAR_POR_NUMERO));
        }
        if (nombreUsuario == null || !nombreUsuario.matches("[A-Za-z0-9_-]+")){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.FORMATO_INVALIDO));
        }
        if(nombreUsuario == null ||nombreUsuario.length()< MIN_LENG_NOMBREUSUARIO){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(nombreUsuario == null ||nombreUsuario.length()> MAX_LENG_NOMBREUSUARIO){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones del form para el email

        if(email == null || email.isBlank()){
            errores.add(new ErrorDto("email", ErrorType.REQUERIDO));
        }
        if (!matcher.matches()) {
            errores.add(new ErrorDto("email", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones del form para la contrasenia

        if (contrasenia == null) {
            errores.add(new ErrorDto("contrasenia", ErrorType.REQUERIDO));
        }
        if(contrasenia == null || contrasenia.length()< MIN_LENG_CONTRASENIA){
            errores.add(new ErrorDto("contrasenia", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (!(mayusculas >= 1 && minusculas >= 1 && digitos >= 1)) {
            errores.add(new ErrorDto("contrasenia", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones del form para el nombreReal

        if(nombreReal==null){
            errores.add(new ErrorDto("nombreReal", ErrorType.REQUERIDO));
        }
        if(nombreReal == null|| nombreReal.length()< MIN_LENG_NOMBREREAL){
            errores.add(new ErrorDto("nombreReal", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(nombreReal == null|| nombreReal.length()> MAX_LENG_NOMBREREAL){
            errores.add(new ErrorDto("nombreReal", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones del form para el pais

        if (pais == null) {
            errores.add(new ErrorDto("pais", ErrorType.REQUERIDO));
        }
        if(pais == null || !paisesValidos.contains(pais)){
            errores.add(new ErrorDto("pais", ErrorType.NO_ENCONTRADO));
        }

        //Validaciones del form para  la fechaNacimiento

        if (fechaNacimiento == null || fechaNacimiento.toString().isBlank()) {
            errores.add(new ErrorDto("fechaNacimiento", ErrorType.REQUERIDO));
        }
        else if(fechaNacimiento == null || edadEnAnios.getYears() < MIN_EDADNACIMIENTO){
            errores.add(new ErrorDto("fechaNacimiento", ErrorType.FECHA_NO_VALIDA));
        }
        else if(fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())){
            errores.add(new ErrorDto("fechaNacimiento", ErrorType.FECHA_NO_VALIDA));
        }

        //Validaciones del avatar

        var av = avatar.orElse(null);
        if( av.length() > MAX_LENG_AVATAR){
            errores.add(new ErrorDto("avatar", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones saldo

        if(!(saldoCartera >= 0)){
            errores.add(new ErrorDto("saldoCartera", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(!ComprobarDosDecimales.tieneDosOMenosDecimales(saldoCartera)){
            errores.add(new ErrorDto("saldoCartera", ErrorType.FORMATO_INVALIDO));
        }


        return errores;
    }

}
