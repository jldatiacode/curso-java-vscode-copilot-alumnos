package curso.inventario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Servicio inicial: registro básico; el recuento y el valor están pendientes. */
public class InventarioService {
    private final List<Producto> productos = new ArrayList<>();

    public void registrarProducto(Producto producto) {
        // TODO: validar producto, código y nombre; registrar logs
        productos.add(producto);
    }

    public List<Producto> listarProductos() {
        return List.copyOf(productos);
    }

    public int contarUnidades() {
        // TODO: sumar stock de todos los productos
        return 0;
    }

    public BigDecimal calcularValorInventario() {
        // TODO: sumar precio * stock de cada producto
        return BigDecimal.ZERO;
    }
}
