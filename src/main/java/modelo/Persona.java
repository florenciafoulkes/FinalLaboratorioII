/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public abstract class Persona {

    // Atributos de la clase Persona
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String dni;
    protected String telefono;

    // Constructor de Persona
    public Persona(String nombre, String apellido, String email, String dni, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.telefono = telefono;
    }

    // Método abstracto para convertir el nombre y apellido a un solo String
    public abstract StringBuilder nombreString(String nombre, String apellido);

}
