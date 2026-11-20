package curso.inventario;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InventarioServiceTest {
    private static Producto producto(String codigo, String nombre, String precio, int stock) {
        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setPrecio(new BigDecimal(precio));
        p.setStock(stock);
        return p;
    }

    @Test void registraProductos() {
        InventarioService inv = new InventarioService();
        inv.registrarProducto(producto("P001", "Teclado", "45.00", 10));
        assertEquals(1, inv.listarProductos().size());
    }

    @Test void sumaUnidades() {
        InventarioService inv = inventarioEjemplo();
        assertEquals(21, inv.contarUnidades());
    }

    @Test void calculaValorInventario() {
        InventarioService inv = inventarioEjemplo();
        assertEquals(0, new BigDecimal("1190.00").compareTo(inv.calcularValorInventario()));
    }

    @Test void rechazaNombreEnBlanco() {
        InventarioService inv = new InventarioService();
        assertThrows(IllegalArgumentException.class,
                () -> inv.registrarProducto(producto("P099", "   ", "12.00", 2)));
    }

    private static InventarioService inventarioEjemplo() {
        InventarioService inv = new InventarioService();
        inv.registrarProducto(producto("P001", "Teclado", "45.00", 10));
        inv.registrarProducto(producto("P002", "Ratón", "25.00", 8));
        inv.registrarProducto(producto("P003", "Monitor", "180.00", 3));
        return inv;
    }
}
