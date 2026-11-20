package curso.inventario;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        InventarioService inventario = new InventarioService();
        inventario.registrarProducto(crear("P001", "Teclado mecánico", "45.00", 10));
        inventario.registrarProducto(crear("P002", "Ratón inalámbrico", "25.00", 8));
        inventario.registrarProducto(crear("P003", "Monitor 27 pulgadas", "180.00", 3));

        System.out.println("=== INVENTARIO ===");
        for (Producto producto : inventario.listarProductos()) {
            System.out.println(producto.mostrarLinea());
        }
        System.out.println("Unidades: " + inventario.contarUnidades());
        System.out.println("Valor: " + inventario.calcularValorInventario() + " EUR");
    }

    private static Producto crear(String codigo, String nombre, String precio, int stock) {
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));
        producto.setStock(stock);
        return producto;
    }
}
