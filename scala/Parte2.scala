// Parte 2 - Entender: que es una funcion anonima (Scala)
// Ejecutar:  scalac scala/Parte2.scala && scala Parte2

case class Item(nombre: String, precio: Double)

object Parte2 {

  // (a) Funcion NOMBRADA: tiene nombre y se declara con def
  def esCaraNombrada(i: Item): Boolean = i.precio > 50

  def main(args: Array[String]): Unit = {
    val items = List(Item("Laptop", 850.0), Item("Mouse", 25.0), Item("Silla", 120.0))

    // (b) Misma logica ANONIMA: sin nombre, escrita justo donde se usa
    println(items.filter(esCaraNombrada).map(i => i.nombre))   // con funcion nombrada
    println(items.filter(i => i.precio > 50).map(i => i.nombre)) // con lambda

    // (c) Funciones de PRIMERA CLASE: se guardan en variables, se pasan y se devuelven
    val esCara: Item => Boolean = i => i.precio > 50            // guardada en un val
    def masCaroQue(umbral: Double): Item => Boolean = i => i.precio > umbral // devuelta por una funcion
    println(items.filter(esCara).size)                          // pasada como argumento
    println(items.filter(masCaroQue(100)).map(i => i.nombre))

    // (d) CLOSURE: la lambda "recuerda" una variable definida fuera de ella
    var descuento = 0.10
    val conDescuento = (i: Item) => i.precio * (1 - descuento)
    println(items.map(conDescuento))   // usa descuento = 0.10
    descuento = 0.50
    println(items.map(conDescuento))   // la misma lambda ahora ve descuento = 0.50
  }
}
