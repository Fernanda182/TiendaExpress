/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tiendaexpress;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author ferch
 */
public class Main {
private static final double IVA = 0.15; // 15%
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
     Scanner sc = new Scanner(System.in);
        List<Double> carrito = new ArrayList<>();
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1 -> {
                    double precio = leerPrecio(sc);
                    agregarProducto(carrito, precio);
                    System.out.println("✅ Producto agregado. En carrito: " + carrito.size() + " artículo(s).\n");
                }
                case 3 -> pagar(carrito, sc);
                case 4 -> {
                    System.out.println("👋 Saliendo de Tienda Express. ¡Gracias por visitarnos!");
                    salir = true;
                }
                default -> System.out.println("Opción no válida. Intenta de nuevo.\n");
            }
        }
        sc.close();
    }

    // --- Métodos solicitados ---
    static void mostrarMenu() {
        System.out.println("==============================");
        System.out.println("        TIENDA EXPRESS");
        System.out.println("==============================");
        System.out.println("1. Agregar producto: ");
        System.out.println("3. Pagar: ");
        System.out.println("4. Salir");
        System.out.print("Elige una opción (1, 3 o 4): ");
    }

    static int leerOpcion(Scanner sc) {
        while (true) {
            String linea = sc.nextLine().trim();
            try {
                int opcion = Integer.parseInt(linea);
                if (opcion == 1 || opcion == 3 || opcion == 4) {
                    return opcion;
                }
                System.out.print("Solo 1, 3 o 4. Intenta de nuevo: ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada no numérica. Intenta de nuevo: ");
            }
        }
    }

    static void agregarProducto(List<Double> carrito, double precio) {
        carrito.add(precio);
    }

    static double total(double base, double imp) {
        return base + imp;
    }

    static void confirmarCompra(String correo, double total) {
        System.out.println("\n📧 Confirmación enviada a: " + correo);
        System.out.println("💳 Monto total cobrado: $" + String.format(Locale.US, "%.2f", total));
        System.out.println("¡Gracias por tu compra en Tienda Express!\n");
    }

    static void pagar(List<Double> carrito, Scanner sc) {
        if (carrito.isEmpty()) {
            System.out.println("\n⚠️ Tu carrito está vacío. Agrega productos antes de pagar.\n");
            return;
        }

        double subtotal = carrito.stream().mapToDouble(Double::doubleValue).sum();
        double impuestos = subtotal * IVA;
        double total = total(subtotal, impuestos);

        // --- Reporte breve ---
        imprimirReporte(carrito, subtotal, impuestos, total);

        // --- Validación de correo ---
        String correo = leerCorreo(sc);

        // --- Confirmación ---
        confirmarCompra(correo, total);

        // Vaciar carrito después de pagar
        carrito.clear();
    }

    // --- Helpers ---
    private static double leerPrecio(Scanner sc) {
        System.out.print("Ingresa el precio del producto ($): ");
        while (true) {
            String linea = sc.nextLine().trim().replace(",", ".");
            try {
                double precio = Double.parseDouble(linea);
                if (precio > 0) return redondearDos(precio);
                System.out.print("Debe ser mayor a 0. Intenta de nuevo: ");
            } catch (NumberFormatException e) {
                System.out.print("Formato inválido. Ej: 12.50. Intenta de nuevo: ");
            }
        }
    }

    private static String leerCorreo(Scanner sc) {
        System.out.print("Ingresa tu correo para enviar el comprobante: ");
        while (true) {
            String correo = sc.nextLine().trim();
            if (esCorreoValido(correo)) return correo;
            System.out.print("Correo inválido. Ej: usuario@dominio.com. Intenta de nuevo: ");
        }
    }

    private static boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return correo.matches(regex);
    }

    private static void imprimirReporte(List<Double> carrito, double subtotal, double impuestos, double total) {
        System.out.println("\n========= REPORTE DE COMPRA =========");
        System.out.println("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Artículos: " + carrito.size());
        for (int i = 0; i < carrito.size(); i++) {
            System.out.println("  - Item " + (i + 1) + ": $" + String.format(Locale.US, "%.2f", carrito.get(i)));
        }
        System.out.println("------------------------------------");
        System.out.println("Subtotal: $" + String.format(Locale.US, "%.2f", subtotal));
        System.out.println("IVA (" + (int)(IVA * 100) + "%): $" + String.format(Locale.US, "%.2f", impuestos));
        System.out.println("TOTAL:   $" + String.format(Locale.US, "%.2f", total));
        System.out.println("====================================\n");
    }

    private static double redondearDos(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }   
    }
    

