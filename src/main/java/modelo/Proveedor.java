package modelo;

public class Proveedor extends Persona {

    // Atributo de la clase Proveedor
    private String categoria;

    // Construcot de Proveedor que invoca al constructor padre
    public Proveedor(String nombre, String apellido, String email, String dni, String telefono, String categoria) {
        super(nombre, apellido, email, dni, telefono);
        this.categoria = categoria;
    }

    // Getter y setter
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDNI() {
        return dni;
    }

    public void setDNI(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Método sobreescrito para convertir nombre y apellido en un String
    @Override
    public StringBuilder nombreString(String nombre, String apellido) {
        StringBuilder x = new StringBuilder();

        x.append(nombre);
        x.append(", ");
        x.append(apellido);

        return x;
    }

}
