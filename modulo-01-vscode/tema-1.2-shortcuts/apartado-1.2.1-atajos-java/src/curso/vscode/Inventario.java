package curso.vscode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Inventario de productos en memoria.
 *
 * Mantiene una lista de productos y ofrece operaciones para registrarlos,
 * buscarlos, actualizar sus existencias y calcular el valor total.
 */
public class Inventario {

    // Productos registrados, en el orden en que se añaden.
    private final List<Producto> productos = new ArrayList<>();

    /**
     * Añade un producto al inventario.
     *
     * @param producto producto que se registra
     */
    public void registrarProducto(Producto producto) {
        productos.add(producto);
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return el producto, o un Optional vacío si no existe
     */
    public Optional<Producto> buscarProducto(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return Optional.of(producto);
            }
        }
        return Optional.empty();
    }

    /**
     * Cambia las unidades disponibles de un producto.
     *
     * @param id            identificador del producto
     * @param nuevaCantidad unidades disponibles a partir de ahora
     * @return true si el producto existía y se ha actualizado
     */
    public boolean actualizarStock(int id, int nuevaCantidad) {
        Optional<Producto> encontrado = buscarProducto(id);
        if (encontrado.isEmpty()) {
            return false;
        }
        encontrado.get().setCantidad(nuevaCantidad);
        return true;
    }

    /**
     * Suma el valor de las existencias de todos los productos.
     *
     * @return valor total del inventario
     */
    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Producto producto : productos) {
            total = total.add(producto.calcularValorStock());
        }
        return total;
    }

    /**
     * Devuelve los productos con menos unidades que el umbral indicado.
     *
     * @param umbral número mínimo de unidades recomendado
     * @return productos que conviene reponer
     */
    public List<Producto> productosConStockBajo(int umbral) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getCantidad() < umbral) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    /**
     * Devuelve todos los productos registrados.
     *
     * @return lista de solo lectura con los productos
     */
    public List<Producto> listarProductos() {
        return List.copyOf(productos);
    }
}
