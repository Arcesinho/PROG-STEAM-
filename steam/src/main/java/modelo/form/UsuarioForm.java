package modelo.form;

import modelo.enums.CategoriaJuegoEnum;
import modelo.enums.EstadoCuentaEnum;
import modelo.enums.EstadoJuegoEnum;
import modelo.enums.PegiJuegoEnum;

import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record UsuarioForm(String nombreUsuario, String email, String contrasenia, String nombreReal, String pais, LocalDateTime fechaNacimiento, LocalDateTime fechaRegistro, String avatar, Double saldoCartera, EstadoCuentaEnum.ESTADOCUENTA estado) {

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

        //Lista paises validos

        List<String> paisesValidos = new ArrayList<>();

        String[] codigosPaises = Locale.getISOCountries();

        for (String codigo : codigosPaises) {
            Locale locale = new Locale("", codigo);
            paisesValidos.add(locale.getDisplayCountry());
        }
        Collections.sort(paisesValidos);




        //Validaciones del form para el nombreUsuario

        if (nombreUsuario == null) {
            errores.add(new ErrorDto("nombreUsuario", ErrorType.REQUERIDO));
        }
        assert nombreUsuario != null;
        if (Character.isDigit(nombreUsuario.charAt(0))) {
            errores.add(new ErrorDto("nombreUsuario", ErrorType.NO_EMPEZAR_POR_NUMERO));
        }
        if (!nombreUsuario.matches("[a-zA-Z]+-+_]")){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.FORMATO_INVALIDO));
        }
        if(nombreUsuario.length()<3){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(nombreUsuario.length()>20){
            errores.add(new ErrorDto("nombreUsuario", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones del form para el email

        if(email == null) {
            errores.add(new ErrorDto("email", ErrorType.REQUERIDO));
        }
        if (!matcher.matches()) {
            errores.add(new ErrorDto("email", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones del form para la contrasenia

        if (contrasenia == null) {
            errores.add(new ErrorDto("contrasenia", ErrorType.REQUERIDO));
        }
        assert contrasenia != null;
        if(contrasenia.length()<8){
            errores.add(new ErrorDto("contrasenia", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if (!(mayusculas >= 1 && minusculas >= 1 && digitos >= 1)) {
            errores.add(new ErrorDto("contrasenia", ErrorType.FORMATO_INVALIDO));
        }

        //Validaciones del form para el nombreReal

        if(nombreReal==null){
            errores.add(new ErrorDto("nombreReal", ErrorType.REQUERIDO));
        }
        assert nombreReal!= null;
        if(nombreReal.length()<2){
            errores.add(new ErrorDto("nombreReal", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(nombreReal.length()>50){
            errores.add(new ErrorDto("nombreReal", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        //Validaciones del form para el pais

        if (pais == null) {
            errores.add(new ErrorDto("pais", ErrorType.REQUERIDO));
        }
        assert pais!= null;
        if(!(pais.matches(String.valueOf(paisesValidos)))){
            errores.add(new ErrorDto("pais", ErrorType.NO_ENCONTRADO));
        }

        //Validaciones del form para  la fechaNacimiento

        if (fechaNacimiento == null) {
            errores.add(new ErrorDto("fechaNacimiento", ErrorType.REQUERIDO));
        }
        assert fechaNacimiento != null;

        return errores;
    }




}
