# Parte 3 - Tipos de problemas resueltos con funciones anonimas (Python 3.8+)
# Ejecutar:  python3 python/parte3.py

from dataclasses import dataclass
from functools import reduce
from itertools import accumulate, chain


@dataclass(frozen=True)  # clase inmutable (equivale a la case class de Scala)
class Producto:
    nombre: str
    categoria: str
    precio: float
    stock: int


# Misma lista de 12 productos en los tres lenguajes
productos = [
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
    Producto("Escritorio",  "Muebles",     180.0, 5),
]


def fmt(x):            # formato fijo: 2 decimales con punto
    return f"{x:.2f}"


def lista(xs):         # imprime como [a, b, c]
    return "[" + ", ".join(xs) + "]"


def bool_txt(b):       # Python imprime True/False; usamos true/false como Scala y Java
    return str(b).lower()


# 1. SELECCION - filter: la lambda devuelve un booleano
print("== 1. Seleccion ==")
caros = filter(lambda p: p.precio > 50, productos)
print("Productos con precio > 50: " + lista(map(lambda p: p.nombre, caros)))

# 2. TRANSFORMACION - map: la lambda convierte cada elemento en otro valor
print("== 2. Transformacion ==")
iva = 0.15  # variable externa que la lambda "captura" (closure)
con_iva = map(lambda p: p.nombre + ":" + fmt(p.precio * (1 + iva)), productos)
print("Precio con IVA 15%: " + lista(con_iva))

# 3. VALIDACION - any / all aplicados sobre map(lambda ...)
print("== 3. Validacion ==")
print("Existe algun producto con precio > 800: "
      + bool_txt(any(map(lambda p: p.precio > 800, productos))))
print("Todos tienen stock > 0: "
      + bool_txt(all(map(lambda p: p.stock > 0, productos))))
print("Todos cuestan menos de 500: "
      + bool_txt(all(map(lambda p: p.precio < 500, productos))))

# 4. CONTEO - filter + len
print("== 4. Conteo ==")
print("Productos con stock < 30: "
      + str(len(list(filter(lambda p: p.stock < 30, productos)))))

# 5. EXTREMOS - max / min con key=lambda
print("== 5. Extremos ==")
print("Producto mas caro: " + max(productos, key=lambda p: p.precio).nombre)
print("Producto con menos stock: " + min(productos, key=lambda p: p.stock).nombre)

# 6. APLANAMIENTO - Python no tiene flatMap: se combina map + chain.from_iterable
print("== 6. Aplanamiento ==")
planas = chain.from_iterable(map(lambda p: [p.nombre, p.categoria], productos))
claves = list(dict.fromkeys(planas))  # distinct conservando el orden
print("Palabras clave de busqueda: " + lista(claves))

# 7. ACUMULACION - reduce (foldLeft) y accumulate (scanLeft)
print("== 7. Acumulacion ==")
total = reduce(lambda acc, p: acc + p.precio * p.stock, productos, 0.0)
print("Valor total del inventario: " + fmt(total))
evolucion = accumulate(map(lambda p: p.precio * p.stock, productos),
                       lambda acc, x: acc + x)
print("Valor acumulado paso a paso: " + lista(map(fmt, evolucion)))
