package modelo.form;

import modelo.enums.CategoriaJuegoEnum;
import modelo.enums.EstadoCuentaEnum;
import modelo.enums.EstadoJuegoEnum;
import modelo.enums.PegiJuegoEnum;

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



        return errores;
    }



}
