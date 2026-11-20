package curso.inventario;

import java.math.BigDecimal;

/**
 * Clase de datos inicial. Completar el constructor y los getters en VS Code.
 */
public class Producto {
    private String codigo;
    private String nombre;
    private BigDecimal precio;
    private int stock;

    public Producto() {
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String mostrarLinea() {
        return codigo + " - " + nombre + " | " + precio + " EUR | Stock: " + stock;
    }
}
