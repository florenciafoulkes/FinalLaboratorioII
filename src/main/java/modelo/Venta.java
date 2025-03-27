/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;


/**
 *
 * @author Usuario
 */
public class Venta {
    // Atributos de la clase Venta
    private int id;
    private Producto producto;
    private int cantidad;
    private LocalDate fecha;

    // Constructor de Venta
    public Venta(Producto producto, int cantidad, LocalDate fecha, int id) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.id = id;
    }

    // Getter y setter
    public int getID(){
        return id;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    
    
    
}
