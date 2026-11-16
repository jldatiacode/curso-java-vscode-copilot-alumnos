package curso.vscode;

import java.math.BigDecimal;

/**
 * Programa principal del ejercicio 1.2.1.
 *
 * Crea un inventario, registra productos, los consulta y actualiza sus
 * existencias. Es el código sobre el que se practican los atajos de VS Code.
 */
public class MainInventario {

    // Por debajo de este número de unidades, un producto se considera bajo mínimos.
    private static final int UMBRAL_STOCK_BAJO = 5;

    public static void main(String[] args) {
        Inventario inventario = new Inventario();

        // Registro de productos de ejemplo.
        inventario.registrarProducto(new Producto(1, "Teclado", new BigDecimal("25.50"), 10));
        inventario.registrarProducto(new Producto(2, "Ratón", new BigDecimal("12.90"), 25));
        inventario.registrarProducto(new Producto(3, "Monitor", new BigDecimal("189.00"), 4));
        inventario.registrarProducto(new Producto(4, "Portátil", new BigDecimal("899.99"), 2));

        mostrarListado(inventario);

        // Consulta de productos por identificador.
        System.out.println();
        mostrarProducto(inventario, 3);
        mostrarProducto(inventario, 99);

        // Actualización de existencias.
        System.out.println();
        boolean actualizado = inventario.actualizarStock(3, 12);
        System.out.println("Actualizar stock del producto 3 a 12 unidades: " + describir(actualizado));
        actualizado = inventario.actualizarStock(99, 1);
        System.out.println("Actualizar stock del producto 99: " + describir(actualizado));

        // Resultados finales.
        System.out.println();
        mostrarListado(inventario);
        System.out.println();
        System.out.println("Productos con menos de " + UMBRAL_STOCK_BAJO + " unidades:");
        for (Producto producto : inventario.productosConStockBajo(UMBRAL_STOCK_BAJO)) {
            System.out.println("  " + producto.getNombre() + " (" + producto.getCantidad() + " uds.)");
        }
        System.out.println("Valor total del inventario: " + inventario.calcularValorTotal() + " EUR");
    }

    /**
     * Muestra todos los productos del inventario.
     *
     * @param inventario inventario que se muestra
     */
    private static void mostrarListado(Inventario inventario) {
        System.out.println("Listado de productos:");
        for (Producto producto : inventario.listarProductos()) {
            System.out.println("  " + producto);
        }
    }

    /**
     * Traduce el resultado de una actualización a un texto legible.
     *
     * @param actualizado resultado devuelto por actualizarStock
     * @return "correcto" o "no existe"
     */
    private static String describir(boolean actualizado) {
        return actualizado ? "correcto" : "no existe";
    }

    /**
     * Busca un producto e indica si existe.
     *
     * @param inventario inventario donde se busca
     * @param id         identificador del producto
     */
    private static void mostrarProducto(Inventario inventario, int id) {
        inventario.buscarProducto(id).ifPresentOrElse(
                producto -> System.out.println("Producto " + id + ": " + producto.getNombre() + ", "
                        + producto.getCantidad() + " uds. a " + producto.getPrecio() + " EUR"),
                () -> System.out.println("Producto " + id + ": no existe"));
    }
}
