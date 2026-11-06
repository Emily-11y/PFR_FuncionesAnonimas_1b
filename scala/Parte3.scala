// Parte 3 - Tipos de problemas resueltos con funciones anonimas (Scala 2.13 / 3)
// Ejecutar:  scala run scala/Parte3.scala      (o)   scalac scala/Parte3.scala && scala Parte3

import java.util.Locale

case class Producto(nombre: String, categoria: String, precio: Double, stock: Int)

object Parte3 {

  // Misma lista de 12 productos en los tres lenguajes
  val productos: List[Producto] = List(
    Producto("Laptop",      "Tecnologia",  850.0, 12),
    Producto("Mouse",       "Tecnologia",   25.0, 80),
    Producto("Teclado",     "Tecnologia",   45.0, 55),
    Producto("Monitor",     "Tecnologia",  220.0, 20),
    Producto("Cuaderno",    "Papeleria",     2.5, 200),
    Producto("Esfero",      "Papeleria",     0.8, 500),
    Producto("Calculadora", "Papeleria",    18.0, 40),
    Producto("Mochila",     "Accesorios",   35.0, 30),
    Producto("Audifonos",   "Accesorios",   60.0, 45),
    Producto("Botella",     "Accesorios",   12.0, 70),
    Producto("Silla",       "Muebles",     120.0, 8),
    Producto("Escritorio",  "Muebles",     180.0, 5)
  )

  // Formato fijo (2 decimales con punto) para que los 3 lenguajes impriman igual
  def fmt(x: Double): String = String.format(Locale.US, "%.2f", Double.box(x))
  def lista(xs: Seq[String]): String = xs.mkString("[", ", ", "]")

  def main(args: Array[String]): Unit = {

    // 1. SELECCION - filter: la lambda devuelve Boolean
    println("== 1. Seleccion ==")
    val caros = productos.filter(p => p.precio > 50)
    println("Productos con precio > 50: " + lista(caros.map(p => p.nombre)))

    // 2. TRANSFORMACION - map: la lambda convierte cada elemento en otro valor
    println("== 2. Transformacion ==")
    val iva = 0.15 // variable externa que la lambda "captura" (closure)
    val conIva = productos.map(p => p.nombre + ":" + fmt(p.precio * (1 + iva)))
    println("Precio con IVA 15%: " + lista(conIva))

    // 3. VALIDACION - exists / forall
    println("== 3. Validacion ==")
    println("Existe algun producto con precio > 800: " + productos.exists(p => p.precio > 800))
    println("Todos tienen stock > 0: " + productos.forall(p => p.stock > 0))
    println("Todos cuestan menos de 500: " + productos.forall(p => p.precio < 500))

    // 4. CONTEO - count
    println("== 4. Conteo ==")
    println("Productos con stock < 30: " + productos.count(p => p.stock < 30))

    // 5. EXTREMOS - maxBy / minBy
    println("== 5. Extremos ==")
    println("Producto mas caro: " + productos.maxBy(p => p.precio).nombre)
    println("Producto con menos stock: " + productos.minBy(p => p.stock).nombre)

    // 6. APLANAMIENTO - flatMap: la lambda devuelve una lista por elemento
    println("== 6. Aplanamiento ==")
    val claves = productos.flatMap(p => List(p.nombre, p.categoria)).distinct
    println("Palabras clave de busqueda: " + lista(claves))

    // 7. ACUMULACION - foldLeft / scanLeft
    println("== 7. Acumulacion ==")
    val total = productos.foldLeft(0.0)((acc, p) => acc + p.precio * p.stock)
    println("Valor total del inventario: " + fmt(total))
    val evolucion = productos.scanLeft(0.0)((acc, p) => acc + p.precio * p.stock).tail
    println("Valor acumulado paso a paso: " + lista(evolucion.map(fmt)))
  }
}
