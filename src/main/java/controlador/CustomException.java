/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class CustomException extends Exception {
    
    // Constructo que llama utiliza el del padre
    public CustomException(){
        super();
    }

    // Mensajes de la excepción
    public static void msgErrorDatos(String x){
        JOptionPane.showMessageDialog(null, x, "Datos inválidos", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void msgErrorNulo(){
        JOptionPane.showMessageDialog(null, "Debe ingresar todos los campos.", "Error:", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void msgSeleccionar(){
        JOptionPane.showMessageDialog(null, "Debe seleccionar una fila.", "Error:", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void msgOperacionC(){
        JOptionPane.showMessageDialog(null,"Operación cancelada.");
    }
    
}
