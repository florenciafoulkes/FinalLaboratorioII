/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.*;

/**
 *
 * @author Usuario
 */
public class ContMenu implements ActionListener {

    FormMenu menu = new FormMenu();

    // Constructor, abre el menu y activa las acciones de los botones
    public ContMenu() {

        this.menu.setVisible(true);
        this.menu.setLocationRelativeTo(null);

        // Define acciones y comandos de los botones
        this.menu.btnProveedores.addActionListener(this);
        this.menu.btnProductos.addActionListener(this);
        this.menu.btnVentas.addActionListener(this);
        this.menu.btnSalir.addActionListener(this);

        this.menu.btnProveedores.setActionCommand("Proveedores");
        this.menu.btnProductos.setActionCommand("Productos");
        this.menu.btnSalir.setActionCommand("Salir");
        this.menu.btnVentas.setActionCommand("Ventas");

    }

    // Métodos para abrir cada formulario o salir del menú
    private void abrirProductos(FormMenu menu) {
        
        ContProducto cp = new ContProducto(menu);
        menu.setVisible(false);
        
    }

    private static void abrirProveedores(FormMenu menu) {
        ContProveedor pr = new ContProveedor(menu);
        menu.setVisible(false);

    }

    private static void abrirVentas(FormMenu menu) {
        ContVenta cv = new ContVenta(menu);
        menu.setVisible(false);
    }

    private static void salirMenu(FormMenu menu) {
        menu.dispose();
    }
    
    // Método sobreescrito para determinar las acciones de los botones
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Productos" -> abrirProductos(this.menu);
            case "Proveedores" -> abrirProveedores(this.menu);
            case "Ventas" -> abrirVentas(this.menu);
            case "Salir" -> salirMenu(this.menu);
        }
    }

}
