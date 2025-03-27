
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


/**
 *
 * @author Usuario
 */
public class Producto {
    
    // Atributos de la clase Producto
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int disponibles;

    // Constructor de producto
    public Producto(String codigo, String nombre, String descripcion, double precio,int disponibles) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponibles = disponibles;
    }
    
    

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }
    
    
}
