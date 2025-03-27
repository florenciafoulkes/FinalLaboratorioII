/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vista.*;

/**
 *
 * @author Usuario
 */
public class ContVenta implements ActionListener, IFunciones {

    private final FormVenta formVenta = new FormVenta();
    private final FormMenu menu;
    private final DefaultTableModel dtm = (DefaultTableModel) formVenta.tablaVentas.getModel();
    private int identificador = 1;

    // Constructor que hace visible el form
    public ContVenta(FormMenu menu) {
        this.menu = menu;
        this.formVenta.setLocationRelativeTo(menu);
        this.formVenta.setVisible(true);

        // Define acciones de los botones y carga la lista
        iniciarAcciones();
        cargar();
    }

    // Define comandos y acciones de los botones
    @Override
    public final void iniciarAcciones() {
        this.formVenta.btnGuardarV.setVisible(false);
        this.formVenta.btnAgregarV.setActionCommand("Agregar");
        this.formVenta.btnModificarV.setActionCommand("Modificar");
        this.formVenta.btnGuardarV.setActionCommand("Guardar");
        this.formVenta.btnEliminarV.setActionCommand("Eliminar");
        this.formVenta.btnEliminarTodoV.setActionCommand("EliminarT");
        this.formVenta.btnVolverV.setActionCommand("Volver");

        this.formVenta.btnAgregarV.addActionListener(this);
        this.formVenta.btnModificarV.addActionListener(this);
        this.formVenta.btnGuardarV.addActionListener(this);
        this.formVenta.btnEliminarV.addActionListener(this);
        this.formVenta.btnEliminarTodoV.addActionListener(this);
        this.formVenta.btnVolverV.addActionListener(this);
    }

    // Carga la lista de ventas
    @Override
    public final void cargar() {
        // Remueve todas las filas
        int rows = this.dtm.getRowCount() - 1;
        for (int i = rows; i >= 0; i--) {
            this.dtm.removeRow(i);
        }
        // Carga desde la base de datos
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Venta ve : BaseDatos.getVentas()) {

            Object[] obj = new Object[6];
            obj[0] = ve.getID();
            obj[1] = ve.getProducto().getCodigo();
            obj[2] = ve.getProducto().getPrecio();
            obj[3] = ve.getCantidad();
            obj[4] = ve.getFecha().format(formato);
            obj[5] = Funciones.calcularTotal(ve.getProducto().getPrecio(), ve.getCantidad());
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
    private boolean validarCampos(String codigo, String cant, String fecha, String btn) {
        String error = "";
        try {
            // Revisa que ningun campo esté vacío
            if (codigo.isEmpty() || cant.isEmpty() || fecha.isEmpty()) {
                throw new NullPointerException();
            }

            // Revisa que el código pertenezca a un producto y la cantidad esté disponible
            boolean bandera = false;
            for (Producto p : BaseDatos.getProductos()) {
                if (codigo.equals(p.getCodigo())) {
                    bandera = true;
                    // Modifica la cant disponible del producto según se modifique la venta
                    switch (btn) {
                        case "Agregar":
                            if (Integer.parseInt(cant) > p.getDisponibles()) {
                                error += String.format("- Hay %d disponibles.\n", p.getDisponibles());
                            }
                            break;
                        case "Guardar":
                            if (Integer.parseInt(cant) > p.getDisponibles()) {
                                error += String.format("- Quedan %d disponibles para agregar.\n", p.getDisponibles());
                            }
                            break;
                    }

                }
            }
            if (!bandera) {
                error += "- El código no pertenece a ningún producto.\n";
            }

            // Revisa la fecha
            if (fecha.length() != 10) {
                error += "- El formato de fecha debe ser: dd/MM/yyyy.\n";
            } else {

                // Revisa que la fecha no sobrepase los límites
                if (Funciones.convertirFecha(fecha).isAfter(LocalDate.now())) {
                    error += "- Fecha posterior a hoy";
                } else {
                    String[] f = fecha.split("/");
                    int dia = Integer.parseInt(f[0]);
                    int mes = Integer.parseInt(f[1]);
                    int anio = Integer.parseInt(f[2]);

                    if (mes > 1 && mes < 12) {
                        switch (mes) {
                            case 2:
                                if (dia < 1 || dia > 29) {
                                    error += "- Día inválido.\n";
                                } else {
                                    if (dia == 29 && !(Funciones.esBisiesto(anio))) {
                                        error += "- Día 29/02 en año no bisiesto.\n";
                                    }
                                }
                            case 4:
                            case 6:
                            case 9:
                            case 11:
                                if (dia < 1 || dia > 30) {
                                    error += "- El mes indicado sólo tiene 30 días.\n";
                                }
                            default:
                                if (dia < 1 || dia > 31) {
                                    error += "- Día fuera de rango.\n";
                                }
                        }
                    } else {
                        error += "- Mes inválido.\n";
                    }
                }
            }

            // Si el string de revisión contiene algún error lanza una excepción
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
    // Agrega una venta
    @Override
    public void agregar() {

        // Busca los datos ingresados
        String codigo = formVenta.txtCodigoV.getText().trim();
        String cant = formVenta.txtCant.getSelectedItem().toString();
        String fecha = formVenta.txtFechaV.getText().trim();

        // Si los campos son váidos agrego la venta a la base de datos y la tabla
        if (validarCampos(codigo, cant, fecha, "Agregar")) {
            LocalDate fechaV = Funciones.convertirFecha(fecha);
            int cantidad = Integer.parseInt(cant);

            for (Producto p : BaseDatos.getProductos()) {
                if (codigo.equals(p.getCodigo())) {
                    Venta v = new Venta(p, cantidad, fechaV, getIdentificador());
                    setIdentificador(); // Incrementa el identificador de ventas
                    BaseDatos.setVentas(v);
                    p.setDisponibles(p.getDisponibles() - cantidad); // Resto los productos vendidos
                    break;
                }
            }
            cargar(); // Actualiza la tabla

            JOptionPane.showMessageDialog(null, "Venta agregada.");
            vaciar(); // Vacía los TextField
        }

    }

    // Trae los datos a modificar
    @Override
    public void modificar() {
        try {
            // Revisa si hay alguna fila seleccionada
            if (formVenta.tablaVentas.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                int row = formVenta.tablaVentas.getSelectedRow();
                int index = (int) this.dtm.getValueAt(row, 3) - 1;

                // Cargo los TextField con los datos seleccionados
                formVenta.txtCodigoV.setText(this.dtm.getValueAt(row, 1).toString());
                formVenta.txtCant.setSelectedIndex(index);
                formVenta.txtFechaV.setText(this.dtm.getValueAt(row, 4).toString());

                formVenta.btnGuardarV.setVisible(true);
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
            if (formVenta.tablaVentas.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                // Busca los nuevos datos ingresados
                String codigo = formVenta.txtCodigoV.getText().trim();
                String cant = formVenta.txtCant.getSelectedItem().toString();
                String fecha = formVenta.txtFechaV.getText().trim();

                int row = formVenta.tablaVentas.getSelectedRow();
                int idSelected = Integer.parseInt(this.dtm.getValueAt(row, 0).toString());

                // Actualiza la información de la venta según el ID y el código del producto
                for (Venta v : BaseDatos.getVentas()) {
                    if (v.getID() == idSelected) {
                        for (Producto p : BaseDatos.getProductos()) {
                            int cantidad = Integer.parseInt(cant)-v.getCantidad();
                            // Si los campos son válidos reescribe el objeto en base de datos
                            if (validarCampos(codigo, Integer.toString(cantidad), fecha, "Guardar")) {
                                if (p.getCodigo().equals(codigo)) {
                                    // Según la modificación de cant vendida modifica la cant disponible
                                    if (Integer.parseInt(cant) > v.getCantidad()) {
                                        int dif = Integer.parseInt(cant) - v.getCantidad();
                                        p.setDisponibles(p.getDisponibles() - dif);
                                    } else {
                                        int dif = v.getCantidad() - Integer.parseInt(cant);
                                        p.setDisponibles(p.getDisponibles() + dif);
                                    }

                                    v.setProducto(p);
                                    v.setCantidad(Integer.parseInt(cant));
                                    v.setFecha(Funciones.convertirFecha(fecha));

                                    break;
                                }
                            }
                        }
                    }

                    // Actualiza la lista y vacía los TextField
                    cargar();
                    vaciar();

                    // El botón Guardar sólo es visible mientras se intenta modificar una venta
                    formVenta.btnGuardarV.setVisible(false);
                }
            }

        } catch (NullPointerException ne) {
            // Mensaje en caso de que no haya una fila seleccionada
            CustomException.msgSeleccionar();
        }
    }

    // Elimina una venta
    @Override
    public void eliminar() {
        try {
            // Revisa si hay alguna fila seleccionada
            if (formVenta.tablaVentas.getSelectedRow() == -1) {
                throw new NullPointerException();
            } else {
                // Consulta antes de borrar completamente una venta
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro desea eliminar?", "Eliminar venta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opc == JOptionPane.YES_OPTION) {
                    int row = formVenta.tablaVentas.getSelectedRow();
                    int idSelected = Integer.parseInt(this.dtm.getValueAt(row, 0).toString());

                    // Crea una lista de ventas a eliminar y borra la misma de base de datos
                    List<Venta> eliminar = new ArrayList<>();
                    for (Venta v : BaseDatos.getVentas()) {
                        if (v.getID() == idSelected) {
                            eliminar.add(v);
                            v.getProducto().setDisponibles(v.getProducto().getDisponibles() + v.getCantidad());
                        }
                    }

                    for (Venta v : eliminar) {
                        BaseDatos.getVentas().remove(v);
                    }
                    cargar(); // Actualiza la lista
                } else {
                    throw new CustomException();
                }
            }
        } catch (NullPointerException ex) {
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

                // Recarga la disponibilidad de productos
                for (Venta v : BaseDatos.getVentas()) {
                    v.getProducto().setDisponibles(v.getProducto().getDisponibles() + v.getCantidad());
                }

                // Vacía la base de datos
                BaseDatos.getVentas().clear();
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
        this.formVenta.setVisible(false);
    }

    // Vacía los TextField
    @Override
    public void vaciar() {
        formVenta.txtCodigoV.setText("");
        formVenta.txtFechaV.setText("");
        formVenta.txtCant.setSelectedIndex(0);
    }

    // Get y set del identificador
    private int getIdentificador() {
        return this.identificador;
    }

    private void setIdentificador() {
        this.identificador++;
    }

}
