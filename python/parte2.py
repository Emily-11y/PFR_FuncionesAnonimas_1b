# Parte 2 - Entender: que es una funcion anonima (Python)
# Ejecutar:  python3 python/parte2.py

from collections import namedtuple

Item = namedtuple("Item", "nombre precio")
items = [Item("Laptop", 850.0), Item("Mouse", 25.0), Item("Silla", 120.0)]


# (a) Funcion NOMBRADA
def es_cara_nombrada(i):
    return i.precio > 50


# (b) Misma logica ANONIMA (lambda)
print([i.nombre for i in filter(es_cara_nombrada, items)])
print([i.nombre for i in filter(lambda i: i.precio > 50, items)])

# (c) PRIMERA CLASE: se guarda en variables, se pasa y se devuelve
es_cara = lambda i: i.precio > 50                   # guardada en una variable
def mas_caro_que(umbral):                           # devuelve otra funcion
    return lambda i: i.precio > umbral
print(len(list(filter(es_cara, items))))
print([i.nombre for i in filter(mas_caro_que(100), items)])

# (d) CLOSURE: la lambda lee una variable de fuera; en Python lee su valor ACTUAL
descuento = 0.10
con_descuento = lambda i: i.precio * (1 - descuento)
print(list(map(con_descuento, items)))   # descuento = 0.10
descuento = 0.50
print(list(map(con_descuento, items)))   # ahora ve descuento = 0.50
