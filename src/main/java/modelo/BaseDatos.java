/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class BaseDatos {
    // Listas para guardar los datos
    public static List<Proveedor> proveedores = new ArrayList<>();
    public static List<Producto> productos = new ArrayList<>();
    public static List<Venta> ventas = new ArrayList<>();

    // Getter y setter
    public static List<Proveedor> getProveedores() {
        return proveedores;
    }

    public static void setProveedores(Proveedor proveedor) {
        proveedores.add(proveedor);
    }

    public static List<Producto> getProductos() {
        return productos;
    }

    public static void setProductos(Producto producto) {
        productos.add(producto);
    }

    public static List<Venta> getVentas() {
        return ventas;
    }

    public static void setVentas(Venta venta) {
        ventas.add(venta);
    }


    
    
}
