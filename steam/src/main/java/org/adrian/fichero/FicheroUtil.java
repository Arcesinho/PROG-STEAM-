package org.adrian.fichero;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class FicheroUtil {

        private static final String SEPARATOR = ", ";

        public static void main(String[] args) {

            Path path = verPathUsuario();

        }

        private static Path verPathUsuario() {

            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce ahora la ruta del fichero de salida:");
            String ruta = sc.nextLine();

            return Path.of(ruta);
        }

        private static void escribirFichero(List<Integer> numeros, Path path) {

            if (numeros == null) {
                throw new IllegalArgumentException("numeros: null");
            }

            if (path == null) {
                throw new IllegalArgumentException("path: null");
            }
            if (Files.exists(path)) {
                throw new IllegalArgumentException("El path ya existe: " + path);
            }

            String s = numeros.isEmpty() ? "No se introdujeron números"
                    : numeros.stream()
                    .map(n -> n + "")
                    .reduce("", (acc, n) -> acc + SEPARATOR + n);

            s = s.substring(0, s.length() - SEPARATOR.length());

            try {
                Files.writeString(path, s);
            } catch (IOException e) {
                System.err.printf("Error escribiendo los números: %s en path: %s%n", s, path);
                e.printStackTrace();
            }
        }
}
