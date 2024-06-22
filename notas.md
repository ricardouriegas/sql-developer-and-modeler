# Uriegas Idea

## Description

Tenemos X cantidad de tablas, por lo que deberiamos usar Hashmaps para establecer la relacion que existe entre estas tablas, basicamente lo que quiero hacer son 2 Objetos principalemente:

1. **Tabla**, con los siguientes atributos:

   - Nombre
   - Atributos (List<String>)
   - Llave Primaria
   - Relaciones (Objeto Relacion)

2. **Relacion**, con los siguientes atributos:
   - Tabla Origen
   - Tabla Destino
   - Tipo de Relacion (1 a 1, 1 a N, N a 1, N a N)

La forma de manejar estos objetos seria por medio de hashmaps el cual guardaria la tabla y sus relaciones, de esta forma podriamos acceder a las relaciones de una tabla en especifico de forma rapida.

El hashmap quedaria algo asi:

```JSON
nombre_tabla_1: {
    objeto_tabla_1, // Objeto tabla
    extra_info // Informacion extra opcional
}
```

 **Nota**: _Se tendria que hacer una validacion, cuando se realize la creacion de una tabla, para que no exista una tabla con el mismo nombre. (ya que el nombre seria basicamente el id de la tabla)_

[UML Representativo de Uriegas](representacionUriegas.uxf)

# Joshua notes
## Menús 
Teniendo en cuenta que cada tabla debe contar con su **menu individual**, propongo una clase llamada `TableEditor.java`, la cual contiene toda la interfaz gráfica para editar valores de la tabla, pero, **¿qué** se va a poder modificar?

Bueno, mi idea es que se tengan 3 submenús: 
1. **General:**
   
   En este menú se modificará el nombre de la tabla, así como otros atributos que vayan surgiendo y que sea necesario atender y no entren en las siguientes categorías.
2. **Atributos:**
   Este menú nos permite: 
   - Agregar atributos
   - Eliminar atributos
   - Editar atributos
   
   Adicionalmente, también despliega opciones como añadir **longitud** a tipos de dato, marcar si es una **primary key** o no, y si dicho argumento es **obligatorio** o no.
3. **Relaciones:**
   Esta opción nos permite: 
   - Establecer la `cardinalidad` de las relaciones.
   - Establecer la `opcionalidad` de las relaciones.
   
   La idea que tengo pensada para esto es que se presenten todas las relaciones en una tabla, con la tabla de **origen** por un lado, y la tabla **destino** en otra casilla. Una vez se muestren las relaciones, el usuario podrá clickear en alguna para modificar la `cardinalidad` y la `opcionalidad` de la misma.