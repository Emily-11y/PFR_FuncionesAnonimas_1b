# Trabajo de Consulta: Funciones Anónimas

**Materia:** Programación Funcional · **Trabajo individual** · **Lenguajes:** Scala, Java, Python

Este repositorio explica qué es una función anónima (lambda), en qué tipos de problemas sirve y cómo se escribe en Scala, Java y Python, siempre con código que se puede ejecutar.

## Cómo ejecutar el código

Probado con **Scala 3.3.4**, **OpenJDK 21** y **Python 3.12** (el código de Scala está escrito para que funcione también en Scala 2.13).

| Qué | Comando (desde la carpeta raíz del repositorio) |
|---|---|
| Parte 2 – Scala | `scalac scala/Parte2.scala && scala Parte2` |
| Parte 2 – Java | `java java/Parte2.java` |
| Parte 2 – Python | `python3 python/parte2.py` |
| Parte 3 – Scala | `scalac scala/Parte3.scala && scala Parte3` |
| Parte 3 – Java | `java java/Parte3.java` |
| Parte 3 – Python | `python3 python/parte3.py` |
| Parte 4 – Scala | `scalac operaciones/Operaciones.scala && scala Operaciones` |

Las salidas de cada ejecución están en la carpeta `img/` (archivos `.png` y `.txt`).

## Estructura

```
README.md        <- documento principal (partes 1 a 5)
scala/           <- Partes 2 y 3 (Parte2.scala, Parte3.scala)
java/            <- Partes 2 y 3 (Parte2.java, Parte3.java)
python/          <- Partes 2 y 3 (parte2.py, parte3.py)
operaciones/     <- 4 operaciones nuevas y combinación (Parte 4)
img/             <- capturas de ejecución
```

## Índice

1. [Parte 1 – La programación funcional en el mundo real](#parte-1--la-programación-funcional-en-el-mundo-real)
2. [Parte 2 – ¿Qué es una función anónima?](#parte-2--qué-es-una-función-anónima)
3. [Parte 3 – ¿En qué problemas sirven?](#parte-3--en-qué-problemas-sirven-las-funciones-anónimas)
4. [Parte 4 – Cuatro operaciones nuevas](#parte-4--cuatro-operaciones-nuevas)
5. [Parte 5 – Comparar y concluir](#parte-5--comparar-y-concluir)
6. [Referencias](#referencias)

---

## Parte 1 – La programación funcional en el mundo real

### Caso 1: Apache Spark (Scala)

- **Problema que resuelve:** procesar conjuntos de datos enormes repartidos en muchas computadoras (un clúster). Por ejemplo, buscar errores en terabytes de logs. El proyecto nació en la Universidad de California, Berkeley, y se presentó en el artículo que introdujo los *RDD* (Resilient Distributed Datasets).
- **Lenguaje o herramienta:** Spark está escrito en Scala y ofrece APIs también en Java, Python y R.
- **Ventaja del enfoque funcional:** el programador no dice *cómo* repartir el trabajo, solo pasa funciones a `filter`, `map` y `reduce`. Como esas operaciones no modifican los datos originales, Spark puede repartirlas entre las máquinas y, si una falla, volver a calcular solo la parte perdida.
- **Fuentes:** Zaharia et al. (2012), artículo original de los RDD; apuntes de curso de OHSU que reproducen el ejemplo del artículo (ver [Referencias](#referencias)).

**Fragmento real con funciones anónimas.** Es el ejemplo de "minería de logs" del artículo de Spark:

```scala
val file = spark.textFile("hdfs://...")
val errs = file.filter(_.contains("ERROR"))
val ones = errs.map(_ => 1)
val count = ones.reduce(_+_)
```

*Qué hace:* lee un archivo de logs enorme, se queda solo con las líneas que contienen `"ERROR"`, convierte cada una en un `1` y suma todos los unos. El resultado es **cuántos errores hay**. Las tres lambdas son `_.contains("ERROR")`, `_ => 1` y `_+_`. Según los apuntes, los argumentos de `filter`, `map` y `reduce` son "function literals" de Scala, es decir, funciones anónimas.

### Caso 2: Twitter y Scala

- **Problema que resuelve:** sostener una plataforma con muchísimo tráfico y muchas tareas ocurriendo a la vez (concurrencia). Twitter pasó de construir su sitio con Ruby on Rails a rediseñarlo sobre la JVM con Scala.
- **Lenguaje o herramienta:** Scala. Su guía interna pública *Effective Scala* dice que Scala es uno de sus lenguajes principales y que gran parte de su infraestructura está escrita en él.
- **Ventaja del enfoque funcional:** en una charla de un ingeniero de Twitter se destacan las operaciones de mapear, filtrar y plegar listas, los valores inmutables y las herramientas de concurrencia. Un estudio de caso de Lightbend cuenta que estimaron que reescribir el código podía dar más de 10 veces de rendimiento.
- **Matiz importante:** la misma guía *Effective Scala* advierte que Scala es un lenguaje grande y que hay que usarlo con cuidado; Twitter decide cuándo usar un estilo "puramente funcional" y cuándo evitarlo. Además, Lightbend es la empresa que vende herramientas para Scala, así que su relato es una fuente interesada.
- **Fuentes:** *Effective Scala* (Twitter), charla "The Why and How of Scala at Twitter" (Alex Payne) y el estudio de caso de Lightbend (ver [Referencias](#referencias)).

---

## Parte 2 – ¿Qué es una función anónima?

Código completo y ejecutable en [`scala/Parte2.scala`](scala/Parte2.scala), [`java/Parte2.java`](java/Parte2.java) y [`python/parte2.py`](python/parte2.py). Salidas en `img/salida_parte2_*.png`.

### ¿Qué es y de dónde viene el nombre *lambda*?

Una **función anónima** es una función **sin nombre**, que se escribe justo donde se necesita. En vez de definir `esCara` en otro lugar, escribimos `p => p.precio > 50` directamente dentro de `filter`.

El nombre viene del **cálculo lambda**, un sistema matemático creado por Alonzo Church en los años 30 para describir funciones. En él, la función "suma 1" se escribe con la letra griega λ: `λx. x + 1`. Por eso los lenguajes llaman *lambda* a sus funciones anónimas.

```scala
val sumaUno = (x: Int) => x + 1   // una lambda guardada en una variable
println(sumaUno(5))               // 6
```

### Diferencia con una función nombrada

La lógica es la misma; lo que cambia es que la nombrada tiene nombre y se declara aparte, y la anónima se escribe en el lugar de uso.

```scala
// Nombrada
def esCaraNombrada(i: Item): Boolean = i.precio > 50
items.filter(esCaraNombrada)

// Anónima: misma lógica
items.filter(i => i.precio > 50)
```

```java
// Java: método nombrado vs lambda
static boolean esCaraNombrada(Item i) { return i.precio() > 50; }
items.stream().filter(Parte2::esCaraNombrada)
items.stream().filter(i -> i.precio() > 50)
```

```python
# Python: def vs lambda
def es_cara_nombrada(i): return i.precio > 50
filter(es_cara_nombrada, items)
filter(lambda i: i.precio > 50, items)
```

La anónima conviene cuando la lógica es corta y se usa una sola vez. Si la lógica es larga o se repite en varios lugares, conviene ponerle nombre.

### Funciones como ciudadanos de primera clase

Que sean "ciudadanos de primera clase" significa que una función se trata **como cualquier otro valor**: se puede guardar en una variable, pasar como argumento y devolver desde otra función. Por eso podemos pasarle una lambda a `map` o `filter`: esos métodos reciben una función como parámetro, igual que recibirían un número.

```scala
val esCara: Item => Boolean = i => i.precio > 50                          // guardada en una variable
def masCaroQue(umbral: Double): Item => Boolean = i => i.precio > umbral  // devuelta por una función
items.filter(esCara)                                                      // pasada como argumento
items.filter(masCaroQue(100))
```

### ¿Qué es un closure?

Un **closure** (clausura) es una lambda que **usa una variable definida fuera de ella** y la "recuerda". En este ejemplo la lambda `conDescuento` usa `descuento`, que no está dentro de ella:

```scala
var descuento = 0.10
val conDescuento = (i: Item) => i.precio * (1 - descuento)
println(items.map(conDescuento))   // List(765.0, 22.5, 108.0)
descuento = 0.50
println(items.map(conDescuento))   // List(425.0, 12.5, 60.0)  <- la misma lambda ve el valor nuevo
```

En Java funciona igual, pero con una regla extra: la variable capturada tiene que ser *efectivamente final* (no se puede reasignar). Esta diferencia se retoma en la [Parte 5](#51-scala-java-y-python-frente-a-frente).

---

## Parte 3 – ¿En qué problemas sirven las funciones anónimas?

Todos los ejemplos usan **la misma lista de 12 productos** (`nombre, categoria, precio, stock`), definida al inicio de cada archivo: [`scala/Parte3.scala`](scala/Parte3.scala), [`java/Parte3.java`](java/Parte3.java) y [`python/parte3.py`](python/parte3.py). Aquí se muestra solo la línea clave de cada lenguaje; el código completo, comentado, está en los archivos.

Se documentan los **7 tipos** de problemas. Las tres versiones imprimen **exactamente la misma salida** (se comprobó con `diff`), así que cada tipo muestra la salida una sola vez.

### 3.1 Selección: ¿qué elementos cumplen una condición?

**Situación real:** una tienda quiere listar sus productos "premium", los que cuestan más de $50.

```scala
productos.filter(p => p.precio > 50)
```
```java
PRODUCTOS.stream().filter(p -> p.precio() > 50)
```
```python
filter(lambda p: p.precio > 50, productos)
```
**Salida:** `[Laptop, Monitor, Audifonos, Silla, Escritorio]`

### 3.2 Transformación: ¿cómo convierto cada elemento en otro valor?

**Situación real:** la tienda necesita imprimir etiquetas con el precio más IVA del 15 % (la lambda usa la variable externa `iva`, o sea, es un closure).

```scala
productos.map(p => p.nombre + ":" + fmt(p.precio * (1 + iva)))
```
```java
PRODUCTOS.stream().map(p -> p.nombre() + ":" + fmt(p.precio() * (1 + iva)))
```
```python
map(lambda p: p.nombre + ":" + fmt(p.precio * (1 + iva)), productos)
```
**Salida:** `[Laptop:977.50, Mouse:28.75, Teclado:51.75, Monitor:253.00, Cuaderno:2.88, Esfero:0.92, Calculadora:20.70, Mochila:40.25, Audifonos:69.00, Botella:13.80, Silla:138.00, Escritorio:207.00]`

### 3.3 Validación: ¿existe alguno que cumpla?, ¿todos cumplen?

**Situación real:** antes de cerrar el mes, el encargado revisa si existe algún producto de más de $800 y si todos tienen stock.

```scala
productos.exists(p => p.precio > 800)
productos.forall(p => p.stock > 0)
```
```java
PRODUCTOS.stream().anyMatch(p -> p.precio() > 800)
PRODUCTOS.stream().allMatch(p -> p.stock() > 0)
```
```python
any(map(lambda p: p.precio > 800, productos))
all(map(lambda p: p.stock > 0, productos))
```
**Salida:** existe algún producto con precio > 800: `true` · todos tienen stock > 0: `true` · todos cuestan menos de 500: `false`

### 3.4 Conteo: ¿cuántos elementos cumplen una condición?

**Situación real:** bodega quiere saber cuántos productos necesitan reposición (stock menor a 30).

```scala
productos.count(p => p.stock < 30)
```
```java
PRODUCTOS.stream().filter(p -> p.stock() < 30).count()
```
```python
len(list(filter(lambda p: p.stock < 30, productos)))
```
**Salida:** `4`

### 3.5 Extremos: ¿cuál es el mejor o el peor según un criterio?

**Situación real:** el gerente quiere saber cuál es el producto más caro y cuál es el más urgente de reponer (menor stock).

```scala
productos.maxBy(p => p.precio)
productos.minBy(p => p.stock)
```
```java
PRODUCTOS.stream().max(Comparator.comparingDouble(p -> p.precio())).get()
PRODUCTOS.stream().min(Comparator.comparingInt(p -> p.stock())).get()
```
```python
max(productos, key=lambda p: p.precio)
min(productos, key=lambda p: p.stock)
```
**Salida:** más caro: `Laptop` · menos stock: `Escritorio`

### 3.6 Aplanamiento: ¿cómo junto en una sola lista los resultados de cada elemento?

**Situación real:** el buscador del catálogo necesita una lista única de palabras clave. Cada producto aporta dos (su nombre y su categoría), y las repetidas se eliminan.

```scala
productos.flatMap(p => List(p.nombre, p.categoria)).distinct
```
```java
PRODUCTOS.stream().flatMap(p -> Stream.of(p.nombre(), p.categoria())).distinct()
```
```python
chain.from_iterable(map(lambda p: [p.nombre, p.categoria], productos))  # Python no tiene flatMap
```
**Salida:** `[Laptop, Tecnologia, Mouse, Teclado, Monitor, Cuaderno, Papeleria, Esfero, Calculadora, Mochila, Accesorios, Audifonos, Botella, Silla, Muebles, Escritorio]`

### 3.7 Acumulación: ¿qué valor resume la lista y cómo evoluciona paso a paso?

**Situación real:** contabilidad necesita el valor total del inventario (precio × stock) y ver cómo va creciendo producto por producto.

```scala
productos.foldLeft(0.0)((acc, p) => acc + p.precio * p.stock)
productos.scanLeft(0.0)((acc, p) => acc + p.precio * p.stock).tail
```
```java
PRODUCTOS.stream().reduce(0.0, (acc, p) -> acc + p.precio() * p.stock(), (a, b) -> a + b)
// Java no tiene scanLeft: se recalcula la suma de cada prefijo de la lista
IntStream.range(0, n).mapToObj(i -> PRODUCTOS.subList(0, i + 1).stream().mapToDouble(p -> p.precio() * p.stock()).sum())
```
```python
reduce(lambda acc, p: acc + p.precio * p.stock, productos, 0.0)
accumulate(map(lambda p: p.precio * p.stock, productos), lambda acc, x: acc + x)
```
**Salida:** total `27185.00` · paso a paso `[10200.00, 12240.00, 14715.00, 19115.00, 19615.00, 20015.00, 20735.00, 21785.00, 24485.00, 25325.00, 26285.00, 27185.00]`

### Capturas de ejecución

![Scala](img/salida_scala.png)
![Java](img/salida_java.png)
![Python](img/salida_python.png)

---

## Parte 4 – Cuatro operaciones nuevas

Código completo en [`operaciones/Operaciones.scala`](operaciones/Operaciones.scala). Se eligieron `sortBy`, `groupBy`, `partition` y `zipWithIndex`, usando la misma lista de 12 productos de la Parte 3.

### Ficha 1: `sortBy` (familia Ordenar)

1. **Qué hace:** ordena la lista según un criterio que tú eliges.
2. **Lambda:** recibe un `Producto` y devuelve la **clave de orden** (un número o texto comparable). Para ordenar de mayor a menor se usa el negativo: `-p.precio`.
3. **Ejemplo:**
   ```scala
   productos.sortBy(p => -p.precio).take(3).map(p => p.nombre)
   ```
   **Salida:** `[Laptop, Monitor, Escritorio]`
4. **Problema que resuelve:** armar un ranking, como "los 3 productos más caros". Es un **tipo nuevo** (Ordenamiento): ninguno de los 7 de la sección 3 devuelve la lista reordenada.

### Ficha 2: `groupBy` (familia Agrupar)

1. **Qué hace:** separa los elementos en grupos según una clave. Devuelve un `Map` de clave a lista de elementos.
2. **Lambda:** recibe un `Producto` y devuelve la **clave del grupo** (aquí, su categoría).
3. **Ejemplo:**
   ```scala
   productos.groupBy(p => p.categoria).toList.map { case (cat, ps) => cat + "=" + ps.size }.sorted
   ```
   **Salida:** `[Accesorios=3, Muebles=2, Papeleria=3, Tecnologia=4]` (el `.sorted` solo hace que el orden de impresión sea siempre el mismo).
4. **Problema que resuelve:** "¿cuántos productos hay por categoría?". Se parece al **Conteo** (3.4), pero hecho por grupos.

### Ficha 3: `partition` (familia Dividir una lista)

1. **Qué hace:** divide la lista en dos: los que cumplen una condición y los que no.
2. **Lambda:** recibe un `Producto` y devuelve **`Boolean`**.
3. **Ejemplo:**
   ```scala
   val (economicos, otros) = productos.partition(p => p.precio < 50)
   ```
   **Salida:** económicos `[Mouse, Teclado, Cuaderno, Esfero, Calculadora, Mochila, Botella]` · los demás `[Laptop, Monitor, Audifonos, Silla, Escritorio]`
4. **Problema que resuelve:** separar productos económicos de los demás para dos listas de precios distintas. Se parece a la **Selección** (3.1), pero sin perder los elementos que no cumplen.

### Ficha 4: `zipWithIndex` (familia Combinar listas)

1. **Qué hace:** convierte cada elemento en un par `(elemento, posición)`.
2. **Lambda:** **no recibe ninguna**. Se encadena con `map`, que sí recibe una lambda para usar la posición.
3. **Ejemplo:**
   ```scala
   productos.sortBy(p => -p.precio).zipWithIndex.map { case (p, i) => s"${i + 1}. ${p.nombre}" }.take(3)
   ```
   **Salida:** `[1. Laptop, 2. Monitor, 3. Escritorio]`
4. **Problema que resuelve:** numerar un ranking o una lista de pasos. Es un **tipo nuevo** (Numeración).

### 4.3 Combinar operaciones

**Pregunta:** ¿cómo se ordenan las categorías según el valor total de su inventario (precio × stock), de mayor a menor?

```scala
productos
  .groupBy(p => p.categoria)                                  // 1) agrupa por categoría
  .map { case (cat, ps) => (cat, ps.map(p => p.precio * p.stock).sum) }  // 2) cada grupo -> (categoría, valor)
  .toList
  .sortBy { case (_, valor) => -valor }                       // 3) ordena de mayor a menor
```

- **Paso 1 – `groupBy` (nueva):** reparte los 12 productos en 4 grupos, uno por categoría.
- **Paso 2 – `map` (vista en clase):** convierte cada grupo en un par `(categoría, valor del inventario)`, multiplicando precio por stock y sumando.
- **Paso 3 – `sortBy` (nueva):** ordena los pares por valor, de mayor a menor.

**Salida:** `Tecnologia: 19075.00` · `Accesorios: 4590.00` · `Muebles: 1860.00` · `Papeleria: 1620.00`

![Parte 4](img/salida_operaciones.png)

---

## Parte 5 – Comparar y concluir

### 5.1 Scala, Java y Python frente a frente

| Criterio | Scala | Java | Python |
|---|---|---|---|
| **Sintaxis de la lambda** | `p => p.precio > 50` (o `_.precio > 50`) | `p -> p.precio() > 50` | `lambda p: p.precio > 50` |
| **Cómo se declara el tipo** | Se infiere del contexto; se puede escribir explícito: `(p: Producto) => ...` | Se infiere de la interfaz funcional esperada (`Predicate`, `Function`, `Comparator`...) | No se declara: Python es de tipado dinámico |
| **Operaciones con listas disponibles** | Todas las de las Partes 3 y 4 vienen incluidas, incluso `flatMap`, `scanLeft`, `groupBy`, `partition` | Streams: `filter`, `map`, `anyMatch`, `allMatch`, `count`, `max`, `min`, `flatMap`, `reduce`. No tiene `scanLeft` | `map`, `filter`, `any`, `all`, `max`/`min` con `key`, `functools.reduce`, `itertools.accumulate` y `chain`. No tiene `flatMap` |
| **Restricción del lenguaje** | Casi ninguna para las lambdas; un cuerpo largo se escribe entre llaves | La variable capturada debe ser *efectivamente final*; la lambda solo existe donde se espera una interfaz funcional | El cuerpo de una `lambda` es **una sola expresión** (no admite varias instrucciones) |
| **Legibilidad (mi opinión)** | La más compacta: las cadenas se leen casi como una frase | Más larga: hay que escribir `stream()`, `.get()` y `collect` | Muy fácil para una lambda sola; las cadenas de `map`/`filter` anidados se leen "de adentro hacia afuera" |

**¿En qué lenguaje fue más natural resolver los problemas?** En **Scala**: cada tipo de problema tiene su operación con nombre propio y se encadenan de izquierda a derecha. Python resultó cómodo para los casos simples, y Java funcionó bien pero con más texto.

**¿En qué tipo de problema hubo más diferencias entre lenguajes?** En el **Aplanamiento (3.6)** y la **Acumulación (3.7)**. Python no trae `flatMap` y hubo que combinar `map` con `chain.from_iterable`. Java no trae `scanLeft` y hubo que recalcular la suma de cada prefijo. En cambio, Selección, Transformación, Validación, Conteo y Extremos se resolvieron casi igual en los tres, solo con distinta sintaxis. La causa es que las operaciones "básicas" existen en las tres bibliotecas, pero las más especializadas se incluyeron (o no) según la filosofía de cada lenguaje.

### 5.2 Conclusión

Las funciones anónimas son la pieza que hace útiles las operaciones con listas. Operaciones como `filter`, `map` o `foldLeft` sirven para muchos problemas distintos, pero por sí solas no saben qué hacer con cada elemento. La lambda les entrega ese "qué": qué condición cumplir, cómo transformar, cómo acumular. Gracias a eso se escribe *qué* se quiere obtener y no *cómo* recorrer la lista paso a paso, lo que da código más corto, más fácil de leer y con menos errores. Los resultados de la Parte 3 lo muestran: con una línea se resolvieron los siete tipos de problemas en tres lenguajes.

Esa misma idea es la base de Apache Spark (Parte 1). En el ejemplo de minería de logs, `_.contains("ERROR")` y `_ => 1` son lambdas que Spark puede enviar a muchas máquinas para ejecutarlas en paralelo sobre pedazos distintos de los datos. Como son funciones sin estado compartido que se pasan como valores, el sistema puede distribuirlas y repetir solo el trabajo que se pierda si una máquina falla. Por eso tratar las funciones como valores, y no solo como código fijo, importa tanto en sistemas reales.

---

## Referencias

**Parte 1**
- Zaharia, M., Chowdhury, M., Das, T., Dave, A., Ma, J., McCauley, M., Franklin, M. J., Shenker, S., Stoica, I. (2012). *Resilient Distributed Datasets: A Fault-Tolerant Abstraction for In-Memory Cluster Computing*. NSDI '12, USENIX. https://www.usenix.org/conference/nsdi12/technical-sessions/presentation/zaharia
- Apuntes de curso (OHSU, CS 624), semana 2, clase 2: reproducen el ejemplo de minería de logs. https://dmice.ohsu.edu/bedricks/courses/cs624/lecs/w2_lec2.pdf
- Towards Data Science, artículo sobre programación funcional en Spark (Spark está escrito en Scala). https://towardsdatascience.com/?p=169014
- Eriksen, M. (Twitter). *Effective Scala*. https://twitter.github.io/effectivescala
- Payne, A. *The Why and How of Scala at Twitter* (charla en Chirp). https://fr2.slideshare.net/al3x/the-how-and-why-of-scala-at-twitter
- Lightbend. *New Tweets per Second Record, and How* (estudio de caso). https://www.lightbend.com/case-studies/new-tweets-per-second-record-and-how

**Documentación consultada para el código**
- Documentación oficial de Scala: https://docs.scala-lang.org
- Expresiones lambda en Java: https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
- Programación funcional en Python: https://docs.python.org/3/howto/functional.html
- Winitzki, S. *The Science of Functional Programming: A Tutorial, with Examples in Scala*: https://github.com/winitzki/sofp/releases/download/v2.2/sofp.pdf

**Herramientas de IA:** se usó Claude (Anthropic) como apoyo para estructurar el trabajo, escribir y revisar el código y redactar borradores de texto. El código fue ejecutado y las salidas verificadas.
