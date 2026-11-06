// Parte 2 - Entender: que es una funcion anonima (Java 17+)
// Ejecutar:  java java/Parte2.java

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Parte2 {

    record Item(String nombre, double precio) {}

    // (a) Metodo NOMBRADO
    static boolean esCaraNombrada(Item i) { return i.precio() > 50; }

    public static void main(String[] args) {
        List<Item> items = List.of(new Item("Laptop", 850.0), new Item("Mouse", 25.0), new Item("Silla", 120.0));

        // (b) Misma logica ANONIMA (lambda)
        System.out.println(items.stream().filter(Parte2::esCaraNombrada).map(i -> i.nombre()).toList());
        System.out.println(items.stream().filter(i -> i.precio() > 50).map(i -> i.nombre()).toList());

        // (c) PRIMERA CLASE: una lambda es un objeto de una interfaz funcional (Predicate, Function...)
        Predicate<Item> esCara = i -> i.precio() > 50;                 // guardada en una variable
        Function<Double, Predicate<Item>> masCaroQue = u -> i -> i.precio() > u; // devuelve otra funcion
        System.out.println(items.stream().filter(esCara).count());
        System.out.println(items.stream().filter(masCaroQue.apply(100.0)).map(i -> i.nombre()).toList());

        // (d) CLOSURE: la lambda usa una variable externa, pero en Java esa variable
        //     debe ser "efectivamente final" (no se puede reasignar).
        double descuento = 0.10;
        Function<Item, Double> conDescuento = i -> i.precio() * (1 - descuento);
        System.out.println(items.stream().map(conDescuento).toList());
        // descuento = 0.50;   // <- descomentar esta linea provoca error de compilacion
    }
}
