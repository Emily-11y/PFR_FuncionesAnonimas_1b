// Parte 3 - Tipos de problemas resueltos con funciones anonimas (Java 17+ con Streams)
// Ejecutar:  java java/Parte3.java      (o)   javac java/Parte3.java && java -cp java Parte3

import java.util.*;
import java.util.stream.*;

public class Parte3 {

    // record: clase inmutable (equivale a la case class de Scala)
    record Producto(String nombre, String categoria, double precio, int stock) {}

    // Misma lista de 12 productos en los tres lenguajes
    static final List<Producto> PRODUCTOS = List.of(
        new Producto("Laptop",      "Tecnologia",  850.0, 12),
        new Producto("Mouse",       "Tecnologia",   25.0, 80),
        new Producto("Teclado",     "Tecnologia",   45.0, 55),
        new Producto("Monitor",     "Tecnologia",  220.0, 20),
        new Producto("Cuaderno",    "Papeleria",     2.5, 200),
        new Producto("Esfero",      "Papeleria",     0.8, 500),
        new Producto("Calculadora", "Papeleria",    18.0, 40),
        new Producto("Mochila",     "Accesorios",   35.0, 30),
        new Producto("Audifonos",   "Accesorios",   60.0, 45),
        new Producto("Botella",     "Accesorios",   12.0, 70),
        new Producto("Silla",       "Muebles",     120.0, 8),
        new Producto("Escritorio",  "Muebles",     180.0, 5)
    );

    // Formato fijo (2 decimales con punto) para que los 3 lenguajes impriman igual
    static String fmt(double x) { return String.format(Locale.US, "%.2f", x); }
    static String lista(Stream<String> s) { return s.collect(Collectors.joining(", ", "[", "]")); }

    public static void main(String[] args) {

        // 1. SELECCION - filter: la lambda es un Predicate<Producto> (devuelve boolean)
        System.out.println("== 1. Seleccion ==");
        System.out.println("Productos con precio > 50: " +
            lista(PRODUCTOS.stream().filter(p -> p.precio() > 50).map(p -> p.nombre())));

        // 2. TRANSFORMACION - map: la lambda es una Function<Producto,String>
        System.out.println("== 2. Transformacion ==");
        final double iva = 0.15; // Java exige que la variable capturada sea "efectivamente final"
        System.out.println("Precio con IVA 15%: " +
            lista(PRODUCTOS.stream().map(p -> p.nombre() + ":" + fmt(p.precio() * (1 + iva)))));

        // 3. VALIDACION - anyMatch / allMatch
        System.out.println("== 3. Validacion ==");
        System.out.println("Existe algun producto con precio > 800: " +
            PRODUCTOS.stream().anyMatch(p -> p.precio() > 800));
        System.out.println("Todos tienen stock > 0: " +
            PRODUCTOS.stream().allMatch(p -> p.stock() > 0));
        System.out.println("Todos cuestan menos de 500: " +
            PRODUCTOS.stream().allMatch(p -> p.precio() < 500));

        // 4. CONTEO - filter + count
        System.out.println("== 4. Conteo ==");
        System.out.println("Productos con stock < 30: " +
            PRODUCTOS.stream().filter(p -> p.stock() < 30).count());

        // 5. EXTREMOS - max / min con un Comparator creado con una lambda
        System.out.println("== 5. Extremos ==");
        System.out.println("Producto mas caro: " +
            PRODUCTOS.stream().max(Comparator.comparingDouble(p -> p.precio())).get().nombre());
        System.out.println("Producto con menos stock: " +
            PRODUCTOS.stream().min(Comparator.comparingInt(p -> p.stock())).get().nombre());

        // 6. APLANAMIENTO - flatMap: la lambda devuelve un Stream por elemento
        System.out.println("== 6. Aplanamiento ==");
        System.out.println("Palabras clave de busqueda: " +
            lista(PRODUCTOS.stream()
                .flatMap(p -> Stream.of(p.nombre(), p.categoria()))
                .distinct()));

        // 7. ACUMULACION - reduce (equivale a foldLeft). Java NO tiene scanLeft:
        //    el paso a paso se arma recalculando la suma de cada prefijo de la lista.
        System.out.println("== 7. Acumulacion ==");
        double total = PRODUCTOS.stream()
            .reduce(0.0, (acc, p) -> acc + p.precio() * p.stock(), (a, b) -> a + b);
        System.out.println("Valor total del inventario: " + fmt(total));
        System.out.println("Valor acumulado paso a paso: " +
            lista(IntStream.range(0, PRODUCTOS.size())
                .mapToObj(i -> fmt(PRODUCTOS.subList(0, i + 1).stream()
                    .mapToDouble(p -> p.precio() * p.stock()).sum()))));
    }
}
