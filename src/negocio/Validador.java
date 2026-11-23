package negocio;



import java.util.Scanner;



public class Validador {



    public static boolean esSoloNumeros(String texto) {

        if (texto.equals("")) return false;

        for (int i = 0; i < texto.length(); i++) {

            if (texto.charAt(i) < '0' || texto.charAt(i) > '9') return false;

        }

        return true;

    }



    public static boolean esSoloLetras(String texto) {

        if (texto.equals("")) return false;

        for (int i = 0; i < texto.length(); i++) {

            char c = texto.charAt(i);

            boolean ok = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == ' ' || c == 'ñ' || c == 'Ñ';

            if (!ok) return false;

        }

        return true;

    }



    public static int leerEntero(Scanner sc, String msg) {

        while (true) {

            System.out.print(msg);

            String in = sc.nextLine().trim();

            if (esSoloNumeros(in)) return Integer.parseInt(in);

            System.out.println("Error: Ingrese solo numeros enteros validos.");

        }

    }



    public static double leerDecimal(Scanner sc, String msg) {

        while (true) {

            System.out.print(msg);

            String in = sc.nextLine().trim().replace(',', '.');

            int puntos = 0;

            boolean ok = !in.equals("") && !in.equals(".");

            for (int i = 0; i < in.length(); i++) {

                char c = in.charAt(i);

                if (c == '.') puntos++;

                else if (c < '0' || c > '9') ok = false;

            }

            if (ok && puntos <= 1) return Double.parseDouble(in);

            System.out.println("Error: Numero decimal invalido (ej. 12.09).");

        }

    }

}