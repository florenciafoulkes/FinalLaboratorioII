/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.BaseDatos;
import modelo.Producto;
import vista.FormMenu;
import vista.FormProducto;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class ContProducto implements ActionListener, IFunciones {

    // Atributos del Controlador Producto
    private final FormProducto formprod = new FormProducto();
    private final FormMenu menu;
    private final DefaultTableModel dtm = (DefaultTableModel) formprod.tablaProductos.getModel();

    // Constructor que hace visible el form
    public ContProducto(FormMenu menu) {
        this.menu = menu;
        this.formprod.setLocationRelativeTo(menu);
        this.formprod.setVisible(true);

        // Define acciones de los botones y carga la lista
        iniciarAcciones();
        cargar();

    }

    // Define comandos y acciones de los botones
    @Override
    public final void iniciarAcciones() {
        this.formprod.btnGuardarP.setVisible(false);
        this.formprod.btnAgregarP.setActionCommand("Agregar");
        this.formprod.btnModificarP.setActionCommand("Modificar");
        this.formprod.btnGuardarP.setActionCommand("Guardar");
        this.formprod.btnEliminarP.setActionCommand("Eliminar");
        this.formprod.btnEliminarTodoP.setActionCommand("EliminarT");
        this.formprod.btnVolverP.setActionCommand("Volver");

        this.formprod.btnAgregarP.addActionListener(this);
        this.formprod.btnModificarP.addActionListener(this);
        this.formprod.btnGuardarP.addActionListener(this);
        this.formprod.btnEliminarP.addActionListener(this);
        this.formprod.btnEliminarTodoP.addActionListener(this);
        this.formprod.btnVolverP.addActionListener(this);
    }

    // Carga la lista de productos
    @Override
    public final void cargar() {
        
        // Remueve todas las filas
        int rows = this.dtm.getRowCount() - 1;
        for (int i = rows; i >= 0; i--) {
            this.dtm.removeRow(i);
        }
        
        // Carga desde la base de datos
        for (Producto pr : BaseDatos.getProductos()) {

            Object[] obj = new Object[5];
            obj[0] = pr.getCodigo();
            obj[1] = pr.getNombre();
            obj[2] = pr.getDescripcion();
            obj[3] = pr.getPrecio();
            obj[4] = pr.getDisponibles();
            this.dtm.addRow(obj);

        }
    }

    // Método sobreescrito para llamar a las funciones de cada botón
    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        switch (command) {
            case "Agregar" ->
                agregar();
            case "Modificar" ->
                modificar();
            case "Guardar" ->
                guardar();
            case "Eliminar" ->
                eliminar();
            case "EliminarT" ->
                eliminarTodo();
            case "Volver" ->
                volver();
        }

    }

    // Valida todos los campos del form
    private boolean validarCampos(String codigo, String nombre, String desc, String precio,String disp,String boton) {
        String error = "";
        try {
            
            // Revisa que ningún campo esté vacío
            if (precio.isEmpty() || codigo.isEmpty() || nombre.isEmpty() || desc.isEmpty() || disp.isEmpty()) {
                throw new NullPointerException();
            }

            // Revisa que el código no haya sido utilizado
            if(boton.equals("Agregar")){
                for(Producto p :BaseDatos.getProductos()){
                    if(codigo.equals(p.getCodigo())){
                        error+= "- El código pertenece a otro producto.\n";
                    }
                }
            }
            
            // Revisa que el código tenga el formato ABC123
            if (codigo.length() != 6) {
                error += "- El código debe contener 6 caracteres.\n";
            } else {
                String a = codigo.substring(0, 2);
                String b = codigo.substring(3, 5);
                if (!Funciones.sonLetras(a) || !Funciones.sonNros(b)) {
                    error += "- Código inválido.\n";
                }
            }

            // Revisa que el nombre y descripción sean válidos
            if (nombre.length() < 3 || !Funciones.nombreValido(nombre)) {
                error += "- Nombre inválido.\n";
            }

            if (desc.length() < 3 || desc.length() > 60) {
                error += "- Descripción inválida.\n";
            }

            // Revisa que el precio sean números y mayor a 0
            if (!Funciones.sonNros(precio) || Double.parseDouble(precio) <= 0) {
                error += "- Precio inválido.\n";
            }

            // Si el string de revisión contiene algún error lanza una excepción
            if (!error.equals("")) {
                throw new CustomException();
            } else {
                return true;
            }
        } catch (NullPointerException e) {
            // Mensaje en caso de campos vacíos
            CustomException.msgErrorNulo();
            return false;
        } catch (CustomException ce) {
            // Mensaje en caso de datos inválidos
            CustomException.msgErrorDatos(error);
            return false;
        }

    }

    // Funciones de los botones
    // Agrega un producto
    @Override
    public void agregar() {
        
            // Busca los datos ingresados
            String codigo = formprod.txtCodigo.getText().trim();
            String nombre = formprod.txtNombreP.getText().trim();
            String desc = formprod.txtDescripcion.getText().trim();
            String precio = formprod.txtPrecio.getText().trim();
            String disp = formprod.txtDispP.getSelectedItem().toString();

            // Si los campos son váidos agrego el producto a la base de datos y la tabla
            if (validarCampos(codigo, nombre, desc, precio,disp,"Agregar")) {
                Producto p = new Producto(codigo, nombre, desc, Double.parseDouble(precio),Integer.parseInt(disp));
                BaseDatos.setProductos(p);

                cargar(); // Actualiza la lista

                JOptionPane.showMessageDialog(null, "Producto agregado.");
                vaciar(); // Vacía los TextField
            }
        
    }

    // Trae los datos a modificar
    @Override
    public void modificar() {
        try {
            // Revisa si hay alguna fila seleccionada
            if (formprod.tablaProductos.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                // Cargo los TextField con los datos seleccionados
                int row = formprod.tablaProductos.getSelectedRow();
                int index = (int) this.dtm.getValueAt(row, 4);
                formprod.txtCodigo.setText(this.dtm.getValueAt(row, 0).toString());
                formprod.txtNombreP.setText(this.dtm.getValueAt(row, 1).toString());
                formprod.txtDescripcion.setText(this.dtm.getValueAt(row, 2).toString());
                formprod.txtPrecio.setText(this.dtm.getValueAt(row, 3).toString());
                formprod.txtDispP.setSelectedIndex(index);
                formprod.btnGuardarP.setVisible(true);
            }
        } catch (NullPointerException n) {
            // Mensaje en caso de que no haya filas seleccionadas
            CustomException.msgSeleccionar();
        }
    }

    // Guarda los nuevos datos
    @Override
    public void guardar() {
        try {
            // Revisa si hay alguna fila seleccionada
            if (formprod.tablaProductos.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                // Busca los nuevos datos ingresados
                String codigo = formprod.txtCodigo.getText().trim();
                String nombre = formprod.txtNombreP.getText().trim();
                String desc = formprod.txtDescripcion.getText().trim();
                String precio = formprod.txtPrecio.getText().trim();
                String disp = formprod.txtDispP.getSelectedItem().toString();

                // Si los campos son válidos reescribe el objeto en base de datos
                if (validarCampos(codigo, nombre, desc, precio,disp,"Guardar")) {
                    int row = formprod.tablaProductos.getSelectedRow();
                    String cod = this.dtm.getValueAt(row, 0).toString();

                    // Actualiza la información del producto según su código 
                    for (Producto pr : BaseDatos.getProductos()) {
                        if (cod.equals(pr.getCodigo())) {
                            pr.setCodigo(codigo);
                            pr.setNombre(nombre);
                            pr.setDescripcion(desc);
                            pr.setPrecio(Double.parseDouble(precio));
                            pr.setDisponibles(Integer.parseInt(disp));
                            break;

                        }
                    }

                    // Recarga la lista en la tabla y vacío los TextField
                    cargar();
                    vaciar();

                    // El botón Guardar sólo es visible mientras se intenta modificar un producto
                    formprod.btnGuardarP.setVisible(false);
                }
            }
        } catch (NullPointerException ne) {
            // Mensaje en caso de que no haya una fila seleccionada
            CustomException.msgSeleccionar();
        } 
    }

    // Elimina un producto
    @Override
    public void eliminar() {
        try {
            // Revisa si hay alguna fila seleccionada
            if (formprod.tablaProductos.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                // Consulta antes de borrar completamente un producto
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro desea eliminar?", "Eliminar producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opc == JOptionPane.YES_OPTION) {
                    int row = formprod.tablaProductos.getSelectedRow();
                    
                    // Crea una lista de productos a eliminar y borra el mismo de base de datos
                    List<Producto> eliminar = new ArrayList<>(); 

                    for (Producto pr : BaseDatos.getProductos()) {
                        if (pr.getCodigo().equals(this.dtm.getValueAt(row, 0))) {
                            eliminar.add(pr);
                        }
                    }

                    for (Producto p : eliminar) {
                        BaseDatos.getProductos().remove(p);
                    }

                    cargar(); // Actualiza la tabla
                } else {
                    throw new CustomException();
                }
            }
        } catch (NullPointerException ne) {
            // Mensaje en caso de que no haya ninguna fila seleccionada
            CustomException.msgSeleccionar();
        } catch (CustomException ce) {
            // Mensaje en caso de que no se desee eliminar
            CustomException.msgOperacionC();
        }
    }

    // Elimina toda la lista
    @Override
    public void eliminarTodo() {

        try {
            // Consulta antes de borrar completamente todo
            int opc = JOptionPane.showConfirmDialog(null, "¿Seguro desea eliminar todo?", "Eliminar todo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opc == JOptionPane.YES_OPTION) {
                
                // Borra cada fila de la tabla
                int rows = this.dtm.getRowCount() - 1;

                for (int i = rows; i >= 0; i--) {
                    this.dtm.removeRow(i);
                }

                // Vacía la base de datos
                BaseDatos.getProductos().clear();
            } else {
                throw new CustomException();
            }

        } catch (CustomException ce) {
            // Mensaje si se seleccionó NO borrar la base de datos
            CustomException.msgOperacionC();
        }
    }

    // Vuelve al menú principal
    @Override
    public void volver() {
        
        this.menu.setVisible(true);
        this.menu.setLocationRelativeTo(null);
        this.formprod.setVisible(false);
    }

    // Vacía los TextField
    @Override
    public void vaciar() {

        formprod.txtCodigo.setText("");
        formprod.txtNombreP.setText("");
        formprod.txtDescripcion.setText("");
        formprod.txtPrecio.setText("");
        formprod.txtDispP.setSelectedIndex(0);
    }
}
