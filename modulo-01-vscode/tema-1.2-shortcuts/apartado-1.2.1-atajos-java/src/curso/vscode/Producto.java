package curso.vscode;

import java.math.BigDecimal;

/**
 * Producto de un inventario.
 *
 * El precio se guarda como BigDecimal, el tipo recomendado en Java para
 * importes: evita los errores de redondeo de double.
 */
public class Producto {

    // Identificador único del producto.
    private final int id;

    // Nombre descriptivo.
    private final String nombre;

    // Precio por unidad.
    private final BigDecimal precio;

    // Unidades disponibles en el almacén. Es lo único que puede cambiar.
    private int cantidad;

    /**
     * Crea un producto con todos sus datos.
     *
     * @param id       identificador único
     * @param nombre   nombre del producto
     * @param precio   precio por unidad
     * @param cantidad unidades disponibles
     */
    public Producto(int id, String nombre, BigDecimal precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    /**
     * Cambia las unidades disponibles. No admite cantidades negativas.
     *
     * @param nuevaCantidad unidades disponibles a partir de ahora
     */
    public void setCantidad(int nuevaCantidad) {
        if (nuevaCantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa: " + nuevaCantidad);
        }
        this.cantidad = nuevaCantidad;
    }

    /**
     * Calcula el valor de las existencias de este producto (precio por cantidad).
     *
     * @return valor de las unidades disponibles
     */
    public BigDecimal calcularValorStock() {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", cantidad=" + cantidad + "]";
    }
}
