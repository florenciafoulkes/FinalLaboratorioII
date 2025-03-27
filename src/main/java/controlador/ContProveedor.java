/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.BaseDatos;
import modelo.Proveedor;
import vista.*;

/**
 *
 * @author Usuario
 */
public class ContProveedor implements ActionListener, IFunciones {

    // Atributos del Controlador Proveedor
    private final FormProveedor formProveedor = new FormProveedor();
    private final FormMenu menu;
    private final DefaultTableModel dtm = (DefaultTableModel) formProveedor.tablaProveedor.getModel();

    // Constructor que hace visible el form
    public ContProveedor(FormMenu menu) {
        this.menu = menu;
        this.formProveedor.setLocationRelativeTo(menu);
        this.formProveedor.setVisible(true);

        iniciarAcciones();
        cargar();
    }

    // Define las acciones y comandos de los botones
    @Override
    public final void iniciarAcciones() {
        this.formProveedor.btnGuardarC.setVisible(false);
        this.formProveedor.btnAgregarC.setActionCommand("Agregar");
        this.formProveedor.btnModificarC.setActionCommand("Modificar");
        this.formProveedor.btnGuardarC.setActionCommand("Guardar");
        this.formProveedor.btnEliminarC.setActionCommand("Eliminar");
        this.formProveedor.btnEliminarTodoC.setActionCommand("EliminarT");
        this.formProveedor.btnVolverC.setActionCommand("Volver");

        this.formProveedor.btnAgregarC.addActionListener(this);
        this.formProveedor.btnModificarC.addActionListener(this);
        this.formProveedor.btnGuardarC.addActionListener(this);
        this.formProveedor.btnEliminarC.addActionListener(this);
        this.formProveedor.btnEliminarTodoC.addActionListener(this);
        this.formProveedor.btnVolverC.addActionListener(this);
    }

    // Carga la lista de Proveedores
    @Override
    public final void cargar() {
        int rows = this.dtm.getRowCount() - 1;
        // Remueve todas las filas
        for (int i = rows; i >= 0; i--) {
            this.dtm.removeRow(i);
        }
        // Carga desde la base de datos
        for (Proveedor cl : BaseDatos.getProveedores()) {

            Object[] obj = new Object[5];
            obj[0] = cl.nombreString(cl.getNombre(), cl.getApellido());
            obj[1] = cl.getDNI();
            obj[2] = cl.getEmail();
            obj[3] = cl.getTelefono();
            obj[4] = cl.getCategoria();
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
    private boolean validarCampos(String nombre, String apellido, String email, String dni, String telefono, String categoria, String boton) {
        String error = "";
        try {
            // Revisa que los TextField no estén vacíos
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()
                    || dni.isEmpty() || telefono.isEmpty() || categoria.isEmpty()) {
                throw new NullPointerException();
            }
            // Revisa que el nombre y apellido sean válidos
            if (nombre.length() < 3 || !Funciones.nombreValido(nombre)) {
                error += "- Nombre inválido.\n";
            }
            if (apellido.length() < 3 || !Funciones.nombreValido(apellido)) {
                error += "- Apellido inválido.\n";
            }

            // Revisa que el mail contenga @ y .com
            if (!(email.contains("@") && email.contains(".com"))) {
                error += "- Email inválido.\n";
            }

            // Revisa que el DNI sean nros y tenga la cantidad de caracteres adecuada
            if (!Funciones.sonNros(dni) || (dni.length() < 7 || dni.length() > 8)) {
                error += "- DNI inválido.\n";
            }

            // Revisa que no se haya cargado alguien con ese DNI
            if (boton.equals("Agregar")) {
                for (Proveedor cl : BaseDatos.getProveedores()) {
                    if (cl.getDNI().equals(dni)) {
                        error += "- El DNI ya fue ingresado.\n";
                    }
                }
            }

            // Revisa que el teléfono sean números y sea de Rafaela
            if (!telefono.contains("3492") || !Funciones.sonNros(telefono)) {
                error += "- Teléfono inválido.\n";
            }

            // Si se detectan datos inválidos tira una excepción
            if (!error.equals("")) {
                throw new CustomException();
            } else {
                return true;
            }
        } catch (NullPointerException ne) {
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
    // Agrega un Proveedor
    @Override
    public void agregar() {

        // Busca los datos ingresados
        String nombre = formProveedor.txtNombre.getText().trim();
        String apellido = formProveedor.txtApellido.getText().trim();
        String email = formProveedor.txtEmail.getText().trim();
        String dni = formProveedor.txtDNI.getText().trim();
        String telefono = formProveedor.txtTelefono.getText().trim();
        String categoria = formProveedor.txtCategoria.getSelectedItem().toString();

        // Si los campos son váidos agrego el proveedor a la base de datos y la tabla
        if (validarCampos(nombre, apellido, email, dni, telefono, categoria, "Agregar")) {
            Proveedor p = new Proveedor(nombre, apellido, email, dni, telefono, categoria);
            BaseDatos.setProveedores(p);

            cargar(); // Actualiza la tabla

            JOptionPane.showMessageDialog(null, "Proveedor agregado.");
            vaciar(); // Vacía los TextField
        }

    }

    // Trae los datos a modificar
    @Override
    public void modificar() {
        try {
            // Revisa si hay alguna fila seleccionada
            if (formProveedor.tablaProveedor.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                int row = formProveedor.tablaProveedor.getSelectedRow();

                String nya = this.dtm.getValueAt(row, 0).toString();
                String[] dividir = nya.split(", ");
                String nombre = dividir[0];
                String apellido = dividir[1];

                // Cargo los TextField con los datos seleccionados
                formProveedor.txtNombre.setText(nombre);
                formProveedor.txtApellido.setText(apellido);
                formProveedor.txtDNI.setText(this.dtm.getValueAt(row, 1).toString());
                formProveedor.txtEmail.setText(this.dtm.getValueAt(row, 2).toString());
                formProveedor.txtTelefono.setText(this.dtm.getValueAt(row, 3).toString());
                formProveedor.txtCategoria.setSelectedItem(this.dtm.getValueAt(row, 4));
                formProveedor.btnGuardarC.setVisible(true);

            }
        } catch (NullPointerException ne) {
            // Mensaje en caso de que no haya filas seleccionadas
            CustomException.msgSeleccionar();
        }
    }

    // Guarda los nuevos datos
    @Override
    public void guardar() {
        try {
            // Revisa si hay alguna fila seleccionada
            if (formProveedor.tablaProveedor.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                // Busca los nuevos datos ingresados
                String nombre = formProveedor.txtNombre.getText().trim();
                String apellido = formProveedor.txtApellido.getText().trim();
                String email = formProveedor.txtEmail.getText().trim();
                String dni = formProveedor.txtDNI.getText().trim();
                String telefono = formProveedor.txtTelefono.getText().trim();
                String categoria = formProveedor.txtCategoria.getSelectedItem().toString();

                // Si los campos son válidos reescribe el objeto en base de datos
                if (validarCampos(nombre, apellido, email, dni, telefono, categoria, "Guardar")) {
                    int row = formProveedor.tablaProveedor.getSelectedRow();
                    String documento = this.dtm.getValueAt(row, 1).toString();

                    for (Proveedor pr : BaseDatos.getProveedores()) {
                        if (pr.getDNI().equals(documento)) {
                            pr.setNombre(nombre);
                            pr.setApellido(apellido);
                            pr.setEmail(email);
                            pr.setDNI(dni);
                            pr.setTelefono(telefono);
                            pr.setCategoria(categoria);
                            break;
                        }
                    }
                    // Recarga la lista en la tabla y vacío los TextField
                    cargar();
                    vaciar();

                    // El botón Guardar sólo es visible mientras se intenta modificar un proveedor
                    formProveedor.btnGuardarC.setVisible(false);
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
            if (formProveedor.tablaProveedor.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                // Consulta antes de borrar completamente un Proveedor
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro desea eliminar?", "Eliminar proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opc == JOptionPane.YES_OPTION) {
                    int row = formProveedor.tablaProveedor.getSelectedRow();

                    // Crea una lista de proveedores a eliminar y borra el mismo de base de datos
                    List<Proveedor> eliminar = new ArrayList<>();

                    for (Proveedor p : BaseDatos.getProveedores()) {
                        if (p.getDNI().equals(this.dtm.getValueAt(row, 1))) {
                            eliminar.add(p);
                        }
                    }

                    for (Proveedor p : eliminar) {
                        BaseDatos.getProveedores().remove(p);
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
                BaseDatos.getProveedores().clear();
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
        this.formProveedor.setVisible(false);
    }

    // Vacía los TextField
    @Override
    public void vaciar() {
        formProveedor.txtNombre.setText("");
        formProveedor.txtApellido.setText("");
        formProveedor.txtEmail.setText("");
        formProveedor.txtDNI.setText("");
        formProveedor.txtTelefono.setText("");
        formProveedor.txtCategoria.setSelectedIndex(0);
    }

}
