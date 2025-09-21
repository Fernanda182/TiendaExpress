/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package CalculoDescuento;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author ferch
 */
public class CalculoDescuento {

    private static final double IVA = 0.15; // IVA del 15%

    
    public static double calcularDescuento(double montoTotal, double porcentajeDescuento) {
        return montoTotal * (porcentajeDescuento / 100.0);
    }

    
    public static double calcularDescuento(double montoTotal) {
        return calcularDescuento(montoTotal, 10);
    }
    public static void main(String[] args) {
        // TODO code application logic here
     double compra1 = 150.0;
        double descuento1 = calcularDescuento(compra1);
        double total1 = compra1 - descuento1;

        double compra2 = 200.0;
        double descuento2 = calcularDescuento(compra2, 15);
        double total2 = compra2 - descuento2;

        System.out.println("----- Cálculo de Descuentos (Prueba Inicial) -----");
        System.out.println("Compra 1: $" + compra1 + " -> Descuento (10%): $" + String.format("%.2f", descuento1) + " -> Total a pagar: $" + String.format("%.2f", total1));
        System.out.println("Compra 2: $" + compra2 + " -> Descuento (15%): $" + String.format("%.2f", descuento2) + " -> Total a pagar: $" + String.format("%.2f", total2));
        System.out.println("--------------------------------------------------\n");

        // ==== Flujo de tienda interactivo ====
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
                    System.out.println(" Producto agregado. En carrito: " + carrito.size() + " artículo(s).\n");
                }
                case 3 -> pagar(carrito, sc);
                case 4 -> {
                    System.out.println(" Saliendo de Tienda Express. ¡Gracias por visitarnos!");
                    salir = true;
                }
                default -> System.out.println("Opción no válida. Intenta de nuevo.\n");
            }
        }

        sc.close();
    }

    static void mostrarMenu() {
        System.out.println("==============================");
        System.out.println("        TIENDA EXPRESS");
        System.out.println("==============================");
        System.out.println("1. Agregar producto");
        System.out.println("3. Pagar");
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

    static double total(double base, double impuestos) {
        return base + impuestos;
    }

    static void pagar(List<Double> carrito, Scanner sc) {
        if (carrito.isEmpty()) {
            System.out.println("\n⚠️ Tu carrito está vacío. Agrega productos antes de pagar.\n");
            return;
        }

        double subtotal = carrito.stream().mapToDouble(Double::doubleValue).sum();

        // Preguntar si desea aplicar un descuento
        System.out.print("¿Deseas aplicar un descuento? (s/n): ");
        String respuesta = sc.nextLine().trim().toLowerCase();
        double descuento = 0.0;

        if (respuesta.equals("s")) {
            System.out.print("Ingresa el porcentaje de descuento (ej. 10): ");
            while (true) {
                try {
                    String entrada = sc.nextLine().trim().replace(",", ".");
                    double porcentaje = Double.parseDouble(entrada);
                    if (porcentaje >= 0 && porcentaje <= 100) {
                        descuento = calcularDescuento(subtotal, porcentaje);
                        break;
                    } else {
                        System.out.print("Debe ser entre 0 y 100. Intenta de nuevo: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Formato inválido. Ej: 10. Intenta de nuevo: ");
                }
            }
        }

        double subtotalConDescuento = subtotal - descuento;
        double impuestos = subtotalConDescuento * IVA;
        double total = total(subtotalConDescuento, impuestos);

        imprimirReporteConDescuento(carrito, subtotal, descuento, subtotalConDescuento, impuestos, total);

        String correo = leerCorreo(sc);
        confirmarCompra(correo, total);

        carrito.clear();
    }

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

    private static void imprimirReporteConDescuento(List<Double> carrito, double subtotalOriginal, double descuento, double subtotalConDescuento, double impuestos, double total) {
        System.out.println("\n========= REPORTE DE COMPRA =========");
        System.out.println("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Artículos: " + carrito.size());
        for (int i = 0; i < carrito.size(); i++) {
            System.out.println("  - Item " + (i + 1) + ": $" + String.format(Locale.US, "%.2f", carrito.get(i)));
        }
        System.out.println("------------------------------------");
        System.out.println("Subtotal: $" + String.format(Locale.US, "%.2f", subtotalOriginal));
        System.out.println("Descuento aplicado: -$" + String.format(Locale.US, "%.2f", descuento));
        System.out.println("Subtotal con descuento: $" + String.format(Locale.US, "%.2f", subtotalConDescuento));
        System.out.println("IVA (" + (int)(IVA * 100) + "%): $" + String.format(Locale.US, "%.2f", impuestos));
        System.out.println("TOTAL:   $" + String.format(Locale.US, "%.2f", total));
        System.out.println("====================================\n");
    }

    private static void confirmarCompra(String correo, double total) {
        System.out.println("\n Confirmación enviada a: " + correo);
        System.out.println(" Monto total cobrado: $" + String.format(Locale.US, "%.2f", total));
        System.out.println("¡Gracias por tu compra en Tienda Express!\n");
    }

    private static double redondearDos(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}