/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Usuario
 */
public class Funciones {
    
    // Funciones varias que utilizan algunas clases
    // Revisa si un string está compuesto por letras
    public static boolean sonLetras(String c) {
        c = c.toLowerCase();
        for (int i = 0; i < c.length(); i++) {
            if (c.charAt(i) < 97 || c.charAt(i) > 122) {
                return false;
            }
        }
        return true;
    }

    // Revisa si un string está compuesto por números
    public static boolean sonNros(String c) {
        for (int i = 0; i < c.length(); i++) {
            if (c.charAt(i) < 46 || c.charAt(i) > 57 || c.charAt(i) == 47) {
                return false;
            }
        }
        return true;
    }
    
    // Valida un nombre o apellido que puede ser compuesto
    public static boolean nombreValido(String c){
        String sinEspacios = c.replace(" ", "");
        return sonLetras(sinEspacios);
    }
    
    // Valida si un año es bisiesto
    public static boolean esBisiesto(int anio) {
        if ((anio % 4 == 0 && anio % 100 != 0) || (anio % 100 == 0 && anio % 400 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    // Calcula el total entre cantidad de productos y precio
    public static double calcularTotal(double precio, int cant) {
        return precio * cant;
    }

    // Convierte un string de fecha a un LocalDate con formato "dd/MM/yyyy"
    public static LocalDate convertirFecha(String fecha) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse(fecha, formato);
        return ld;
    }
}
